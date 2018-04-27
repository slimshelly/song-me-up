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
import edu.brown.cs.jmst.spotify.SpotifyException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class HostHandler implements TemplateViewRoute {

  private SmuState state;

  public HostHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    User u = state.getUser(req.session().id());
    if (!u.loggedIn()) {
      res.redirect("/login");
    } else {
      SparkErrorEnum err = null;
      try {
        Party p = state.startParty(u);
        Map<String,
            Object> variables = new ImmutableMap.Builder<String, Object>()
                .put("party_id", p.getId()).put("hostname", p.getHostName())
                .build();
        return new ModelAndView(variables, "songmeup/host/host.ftl");
      } catch (PartyException pe) {
        err = SparkErrorEnum.ALREADY_IN_PARTY;
        // List<BasicNameValuePair> pairs = new ArrayList<>();
        // pairs.add(new BasicNameValuePair("error",
        // SparkErrorEnum.ALREADY_IN_PARTY.toString()));
        // res.redirect("/error?" + URLEncodedUtils.format(pairs, "UTF-8"));
      } catch (SpotifyException se) {
        err = SparkErrorEnum.NEEDS_PREMIUM;
        // List<BasicNameValuePair> pairs = new ArrayList<>();
        // pairs.add(new BasicNameValuePair("error",
        // SparkErrorEnum.NEEDS_PREMIUM.toString()));
        // res.redirect("/error?" + URLEncodedUtils.format(pairs, "UTF-8"));
      }
      if (err != null) {
        List<BasicNameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("error", err.toString()));
        res.redirect("/error?" + URLEncodedUtils.format(pair, "UTF-8"));
      }
    }
    return null;
  }

}
