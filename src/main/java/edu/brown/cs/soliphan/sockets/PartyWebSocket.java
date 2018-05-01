package edu.brown.cs.soliphan.sockets;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebSocket
public class PartyWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  private static enum MESSAGE_TYPE {
    CONNECT, VOTESONG, ADDSONG, REMOVESONG
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // Add the session to the queue
    sessions.add(session);
    // Build the CONNECT message
    JsonObject jpayload = new JsonObject();
    jpayload.addProperty("id", nextId);
    JsonObject jo = new JsonObject();
    jo.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    jo.add("payload", jpayload);
    nextId++;
    // Send the CONNECT message
    session.getRemote().sendString(GSON.toJson(jo));
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    // Remove the session from the queue
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // JsonObject received = GSON.fromJson(message, JsonObject.class);
    JsonParser parser = new JsonParser();
    JsonObject received = parser.parse(message).getAsJsonObject();
    // JsonObject received = new JsonObject(message);
    // assert received.get("type").getAsInt() == MESSAGE_TYPE.SCORE.ordinal();
    JsonObject payload = received.get("payload").getAsJsonObject();
    // Compute the player's score
    int id = payload.get("id").getAsInt();
    // Board board = new Board(payload.get("board").getAsString());
    String[] text = payload.get("text").getAsString().split(" ");

    // Set<String> legal = board.play();
    int score = 0;
    for (String word : text) {
      // if (legal.contains(word)) {
      // int s = Board.score(word);
      // score += s;
      // }
    }
    // Send an UPDATE message to all users
    JsonObject jpayload = new JsonObject();
    jpayload.addProperty("id", id);
    jpayload.addProperty("score", score);
    JsonObject jo = new JsonObject();
    // jo.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    jo.add("payload", jpayload);

    for (Session s : sessions) {
      s.getRemote().sendString(GSON.toJson(jo));
    }
  }
}
