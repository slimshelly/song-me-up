package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public abstract class Track extends Entity {
  
  private Album album;
  private List<Artist> artists;
  private Boolean explicit;
  private String id;
  private Boolean playable;
  private Integer popularity;
  private Integer duration_ms;

  private String name;
  private String url;
}
