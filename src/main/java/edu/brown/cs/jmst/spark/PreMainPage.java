package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class PreMainPage implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    Map<String, Object> variables =
        new ImmutableMap.Builder<String, Object>().build();
    return new ModelAndView(variables, "songmeup/premain.ftl");
  }

}
