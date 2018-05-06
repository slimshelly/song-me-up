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
import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;

public class SpotifyQuery {

  public static JsonObject getRawTrack(String song_id, String access_token)
      throws IOException {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet get = new HttpGet("https://api.spotify.com/v1/tracks/" + song_id);
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        return jo;
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

        JsonArray tracks =
            jo.get("tracks").getAsJsonObject().get("items").getAsJsonArray();

        return tracks;
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    }
  }
  
  

  public static List<Track> searchSong(String keywords, String access_token)
      throws Exception {
    List<Track> songs = new ArrayList<>();
    JsonArray ja = searchSongRaw(keywords, access_token);
    Iterator<JsonElement> iterator = ja.iterator();
    while (iterator.hasNext()) {
      JsonObject trackjo = iterator.next().getAsJsonObject();
      String id = trackjo.get("id").getAsString();
      String name = trackjo.get("name").getAsString();
      String uri = trackjo.get("uri").getAsString();
      boolean explicit = trackjo.get("explicit").getAsBoolean();
      int popularity = trackjo.get("popularity").getAsInt();
      int duration_ms = trackjo.get("duration_ms").getAsInt();
      JsonArray artists = trackjo.get("artists").getAsJsonArray();
      // General.printInfo(artists.toString());
      List<String> artist_ids = new ArrayList<>();
      List<String> artist_names = new ArrayList<>();
      
      Iterator<JsonElement> iterator2 = artists.iterator();
      while (iterator2.hasNext()) {
        JsonObject ajo = iterator2.next().getAsJsonObject();
        artist_ids.add(ajo.get("id").getAsString());
        artist_names.add(ajo.get("id").getAsString());
      }

      String album_id =
          trackjo.get("album").getAsJsonObject().get("id").getAsString();
      String album_art = getAlbumArt(album_id, access_token);

      songs.add(new TrackBean(id, name, explicit, popularity, duration_ms,
          artist_ids, artist_names, album_id, uri, album_art));
    }

    return songs;
  }

  /**
   * Accessor for song art. Returns a URL link to album art.
   *
   * @throws IOException
   */
  public static String getAlbumArt(String albumId, String access_token)
      throws IOException {
    String albumURL = "";
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("id", albumId));

      HttpGet get = new HttpGet("https://api.spotify.com/v1/albums/" + albumId
          + "?" + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray images = jo.get("images").getAsJsonArray();
        Iterator<JsonElement> iterator = images.iterator();
        while (iterator.hasNext()) {
          JsonObject image = iterator.next().getAsJsonObject();
          // Integer height = image.get("height").getAsInt();
          String url = image.get("url").getAsString();
          albumURL = url; // maybe make albumArt object later
          // Integer width = image.get("width").getAsInt();
          break; // first one will be widest
        }
      }
    } catch (UnsupportedEncodingException | ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return albumURL;
  }

  /**
   * Requires an ID.
   *
   */
  public static AudioFeatures searchAudioFeatures(String song_id,
      String access_token) throws Exception {
    
    AudioFeatures audioFeature = new AudioFeatures();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/audio-features/" + song_id);
      get.setHeader("Authorization", "Bearer " + access_token);
      List<BasicNameValuePair> pairs = new ArrayList<>();
      UrlEncodedFormEntity urlentity = new UrlEncodedFormEntity(pairs, "UTF-8");
      urlentity.setContentEncoding("application/json");
      get.setEntity(urlentity);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
//        
        String id = jo.get("id").getAsString();
//        Float acousticness = jo.get("acousticness").getAsFloat();   
//        Integer duration_ms = jo.get("duration_ms").getAsInt();
//        Float instrumentalness = jo.get("instrumentalness").getAsFloat();
//        Integer key = jo.get("key").getAsInt();
//        Float liveness = jo.get("liveness").getAsFloat();
//        Float loudness = jo.get("loudness").getAsFloat();
//        Integer mode = jo.get("mode").getAsInt();
//        Float speechiness =jo.get("speechiness").getAsFloat();
//        Float tempo = jo.get("tempo").getAsFloat();
//        Integer time_signature = jo.get("time_signature").getAsInt();
        
        Float valence = jo.get("valence").getAsFloat();
        Float energy = jo.get("energy").getAsFloat();
        Float danceability = jo.get("danceability").getAsFloat();
        
        audioFeature = new AudioFeatures(id, danceability,
            energy, valence);
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
  public static List<Artist> searchArtist(String keywords, String access_token)
      throws Exception {
    General.printVal("Keywords", keywords);

    List<Artist> artists = new ArrayList<>();

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get = new HttpPost("https://api.spotify.com/v1/search");
      get.setHeader("Authorization", "Bearer " + access_token);
      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "artist"));
      pairs.add(new BasicNameValuePair("limit", "10"));
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

          String uri = afjo.get("uri").getAsString();
          String name = afjo.get("name").getAsString();
          String type = afjo.get("type").getAsString();
          Integer popularity = afjo.get("popularity").getAsInt();

          artists.add(new Artist(uri, genreNames, id, name, popularity, type));
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

  public static List<Album> searchAlbum(String keywords, String access_token)
      throws Exception {

    List<Album> returnAlbums = new ArrayList<>();

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "album"));
      pairs.add(new BasicNameValuePair("market", "from_token"));
      pairs.add(new BasicNameValuePair("limit", "10"));
      
      HttpGet get = new HttpGet("https://api.spotify.com/v1/search?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray albums =
            jo.get("albums").getAsJsonObject().get("items").getAsJsonArray();
        Iterator<JsonElement> iterator = albums.iterator();

        while (iterator.hasNext()) {

          JsonObject albumjo = iterator.next().getAsJsonObject();

          // uri
          String uri = albumjo.get("uri").getAsString();
          // artist ids
          JsonArray artists = albumjo.get("artists").getAsJsonArray();
          List<String> artist_ids = new ArrayList<>();
          Iterator<JsonElement> iterator2 = artists.iterator();

          while (iterator2.hasNext()) {
            JsonObject ajo = iterator2.next().getAsJsonObject();
            artist_ids.add(ajo.get("id").getAsString());
          }

          // id
          String id = albumjo.get("id").getAsString();

          // name
          String name = albumjo.get("name").getAsString();

          // track ids

          List<String> track_ids = new ArrayList<>();

          // type
          String type = albumjo.get("type").getAsString();

          returnAlbums
              .add(new Album(uri, artist_ids, id, name, track_ids, type));

        }

      } else {
        throw new ClientProtocolException(
            "Failed to get albums: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return returnAlbums;
  }

  public static List<SpotifyPlaylist> getUserPlaylist(String access_token)
      throws Exception {
	System.out.println("Getting playlists");
    List<SpotifyPlaylist> returnPlaylists = new ArrayList<>();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("limit", "10"));
      pairs.add(new BasicNameValuePair("offset", "0"));
      
      HttpGet get = new HttpGet("https://api.spotify.com/v1/me/playlists?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
    	    System.out.println("got response");
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        
        System.out.println("got as json object");
        JsonArray playlists =
            jo.get("items").getAsJsonArray();
        Iterator<JsonElement> iterator = playlists.iterator();
        
        
        while (iterator.hasNext()) {
        	  System.out.println("in loop");
          JsonObject playlistjo = iterator.next().getAsJsonObject();
          System.out.println("got playlistjo");
          
          // uri
          String uri = playlistjo.get("uri").getAsString();
          System.out.println("got uri");
          
          // id
          String id = playlistjo.get("id").getAsString();
          System.out.println("got id");
          
          // images
          JsonArray playlist_images = playlistjo.get("images").getAsJsonArray();
          System.out.println("got images 1");
          List<String> images = new ArrayList<>();
          Iterator<JsonElement> iterator2 = playlist_images.iterator();
          while (iterator2.hasNext()) {
            JsonObject ajo = iterator2.next().getAsJsonObject();
            images.add(ajo.get("url").getAsString());
            System.out.println("got url");
          }
          System.out.println("got images");
          
          // name
          String name = playlistjo.get("name").getAsString();
          
          // tracks
          JsonObject tracks = playlistjo.get("tracks").getAsJsonObject();
         
          // number of tracks
          int num_of_tracks = tracks.get("total").getAsInt();
          
          // track ids - HALP
          
//          JsonArray playlist_tracks = tracks.get("items").getAsJsonArray();
//          System.out.println("got tracks");
          List<String> track_ids = new ArrayList<>();
//          Iterator<JsonElement> iterator3 = playlist_tracks.iterator();
//          System.out.println("made iterator");
//          while (iterator3.hasNext()) {
//            JsonObject ajo = iterator3.next().getAsJsonObject();
//            JsonObject ajo2 = ajo.get("track").getAsJsonObject();
//            track_ids.add(ajo2.get("id").getAsString());
//          }
//          System.out.println("got track ids");
          
          // type
          String type = playlistjo.get("type").getAsString();
          System.out.println("about to add playlist");
          returnPlaylists
              .add(new SpotifyPlaylist(id, uri, num_of_tracks, track_ids, name, type, images) );
          
        }

      } else {
        throw new ClientProtocolException(
            "Failed to get playlists: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    System.out.println("About to send playlist");
    return returnPlaylists;
  }

//
//  public static List<Track> getPlaylistTracks(String user_id, String playlist_id, String access_token)
//      throws Exception {
//
//    List<Track> returnTracks = new ArrayList<>();
//    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
//
//      List<BasicNameValuePair> pairs = new ArrayList<>();
//      pairs.add(new BasicNameValuePair("limit", "10"));
//      pairs.add(new BasicNameValuePair("offset", "0"));
//      
//      HttpGet get = new HttpGet("https://api.spotify.com/v1/me/playlists?"
//          + URLEncodedUtils.format(pairs, "UTF-8"));
//      get.setHeader("Authorization", "Bearer " + access_token);
//
//      HttpResponse response = client.execute(get);
//      if (response.getStatusLine().getStatusCode() == 200) {
//        String json_string = EntityUtils.toString(response.getEntity());
//        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
//
//        JsonArray playlists =
//            jo.get("items").getAsJsonArray();
//        Iterator<JsonElement> iterator = playlists.iterator();
//
//        while (iterator.hasNext()) {
//          JsonObject trackjo = iterator.next().getAsJsonObject();
//          String id = trackjo.get("id").getAsString();
//          String name = trackjo.get("name").getAsString();
//          String uri = trackjo.get("uri").getAsString();
//          boolean explicit = trackjo.get("explicit").getAsBoolean();
//          int popularity = trackjo.get("popularity").getAsInt();
//          int duration_ms = trackjo.get("duration_ms").getAsInt();
//          JsonArray artists = trackjo.get("artists").getAsJsonArray();
//          // General.printInfo(artists.toString());
//          List<String> artist_ids = new ArrayList<>();
//          List<String> artist_names = new ArrayList<>();
//          
//          Iterator<JsonElement> iterator2 = artists.iterator();
//          while (iterator2.hasNext()) {
//            JsonObject ajo = iterator2.next().getAsJsonObject();
//            artist_ids.add(ajo.get("id").getAsString());
//            artist_names.add(ajo.get("id").getAsString());
//          }
//
//          String album_id =
//              trackjo.get("album").getAsJsonObject().get("id").getAsString();
//          String album_art = getAlbumArt(album_id, access_token);
//
//          returnTracks.add(new TrackBean(id, name, explicit, popularity, duration_ms,
//              artist_ids, artist_names, album_id, uri, album_art));
//        }
//      }
//        
//      }
//    
//    
//    return returnTracks;
//  }

  
  public static List<SpotifyPlaylist> searchPlaylist(String keywords, String access_token)
      throws Exception {

    List<SpotifyPlaylist> returnPlaylists = new ArrayList<>();

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("q", keywords));
      pairs.add(new BasicNameValuePair("type", "playlist"));
      pairs.add(new BasicNameValuePair("market", "from_token"));
      pairs.add(new BasicNameValuePair("limit", "10"));

      HttpGet get = new HttpGet("https://api.spotify.com/v1/search?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray playlists =
            jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
        Iterator<JsonElement> iterator = playlists.iterator();

        while (iterator.hasNext()) {

          JsonObject playlistjo = iterator.next().getAsJsonObject();

          // uri
          String uri = playlistjo.get("uri").getAsString();

          // id
          String id = playlistjo.get("id").getAsString();
          
          // playlist images
          JsonArray playlist_images = playlistjo.get("images").getAsJsonArray();
          List<String> images = new ArrayList<>();
          Iterator<JsonElement> iterator2 = playlist_images.iterator();
          while (iterator2.hasNext()) {
            JsonObject ajo = iterator.next().getAsJsonObject();
            images.add(ajo.get("url").getAsString());
          }
          
          // name
          String name = playlistjo.get("name").getAsString();
          

          // tracks
          JsonObject tracks = playlistjo.get("tracks").getAsJsonObject();
         
          int num_of_tracks = tracks.get("total").getAsInt();
          
          List<String> track_ids = new ArrayList<>();
          
          // type
          String type = playlistjo.get("type").getAsString();

          returnPlaylists
              .add(new SpotifyPlaylist(id, uri, num_of_tracks, track_ids, name, type, images) );
          
        }

      } else {
        throw new ClientProtocolException(
            "Failed to get playlists: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
    } catch (UnsupportedEncodingException e) {
      throw e;
    } catch (ClientProtocolException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return returnPlaylists;
  }

}
