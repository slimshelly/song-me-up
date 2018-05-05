package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class JoinHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    if (u == null || !u.loggedIn()) {
      res.redirect(General.getNewUrl(req.url(), "/songmeup"));
    } else {
      QueryParamsMap qm = req.queryMap();
      String party_id = qm.value("party_id");
      SparkErrorEnum err = null;
      try {
        Party p = state.addPartyPerson(u, party_id);
        Map<String,
            Object> variables = new ImmutableMap.Builder<String, Object>()
                .put("hostname", p.getHostName()).put("user_id", u.getId())
                .build();
        return new ModelAndView(variables, "songmeup/join/join.ftl");
      } catch (IllegalArgumentException e) {
        err = SparkErrorEnum.INVALID_PARTY_ID;
      } catch (PartyException pe) {
        err = SparkErrorEnum.ALREADY_IN_PARTY;
      }
      if (err != null) {
        List<BasicNameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("error", err.toString()));
        res.redirect(General.getNewUrl(req.url(),
            "/error?" + URLEncodedUtils.format(pair, "UTF-8")));
      }
    }
    return null;
  }

}
