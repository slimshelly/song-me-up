package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.Request;
import spark.Response;
import spark.Route;

public class PlaylistHandler implements Route {

  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request request, Response response) throws Exception {
    System.out.println("IN PLAYLIST HANDLER");
    SmuState state = SmuState.getInstance();
    String userId = request.session().attribute("user");
    User u = state.getUser(userId);
    String partyId = u.getCurrentParty(); // retrieve party id from user
    Party currParty = state.getParty(partyId); // retrieve party from id
    //TODO: call refreshAll() ??
    JsonArray suggestingBlock = currParty.refreshSuggBlock();
    JsonArray votingBlock = currParty.refreshVoteBlock(userId);
    JsonArray playingBlock = currParty.refreshPlayBlock();
    System.out.println("About to get now playing");
    JsonObject nowPlaying = new JsonObject();
    System.out.println("Here's now playing");
    System.out.println(currParty.getNowPlaying());
    if (currParty.getNowPlaying() != null) {
    	  nowPlaying = currParty.getNowPlaying().toJson();
    }

    System.out.println("About to send variables");
    Map<String, Object> variables = ImmutableMap.of("sugg",
            suggestingBlock, "vote", votingBlock, "play", playingBlock,
            "now_playing", nowPlaying);
    return GSON.toJson(variables); // only sending info, not reloading page
  }

}
