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

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;

public class SpotifyQuery {

  public static List<Track> searchSong(String keywords) throws Exception {
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
          String name = trackjo.get("name").getAsString();
          boolean explicit = trackjo.get("explicit").getAsBoolean();
          int popularity = trackjo.get("popularity").getAsInt();
          int duration_ms = trackjo.get("duration_ms").getAsInt();
          boolean playable = trackjo.get("is_playable").getAsBoolean();
          JsonArray artists = trackjo.get("artists").getAsJsonArray();
          List<String> artist_ids = new ArrayList<>();
          Iterator<JsonElement> iterator2 = artists.iterator();
          while (iterator2.hasNext()) {
            artist_ids
                .add(iterator.next().getAsJsonObject().get("id").getAsString());
          }

          String album_id =
              trackjo.get("album").getAsJsonObject().get("id").getAsString();

          songs.add(new TrackBean(id, name, explicit, popularity, duration_ms,
              artist_ids, playable, album_id));
        }
      } else {
        throw new ClientProtocolException("Failed to get tracks.");
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    for (Track t : songs) {
      General.printInfo(t.toString());
    }
    return songs;
  }

}
