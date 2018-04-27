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
  private boolean in_party = false;
  private boolean premium = false;

  public User() {
  }

  public void logIn(String auth, String refresh)
      throws ClientProtocolException, IOException {
    auth_key = auth;
    refresh_key = refresh;
    logged_in = true;
    JsonObject jo = getInfo(false);
    display_name = jo.get("display_name").getAsString();
    id = jo.get("id").getAsString();
    premium = jo.get("product").getAsString().equals("premium");
  }

  public void logIn(String code) {
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
        this.logIn(access_token, refresh_token);
      } else {
        pairs2.add(new BasicNameValuePair("error", "invalid_token"));
      }
    } catch (Exception e) {
      List<BasicNameValuePair> pairs2 = new ArrayList<>();
      pairs2.add(new BasicNameValuePair("error", "client_error"));
    }
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

  private JsonObject getInfo(boolean attempted)
      throws ClientProtocolException, IOException {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet get = new HttpGet("https://api.spotify.com/v1/me");
      get.setHeader("Authorization", "Bearer " + auth_key);
      HttpResponse getResponse = client.execute(get);
      if (getResponse.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(getResponse.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        return jo;
      } else {
        // try refreshing if request does not work.
        if (attempted) {
          // throw error if refresh did not work.
          throw new ClientProtocolException("Refresh not working.");
        } else {
          refresh();
          return getInfo(true);
        }
      }
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

  public boolean inParty() {
    return in_party;
  }

  public void leaveParty() throws PartyException {
    if (in_party) {
      in_party = false;
    } else {
      throw new PartyException("Not in a party.");
    }
  }

  public void joinParty() throws PartyException {
    if (in_party) {
      throw new PartyException("Already in a party.");
    } else {
      in_party = true;
    }
  }

  public boolean isPremium() {
    return premium;
  }
}
