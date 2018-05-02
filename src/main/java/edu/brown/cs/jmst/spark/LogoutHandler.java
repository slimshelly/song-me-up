package edu.brown.cs.jmst.spark;

import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class LogoutHandler implements TemplateViewRoute {

  private SmuState state;

  public LogoutHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    String userid = request.session().attribute("user");
    request.session().removeAttribute("user");
    state.removeUser(userid);
    response.redirect("https://www.spotify.com/us/logout/");
    return null;
  }

}
