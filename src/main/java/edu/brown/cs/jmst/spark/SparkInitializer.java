package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import edu.brown.cs.jmst.sockets.PartyWebSocket;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Adds all of the various spark handlers.
 *
 */
public class SparkInitializer {

  public static final Gson GSON = new Gson();

  public static void setHandlers(FreeMarkerEngine freeMarker, boolean web) {
    Spark.webSocket("/songupdates", PartyWebSocket.class);
    Spark.webSocketIdleTimeoutMillis(1000 * 60 * 60);
    Spark.get("/main", new MainPage(), freeMarker);
    Spark.get("/songmeup", new PreMainPage(), freeMarker);
    Spark.redirect.get("/", "/songmeup");

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
    Spark.get("/join", new JoinHandler(), freeMarker);

    // INSERT SEED PLAYLIST INTO PLAYLIST PAGE
    Spark.post("/seedPlaylist", new SeedPlaylist());

    // GENERATE INFO FOR PLAYLIST PAGE
    Spark.post("/suggestions", new SongSuggestor());
    Spark.post("/refresh", new RefreshToken());
    // below reloads party playlist on page reload
    Spark.post("/playlist", new PlaylistHandler());

    Spark.get("/player", new PlayerPage(), freeMarker);
    Spark.get("/error", new ErrorHandler(), freeMarker);
    Spark.get("/faq", new FAQPage(), freeMarker);
    List<BasicNameValuePair> pair = new ArrayList<>();
    pair.add(new BasicNameValuePair("error",
        SparkErrorEnum.PAGE_NOT_FOUND.toString()));
    // Spark.redirect.get("/*", SpotifyAuthentication.getRootUri() + "/error?"
    // + URLEncodedUtils.format(pair, "UTF-8"));
  }

}
