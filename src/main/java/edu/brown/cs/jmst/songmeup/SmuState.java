package edu.brown.cs.jmst.songmeup;

import java.util.List;
import java.util.Set;

import edu.brown.cs.jmst.party.Party;

/**
 * Storage class for references to all active parties, party hosts, etc.
 *
 * @author Samuel Oliphant
 *
 */
public class SmuState {

  private List<String> listMessage;
  private String message;
  private Set<Party> parties;

  /**
   * Add a party to the set.
   *
   * @param party
   *          party to add
   */
  public void addParty(Party party) {
    parties.add(party);
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

}
