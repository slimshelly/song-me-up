package edu.brown.cs.jmst.music;

import java.util.List;

public class SpotifyPlaylist {
	
	private String description;
	private String id;
	private String url;
  
	private List<Track> songs;
	private String name;
	
	public SpotifyPlaylist(String description, String id, String url, List<Track> songs, String name) {
		this.setDescription(description);
		this.id = id;
		this.url = url;
		this.songs = songs;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<Track> getSongs() {
		return songs;
	}
	
	public void setSongs(List<Track> songs) {
		this.songs = songs;
	}
	
	public void addSong(Track song) {
		this.songs.add(song);
	}
	
	// for use when an admin adds a playlist to get the party started
	public void addSongs(List<Track> songs) {
		this.songs.addAll(songs);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
}
