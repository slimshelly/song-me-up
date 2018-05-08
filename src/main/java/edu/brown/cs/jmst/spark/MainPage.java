package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainPage implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    if (u == null || !u.loggedIn()) {
      res.redirect(SpotifyAuthentication.getRootUri() + "/songmeup");
      return null;
    }
    QueryParamsMap qm = req.queryMap();
    if (qm.hasKey("leave")) {

      String user_partyid = u.getCurrentParty();
      Party p = state.getParty(user_partyid);
      // if user is host, end the party.
      if (p.getHostId().equals(userid)) {
        System.out.println("host tried to leave party");
        p.end();

      } else {
        try {
          state.leaveParty(u, u.getCurrentParty());
        } catch (PartyException e) {
          General.printErr("Could not leave party.");
        }
      }

    }
    if (qm.hasKey("logout")) {
      res.redirect(SpotifyAuthentication.getRootUri() + "/logout");
      return null;
    } else {
      String name = u.getName() == null ? u.getId() : u.getName();
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("premium", u.isPremium()).put("name", name).build();
      return new ModelAndView(variables, "songmeup/main_page/index.ftl");
    }

  }

}
