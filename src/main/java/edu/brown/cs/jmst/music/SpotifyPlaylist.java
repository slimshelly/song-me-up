package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.Entity;

public class SpotifyPlaylist extends Entity {
	
	private String id;
	private String uri;
	private String type;
	private String track_link;
	private List<String> track_ids;
	private String name;
	
	public SpotifyPlaylist(String id, String uri, String track_link, List<String> track_ids, String name, String type) {
		this.id = id;
		this.uri = uri;
		this.track_link = track_link;
		this.track_ids = track_ids;
		this.name = name;
		this.type = type;
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
	
	public String getLink() {
	  return this.track_link;
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
		return String.format("Name: '%s', Id: '%s', uri: '%s'", this.name, this.id, this.uri);
	}
	
 
}
