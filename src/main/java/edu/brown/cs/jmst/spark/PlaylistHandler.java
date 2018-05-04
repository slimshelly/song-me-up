package edu.brown.cs.jmst.spark;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.SongQueue;
import edu.brown.cs.jmst.party.Suggestion;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.Request;
import spark.Response;
import spark.Route;

public class PlaylistHandler implements Route {

  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request request, Response response) throws Exception {
	System.out.println("in handle");
    SmuState state = SmuState.getInstance();
    String userid = request.session().attribute("user");
    User u = state.getUser(userid);
    String partyId = u.getCurrentParty(); // retrieve party id from user
    System.out.println("partyid: " + partyId);
    Party currParty = state.getParty(partyId); // retrieve party from id
    
    System.out.println("about to get playlist");
    // get all 3 song blocks, then extract individual song blocks to send to frontend
    SongQueue songBlocks = currParty.getSuggestions();
    List<Suggestion> suggestingBlock = songBlocks.getSongsToPlay();
    System.out.println("songs in suggest: " + suggestingBlock.size());
    List<Suggestion> votingBlock = songBlocks.getSongsToPlay();
    System.out.println("songs in vote: " + votingBlock.size());
    List<Suggestion> playingBlock = songBlocks.getSongsToPlay();
    System.out.println("songs in play: " + playingBlock.size());
    
    System.out.println("about to send songs");
    Map<String, Object> variables = ImmutableMap.of("suggest", suggestingBlock, "vote", votingBlock, "play", playingBlock);
    return GSON.toJson(variables); // only sending info, not reloading page
  }

}
