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
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.music.TrackBean;

/**
 * A class for converting spotify queries into BACKEND objects.
 */
public class SpotifyQuery {

  public static List<Track> searchSong(String keywords, String access_token)
      throws Exception {
    List<Track> songs = new ArrayList<>();
    JsonArray ja = SpotifyQueryRaw.searchSongRaw(keywords, access_token);
    for (JsonElement aJa : ja) {
      JsonObject trackjo = aJa.getAsJsonObject();
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

      for (JsonElement artist : artists) {
        JsonObject ajo = artist.getAsJsonObject();
        artist_ids.add(ajo.get("id").getAsString());
        artist_names.add(ajo.get("name").getAsString());
      }

      String album_id =
          trackjo.get("album").getAsJsonObject().get("id").getAsString();
      String album_art = getAlbumArt(album_id, access_token);

      songs.add(new TrackBean(id, name, explicit, popularity, duration_ms,
          artist_ids, artist_names, album_id, uri, album_art));
    }

    return songs;
  }

  public static List<Track> getTracksFromSeed(List<String> seeds,
      String access_token) throws Exception {
    List<Track> list = new ArrayList<>();
    JsonArray ja = SpotifyQueryRaw.getSuggestionsFromSeed(seeds, access_token);
    for (JsonElement aJa : ja) {
      JsonObject trackjo = aJa.getAsJsonObject();
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

      for (JsonElement artist : artists) {
        JsonObject ajo = artist.getAsJsonObject();
        artist_ids.add(ajo.get("id").getAsString());
        artist_names.add(ajo.get("name").getAsString());
      }

      String album_id =
          trackjo.get("album").getAsJsonObject().get("id").getAsString();
      String album_art = getAlbumArt(album_id, access_token);

      list.add(new TrackBean(id, name, explicit, popularity, duration_ms,
          artist_ids, artist_names, album_id, uri, album_art));
    }
    return list;
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
        for (JsonElement image1 : images) {
          JsonObject image = image1.getAsJsonObject();
          // Integer height = image.get("height").getAsInt();
          albumURL = image.get("url").getAsString(); // maybe make albumArt
                                                     // object later
          // Integer width = image.get("width").getAsInt();
          break; // first one will be widest
        }
      }
    } catch (IOException e) {
      throw e;
    }
    return albumURL;
  }

  public static AudioFeaturesSimple getSimpleFeatures(String song_id,
      String access_token) throws IOException {
    Float danceability;
    Float energy;
    Float valence;
    String id;

    AudioFeatures audioFeature = new AudioFeatures();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet get =
          new HttpGet("https://api.spotify.com/v1/audio-features/" + song_id);
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        valence = jo.get("valence").getAsFloat();
        energy = jo.get("energy").getAsFloat();
        danceability = jo.get("danceability").getAsFloat();
        id = jo.get("id").getAsString();

        System.out.println("audio3");
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

    // TODO: query spotify and get ONLY THESE THREE FIELDS
    return new AudioFeaturesSimple("", danceability, energy, valence);

  }

  /**
   * Requires an ID.
   *
   */
  public static AudioFeatures searchAudioFeatures(String song_id,
      String access_token) throws Exception {

    AudioFeatures audioFeature = new AudioFeatures();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost get =
          new HttpPost("https://api.spotify.com/v1/audio-features/" + song_id);
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

        Float valence = jo.get("valence").getAsFloat();
        Float energy = jo.get("energy").getAsFloat();
        Float danceability = jo.get("danceability").getAsFloat();

        audioFeature = new AudioFeatures(id, danceability, energy, valence);
      } else {
        throw new ClientProtocolException(
            "Failed to get tracks: " + response.getStatusLine().getStatusCode()
                + " " + response.toString());
      }
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
        for (JsonElement anArtistArray : artistArray) {
          JsonObject afjo = anArtistArray.getAsJsonObject();
          // make artist class
          String id = afjo.get("id").getAsString();

          JsonArray genres = afjo.get("genres").getAsJsonArray();
          List<String> genreNames = new ArrayList<>();

          for (JsonElement genre : genres) {
            JsonObject ajo = genre.getAsJsonObject();
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

        for (JsonElement album : albums) {

          JsonObject albumjo = album.getAsJsonObject();

          // uri
          String uri = albumjo.get("uri").getAsString();
          // artist ids
          JsonArray artists = albumjo.get("artists").getAsJsonArray();
          List<String> artist_ids = new ArrayList<>();

          for (JsonElement artist : artists) {
            JsonObject ajo = artist.getAsJsonObject();
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
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray playlists = jo.get("items").getAsJsonArray();

        for (JsonElement playlist : playlists) {
          JsonObject playlistjo = playlist.getAsJsonObject();
          // uri
          String uri = playlistjo.get("uri").getAsString();

          JsonObject owner = playlistjo.get("owner").getAsJsonObject();
          String owner_id = owner.get("id").getAsString();
          // id
          String id = playlistjo.get("id").getAsString();

          // images
          JsonArray playlist_images = playlistjo.get("images").getAsJsonArray();
          List<String> images = new ArrayList<>();
          for (JsonElement playlist_image : playlist_images) {
            JsonObject ajo = playlist_image.getAsJsonObject();
            images.add(ajo.get("url").getAsString());
          }

          // name
          String name = playlistjo.get("name").getAsString();

          // tracks
          JsonObject tracks = playlistjo.get("tracks").getAsJsonObject();

          // number of tracks
          int num_of_tracks = tracks.get("total").getAsInt();

          List<String> track_ids = new ArrayList<>();
          // type
          String type = playlistjo.get("type").getAsString();
          returnPlaylists.add(new SpotifyPlaylist(owner_id, id, uri,
              num_of_tracks, track_ids, name, type, images));

        }

      } else {
        throw new ClientProtocolException("Failed to get playlists: "
            + response.getStatusLine().getStatusCode() + " "
            + response.toString());
      }
    }
    System.out.println("About to send playlist");
    return returnPlaylists;
  }

  public static List<Track> getPlaylistTracks(String owner_id,
      String playlist_id, String access_token) throws Exception {

    List<Track> returnTracks = new ArrayList<>();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("limit", "10"));

      HttpGet get = new HttpGet("https://api.spotify.com/v1/users/" + owner_id
          + "/playlists/" + playlist_id + "/tracks?"
          + URLEncodedUtils.format(pairs, "UTF-8"));
      get.setHeader("Authorization", "Bearer " + access_token);

      HttpResponse response = client.execute(get);

      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();

        JsonArray tracks = jo.get("items").getAsJsonArray();
        Iterator<JsonElement> iterator = tracks.iterator();

        while (iterator.hasNext()) {

          JsonObject currjo = iterator.next().getAsJsonObject();

          JsonObject trackjo = currjo.get("track").getAsJsonObject();

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
            artist_names.add(ajo.get("name").getAsString());
          }

          String album_id =
              trackjo.get("album").getAsJsonObject().get("id").getAsString();
          String album_art = getAlbumArt(album_id, access_token);

          returnTracks
              .add(new TrackBean(id, name, explicit, popularity, duration_ms,

                  artist_ids, artist_names, album_id, uri, album_art));
        }
      }
    }
    return returnTracks;
  }

  public static List<SpotifyPlaylist> searchPlaylist(String keywords,
      String access_token) throws Exception {

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

          JsonObject owner = playlistjo.get("owner").getAsJsonObject();
          String owner_id = owner.get("id").getAsString();
          // id

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

          returnPlaylists.add(new SpotifyPlaylist(owner_id, id, uri,
              num_of_tracks, track_ids, name, type, images));

        }

      } else {
        throw new ClientProtocolException("Failed to get playlists: "
            + response.getStatusLine().getStatusCode() + " "
            + response.toString());
      }
    }
    return returnPlaylists;
  }

}
