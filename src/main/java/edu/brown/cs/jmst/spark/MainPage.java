package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainPage implements TemplateViewRoute {

  private SmuState state;

  public MainPage(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    User u = state.getUser(req.session().id());

    String login;
    if (u.loggedIn()) {
      login = "SWITCH USER";
    } else {
      login = "LOG IN";
    }

    Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
        .put("logchange", login).build();
    return new ModelAndView(variables, "songmeup/main_page/index.ftl");
  }

}
