package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public class Album extends Entity {
  
  public Album(String uri, List<String> artistIds, String id, String name,  List<String> trackIds, String type) {
    this.artistIds = artistIds;
    this.id = id;
    this.name = name;
    this.trackIds = trackIds;
    this.type = type;
    this.uri = uri;
  }
  
  String uri;
  List<String> artistIds;
  String id;
  String name;
  List<String> trackIds;
  String type;
  
  @Override
  public String toString() {
    return String.format("Name: '%s', Id: '%s', uri: '%s'", this.name, this.id, this.uri);
  }
}
