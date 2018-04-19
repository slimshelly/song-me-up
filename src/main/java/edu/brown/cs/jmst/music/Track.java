package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public class Track extends Entity {
  
  Album album;
  List<Artist> artists;
  Boolean explicit;
  String id;
  Boolean playable;
  int popularity;
  
  String name;
  String url;
}