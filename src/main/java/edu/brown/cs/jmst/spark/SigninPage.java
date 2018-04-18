package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Handles routing of sign in page.
 *
 * @author Samuel Oliphant
 *
 */
public class SigninPage implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request arg0, Response arg1) throws Exception {
    Map<String,
        Object> variables = new ImmutableMap.Builder<String, Object>()
            .put("title", "Example of the Authorization Code flow with Spotify")
            .build();
    // return new ModelAndView(variables, "songmeup/signin.ftl");
    // return new ModelAndView(variables, "songmeup/spotifyplayer.ftl");
    return new ModelAndView(variables, "songmeup/logintest.ftl");
  }
}
