package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.party.UserException;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CallbackHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    // User u = state.getUser(req.session().id());
    QueryParamsMap qm = req.queryMap();
    String code = qm.value("code");
    String state = qm.value("state");

    String storedState = req.cookies().get("spotify_auth_state");
    General.printInfo("State: " + state);
    General.printInfo("Stored: " + storedState);
    SparkErrorEnum err = null;
    if (state == null || !state.equals(storedState)) {
      err = SparkErrorEnum.STATE_MISMATCH;
    } else {
      res.removeCookie("spotify_auth_state");
      try {
        User u = SmuState.getInstance().addUser(code);
        req.session().attribute("user", u.getId());
        General.printInfo(u.getId());
      } catch (IllegalArgumentException e) {
        err = SparkErrorEnum.INVALID_TOKEN;
      } catch (UserException e) {
        err = SparkErrorEnum.USER_ERROR;
      } catch (Exception e) {
        err = SparkErrorEnum.CLIENT_ERROR;
      }
    }
    if (err != null) {
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("error", err.toString()));
      // res.redirect("../error?" + URLEncodedUtils.format(pair, "UTF-8"));
      res.redirect(SpotifyAuthentication.ROOT_URI + "/error?"
          + URLEncodedUtils.format(pair, "UTF-8"));
    } else {
      // res.redirect("../main");
      res.redirect(SpotifyAuthentication.ROOT_URI + "/main");
    }
    return null;
  }

}
