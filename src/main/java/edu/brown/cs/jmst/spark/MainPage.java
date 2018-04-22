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

    // if (req.session().isNew()) {
    // String id = req.session().id();
    //
    // Map<String, Object> variables = new ImmutableMap.Builder<String,
    // Object>()
    // .put("logchange", "LOG IN").build();
    // return new ModelAndView(variables, "songmeup/main_page/index.ftl");
    // } else {
    // String id = req.session().id();
    //
    //
    // }

    // String id = req.session().id();
    //
    // QueryParamsMap qm = req.queryMap();
    // if (qm.hasKey("loggedin")) {
    // String loggedin = qm.value("loggedin");
    // if (loggedin.equals("true") && qm.hasKey("access_token")
    // && qm.hasKey("refresh_token")) {
    // String access_token = qm.value("access_token");
    // String refresh_token = qm.value("refresh_token");
    //
    // List<BasicNameValuePair> pairs = new ArrayList<>();
    // pairs.add(new BasicNameValuePair("access_token", access_token));
    // pairs.add(new BasicNameValuePair("refresh_token", refresh_token));
    // pairs.add(new BasicNameValuePair("loggedin", "true"));
    //
    // Map<String,
    // Object> variables = new ImmutableMap.Builder<String, Object>()
    // .put("logchange", "SWITCH USER")
    // .put("homelink", "<a id=\"home\" href=\"/main?"
    // + URLEncodedUtils.format(pairs, "UTF-8") + "\">HOME</a>")
    // .build();
    // return new ModelAndView(variables, "songmeup/main_page/index.ftl");
    // }
    // }

  }

}
