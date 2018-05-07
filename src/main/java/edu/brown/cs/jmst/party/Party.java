package edu.brown.cs.jmst.party;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.music.AudioFeatures;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.SongMeUpPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.spotify.SpotifyException;

public class Party extends Entity {

  private User ph;
  private Set<User> partygoers;
  private Set<String> userIds; // Just user ID strings (excluding host!)
  private SongQueue songQueue; // Contains the algorithm's block system
  private SongMeUpPlaylist partyPlaylist; // Holds current playlist state
  public static final int ID_LENGTH = 6;

  private Suggestion nowPlaying = null;

  public Party(User host, String id, SongMeUpPlaylist partyPlaylist)
      throws PartyException, SpotifyException {
    assert id.length() == ID_LENGTH;
    this.id = id;
    if (!host.isPremium()) {
      throw new SpotifyException("Host must have Spotify premium.");
    }
    host.joinParty(this.id);
    ph = host;
    partygoers = Collections.synchronizedSet(new HashSet<>());
    userIds = Collections.synchronizedSet(new HashSet<>());
    // total_votes = Collections.synchronizedMap(new HashMap<>());
    songQueue = new SongQueue();
    this.partyPlaylist = partyPlaylist;
  }

  public SongMeUpPlaylist getPlaylist() {
    return this.partyPlaylist;
  }

  public void addPartyGoer(User pg) throws PartyException {
    pg.joinParty(this.id);
    partygoers.add(pg);
    userIds.add(pg.getId());
  }

  public void removePartyGoer(User u) throws PartyException {
    u.leaveParty();
    partygoers.remove(u);
    userIds.remove(u.getId());
  }

  public String getHostName() {
    return ph.getName() == null ? ph.getId() : ph.getName();
  }

  /**
   * @param song A Track to add to the current pool of suggestions
   * @param userId the ID string of the user submitting the suggestion
   * @throws PartyException
   */
  public SuggestResult suggest(Track song, String userId,
                               AudioFeaturesSimple features)
      throws PartyException {
    return songQueue.suggest(song, userId, features);
  }

  public Suggestion getNextSongToPlay() throws Exception {
    this.nowPlaying = songQueue.getNextSongToPlay();
    return nowPlaying;
  }

  public Suggestion getNowPlaying() {
    return nowPlaying;
  }

  public Collection<Suggestion> voteOnSong(String userId, String songId,
      boolean isUpVote) throws PartyException {
    if (!userIds.contains(userId) && !userId.equals(getHostId())) {
      throw new PartyException("User not found in party.");
    }
    Suggestion voteOn = songQueue.getSuggestionInVoteBlockById(songId);
    return songQueue.vote(voteOn, userId, isUpVote);
  }

  public void end() throws PartyException {
    for (User u : partygoers) {
      u.leaveParty();
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
    return Collections.unmodifiableSet(userIds);
  }

  // Includes the host's ID
  public Set<String> getIds() {
    Set<String> idSet = new HashSet<>(userIds);
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
