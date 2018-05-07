package edu.brown.cs.jmst.sockets;

import java.io.IOException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.SuggestResult;
import edu.brown.cs.jmst.party.SuggestResult.STATUS_TYPE;
import edu.brown.cs.jmst.party.Suggestion;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;

@WebSocket
public class PartyWebSocket {
  private static final Gson GSON = new Gson();
  // private static final Queue<Session> sessions = new
  // ConcurrentLinkedQueue<>();
  private static final BidiMap<String, Session> userSession =
      new DualHashBidiMap<>();

  private enum MESSAGE_TYPE {
    CONNECT, SUGGEST, REFRESH_SUGG, VOTESONG, REFRESH_VOTE, NEXT_SONG,
    REFRESH_PLAY, REFRESH_ALL
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // Add the session to the queue
    // sessions.add(session);
    // // Build the CONNECT message
    JsonObject jo = new JsonObject();
    jo.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    // Send the CONNECT message
    session.getRemote().sendString(GSON.toJson(jo));
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    // Remove the session from the queue
    // sessions.remove(session);
    userSession.removeValue(session);
  }


  public void signalRefreshAll(Party party) throws IOException {
    if (party == null) {
      return;
    }
    try {
      JsonObject jo = new JsonObject();
      jo.addProperty("type", MESSAGE_TYPE.REFRESH_ALL.ordinal());
      jo.add("payload", party.refreshAllBlocks());
      for (String partyer_id : party.getIds()) {
        Session s = userSession.get(partyer_id);
        s.getRemote().sendString(GSON.toJson(jo));
      }
      System.out.println("Sent REFRESH_ALL message");
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      General.printErr(e.getMessage());
    }
  }

  public void signalRefreshPlay(Party party) throws IOException {
    if (party == null) {
      return;
    }
    try {
      JsonObject jo = new JsonObject();
      jo.addProperty("type", MESSAGE_TYPE.REFRESH_PLAY.ordinal());
      jo.add("payload", party.refreshPlayBlock());
      for (String partyer_id : party.getIds()) {
        Session s = userSession.get(partyer_id);
        s.getRemote().sendString(GSON.toJson(jo));
      }
      System.out.println("Sent REFRESH_PLAY message");
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      General.printErr(e.getMessage());
    }
  }

  public void signalRefreshVote(Party party) throws IOException {
    if (party == null) {
      return;
    }
    try {
      JsonObject jo = new JsonObject();
      jo.addProperty("type", MESSAGE_TYPE.REFRESH_VOTE.ordinal());
      jo.add("payload", party.refreshVoteBlock());
      for (String partyer_id : party.getIds()) {
        Session s = userSession.get(partyer_id);
        s.getRemote().sendString(GSON.toJson(jo));
      }
      System.out.println("Sent REFRESH_VOTE message");
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      General.printErr(e.getMessage());
    }
  }

