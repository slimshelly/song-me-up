package edu.brown.cs.jmst.spotify;

public class SpotifyAuthentication {
  public static final String CLIENT_ID = "4dbc736d594345a99c6e00bf776f5464";
  public static final String CLIENT_SECRET = "3a287186a58c4276804beb25a97f12de";
  public static final String REDIRECT_HANDLE = "/callback";
  public static final String ROOT_URI = "http://localhost:4567";
  public static final String REDIRECT_URI = ROOT_URI + REDIRECT_HANDLE;

  private static final String VALID =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public static String randomString(int length) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append(VALID.charAt((int) Math.floor(Math.random() * VALID.length())));
    }
    return sb.toString();
  }
}
