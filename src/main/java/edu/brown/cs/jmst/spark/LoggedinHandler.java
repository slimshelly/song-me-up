package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class LoggedinHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    Map<String, Object> variables =
        new ImmutableMap.Builder<String, Object>().build();
    return new ModelAndView(variables, "songmeup/loggedintest.ftl");
  }

}
