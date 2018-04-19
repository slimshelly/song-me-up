package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class LoginHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    // QueryParamsMap qm = req.queryMap();
    // String[] params = req.queryParamsValues("access_token");
    // Map<String, String> params2 = null;
    // try {
    // General.printInfo("LOGIN");
    // General.printInfo(req.pathInfo());
    // General.printInfo(req.contextPath());
    // params2 = req.params();
    // General.printInfo(params2.get("access_token"));
    // } catch (Exception e) {
    // General.printErr(e.getMessage());
    // }
    // if (params2 != null && !params2.containsKey("access_token")) {
    // res.removeCookie("spotify_auth_state");
    String state = SpotifyAuthentication.randomString(16);
    // General.printInfo("State: " + state);
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
    // General.printInfo(
    // "About to redirect to: " + "https://accounts.spotify.com/authorize?"
    // + URLEncodedUtils.format(pairs, "UTF-8"));
    res.redirect("https://accounts.spotify.com/authorize?"
        + URLEncodedUtils.format(pairs, "UTF-8"));
    // General.printInfo("Returning from login...");
    // }
    return new ModelAndView(new ImmutableMap.Builder<String, Object>().build(),
        "songmeup/logintest.ftl");
  }

}
