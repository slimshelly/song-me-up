package edu.brown.cs.jmst.spark;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MockPlaylist implements TemplateViewRoute {
	
	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
		// TODO Auto-generated method stub
	    Map<String, Object> variables = new HashMap<>();
	    return new ModelAndView(variables, "songmeup/join/mock_join.ftl");
	}

}
