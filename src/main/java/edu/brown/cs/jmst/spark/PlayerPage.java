package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Handles routing of sign in page.
 */
public class PlayerPage implements TemplateViewRoute {
  @Override
  
  public ModelAndView handle(Request arg0, Response arg1) throws Exception {
    Map<String,
        Object> variables = new ImmutableMap.Builder<String, Object>().build();
    return new ModelAndView(variables, "songmeup/manager.ftl");
  }
}
