package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import edu.brown.cs.jmst.spotify.SpotifyException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class PartyFormHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    try {
      state.startParty(u);
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("user_id", u.getId()).build();
      return new ModelAndView(variables, "songmeup/host/party_form.ftl");
    } catch (PartyException e) {
      SparkErrorEnum err = SparkErrorEnum.ALREADY_IN_PARTY;
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("error", err.toString()));
      res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
          + URLEncodedUtils.format(pair, "UTF-8"));
    } catch (SpotifyException e) {
      SparkErrorEnum err = SparkErrorEnum.NEEDS_PREMIUM;
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("error", err.toString()));
      res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
          + URLEncodedUtils.format(pair, "UTF-8"));
    }
    return null;
  }

}
