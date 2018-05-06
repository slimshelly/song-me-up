package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class PreMainPage implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    if (u == null || !u.loggedIn()) {
      Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>().build();
      return new ModelAndView(variables, "songmeup/premain.ftl");
    } else {
      if (u.inParty()) {
        Party p = SmuState.getInstance().getParty(u.getCurrentParty());
        res.redirect(u.getCurrentPartyUrl(p));
      } else {
        res.redirect(SpotifyAuthentication.getRootUri() + "/main");
      }
      return null;
    }
  }

}
