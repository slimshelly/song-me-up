package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
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

      try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
        HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");
        post.setHeader("Authorization",
            "Basic " + SpotifyAuthentication.ENCODED_CLIENT_KEY);
        List<BasicNameValuePair> pairs2 = new ArrayList<>();
        pairs2.add(new BasicNameValuePair("code", code));
        pairs2.add(new BasicNameValuePair("redirect_uri",
            SpotifyAuthentication.REDIRECT_URI));
        pairs2.add(new BasicNameValuePair("grant_type", "authorization_code"));
        UrlEncodedFormEntity urlentity =
            new UrlEncodedFormEntity(pairs2, "UTF-8");
        urlentity.setContentEncoding("application/json");
        post.setEntity(urlentity);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
          String json_string = EntityUtils.toString(response.getEntity());
          JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
          String access_token = jo.get("access_token").getAsString();
          String refresh_token = jo.get("refresh_token").getAsString();
          u.logIn(access_token, refresh_token);
        } else {
          pairs2.add(new BasicNameValuePair("error", "invalid_token"));
        }
      } catch (Exception e) {
        List<BasicNameValuePair> pairs2 = new ArrayList<>();
        pairs2.add(new BasicNameValuePair("error", "client_error"));
      }
    }
    res.redirect("/main?" + URLEncodedUtils.format(pairs, "UTF-8"));
    return null;
  }

}
