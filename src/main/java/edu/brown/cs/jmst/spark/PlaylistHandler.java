package edu.brown.cs.jmst.spark;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.jmst.music.SongMeUpPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.Request;
import spark.Response;
import spark.Route;

public class PlaylistHandler implements Route {
	
  private static final Gson GSON = new Gson();
  private SmuState state;

  public PlaylistHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response)
      throws Exception {
    // Pass list
    Party currParty = state.getParty(request.session().id()); // is this right?
//    SongMeUpPlaylist playlist = currParty.getPlaylist();
//    List<Track> playlistSongs = playlist.getSongs();
    String userId = request.session().attribute("user");
    User u = state.getUser(userId);
    String partyId = u.getCurrentParty();
    Party currParty = state.getParty(partyId);
    SongMeUpPlaylist playlist = currParty.getPlaylist();
    List<Track> playlistSongs = playlist.getSongs();

    Map<String, Object> variables = ImmutableMap.of("songs", playlistSongs);
    return GSON.toJson(variables); //only sending info, not reloading page
  }

}
