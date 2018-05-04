package edu.brown.cs.jmst.spark;

import com.google.gson.Gson;

import edu.brown.cs.jmst.sockets.PartyWebSocket;
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

  public static void setHandlers(FreeMarkerEngine freeMarker, String root) {
    Spark.webSocket("/~jmst/songupdates", PartyWebSocket.class);
    Spark.get("/~jmst/main", new MainPage(), freeMarker);
    Spark.get("/~jmst/songmeup", new PreMainPage(), freeMarker);

    Spark.get("/~jmst/logout", new LogoutHandler(), freeMarker);
    Spark.get("/~jmst/login", new LoginHandler(), freeMarker);
    Spark.get(SpotifyAuthentication.REDIRECT_HANDLE, new CallbackHandler(),
        freeMarker);

    // PRE PLAYLIST PAGE
    Spark.get("/~jmst/form", new PartyFormHandler(), freeMarker); // leads to
                                                                  // joe's
    // party form, fill
    // out before
    // creating party
    Spark.get("/~jmst/host", new HostHandler(), freeMarker); // leads to joe's
                                                             // "you
    // have made a party
    // page" pre-playlist

    // PLAYLIST PAGES (2)
    Spark.get("/~jmst/admin", new AdminPageHandler(), freeMarker); // leads to
                                                                   // joe's
    // own playlist
    // page
    Spark.get("/~jmst/join", new JoinHandler(), freeMarker); // leads to user's
    // playlist page

    // GENERATE INFO FOR PLAYLIST PAGE
    Spark.post("/~jmst/suggestions", new SongSuggestor());
    Spark.post("/~jmst/refresh", new RefreshToken());
    Spark.post("/~jmst/playlist", new PlaylistHandler()); // reloads party
                                                          // playlist
    // on
    // page reload

    Spark.get("/~jmst/player", new PlayerPage(), freeMarker);
    Spark.get("/~jmst/error", new ErrorHandler(), freeMarker);

    // NOT RELEVANT, will delete later
    Spark.get("/~jmst/playlists", new MockPlaylist(), freeMarker);
  }

}
