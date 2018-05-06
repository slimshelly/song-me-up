package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;
import spark.Request;
import spark.Response;
import spark.Route;

public class PlaylistSuggestor implements Route {
	
	private static final Gson GSON = new Gson();
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		System.out.println("Getting user's playlists");
	    SmuState state = SmuState.getInstance();
	    String userid = request.session().attribute("user");
	    User u = state.getUser(userid);
	    List<SpotifyPlaylist> userPlaylists = SpotifyQuery.getUserPlaylist(u.getAuth());
	    System.out.println("Got playlists");
	    
	    List<JsonObject> playlistObjects = new ArrayList<>();
	    for (SpotifyPlaylist playlist: userPlaylists) {
	    		JsonObject currPlaylist = new JsonObject();
	    		currPlaylist.addProperty("name", playlist.getName());
	    		currPlaylist.addProperty("id", playlist.getId());
	    		currPlaylist.addProperty("numberOfTracks", playlist.getSongs().size());
	    		currPlaylist.addProperty("images", playlist.getPlaylistImages());
	    		playlistObjects.add(currPlaylist);
	    }
	    
	    Map<String, Object> variables = ImmutableMap.of("playlists", playlistObjects);
	    System.out.println("About to send to frontend");
	    return GSON.toJson(variables); // only sending info, not reloading page
	}

}
