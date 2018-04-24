package edu.brown.cs.jmst.party;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Party {

  private PartyHost ph;
  private Set<PartyGoer> partygoers;
  private SongQueue suggestions;

  public Party(PartyHost host) {
    ph = host;
    partygoers = Collections.synchronizedSet(new HashSet<>());
    suggestions = new SongQueue();
  }

  public void addPartyGoer(PartyGoer pg) {
    partygoers.add(pg);
  }

  public String getHostName() {
    return ph.getName();
  }

  public Set<String> getPartyGoerIds() {
    Set<String> ids = new HashSet<>();
    for (PartyGoer p : partygoers) {
      ids.add(p.getId());
    }
    return ids;
  }

}
