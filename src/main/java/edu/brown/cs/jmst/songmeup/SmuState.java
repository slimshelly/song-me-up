package edu.brown.cs.jmst.songmeup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import edu.brown.cs.jmst.spotify.SpotifyException;

/**
 * Storage class for references to all active parties, party hosts, etc.
 *
 * @author Samuel Oliphant
 *
 */
public class SmuState {

  private List<String> listMessage;
  private String message;
  private Map<String, Party> parties =
      Collections.synchronizedMap(new HashMap<>());
  private Map<String, User> users =
      Collections.synchronizedMap(new HashMap<>());

  /**
   * Add a party to the set.
   *
   * @param party
   *          party to add
   * @throws SpotifyException
   * @throws PartyException
   */
  public Party startParty(User u) throws PartyException, SpotifyException {
    String key = SpotifyAuthentication.randomString(Party.ID_LENGTH);
    while (parties.containsKey(key)) {
      key = SpotifyAuthentication.randomString(Party.ID_LENGTH);
    }
    Party party = new Party(u, key);
    parties.put(key, party);
    return party;
  }

  /**
   * Get party with given id.
   *
   * @param id
   *          id to look for
   * @return party with that id
   */
  public Party getParty(String id) {
    return parties.get(id);
  }

  /**
   * Adds a user to a party.
   *
   * @param u
   *          user
   * @param partyId
   *          party id to add to
   * @throws PartyException
   *           if user is already in a party
   */
  public Party addPartyPerson(User u, String partyId)
      throws PartyException, IllegalArgumentException {
    // if (party_people_ids.contains(u.getId())) {
    // if(u.inParty()){
    // throw new IllegalArgumentException(
    // "User cannot be in two parties at once.");
    // } else {
    // assert parties.containsKey(partyId);
    // Party p = parties.get(partyId);
    // assert !p.getPartyGoerIds().contains(u.getId());
    // p.addPartyGoer(u);
    // party_people_ids.add(u.getId());
    // }
    if (!parties.containsKey(partyId)) {
      throw new IllegalArgumentException("Invalid party id.");
    }
    Party p = parties.get(partyId);
    p.addPartyGoer(u);
    return p;
  }

  /**
   * Allows a user to leave a party.
   *
   * @param u
   *          leaving user
   * @param partyId
   *          id of party they are leaving
   * @throws PartyException
   *           if u is not in a party.
   */
  public void leaveParty(User u, String partyId) throws PartyException {
    // assert party_people_ids.contains(u.getId());
    // assert parties.containsKey(partyId);
    // Party p = parties.get(partyId);
    // assert p.getPartyGoerIds().contains(u.getId());
    // p.removePartyGoer(u);
    // party_people_ids.remove(u.getId());
    if (!parties.containsKey(partyId)) {
      throw new IllegalArgumentException("Invalid party id.");
    }
    parties.get(partyId).removePartyGoer(u);
  }

  /**
   * End a party.
   *
   * @param id
   *          id of party to end.
   * @throws PartyException
   */
  public void endParty(String id) throws PartyException {
    // assert parties.containsKey(id);
    // Party p = parties.get(id);
    // for (String s : p.getPartyGoerIds()) {
    // party_people_ids.remove(s);
    // }
    // parties.remove(id);
    if (!parties.containsKey(id)) {
      throw new IllegalArgumentException("Invalid party id.");
    }
    Party p = parties.remove(id);
    p.end();
  }

  /**
   * Will either create a new user with the given id, or return the user with
   * the given id.
   *
   * @param id
   *          id of user
   * @return user with given id
   */
  public User getUser(String id) {
    // General.printVal("Users", Integer.toString(users.size()));
    if (users.containsKey(id)) {
      return users.get(id);
    } else {
      User user = new User();
      users.put(id, user);
      return user;
    }
  }

  /**
   * @return the listMessage
   */
  public List<String> getListMessage() {
    return listMessage;
  }

  /**
   * @param listMessage
   *          the listMessage to set
   */
  public void setListMessage(List<String> listMessage) {
    this.listMessage = listMessage;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message
   *          the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  public String getAuth() {
    if (users.size() == 0) {
      throw new IllegalStateException("No users logged in.");
    } else {
      Set<String> ids = users.keySet();
      String id = ids.iterator().next();
      User u = users.get(id);
      if (u.loggedIn()) {
        return u.getAuth();
      } else {
        throw new IllegalStateException("User not logged in.");
      }

    }
  }

}
