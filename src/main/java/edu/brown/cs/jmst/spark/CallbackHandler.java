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
    QueryParamsMap qm = req.queryMap();
    String code = qm.value("code");
    String state = qm.value("state");

    General.printInfo("State: " + state);

    SparkErrorEnum err = null;
    if (!SpotifyAuthentication.hasState(state)) {
      err = SparkErrorEnum.STATE_MISMATCH;
    } else {
      res.removeCookie("spotify_auth_state");
      try {
        User u = SmuState.getInstance().addUser(code);
        req.session().attribute("user", u.getId());
        General.printInfo(u.getId());
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        err = SparkErrorEnum.INVALID_TOKEN;
      } catch (UserException e) {
        e.printStackTrace();
        err = SparkErrorEnum.USER_ERROR;
      } catch (Exception e) {
        e.printStackTrace();
        err = SparkErrorEnum.CLIENT_ERROR;
      }
    }
    if (err != null) {
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("error", err.toString()));

      res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
          + URLEncodedUtils.format(pair, "UTF-8"));
    } else {
      res.redirect(SpotifyAuthentication.getRootUri() + "/main");
    }
    return null;
  }

}
