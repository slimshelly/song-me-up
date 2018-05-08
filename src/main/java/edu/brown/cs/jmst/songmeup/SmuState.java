package edu.brown.cs.jmst.songmeup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.PartyException;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.party.UserException;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import edu.brown.cs.jmst.spotify.SpotifyException;

/**
 * Storage class for references to all active parties, party hosts, etc.
 *
 * @author Samuel Oliphant
 *
 */
public class SmuState {

  private static SmuState instance = null;

  private List<String> listMessage;
  private String message;
  private Map<String, Party> parties =
      Collections.synchronizedMap(new HashMap<>());
  private Map<String, User> users =
      Collections.synchronizedMap(new HashMap<>());

  private SmuState() {

  }
  
  private void printActiveParties() {
    
    for (String key : parties.keySet()) {
      System.out.println(key);
    }
    
  }

  public static SmuState getInstance() {
    if (instance == null) {
      instance = new SmuState();
      return instance;
    } else {
      return instance;
    }
  }

  /**
   * Add a party to the set.
   *
   * @param party
   *          party to add
   * @throws SpotifyException
   * @throws PartyException
   */
  public Party startParty(User u) throws PartyException, SpotifyException {
    String key = SpotifyAuthentication.randomReadableString(Party.ID_LENGTH);
    while (parties.containsKey(key)) {
      key = SpotifyAuthentication.randomReadableString(Party.ID_LENGTH);
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
    if (!parties.containsKey(partyId)) {
      throw new IllegalArgumentException("Invalid party id.");
    }
    Party p = parties.get(partyId);
    if (!partyId.equals(u.getCurrentParty())) {
      System.out.println("party " + partyId + " added " + u.getId());
      p.addPartyGoer(u);
    }
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
    if (!parties.containsKey(partyId)) {
      throw new PartyException("Invalid party id.");
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
    
    System.out.println("ending party now."); 
    if (!parties.containsKey(id)) {
      throw new IllegalArgumentException("Invalid party id.");
    }
    // get the party
    Party p = parties.get(id);
    
    // end the party 
    p.end();
    
    

    
    // remove party from list of active parties
    parties.remove(id);
    System.out.println("just removed party id from list of parties");
    
  }

  /**
   * Will return the user with the given Spotify id.
   *
   * @param id
   *          id of user
   * @return user with given id
   */
  public User getUser(String id) {
    // General.printVal("Users", Integer.toString(users.size()));
    return users.get(id);
  }

  public User addUser(String code) throws Exception, UserException {
    
    User u = new User();
    u.logIn(code);
    
    System.out.println("adding a player with ID: " + u.getId());
    
    if (users.containsKey(u.getId())) {
      throw new UserException("User already exists.");
    }
    users.put(u.getId(), u);
    return u;
  }

  public void removeUser(String id) throws PartyException {
    User u = getUser(id);
    if (u != null) {
      if (u.getCurrentParty() != null) {
        leaveParty(u, u.getCurrentParty());
      }
      users.remove(id);
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
