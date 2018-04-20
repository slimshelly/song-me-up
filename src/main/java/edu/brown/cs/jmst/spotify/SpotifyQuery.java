package edu.brown.cs.jmst.spotify;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.music.Track;

public class SpotifyQuery {

  public static List<Track> searchSong(String keywords) {
    List<Track> songs = new ArrayList<>();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/search");
      get.setHeader("Authorization",
          "Basic " + SpotifyAuthentication.ENCODED_CLIENT_KEY);
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "track"));
      UrlEncodedFormEntity urlentity = new UrlEncodedFormEntity(pairs, "UTF-8");
      urlentity.setContentEncoding("application/json");
      get.setEntity(urlentity);
      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        JsonArray tracks = jo.get("tracks").getAsJsonArray();
        Iterator<JsonElement> iterator = tracks.iterator();
        while (iterator.hasNext()) {
          JsonObject trackjo = iterator.next().getAsJsonObject();
          // make track class
          // String id, Boolean explicit, int popularity, int duration_ms,
          // List<String> artistIds, Boolean playable
          String id = trackjo.get("id").getAsString();
          boolean explicit = trackjo.get("explicit").getAsBoolean();
          int popularity = trackjo.get("popularity").getAsInt();
          int duration_ms = trackjo.get("duration_ms").getAsInt();
          JsonArray artists = trackjo.get("artists").getAsJsonArray();
          List<String> artist_ids = new ArrayList<>();
          Iterator<JsonElement> iterator2 = artists.iterator();
          while (iterator2.hasNext()) {
            artist_ids
                .add(iterator.next().getAsJsonObject().get("id").getAsString());
          }
          String album_id =
              trackjo.get("album").getAsJsonObject().get("id").getAsString();
        }
      } else {
        throw new ClientProtocolException("Failed to get tracks.");
      }
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return songs;
  }

}
