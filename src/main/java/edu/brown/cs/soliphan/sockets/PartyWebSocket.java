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

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;

@WebSocket
public class PartyWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

  private static enum MESSAGE_TYPE {
    VOTESONG, ADDSONG, REMOVESONG, PLAYLIST
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // Add the session to the queue
    sessions.add(session);
    // // Build the CONNECT message
    // JsonObject jpayload = new JsonObject();
    // jpayload.addProperty("id", nextId);
    // JsonObject jo = new JsonObject();
    // jo.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    // jo.add("payload", jpayload);
    // nextId++;
    // // Send the CONNECT message
    // session.getRemote().sendString(GSON.toJson(jo));
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    // Remove the session from the queue
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonParser parser = new JsonParser();
    JsonObject received = parser.parse(message).getAsJsonObject();
    assert received.get("type").getAsInt() < 4
        && received.get("type").getAsInt() >= 0;
    SmuState state = SmuState.getInstance();
    MESSAGE_TYPE type = MESSAGE_TYPE.values()[received.get("type").getAsInt()];
    JsonObject inputPayload = received.get("payload").getAsJsonObject();
    String user_id = inputPayload.get("id").getAsString();
    String song_id = inputPayload.get("song_id").getAsString();
    User u = state.getUser(user_id);
    String partyId = u.getCurrentParty();
    if (partyId != null) {
      Party p = state.getParty(partyId);
      switch (type) {
        case VOTESONG:
          boolean vote = inputPayload.get("vote").getAsBoolean();
          try {
            JsonObject jpayload = new JsonObject();
            jpayload.addProperty("song_id", song_id);
            jpayload.addProperty("votes", p.voteOnSong(user_id, song_id, vote));
            JsonObject jo = new JsonObject();
            jo.addProperty("type", MESSAGE_TYPE.VOTESONG.ordinal());
            jo.add("payload", jpayload);
            for (Session s : sessions) {
              s.getRemote().sendString(GSON.toJson(jo));
            }
          } catch (PartyException e) {
            General.printErr("Failed to vote on song. " + e.getMessage());
          }
          break;
        case ADDSONG:
          try {
      	    // get track object from spotify (access to all spotify track fields)
      	    // build trackbean, which includes all spotify track fields and album art
      	    // suggest the song to the current party
            JsonObject track = SpotifyQuery.getRawTrack(song_id, u.getAuth());
            Track suggested = new TrackBean(track, u.getAuth());
          	p.suggest(suggested, user_id);
          	
          	// build track object to send to frontend with message type ADDSONG
          	JsonObject jo = new JsonObject();
          	jo.addProperty("type", MESSAGE_TYPE.ADDSONG.ordinal());
          	jo.add("payload", track);
          	for (Session s : sessions) {
          		// sending song to all users
          		s.getRemote().sendString(GSON.toJson(jo));
          	}
          } catch (Exception e) {
            General.printErr(e.getMessage());
          }
          break;
        case REMOVESONG:
          break;
        case PLAYLIST:
          break;
      }
    }
  }
}
