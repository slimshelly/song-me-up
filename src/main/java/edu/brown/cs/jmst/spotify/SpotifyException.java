package edu.brown.cs.jmst.spotify;

public class SpotifyException extends Exception {
  /**
   * Default id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Make exception with the input message.
   *
   * @param message
   *          message input.
   */
  public SpotifyException(String message) {
    super(message);
  }
}
