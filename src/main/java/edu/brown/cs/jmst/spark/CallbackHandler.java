package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

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
    String storedState = req.cookies().get("stateKey");
    if (state == null || state != storedState) {
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("error", "state_mismatch"));
      res.redirect("/#" + URLEncodedUtils.format(pairs, "UTF-8"));
    } else {
      res.removeCookie("stateKey");

      JsonObject form = new JsonObject();
      form.addProperty("code", code);
      form.addProperty("redirect_uri", SpotifyAuthentication.REDIRECT_URI);
      form.addProperty("grant_type", "authorization_code");

      JsonObject headers = new JsonObject();
      String temp = SpotifyAuthentication.CLIENT_ID + ":"
          + SpotifyAuthentication.CLIENT_SECRET;
      String encoded = Base64.getEncoder().encodeToString(temp.getBytes());
      headers.addProperty("Authorization", "Basic " + encoded);

      JsonObject authOptions = new JsonObject();
      authOptions.addProperty("url", "https://accounts.spotify.com/api/token");
      authOptions.add("form", form);
      authOptions.add("headers", headers);
      authOptions.addProperty("json", true);

      HttpClient httpclient = HttpClients.createDefault();

      // HttpClient httpclient = HttpClients.createDefault();
      // HttpPost httppost = new
      // HttpPost("https://accounts.spotify.com/api/token");
      // List<BasicNameValuePair> pairs = new ArrayList<>();
      // pairs.add(new BasicNameValuePair("error", "state_mismatch"));
    }

    return null;
  }

}
