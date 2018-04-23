package edu.brown.cs.jmst.spotify;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.Album;
import edu.brown.cs.jmst.music.Artist;
import edu.brown.cs.jmst.music.AudioFeatures;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;

public class SpotifyQuery {

  public static List<Track> searchSong(String keywords, String access_token)
      throws Exception {
    // General.printVal("Keywords", keywords);
    List<Track> songs = new ArrayList<>();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "track"));
      pairs.add(new BasicNameValuePair("market", "from_token"));

      HttpGet get = new HttpGet("https://api.spotify.com/v1/search?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray tracks =
            jo.get("tracks").getAsJsonObject().get("items").getAsJsonArray();
        Iterator<JsonElement> iterator = tracks.iterator();
        while (iterator.hasNext()) {
          JsonObject trackjo = iterator.next().getAsJsonObject();
          String id = trackjo.get("id").getAsString();
          String name = trackjo.get("name").getAsString();
          boolean explicit = trackjo.get("explicit").getAsBoolean();
          int popularity = trackjo.get("popularity").getAsInt();
          int duration_ms = trackjo.get("duration_ms").getAsInt();
          JsonArray artists = trackjo.get("artists").getAsJsonArray();
          // General.printInfo(artists.toString());
          List<String> artist_ids = new ArrayList<>();
          Iterator<JsonElement> iterator2 = artists.iterator();
          while (iterator2.hasNext()) {
            JsonObject ajo = iterator2.next().getAsJsonObject();
            artist_ids.add(ajo.get("id").getAsString());
          }

          String album_id =
              trackjo.get("album").getAsJsonObject().get("id").getAsString();

          songs.add(new TrackBean(id, name, explicit, popularity, duration_ms,
              artist_ids, album_id));
        }
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException | ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    // for (Track t : songs) {
    // General.printInfo(t.toString());
    // }
    return songs;
  }

  /**
   * Requires an ID.
   *
   */
  public static AudioFeatures searchAudioFeatures(String keywords,
      String access_token) throws Exception {
    General.printVal("Keywords", keywords);
    AudioFeatures audioFeature = new AudioFeatures();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/search");
      get.setHeader("Authorization", "Bearer " + access_token);
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
          JsonObject afjo = iterator.next().getAsJsonObject();
          // make track class
          // String id, Boolean explicit, int popularity, int duration_ms,
          // List<String> artistIds, Boolean playable
          String id = afjo.get("id").getAsString();
          Float acousticness = afjo.get("acousticness").getAsFloat();
          Float danceability = afjo.get("danceability").getAsFloat();
          Integer duration_ms = afjo.get("duration_ms").getAsInt();
          Float energy = afjo.get("energy").getAsFloat();
          Float instrumentalness = afjo.get("instrumentalness").getAsFloat();
          Integer key = afjo.get("key").getAsInt();
          Float liveness = afjo.get("liveness").getAsFloat();
          Float loudness = afjo.get("loudness").getAsFloat();
          Integer mode = afjo.get("mode").getAsInt();
          Float speechiness = afjo.get("speechiness").getAsFloat();
          Float tempo = afjo.get("tempo").getAsFloat();
          Integer time_signature = afjo.get("time_signature").getAsInt();
          Float valence = afjo.get("valence").getAsFloat();

          audioFeature = new AudioFeatures(id, acousticness, danceability,
              duration_ms, energy, instrumentalness, key, liveness, loudness,
              mode, speechiness, tempo, time_signature, valence);
        }
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return audioFeature;
  }

  /**
   * Requires an ID.
   *
   */
  public static List<Artist> searchArtist(String keywords,
      String access_token) throws Exception {
    General.printVal("Keywords", keywords);
    
    List<Artist> artists = new ArrayList<>();
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/search");
      get.setHeader("Authorization", "Bearer " + access_token);
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "artist"));
      UrlEncodedFormEntity urlentity = new UrlEncodedFormEntity(pairs, "UTF-8");
      urlentity.setContentEncoding("application/json");
      get.setEntity(urlentity);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        JsonArray artistArray = jo.get("artists").getAsJsonArray();
        // returns a list of artists
        Iterator<JsonElement> iterator = artistArray.iterator();
        while (iterator.hasNext()) {
          JsonObject afjo = iterator.next().getAsJsonObject();
          // make artist class
          String id = afjo.get("id").getAsString();
          
          JsonArray genres = afjo.get("genres").getAsJsonArray();
          List<String> genreNames = new ArrayList<>();
          
              Iterator<JsonElement> iterator2 = genres.iterator();
              while (iterator2.hasNext()) {
                JsonObject ajo = iterator2.next().getAsJsonObject();
                genreNames.add(ajo.getAsString());
              }
 
          String name = afjo.get("name").getAsString();
          String type = afjo.get("type").getAsString();
          Integer popularity = afjo.get("popularity").getAsInt();
          
          artists.add(new Artist(genreNames, id, name, popularity, type));
        }
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return artists;
  }
  

  /**
   * Requires an ID.
   *
   */
  public static List<Album> searchAlbumss(String keywords,
      String access_token) throws Exception {
    General.printVal("Keywords", keywords);
    
    List<Album> albums = new ArrayList<>();
    
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/search");
      get.setHeader("Authorization", "Bearer " + access_token);
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
        JsonArray albumList = jo.get("album").getAsJsonArray();
        Iterator<JsonElement> iterator = albumList.iterator();
        while (iterator.hasNext()) {
          JsonObject afjo = iterator.next().getAsJsonObject();
          // make track class
          // String id, Boolean explicit, int popularity, int duration_ms,
          // List<String> artistIds, Boolean playable
          String id = afjo.get("id").getAsString();
          
          JsonArray genres = afjo.get("genres").getAsJsonArray();
          List<String> genreNames = new ArrayList<>();
          
          Iterator<JsonElement> iterator2 = genres.iterator();
          while (iterator2.hasNext()) {
            JsonObject ajo = iterator2.next().getAsJsonObject();
            genreNames.add(ajo.getAsString());
          }
          
          JsonArray tracks = afjo.get("tracks").getAsJsonArray();
          List<String> trackIds = new ArrayList<>();
          
          Iterator<JsonElement> iterator3 = tracks.iterator();
          while (iterator3.hasNext()) {
            JsonObject ajo = iterator3.next().getAsJsonObject();
            trackIds.add(ajo.getAsString());
          }
          
          String name = afjo.get("name").getAsString();
          String type = afjo.get("type").getAsString();
          Integer popularity = afjo.get("popularity").getAsInt();
          
          JsonArray artists = afjo.get("artists").getAsJsonArray();
          // General.printInfo(artists.toString());
          List<String> artistIds = new ArrayList<>();
          Iterator<JsonElement> iterator4 = artists.iterator();
          while (iterator4.hasNext()) {
            JsonObject ajo = iterator4.next().getAsJsonObject();
            artistIds.add(ajo.get("id").getAsString());
          }
          
          albums.add(new Album(artistIds, genreNames, id, name, popularity, trackIds, type));
          
        }
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return albums;
  }

}
