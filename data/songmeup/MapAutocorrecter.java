package edu.brown.cs.soliphan.search;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles autocorrect requests.
 */
public class MapAutocorrecter implements Route {

  // private MapState state;
  // private static final Gson GSON = new Gson();
  //
  // /**
  // * @param ms
  // * map state
  // */
  // public MapAutocorrecter(MapState ms) {
  // state = ms;
  // }

  @Override
  public Object handle(Request arg0, Response arg1) throws Exception {
    // QueryParamsMap qm = arg0.queryMap();
    // String acInput = qm.value("acString");
    // List<String> suggestions =
    // MapParseHandler.getSuggestions(acInput, state.getNameTrie());
    // Map<String, Object> variables =
    // ImmutableMap.of("suggestionlist", suggestions);
    // return GSON.toJson(variables);
    return null;
  }

}
