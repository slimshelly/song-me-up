package edu.brown.cs.jmst.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.sockets.PartyWebSocket;
import edu.brown.cs.jmst.spotify.SpotifyException;

public class Party extends Entity {

  private boolean open;
  private User ph;
  private Set<User> partygoers;
  private Set<String> partyGoerIds; // Just user ID strings (excluding host!)
  private SongQueue songQueue; // Contains the algorithm's block system
  public static final int ID_LENGTH = 6;

  private Suggestion nowPlaying = null;
  private List<Suggestion> topVoted = new ArrayList<>();
  private List<Suggestion> songsPlayed = new ArrayList<>();
  private int songsPlayedPosition = -1;

  public Party(User host, String id) throws PartyException, SpotifyException {
    assert id.length() == ID_LENGTH;
    this.id = id;
    if (!host.isPremium()) {
      throw new SpotifyException("Host must have Spotify premium.");
    }
    host.joinParty(this.id);
    ph = host;
    partygoers = Collections.synchronizedSet(new HashSet<>());
    partyGoerIds = Collections.synchronizedSet(new HashSet<>());
    // total_votes = Collections.synchronizedMap(new HashMap<>());
    songQueue = new SongQueue();
    open = false;
  }

  public void addPartyGoer(User pg) throws PartyException {
    pg.joinParty(this.id);
    partygoers.add(pg);
    partyGoerIds.add(pg.getId());
    System.out.println(partygoers.size());
    System.out.println(partyGoerIds.size());
  }

  public void setOpen(boolean b) {
    this.open = b;
  }

  public boolean getOpen() {
    return this.open;
  }

  public void removePartyGoer(User u) throws PartyException {
    u.leaveParty();
    partygoers.remove(u);
    partyGoerIds.remove(u.getId());
    try {
      PartyWebSocket.signalLeft(this, u.getId());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getHostName() {
    return ph.getName() == null ? ph.getId() : ph.getName();
  }

  /**
   * @param song
   *          A Track to add to the current pool of suggestions
   * @param userId
   *          the ID string of the user submitting the suggestion
   * @throws PartyException
   */
  public SuggestResult suggest(Track song, String userId,
      AudioFeaturesSimple features) throws PartyException {
    return songQueue.suggest(song, userId, features);
  }

  public Suggestion getPrevSongToPlay() throws Exception {
    if (songsPlayed.isEmpty()) {
      throw new PartyException("No previous song to play!");
    }
    if (songsPlayed.size() < songsPlayedPosition) {
      throw new PartyException("Invalid position in list of played songs!");
    }
    this.nowPlaying = songsPlayed.remove(songsPlayedPosition);
    this.songsPlayedPosition -= 1;
    return nowPlaying;
  }

  public Suggestion getNextSongToPlay() throws Exception {
    this.nowPlaying = songQueue.getNextSongToPlay(nowPlaying);
    this.songsPlayed.add(nowPlaying);
    this.songsPlayedPosition += 1;
    if (this.topVoted.size() < 5) {
      this.topVoted.add(nowPlaying);
    } else {
      Collections.sort(topVoted);
      if (topVoted.get(topVoted.size() - 1).compareToScoreOnly(nowPlaying) > 0) {
        topVoted.remove(topVoted.size() - 1);
        topVoted.add(nowPlaying);
      }
    }
    return nowPlaying;
  }

  public List<String> getTopVotedIds() {
    List<String> ids = new ArrayList<>();
    for (Suggestion s: this.topVoted) {
      ids.add(s.getSong().getId());
    }
    return ids;
  }

  public Suggestion getNowPlaying() {
    return nowPlaying;
  }

  public Collection<Suggestion> voteOnSong(String userId, String songId,
      boolean isUpVote) throws PartyException {
    if (!partyGoerIds.contains(userId) && !userId.equals(getHostId())) {
      throw new PartyException("User not found in party.");
    }
    Suggestion voteOn = songQueue.getSuggestionInVoteBlockById(songId);
    return songQueue.vote(voteOn, userId, isUpVote);
  }

  // **??
  public void end() throws PartyException {

    try {
      System.out.println("signal end party");
      PartyWebSocket.signalLeaveParty(this);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // all users need to leave (be removed)
    for (User u : partygoers) {
      // set the user's currParty ID to null
      removePartyGoer(u);
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public String getHostId() {
    return ph.getId();
  }

  // Excludes the host's ID
  public Set<String> getPartyGoerIds() {
    return Collections.unmodifiableSet(partyGoerIds);
  }

  // Includes the host's ID
  public Set<String> getIds() {
    Set<String> idSet = new HashSet<>(partyGoerIds);
    idSet.add(ph.getId());
    return Collections.unmodifiableSet(idSet);
  }

  public JsonObject sendSuggestionToSuggBlock(Suggestion sugg)
      throws Exception {
    return sugg.toJson();
  }

  public JsonArray refreshSuggBlock() throws Exception {
    JsonArray suggBlock = new JsonArray();
    for (Suggestion s : songQueue.getSuggestedSongs()) {
      suggBlock.add(s.toJson());
    }
    return suggBlock;
  }

  public JsonArray refreshVoteBlock(String userId) throws Exception {
    JsonArray voteBlock = new JsonArray();
    PriorityBlockingQueue<Suggestion> voteSongs = songQueue.getSongsToVoteOn();
    Suggestion s;
    while ((s = voteSongs.poll()) != null) {
      voteBlock.add(s.toJson(userId));
    }
    return voteBlock;
  }

  public JsonArray refreshPlayBlock() throws Exception {
    JsonArray playBlock = new JsonArray();
    for (Suggestion s : songQueue.getSongsToPlaySoon()) {
      playBlock.add(s.toJson());
    }
    return playBlock;
  }

  public JsonObject refreshAllBlocks(String userId) throws Exception {
    JsonObject allBlocks = new JsonObject();
    allBlocks.add("sugg", refreshSuggBlock());
    allBlocks.add("vote", refreshVoteBlock(userId));
    allBlocks.add("play", refreshPlayBlock());
    return allBlocks;
  }

}
