package edu.brown.cs.jmst.spark;

import com.google.gson.JsonArray;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQueryRaw;
import spark.Request;
import spark.Response;
import spark.Route;

public class SongSuggestor implements Route {

  @Override
  public Object handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    String query = req.queryMap().value("word");
    JsonArray ja = SpotifyQueryRaw.searchSongRaw(query, u.getAuth());
    return SparkInitializer.GSON.toJson(ja);
  }

}
