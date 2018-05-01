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
    QueryParamsMap qm = req.queryMap();
    String code = qm.value("code");
    String state = qm.value("state");
    String storedState = req.cookies().get("spotify_auth_state");
    SparkErrorEnum err = null;
    if (state == null || !state.equals(storedState)) {
      err = SparkErrorEnum.STATE_MISMATCH;
    } else {
      res.removeCookie("spotify_auth_state");
      try {
        u.logIn(code);
      } catch (IllegalArgumentException e) {
        err = SparkErrorEnum.INVALID_TOKEN;
      } catch (Exception e) {
        err = SparkErrorEnum.CLIENT_ERROR;
      }
    }
    if (err != null) {
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("error", err.toString()));
      res.redirect("/error?" + URLEncodedUtils.format(pair, "UTF-8"));
    } else {
      res.redirect("/main");
    }

    return null;
  }

}
