package edu.brown.cs.jmst.songmeup;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.jmst.command.Command;
import edu.brown.cs.jmst.command.Commander;
import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.music.Album;
import edu.brown.cs.jmst.music.SpotifyPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.spotify.SpotifyQuery;

/**
 * Handles REPL input
 *
 * @author Samuel Oliphant
 *
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
    commands.add(new AlbumSearch());
    commands.add(new PlaylistSearch());
    commands.add(new MarcoPoloCommand());
    commands.add(new CapsCommand());
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
      // TODO Auto-generated method stub
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
      // TODO Auto-generated method stub
      for (String s : state.getListMessage()) {
        General.printInfo(s);
      }
    }

  }


  /**
   * This is just an example command. The class defines the regex to match in
   * the constructor and performs execution in the execute method. If the
   * execute method is called then the print method, the print method should
   * print the results.
   *
   * @author Samuel Oliphant
   *
   */
  private class MarcoPoloCommand extends Command {

    public MarcoPoloCommand() {
      super("marco " + General.REG_POSINT + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      int numPolos = Integer.parseInt(toks.get(0));
      state.setListMessage(SmuExecutor.getPolos(numPolos));
    }

    @Override
    public void print() {
      for (String p : state.getListMessage()) {
        General.printInfo(p);
      }
    }
  }

  private class CapsCommand extends Command {

    public CapsCommand() {
      super("caps " + "(.+)" + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      state.setMessage(SmuExecutor.toCaps(toks.get(0)));
    }

    @Override
    public void print() {
      General.printInfo(state.getMessage());
    }
  }

}
