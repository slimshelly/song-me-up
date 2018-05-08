package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.Request;
import spark.Response;
import spark.Route;

public class RefreshToken implements Route {

  @Override
  public Object handle(Request req, Response res) throws Exception {

    String userid = req.session().attribute("user");

    User u = SmuState.getInstance().getUser(userid);
    u.refresh();
    String newAuth = u.getAuth();
    
    Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
        .put("access_token", newAuth).build();
    return SparkInitializer.GSON.toJson(variables);
  }

}
