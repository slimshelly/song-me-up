package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public class Album extends Entity {
  
  public Album(List<String> artistIds, List<String> genres, String id, String name, Integer popularity, List<String> trackIds, String type) {
    this.artistIds = artistIds;
    this.genres = genres;
    this.id = id;
    this.name = name;
    this.popularity = popularity;
    this.trackIds = trackIds;
    this.type = type;
  }
  
  List<String> artistIds;
  List<String> genres;
  String id;
  String name;
  Integer popularity;
  List<String> trackIds;
  String type;
}
