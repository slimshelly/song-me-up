package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class LoginHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    // res.removeCookie("spotify_auth_state");
    String state = SpotifyAuthentication.randomString(16);
    General.printInfo("State: " + state);
    res.cookie("spotify_auth_state", state);
    String scope = "user-read-private user-read-email";

    List<BasicNameValuePair> pairs = new ArrayList<>();
    pairs.add(new BasicNameValuePair("response_type", "code"));
    pairs.add(
        new BasicNameValuePair("client_id", SpotifyAuthentication.CLIENT_ID));
    pairs.add(new BasicNameValuePair("scope", scope));
    pairs.add(new BasicNameValuePair("redirect_uri",
        SpotifyAuthentication.REDIRECT_URI));
    pairs.add(new BasicNameValuePair("state", state));
    General.printInfo(
        "About to redirect to: " + "https://accounts.spotify.com/authorize?"
            + URLEncodedUtils.format(pairs, "UTF-8"));
    res.redirect("https://accounts.spotify.com/authorize?"
        + URLEncodedUtils.format(pairs, "UTF-8"));
    General.printInfo("Returning from login...");
    return new ModelAndView(new ImmutableMap.Builder<String, Object>().build(),
        "songmeup/logintest.ftl");
  }

}
