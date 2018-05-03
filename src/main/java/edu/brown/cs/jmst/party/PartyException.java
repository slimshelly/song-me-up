package edu.brown.cs.jmst.party;

public class PartyException extends Exception {

  /**
   * Default id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Make an error with the given message.
   *
   * @param message
   *          error message
   */
  public PartyException(String message) {
    super(message);
  }
}
