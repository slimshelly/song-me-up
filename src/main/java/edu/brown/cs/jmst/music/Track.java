package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public abstract class Track extends Entity {

  private Album album;
  private List<String> artistIds;
  private Boolean explicit;
  private String id;
  private Boolean playable;
  private int popularity;
  private int duration_ms;

  private String name;

  public Track(String id, Boolean explicit, int popularity, int duration_ms,
      List<String> artistIds, Boolean playable) {
    this.id = id;
    this.explicit = explicit;
    this.popularity = popularity;
    this.duration_ms = duration_ms;
    this.artistIds = artistIds;
    this.playable = playable;
  }

  private String url;
}