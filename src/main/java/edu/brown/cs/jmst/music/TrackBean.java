package edu.brown.cs.jmst.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.beans.EntityBean;
import edu.brown.cs.jmst.spotify.SpotifyQuery;

public class TrackBean extends EntityBean implements Track {
  // NOTE: because this extends EntityBean, it already extends Entity. getId()
  // is a default method and does not need to be re-implemented.
  private boolean explicit;
  // private boolean playable;
  private int popularity;
  private int duration_ms;
  private String name;
  private List<String> artistIds;
  private String album_id;
  private String uri;
  private String album_cover;

  public TrackBean(String id, String name, Boolean explicit, int popularity,
      int duration_ms, List<String> artistIds, String album_id, String uri, String album_cover) {
    this.id = id;
    this.uri = uri;
    this.explicit = explicit;
    this.popularity = popularity;
    this.duration_ms = duration_ms;
    this.artistIds = artistIds;
    // this.playable = playable;
    this.album_id = album_id;
    this.name = name;
    this.album_cover = album_cover;
  }
  
  public TrackBean(JsonObject track, String access_token) throws IOException {
    this.id = track.get("id").getAsString();
    this.uri = track.get("uri").getAsString();
    this.explicit = track.get("explicit").getAsBoolean();
    this.popularity = track.get("popularity").getAsInt();
    this.duration_ms = track.get("duration_ms").getAsInt();
    	// 
    JsonArray artists = track.get("artists").getAsJsonArray();
    List<String> artist_ids = new ArrayList<>();
    Iterator<JsonElement> iterator2 = artists.iterator();
    while (iterator2.hasNext()) {
    		JsonObject ajo = iterator2.next().getAsJsonObject();
    		artist_ids.add(ajo.get("id").getAsString());
    }
    
    this.artistIds = artist_ids;
    this.album_id = track.get("album").getAsJsonObject().get("id").getAsString();
    // ALBUM ART!!!
    this.album_cover = SpotifyQuery.getAlbumArt(album_id, access_token);
    this.name = track.get("name").getAsString();
  }
  

  @Override
  public String getAlbumId() throws Exception {
    return this.album_id;
  }

  @Override
  public List<String> getArtistIds() throws Exception {
    //TODO: why are we returning a new list instead of this.artistIds?
    List<String> artists = new ArrayList<>();
    artists.addAll(this.artistIds);
    return artists;
  }

  @Override
  public boolean isExplicit() throws Exception {
    return this.explicit;
  }

  // @Override
  // public boolean isPlayable() throws Exception {
  // return this.playable;
  // }

  @Override
  public int getPopularity() throws Exception {
    return this.popularity;
  }

  @Override
  public int getDuration_ms() throws Exception {
    return this.duration_ms;
  }

  @Override
  public String getName() throws Exception {
    return this.name;
  }

  @Override
  public String getUri() throws Exception {
    return String.format("https://api.spotify.com/tracks/%s", this.id);
  }

  @Override
  public String toString() {
    return String.format("Name: '%s', Id: '%s', uri: '%s'", this.name, this.id, this.uri);
  }

  @Override
  public String getAlbumArt() {
    return album_cover;
  }

}
