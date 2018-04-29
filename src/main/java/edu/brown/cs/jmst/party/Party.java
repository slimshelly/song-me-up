package edu.brown.cs.jmst.party;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.jmst.beans.Entity;
import edu.brown.cs.jmst.spotify.SpotifyException;

public class Party extends Entity {

  private User ph;
  private Set<User> partygoers;
  private SongQueue suggestions;
  public static final int ID_LENGTH = 6;

  public Party(User host, String id) throws PartyException, SpotifyException {
    assert id.length() == ID_LENGTH;
    this.id = id;
    if (!host.isPremium()) {
      throw new SpotifyException("Host must have Spotify premium.");
    }
    host.joinParty(this.id);
    ph = host;
    partygoers = Collections.synchronizedSet(new HashSet<>());
    suggestions = new SongQueue();
  }

  public void addPartyGoer(User pg) throws PartyException {
    pg.joinParty(this.id);
    partygoers.add(pg);
  }

  public void removePartyGoer(User u) throws PartyException {
    u.leaveParty();
    partygoers.remove(u);
  }

  public String getHostName() {
    return ph.getName();
  }

  // public Set<String> getPartyGoerIds() {
  // Set<String> ids = new HashSet<>();
  // for (User p : partygoers) {
  // ids.add(p.getId());
  // }
  // return ids;
  // }

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
