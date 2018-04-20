package edu.brown.cs.jmst.music;

import edu.brown.cs.jmst.beans.EntityBean;

import java.util.List;

public class TrackBean extends EntityBean implements Track {
  //NOTE: because this extends EntityBean, it already extends Entity. getId() is
  //       a default method and does not need to be re-implemented.
  private boolean explicit;
  private boolean playable;
  private int popularity;
  private int duration_ms;
  private String name;
  private String url;
  private List<String> artistIds;
  private Album album;

  public TrackBean(String id, Boolean explicit, int popularity, int duration_ms,
                   List<String> artistIds, Boolean playable) {
    this.id = id;
    this.explicit = explicit;
    this.popularity = popularity;
    this.duration_ms = duration_ms;
    this.artistIds = artistIds;
    this.playable = playable;
  }

  @Override
  public Album getAlbum() throws Exception {
    //TODO: initialize album
    return this.album;
  }

  @Override
  public List<String> getArtistIds() throws Exception {
    return this.artistIds;
  }

  @Override
  public Boolean isExplicit() throws Exception {
    return this.isExplicit();
  }

  @Override
  public Boolean isPlayable() throws Exception {
    return this.isPlayable();
  }

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
  public String getUrl() throws Exception {
    return this.url;
  }
}
