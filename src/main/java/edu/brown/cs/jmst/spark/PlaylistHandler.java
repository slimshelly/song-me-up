package edu.brown.cs.jmst.spark;

import java.util.Collection;
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
    Collection<Suggestion> suggestingBlock = currParty.getSuggestedSongs();
    Collection<Suggestion> votingBlock = currParty.getSongsToVoteOn();
    Collection<Suggestion> playingBlock = currParty.getSongsToPlay();
    
    System.out.println("about to send songs");
    Map<String, Object> variables = ImmutableMap.of("suggest", suggestingBlock, "vote", votingBlock, "play", playingBlock);
    return GSON.toJson(variables); // only sending info, not reloading page
  }

}