  public void signalRefreshSugg(Party party) throws IOException {
    if (party == null) {
      return;
    }
    try {
      JsonObject jo = new JsonObject();
      jo.addProperty("type", MESSAGE_TYPE.REFRESH_SUGG.ordinal());
      jo.add("payload", party.refreshSuggBlock());
      for (String partyer_id : party.getIds()) {
        Session s = userSession.get(partyer_id);
        s.getRemote().sendString(GSON.toJson(jo));
      }
      System.out.println("Sent REFRESH_SUGG message");
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      General.printErr(e.getMessage());
    }
  }
  
  

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonParser parser = new JsonParser();
    JsonObject received = parser.parse(message).getAsJsonObject();
    assert received.get("type").getAsInt() < 8
        && received.get("type").getAsInt() >= 0;
    SmuState state = SmuState.getInstance();
    MESSAGE_TYPE type = MESSAGE_TYPE.values()[received.get("type").getAsInt()];
    JsonObject inputPayload = received.get("payload").getAsJsonObject();
    String user_id = inputPayload.get("id").getAsString();
    if (type == MESSAGE_TYPE.CONNECT) {
      System.out.println("Received CONNECT message");
      userSession.put(user_id, session);
      return;
    }
    String song_id = inputPayload.get("song_id").getAsString();
    User u = state.getUser(user_id);
    String partyId = u.getCurrentParty();
    if (partyId != null) {
      Party party = state.getParty(partyId);
      switch (type) {
        case CONNECT: { //unreachable
          break;
        }
        case SUGGEST: {
          System.out.println("Received SUGGEST message");
          try {
            // get track object from spotify (access to all spotify track
            // fields)
            //  build trackbean, which includes all spotify track fields and
            // album art
            // suggest the song to the current party
            JsonObject track = SpotifyQuery.getRawTrack(song_id, u.getAuth());
            Track song = new TrackBean(track, u.getAuth());
            System.out.println("Line 178 PartyWebSocket.java (before getSimpleFeatures)");
            AudioFeaturesSimple features = SpotifyQuery.getSimpleFeatures(song_id,
                    u.getAuth());
            System.out.println("Line 181 PartyWebSocket.java (after getSimpleFeatures)");
            SuggestResult suggested = party.suggest(song, user_id, features);
            STATUS_TYPE status = suggested.getStatus();
            switch (status) {
              case VOTE: {
                System.out.println("Result of suggesting: VOTE");
                signalRefreshVote(party);
                break;
              }
              case DUPLICATE_SUGG: {
                System.out.println("Result of suggesting: DUPLICATE_SUGG");
                signalRefreshSugg(party);
                break;
              }
              case UNIQUE_SUGG: {
                System.out.println("Result of suggesting: UNIQUE_SUGG");
                try {
                  JsonObject suggestion = suggested.getSuggested().toJson();
                  JsonObject jo = new JsonObject();
                  jo.addProperty("type", MESSAGE_TYPE.SUGGEST.ordinal());
                  jo.add("payload", suggestion);
                  for (String partyer_id : party.getIds()) {
                    Session s = userSession.get(partyer_id);
                    s.getRemote().sendString(GSON.toJson(jo));
                  }
                  System.out.println("Sent SUGGEST message");
                } catch (Exception e) {
                  General.printErr("Error accessing Track information. "
                          + e.getMessage());
                }
              }
            }
          } catch (Exception e) {
            General.printErr(e.getMessage());
          }
          break;
        }
        case VOTESONG: {
          System.out.println("Received VOTESONG message");
          // retrieve boolean of vote (up or down)
          boolean vote = inputPayload.get("vote").getAsBoolean();
          try {
            // record vote with party
            // retrieve list of voting block songs from backend
            party.voteOnSong(user_id, song_id, vote);
            signalRefreshVote(party);
//            Collection<Suggestion> votingBlock =
//                    party.voteOnSong(user_id, song_id, vote);
//            JsonArray orderedSuggestions = new JsonArray();
//            for (Suggestion s : votingBlock) {
//              try {
//                orderedSuggestions.add(s.toJson());
//              } catch (Exception e) {
//                General.printErr("Error accessing Track information. "
//                        + e.getMessage());
//              }
//            }
//            JsonObject jo = new JsonObject();
//            jo.addProperty("type", MESSAGE_TYPE.VOTESONG.ordinal());
//            jo.add("payload", orderedSuggestions);
//            for (String partyer_id : party.getPartyGoerIds()) {
//              Session s = userSession.get(partyer_id);
//              s.getRemote().sendString(GSON.toJson(jo));
//            }
          } catch (PartyException e) {
            General.printErr("Failed to vote on song. " + e.getMessage());
          }
          break;
        }
        case REFRESH_VOTE: {
          System.out.println("Received REFRESH_VOTE message (illegal!)");
          break;
        }
        case NEXT_SONG:
          System.out.println("Received NEXT_SONG message");
          try {
            Suggestion nextSong = party.getNextSongToPlay();
            
            signalRefreshAll(party); //TODO: only refresh play if possible
            JsonObject jo = new JsonObject();
            jo.addProperty("type", MESSAGE_TYPE.NEXT_SONG.ordinal());
            jo.add("payload", nextSong.toJson());
            for (String partyer_id : party.getIds()) {
              Session s = userSession.get(partyer_id);
              s.getRemote().sendString(GSON.toJson(jo));
            }
            System.out.println("Sent NEXT_SONG message");
          } catch (Exception e) {
            General.printErr("Error getting next song. " + e.getMessage());
          }
          break;
      }
    }
  }
}
