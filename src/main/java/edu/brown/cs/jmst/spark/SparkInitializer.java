package edu.brown.cs.jmst.spark;

import com.google.gson.Gson;

import edu.brown.cs.jmst.songmeup.SmuState;
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

  public static void setHandlers(FreeMarkerEngine freeMarker, SmuState state) {
    Spark.get("/main", new MainPage(state), freeMarker);

    Spark.get("/songmeup", new PreMainPage(), freeMarker);

    Spark.get("/logout", new LogoutHandler(state), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.get(SpotifyAuthentication.REDIRECT_HANDLE, new CallbackHandler(state),
        freeMarker);

    Spark.get("/host", new HostHandler(state), freeMarker);
    Spark.get("/join", new JoinHandler(state), freeMarker);

    Spark.get("/player", new PlayerPage(), freeMarker);

    Spark.get("/error", new ErrorHandler(state), freeMarker);

    // for purposes of editing join page
    Spark.get("/playlist", new MockPlaylist(), freeMarker);
    // for purposes of linking playlist to join page
    Spark.post("/playlist", new PlaylistHandler(state));
  }

}
