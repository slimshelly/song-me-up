package edu.brown.cs.jmst.spark;

public enum SparkErrorEnum {
  ALREADY_IN_PARTY("already_in_party"), NOT_IN_PARTY("not_in_party"),
  INVALID_PARTY_ID("invalid_party_id"), NEEDS_PREMIUM("needs_premium"),
  INVALID_TOKEN("invalid_token"), STATE_MISMATCH("state_mismatch"),
  CLIENT_ERROR("client_error");

  private String errstring;

  private SparkErrorEnum(String err) {
    this.errstring = err;
  }

  public static String errHelp(String err) {
    String retval = null;
    switch (err) {
      case "already_in_party":
        retval =
            "Sorry, we do not support joining two parties at the same time!";
        break;
      case "not_in_party":
        retval = "No need to leave a party if you aren't in one!";
        break;
      case "invalid_party_id":
        retval = "Oops! We couldn't find a party with that ID.";
        break;
      case "needs_premium":
        retval = "Sorry, you need Spotify premium to host a party :/";
        break;
      case "invalid_token":
        retval =
            "Hmm we were not able to access your Spotify information. Perhaps try logging in again?";
        break;
      case "state_mismatch":
        retval =
            "Something odd happened as you were logging in. Give it another go!";
        break;
      case "client_error":
        retval = "We aren't sure what went wrong... Maybe a Spotify error.";
        break;
      default:
        retval = "We really aren't sure what went wrong... Try again?";
        break;
    }
    return retval;
  }

  @Override
  public String toString() {
    return errstring;
  }
}
