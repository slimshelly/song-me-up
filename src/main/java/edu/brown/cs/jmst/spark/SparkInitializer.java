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

  public static void setHandlers(FreeMarkerEngine freeMarker, boolean web) {
    Spark.webSocket("/songupdates", PartyWebSocket.class);

    Spark.get("/main", new MainPage(), freeMarker);
    Spark.get("/", new PreMainPage(), freeMarker);

    Spark.get("/logout", new LogoutHandler(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.get(SpotifyAuthentication.REDIRECT_HANDLE, new CallbackHandler(),
        freeMarker);

    // PRE PLAYLIST PAGE
    // below leads to joe's party form, fill out before creating party
    Spark.get("/form", new PartyFormHandler(), freeMarker);
    Spark.post("/spotifyPlaylists", new PlaylistSuggestor());
    // below leads to joe's "you have made a party page"
    Spark.get("/host", new HostHandler(), freeMarker);

    // PLAYLIST PAGES (2)
    // Spark.get(prefix + "/admin", new AdminPageHandler(), freeMarker);
    // below leads to user's playlist page
    Spark.get("/join", new JoinHandler(), freeMarker);

    // GENERATE INFO FOR PLAYLIST PAGE
    Spark.post("/suggestions", new SongSuggestor());
    Spark.post("/refresh", new RefreshToken());
    // below reloads party playlist on page reload
    Spark.post("/playlist", new PlaylistHandler());

    Spark.get("/player", new PlayerPage(), freeMarker);
    Spark.get("/error", new ErrorHandler(), freeMarker);
    Spark.get("/faq", new FAQPage(), freeMarker);

    // NOT RELEVANT, will delete later
    Spark.get("/playlists", new MockPlaylist(), freeMarker);
  }

}
