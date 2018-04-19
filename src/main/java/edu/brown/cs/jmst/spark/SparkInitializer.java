package edu.brown.cs.jmst.spark;

import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Adds all of the various spark handlers.
 *
 * @author Samuel Oliphant
 *
 */
public class SparkInitializer {

  public static void setHandlers(FreeMarkerEngine freeMarker) {
    Spark.get("/test", new SigninPage(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.get(SpotifyAuthentication.REDIRECT_HANDLE, new CallbackHandler(),
        freeMarker);
    Spark.get("/refresh_token", new RefreshToken(), freeMarker);
  }

}
