package edu.brown.cs.jmst.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;

public class User extends Entity {

  private String auth_key = null;
  private String refresh_key = null;
  private boolean logged_in = false;
  private String display_name = null;
  private String id = null;

  public User() {
  }

  public void logIn(String auth, String refresh)
      throws ClientProtocolException, IOException {
    auth_key = auth;
    refresh_key = refresh;
    logged_in = true;
    JsonObject jo = getInfo();
    display_name = jo.get("display_name").getAsString();
    id = jo.get("id").getAsString();
  }

  public void refresh() throws ParseException, IOException {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");
      post.setHeader("Authorization",
          "Basic " + SpotifyAuthentication.ENCODED_CLIENT_KEY);

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("refresh_token", refresh_key));
      pairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
      UrlEncodedFormEntity urlentity = new UrlEncodedFormEntity(pairs, "UTF-8");
      urlentity.setContentEncoding("application/json");
      post.setEntity(urlentity);

      HttpResponse response = client.execute(post);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        String access_token = jo.get("access_token").getAsString();
        auth_key = access_token;
      }
    }
  }

  private JsonObject getInfo() throws ClientProtocolException, IOException {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet get = new HttpGet("https://api.spotify.com/v1/me");
      get.setHeader("Authorization", "Bearer " + auth_key);
      HttpResponse getResponse = client.execute(get);
      String json_string = EntityUtils.toString(getResponse.getEntity());
      JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
      return jo;
    }
  }

  public boolean loggedIn() {
    return logged_in;
  }

  public String getAuth() {
    return auth_key;
  }

  public String getName() {
    return display_name;
  }

  @Override
  public String getId() {
    return id;
  }

}
