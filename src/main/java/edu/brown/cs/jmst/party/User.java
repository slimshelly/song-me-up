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
  private boolean premium = false;
  private String curr_party = null;
  private String image = null;

  public User() {
  }

  public void logIn(String auth, String refresh)
      throws ClientProtocolException, IOException {
    auth_key = auth;
    refresh_key = refresh;
    logged_in = true;
    JsonObject jo = getInfo(false);
    if (jo.get("display_name").isJsonNull()) {
      display_name = jo.get("id").getAsString();
    } else {
      display_name = jo.get("display_name").getAsString();
    }
    if (jo.get("images").isJsonNull() || jo.get("images").getAsJsonArray().size() == 0 || jo.get("images").getAsJsonArray().get(0).isJsonNull()) {
        image = "data:image/png;base64,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";
    } else {
        image = jo.get("images").getAsJsonArray().get(0).getAsString();
    }

    id = jo.get("id").getAsString();
    premium = jo.get("product").getAsString().equals("premium");
  }

  public void logIn(String code) throws IllegalArgumentException, Exception {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");
      post.setHeader("Authorization",
          "Basic " + SpotifyAuthentication.ENCODED_CLIENT_KEY);
      List<BasicNameValuePair> pairs2 = new ArrayList<>();
      pairs2.add(new BasicNameValuePair("code", code));
      pairs2.add(new BasicNameValuePair("redirect_uri",
          SpotifyAuthentication.getRedirectUri()));
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
        throw new IllegalArgumentException("Invalid token.");
      }
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

  public String getImage() {
    return image;
  }

  public boolean inParty() {
    return curr_party != null;
  }

  public String leaveParty() throws PartyException {
    // make the user's CURR_PARTy == null
    System.out.println(display_name + "just left party ID " + curr_party);
    if (curr_party != null) {
      String retval = curr_party;
      curr_party = null;
      return retval;
    } else {
      throw new PartyException("Not in a party.");
    }
  }

  public void joinParty(String id) throws PartyException {
    
    System.out.println("user is joining party of ID " + id);
    if (curr_party != null) {
      throw new PartyException("Already in a party.");
    } else {
      System.out.println("user is already in party of ID " + id);
      curr_party = id;
    }
  }

  public String getCurrentParty() {
    return curr_party;
  }

  public boolean isPremium() {
    return premium;
  }

  public String getRefresh() {
    return refresh_key;
  }

  public String getCurrentPartyUrl(Party p) {
    if (p.getHostId().equals(getId())) {
      return SpotifyAuthentication.getRootUri() + "/host?party_id=" + p.getId();
    } else {
      return SpotifyAuthentication.getRootUri() + "/join?party_id=" + p.getId();
    }
  }
}
