package edu.brown.cs.jmst.songmeup;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

import edu.brown.cs.jmst.command.Command;
import edu.brown.cs.jmst.command.Commander;
import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.Album;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.spotify.SpotifyQuery;
import edu.brown.cs.jmst.spotify.SpotifyQueryRaw;

/**
 * Stores the user facing & backend feature commands available to the project, including Song search, Album Search, Playlist search, etc
 */
public class SmuInputHandler implements Commander {

  private SmuState state;

  public SmuInputHandler(SmuState smuState) {
    state = smuState;
  }

  @Override
  public List<Command> getCommands() {
    List<Command> commands = new ArrayList<>();
    commands.add(new SongSearch());
    commands.add(new getPlaylistTracksRaw());
    commands.add(new getAudioFeature());
    commands.add(new GetPlaylistTracks());
    commands.add(new AlbumSearch());
    commands.add(new PlaylistSearch());
    commands.add(new UserPlaylistSearch());
    return commands;
  }
  
  private class SongSearch extends Command {

    public SongSearch() {
      super("song " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      List<Track> tracks =
          SpotifyQuery.searchSong(toks.get(0), state.getAuth());
      List<String> trackinfo = new ArrayList<>();
      for (Track t : tracks) {
        trackinfo.add(t.toString());
      }
      state.setListMessage(trackinfo);
    }

    @Override
    public void print() {

      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class getPlaylistTracksRaw extends Command {

    public getPlaylistTracksRaw() {
      super("getPlaylistTracksRaw " + "(.+) " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      
      JsonArray tracks =
          SpotifyQueryRaw.getPlaylistTracksRaw(toks.get(0), toks.get(1), state.getAuth());
      List<String> trackinfo = new ArrayList<>();
      
      for (int i = 0; i < tracks.size(); i++) {
        trackinfo.add(tracks.getAsJsonObject().toString());
      }
      state.setListMessage(trackinfo);
    }

    @Override
    public void print() {

      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class getAudioFeature extends Command {

    public getAudioFeature() {
      super("getAudioFeature " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      AudioFeaturesSimple audioFeature =
          SpotifyQuery.getSimpleFeatures(toks.get(0), state.getAuth());
      List<String> trackinfo = new ArrayList<>();
      trackinfo.add(
          "danciability is" + Float.toString(audioFeature.getDanceability()));
      trackinfo.add("energy is" + Float.toString(audioFeature.getEnergy()));
      trackinfo.add("valence is" + Float.toString(audioFeature.getValence()));
      state.setListMessage(trackinfo);
    }

    @Override
    public void print() {

      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class GetPlaylistTracks extends Command {

    public GetPlaylistTracks() {
      super("getPlaylistTracks " + "(.+) " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      List<Track> tracks = 
          SpotifyQuery.getPlaylistTracks(toks.get(0), toks.get(1), state.getAuth());
      
      List<String> trackinfo = new ArrayList<>();
      for (Track t : tracks) {
        trackinfo.add(t.toString());
      }
      state.setListMessage(trackinfo);
    }

    @Override
    public void print() {

      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class AlbumSearch extends Command {

    public AlbumSearch() {
      super("album " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      List<Album> albums =
          SpotifyQuery.searchAlbum(toks.get(0), state.getAuth());

      List<String> albuminfo = new ArrayList<>();

      for (Album t : albums) {
        albuminfo.add(t.toString());
        System.out.println(t.toString());
      }
      state.setListMessage(albuminfo);
    }

    @Override
    public void print() {
      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class UserPlaylistSearch extends Command {

    public UserPlaylistSearch() {
      super("userPlaylist" + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      List<SpotifyPlaylist> playlists =
          SpotifyQuery.getUserPlaylist(state.getAuth());

      System.out.println("here9");
      List<String> playlistinfo = new ArrayList<>();

      for (SpotifyPlaylist t : playlists) {
        playlistinfo.add(t.toString());
      }
      state.setListMessage(playlistinfo);
    }

    @Override
    public void print() {
      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class PlaylistSearch extends Command {

    public PlaylistSearch() {
      super("playlist" + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      List<SpotifyPlaylist> playlists =
          SpotifyQuery.searchPlaylist(toks.get(0), state.getAuth());

      List<String> playlistinfo = new ArrayList<>();

      for (SpotifyPlaylist t : playlists) {
        playlistinfo.add(t.toString());
      }
      state.setListMessage(playlistinfo);
    }

    @Override
    public void print() {
      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }

  private class AlbumSongSearch extends Command {

    public AlbumSongSearch() {
      super("album song " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      List<Track> tracks =
          SpotifyQuery.searchSong(toks.get(0), state.getAuth());
      List<String> trackinfo = new ArrayList<>();
      for (Track t : tracks) {
        trackinfo.add(t.toString());
      }
      state.setListMessage(trackinfo);
    }

    @Override
    public void print() {

      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }


}
