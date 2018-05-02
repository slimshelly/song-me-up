package edu.brown.cs.jmst.spark;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class PlaylistHandler implements TemplateViewRoute {

  private SmuState state;

  public PlaylistHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    // Pass list
    Party currParty = state.getParty(request.session().id()); // is this right?
//    SongMeUpPlaylist playlist = currParty.getPlaylist();
//    List<Track> playlistSongs = playlist.getSongs();

    Map<String, Object> variables = new HashMap<>();
    return new ModelAndView(variables, "songmeup/join/join.ftl");
  }

}
