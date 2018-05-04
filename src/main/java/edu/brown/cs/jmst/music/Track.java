package edu.brown.cs.jmst.music;

import java.util.List;

public interface Track {
  String getAlbumId() throws Exception;

  List<String> getArtistIds() throws Exception;

  boolean isExplicit() throws Exception;

  String getId();

  // boolean isPlayable() throws Exception;

  int getPopularity() throws Exception;

  int getDuration_ms() throws Exception;

  String getName() throws Exception;

  String getUri() throws Exception;
  
  String getAlbumArt() throws Exception;
  
  List<String> getArtistNames() throws Exception;

}