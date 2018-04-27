package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyHost;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
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
      if (u.isPremium()) {
        Party p = new Party(new PartyHost(u));

        Map<String,
            Object> variables = new ImmutableMap.Builder<String, Object>()
                .put("logchange", "SWITCH USER")
                .put("hostname", p.getHostName()).build();
        return new ModelAndView(variables, "songmeup/main_page/index.ftl");

      } else {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("error", "not_premium"));
        res.redirect("/main?" + URLEncodedUtils.format(pairs, "UTF-8"));
      }
    }
    return null;
  }

}
