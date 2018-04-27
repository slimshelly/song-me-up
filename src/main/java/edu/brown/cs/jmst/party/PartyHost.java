package edu.brown.cs.jmst.party;

public class PartyHost extends User {

  public PartyHost(User u) {
    assert u.isPremium();
  }

}
