package edu.brown.cs.jmst.spark;

import com.google.gson.JsonArray;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;
import spark.Request;
import spark.Response;
import spark.Route;

public class PlaylistSuggestor implements Route {

	@Override
	public Object handle(Request request, Response response) throws Exception {
	    SmuState state = SmuState.getInstance();
	    String userid = request.session().attribute("user");
	    User u = state.getUser(userid);
	    String query = request.queryMap().value("word");
	    // SHELL REPLACE THE QUERY BELOW WITH the spotify query to get the user's playlists
	    // AND UNCOMMENT BOTH LINES AND DELETE LAST LINES
	    // JsonArray ja = SpotifyQuery.searchSongRaw(query, u.getAuth());
	    // return SparkInitializer.GSON.toJson(ja);
	    return null;
	}

}
