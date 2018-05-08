package edu.brown.cs.jmst.party;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.spotify.SpotifyException;

public class Party extends Entity {

  private boolean open;
  private User ph;
  private Set<User> partygoers;
  private Set<String> partyGoerIds; // Just user ID strings (excluding host!)
  private SongQueue songQueue; // Contains the algorithm's block system
  public static final int ID_LENGTH = 6;

  private Suggestion nowPlaying = null;

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
    System.out.println("-------adding partiy goer " + pg);
    pg.joinParty(this.id);
    partygoers.add(pg);
    partyGoerIds.add(pg.getId());
    System.out.println(partygoers.size());
    System.out.println(partyGoerIds.size());
  }

  public void removePartyGoer(User u) throws PartyException {

    u.leaveParty();
    System.out.println("removing " + u.getName() + " from partigoers");
    partygoers.remove(u);
    System.out.println("removing " + u.getName() + "  from partyGoerIds");
    partyGoerIds.remove(u.getId());

    System.out.println("number of ids is " + this.getIds().size());
    System.out
        .println("number of party goers is " + this.getPartyGoerIds().size());
    // after removing party goer

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

  public Suggestion getNextSongToPlay() throws Exception {
    this.nowPlaying = songQueue.getNextSongToPlay(nowPlaying);
    return nowPlaying;
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

    System.out.println("number of ids is " + this.getIds().size());
    System.out.println("number of party goers is " + this.getIds().size());
    System.out.println("before ending party");
    for (String g : this.getIds()) {
      System.out.println(g);
    }

    System.out.println("******** number of party goers is ");
    for (String g : this.getPartyGoerIds()) {
      System.out.println(g);
    }

    // all users need to leave (be removed)
    for (User u : partygoers) {
      // set the user's currParty ID to null
      removePartyGoer(u);

    }

    System.out.println("after ending party");
    System.out.println("number of ids is " + this.getIds().size());
    for (String g : this.getIds()) {
      System.out.println(g);
    }
    System.out
        .println("number of party goers is " + this.getPartyGoerIds().size());
    for (String g : this.getPartyGoerIds()) {
      System.out.println(g);
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
