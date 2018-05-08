package edu.brown.cs.jmst.spark;

import java.util.List;

import com.google.gson.JsonArray;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.SuggestResult;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyQuery;
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
	    String partyId = u.getCurrentParty();
	    Party currParty = state.getParty(partyId);
	    String playlistId = request.queryMap().value("playlist_id");
	    String ownerId = request.queryMap().value("owner_id");
	    System.out.println("Getting spotify raw tracks");
	    try {
	    		// suggest(?) top 10 playlist songs to the current party
		    	List<Track> songs = SpotifyQuery.getPlaylistTracks(ownerId, playlistId, u.getAuth());
		    	System.out.println(songs.size());
		    	System.out.println("got playlist tracks");
		    	for (Track song: songs) {
		    		AudioFeaturesSimple audio = SpotifyQuery.getSimpleFeatures(song.getId(), u.getAuth());
		    		System.out.println(song.getId());
		    		System.out.println("audio features retrieved");
		    		SuggestResult result = currParty.suggest(song, userid, audio);
		    	}
		    	System.out.println("added playlist tracks to party");
		    JsonArray ja = SpotifyQueryRaw.getPlaylistTracksRaw(ownerId, playlistId, u.getAuth());
		    System.out.println("Get raw playlist tracks");
		    return SparkInitializer.GSON.toJson(ja); // ok to return something I don't need?
	    } catch(Exception e) {
	    		General.printInfo(e.getMessage());
	    		e.printStackTrace();
	    		return null;
	    }	    
	}
}
