package edu.brown.cs.jmst.spark;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;
import spark.Request;
import spark.Response;
import spark.Route;

public class SongSuggestor implements Route {

  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    String query = req.queryMap().value("word");
    List<Track> tracks = SpotifyQuery.searchSong(query, u.getAuth());
    JsonArray ja = new JsonArray();
    for (Track t : tracks) {
      JsonObject jo = new JsonObject();
      jo.addProperty("song_id", t.getId());
      jo.addProperty("song_name", t.getName());
      JsonArray artists = new JsonArray();
      for (String artist : t.getArtistIds()) {
        artists.add(artist);
      }
      jo.add("song_artist", artists);
      jo.addProperty("song_album", t.getAlbumId());
      jo.addProperty("song_duration_s", t.getDuration_ms() / 1000);
      // jo.addProperty("song_album_cover", t.get);
      ja.add(jo);
    }
    return GSON.toJson(ja);
  }

}
