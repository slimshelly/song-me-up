package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CallbackHandler implements TemplateViewRoute {

  private SmuState state;

  public CallbackHandler(SmuState state) {
    this.state = state;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    User u = state.getUser(req.session().id());
    List<BasicNameValuePair> pairs = new ArrayList<>();

    QueryParamsMap qm = req.queryMap();
    String code = qm.value("code");
    String state = qm.value("state");
    String storedState = req.cookies().get("spotify_auth_state");
    if (state == null || !state.equals(storedState)) {
      pairs.add(new BasicNameValuePair("error", "state_mismatch"));
    } else {
      res.removeCookie("spotify_auth_state");
      try {
        u.logIn(code);
      } catch (IllegalArgumentException e) {
        pairs.add(new BasicNameValuePair("error", "invalid_token"));
      } catch (Exception e) {
        pairs.add(new BasicNameValuePair("error", "client_error"));
      }
    }
    if (pairs.size() > 0) {
      res.redirect("/main?" + URLEncodedUtils.format(pairs, "UTF-8"));
    } else {
      res.redirect("/main");
    }

    return null;
  }

}
