package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import edu.brown.cs.jmst.spotify.SpotifyException;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class HostHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    if (u == null || !u.loggedIn()) {
      res.redirect(SpotifyAuthentication.getRootUri() + "/");
    } else {
      SparkErrorEnum err = null;
      QueryParamsMap qm = req.queryMap();
      if (u.inParty() && !u.getCurrentParty().equals(qm.value("party_id"))) {
        Party p = state.getParty(u.getCurrentParty());
        res.redirect(u.getCurrentPartyUrl(p));
        return null;
      }
      try {
        if (qm.hasKey("party_id")) {
          Party p = state.getParty(qm.value("party_id"));
          if (!p.getHostId().equals(u.getId())) {
            res.redirect(u.getCurrentPartyUrl(p));
            return null;
          }
          Map<String,
              Object> variables = new ImmutableMap.Builder<String, Object>()
                  .put("party_id", p.getId()).put("hostname", p.getHostName())
                  .put("user_id", u.getId()).build();
          return new ModelAndView(variables, "songmeup/host/host_join.ftl");
        } else {
          Party p = state.startParty(u);
          List<BasicNameValuePair> pair = new ArrayList<>();
          pair.add(new BasicNameValuePair("party_id", p.getId()));
          res.redirect(SpotifyAuthentication.getRootUri() + "/host?"
              + URLEncodedUtils.format(pair, "UTF-8"));
        }
      } catch (PartyException pe) {
        err = SparkErrorEnum.ALREADY_IN_PARTY;
      } catch (SpotifyException se) {
        err = SparkErrorEnum.NEEDS_PREMIUM;
      }

      if (err != null) {
        List<BasicNameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("error", err.toString()));
        res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
            + URLEncodedUtils.format(pair, "UTF-8"));
      }
    }
    return null;
  }

}
