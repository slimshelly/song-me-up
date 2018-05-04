package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class AdminPageHandler implements TemplateViewRoute {

	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
	    SmuState state = SmuState.getInstance();
	    String userid = request.session().attribute("user");
	    User u = state.getUser(userid);
	    Map<String,
	    Object> variables = new ImmutableMap.Builder<String, Object>().put("user_id", u.getId()).build();
	    return new ModelAndView(variables, "songmeup/host/host_page.ftl");
	}

}
