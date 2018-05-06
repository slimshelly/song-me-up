package edu.brown.cs.jmst.party;

public class SuggestResult {

  public enum STATUS_TYPE {
    UNIQUE_SUGG, DUPLICATE_SUGG, VOTE
  }

  private STATUS_TYPE status;
  private Suggestion suggested;
  SuggestResult(STATUS_TYPE status, Suggestion suggested) {
    this.status = status;
    this.suggested = suggested;
  }
  SuggestResult(STATUS_TYPE status) {
    this.status = status;
  }

  public STATUS_TYPE getStatus() {
    return status;
  }

  public Suggestion getSuggested() {
    return suggested;
  }
}
