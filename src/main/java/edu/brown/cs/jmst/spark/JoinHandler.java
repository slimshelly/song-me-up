package edu.brown.cs.jmst.spark;

import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class JoinHandler implements TemplateViewRoute {

  private SmuState state;

  public JoinHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {

    return null;
  }

}
