package edu.brown.cs.jmst.party;

import java.util.*;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.music.SongMeUpPlaylist;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.spotify.SpotifyException;

public class Party extends Entity {

  private User ph;
  private Set<User> partygoers;
  private Set<String> userIds; // just user ID strings
  private SongQueue songQueue; // object to hold all songQueue. NOTE: at this point, SongQueue does much more than hold songQueue
  private SongMeUpPlaylist partyPlaylist; // object to hold current playlist
                                          // state
  private Map<String, Map<String, Integer>> votes; // maps user ids to maps from
                                                   // songs to votes.
  private Map<String, Integer> total_votes; // maps song ids to total votes.
  public static final int ID_LENGTH = 6;

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
    votes = Collections.synchronizedMap(new HashMap<>());
    total_votes = Collections.synchronizedMap(new HashMap<>());
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
    votes.put(pg.getId(), Collections.synchronizedMap(new HashMap<>()));
  }

  public void removePartyGoer(User u) throws PartyException {
    u.leaveParty();
    partygoers.remove(u);
    userIds.remove(u.getId());
  }

  public String getHostName() {
    return ph.getName();
  }
  
  /**
   * @param song A Track to add to the current pool of suggestions
   * @param userId the ID string of the user submitting the suggestion
   * @throws PartyException
   */
  public Suggestion suggest(Track song, String userId) throws PartyException {
    return songQueue.suggest(song, userId);
  }

  public Collection<Suggestion> getSuggestedSongs() {
    return songQueue.getSuggestedSongs();
  }
  
  public Collection<Suggestion> getSongsToVoteOn() {
    return songQueue.getSongsToVoteOn();
  }

  public Collection<Suggestion> getSongsToPlay() {
    return songQueue.getSongsToPlay();
  }

  public Collection<Suggestion> voteOnSong(String userId, String songId, boolean isUpVote)
          throws PartyException {
    if (!userIds.contains(userId)) {
      throw new PartyException("User not found in party.");
    }
    Suggestion voteOn = songQueue.getSuggestionById(songId);
    return songQueue.vote(voteOn, userId, isUpVote);
  }

//  public int voteOnSong(String userid, String songid, boolean vote)
//      throws PartyException {
//    if (!votes.containsKey(userid)) {
//      throw new PartyException("User not found in party.");
//    } else {
//      Map<String, Integer> user_votes = votes.get(userid);
//      if (!user_votes.containsKey(songid)) {
//        user_votes.put(songid, 0);
//      }
//      if (!total_votes.containsKey(songid)) {
//        total_votes.put(songid, 0);
//      }
//      int val = user_votes.get(songid);
//      int newval;
//      if (vote) {
//        if (val != 1) {
//          newval = 1;
//        } else {
//          newval = 0;
//        }
//      } else {
//        if (val != -1) {
//          newval = -1;
//        } else {
//          newval = 0;
//        }
//      }
//      user_votes.put(songid, newval);
//      total_votes.put(songid, total_votes.get(songid) + (newval - val));
//      return total_votes.get(songid);
//    }
//  }

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

}
