package edu.brown.cs.jmst.spotify;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.general.General;

/**
 * A class for raw JSON queries from Spotify. Not yet turning them into backend objects.
 * @author maddiebecker
 *
 */
public class SpotifyQueryRaw {
	
  public static JsonObject getRawTrack(String song_id, String access_token) throws IOException {
	try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
	  HttpGet get = new HttpGet("https://api.spotify.com/v1/tracks/" + song_id);
	  get.setHeader("Authorization", "Bearer " + access_token);
	
	  HttpResponse response = client.execute(get);
	  if (response.getStatusLine().getStatusCode() == 200) {
	    String json_string = EntityUtils.toString(response.getEntity());
	    return new JsonParser().parse(json_string).getAsJsonObject();
	  } else {
	    throw new ClientProtocolException(
	        "Failed to get track: " + response.getStatusLine().getStatusCode()
	            + " " + response.toString());
	  }
	}
  }

  public static JsonArray searchSongRaw(String keywords, String access_token)
      throws IOException, UnsupportedEncodingException,
      ClientProtocolException {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "track"));
      pairs.add(new BasicNameValuePair("market", "from_token"));
      pairs.add(new BasicNameValuePair("limit", "10"));
      // want to add limit of 10! HELP

      HttpGet get = new HttpGet("https://api.spotify.com/v1/search?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        
        

        return jo.get("tracks").getAsJsonObject().get("items").getAsJsonArray();
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    }
  }
  
  public static JsonArray getPlaylistTracksRaw(String user_id, String playlist_id, String access_token)
	      throws Exception {
	  General.printInfo(access_token);
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
    	
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("limit", "10"));
      
      System.out.println(user_id);
      System.out.println(playlist_id);
      
      HttpGet get = new HttpGet("https://api.spotify.com/v1/users/" + user_id
              + "/playlists/" + playlist_id + "/tracks?"
              + URLEncodedUtils.format(pairs, "UTF-8"));
          get.setHeader("Authorization", "Bearer " + access_token);
      
      HttpResponse response = client.execute(get);
      
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray tracks =
            jo.get("items").getAsJsonArray();
        
        return tracks;
      } else {
          throw new ClientProtocolException(
                  "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                      + " " + response.toString());
      }
    }
  }

}
