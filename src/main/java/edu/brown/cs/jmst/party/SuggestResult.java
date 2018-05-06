package edu.brown.cs.jmst.party;

/**
 * A class to contain the result of attempting to make a suggestion. Because
 * there are multiple possible outcomes of suggesting a song, each of which has
 * different desired behavior, this class makes use of an enum type to
 * distinguish the different cases while still treating them as a single type.
 */
public class SuggestResult {

  /**
   * enum to identify the different cases.
   * UNIQUE_SUGG (0): a unique Suggestion that should be added to the suggestion
   *    block.
   * DUPLICATE_SUGG (1): a repeat Suggestion that was found in the suggestion
   *    block. The frontend should refresh the suggestion block.
   * VOTE (2): either a unique or a repeat Suggestion that belongs in the voting
   *    block. The frontend should refresh the voting block.
   */
  public enum STATUS_TYPE {
    UNIQUE_SUGG, DUPLICATE_SUGG, VOTE
  }

  private STATUS_TYPE status;
  private Suggestion suggested;

  /**
   * Use this constructor when the desired frontend behavior is dependent on the
   * specific Suggestion that was created/modified.
   * @param status the identifying enum of the SuggestResult
   * @param suggested the Suggestion that was created/modified to send to the
   *                  frontend to update the display
   */
  SuggestResult(STATUS_TYPE status, Suggestion suggested) {
    this.status = status;
    this.suggested = suggested;
  }

  /**
   * Use this constructor when the desired frontend behavior is not dependent on
   * any specific Suggestion (eg, refresh a whole block).
   * @param status the identifying enum of the SuggestResult
   */
  SuggestResult(STATUS_TYPE status) {
    this.status = status;
  }

  /**
   * @return the identifying enum of the SuggestResult
   */
  public STATUS_TYPE getStatus() {
    return status;
  }

  /**
   * @return the Suggestion to modify/add
   */
  public Suggestion getSuggested() {
    return suggested;
  }
}
