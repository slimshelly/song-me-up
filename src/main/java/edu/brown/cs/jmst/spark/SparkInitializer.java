package edu.brown.cs.jmst.spark;

import com.google.gson.Gson;

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

  public static final Gson GSON = new Gson();

  public static void setHandlers(FreeMarkerEngine freeMarker) {
    Spark.get("/main", new MainPage(), freeMarker);

    Spark.get("/test", new SigninPage(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    // Spark.get("/loggedin", new LoggedinHandler(), freeMarker);
    Spark.get(SpotifyAuthentication.REDIRECT_HANDLE, new CallbackHandler(),
        freeMarker);
    Spark.post("/refresh_token", new RefreshToken());
  }

}
