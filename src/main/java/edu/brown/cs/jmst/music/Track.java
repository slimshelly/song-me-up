package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public abstract class Track extends Entity {
  
  Album album;
  List<Artist> artists;
  Boolean explicit;
  String id;
  Boolean playable;
  Integer popularity;
  Integer duration_ms;

  
  String name;
  String url;


}