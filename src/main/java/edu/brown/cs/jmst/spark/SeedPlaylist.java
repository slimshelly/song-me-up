package edu.brown.cs.jmst.spark;

import com.google.gson.JsonArray;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQueryRaw;
import spark.Request;
import spark.Response;
import spark.Route;

public class SeedPlaylist implements Route {
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
	    SmuState state = SmuState.getInstance();
	    String userid = request.session().attribute("user");
	    User u = state.getUser(userid);
	    String playlistId = request.queryMap().value("id");
	    System.out.println("Getting spotify raw tracks");
	    System.out.println(playlistId);
	    System.out.println(userid);
	    try {
	    JsonArray ja = SpotifyQueryRaw.getPlaylistTracksRaw(userid, playlistId, u.getAuth());
	    return SparkInitializer.GSON.toJson(ja);
	    } catch(Exception e) {
	    		General.printInfo(e.getMessage());
	    		e.printStackTrace();
	    		return null;
	    }
	    
	}

}
