package edu.brown.cs.jmst.party;

import java.util.ArrayList;
import java.util.List;

public class Party {

  private PartyHost ph;
  private List<PartyGoer> partygoers;
  private SongQueue suggestions;

  public Party(PartyHost host) {
    ph = host;
    partygoers = new ArrayList<>();
    suggestions = new SongQueue();
  }

  public void addPartyGoer(PartyGoer pg) {
    partygoers.add(pg);
  }

}
