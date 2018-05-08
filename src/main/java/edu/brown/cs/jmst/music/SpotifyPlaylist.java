package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public class SpotifyPlaylist extends Entity {
	
  private String owner_id;
	private String id;
	private String uri;
	private String type;
	private int num_of_tracks;
	private List<String> track_ids;
	private String name;
	private List<String> playlist_images;
	
	public SpotifyPlaylist(String owner_id, String id, String uri, int num_of_tracks, List<String> track_ids, String name, String type, List<String> playlist_images) {
		this.owner_id = owner_id;
		this.id = id;
		this.uri = uri;
		this.num_of_tracks = num_of_tracks;
		this.track_ids = track_ids;
		this.name = name;
		this.type = type;
		this.playlist_images = playlist_images;
	}
	
	public String getOwnerId() {
	  return owner_id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrl() {
		return this.uri;
	}
	
	public String getType() {
	  return this.type;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int getNumOfTracks() {
	  return this.num_of_tracks;
	}
	
	public List<String> getSongs() {
		return this.track_ids;
	}
	
	public void setSongs(List<String> track_ids) {
		this.track_ids = track_ids;
	}
	
	public void addSong(String song_id) {
		this.track_ids.add(song_id);
	}
	
	// for use when an admin adds a playlist to get the party started
	public void addSongs(List<String> songs) {
		this.track_ids.addAll(songs);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("Name: '%s', OwnerId: '%s', Id: '%s', uri: '%s'", this.name, this.owner_id, this.id, this.uri);
	}

	public List<String> getPlaylistImages() {
		return playlist_images;
	}

	public void setPlaylistImages(List<String> playlist_images) {
		this.playlist_images = playlist_images;
	}
	
 
}
