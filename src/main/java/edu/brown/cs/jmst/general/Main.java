package edu.brown.cs.jmst.general;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.jmst.command.CommandHandler;
import edu.brown.cs.jmst.command.Commander;
import edu.brown.cs.jmst.io.Repl;
import edu.brown.cs.jmst.songmeup.SmuInputHandler;
import edu.brown.cs.jmst.songmeup.SmuState;
import edu.brown.cs.jmst.spark.SparkInitializer;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import freemarker.template.Configuration;
import freemarker.template.Version;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Root class for the project. Initializes the state and handler, then starts
 * the REPL to handle user input.
 *
 * @author Samuel Oliphant
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final String DEFAULT_IP = "0.0.0.0";
  private static final Version DEFAULT_VERSION = new Version(2, 3, 20);
  public static final String DEFAULT_ROOT_URI = "http://localhost:4567";
  public static final String WEB_ROOT_URI = "https://cs.hiram.edu/~jmst";
  public static final int WEB_PORT = 4582;
  public static final String WEB_IP = "127.0.0.1";
  private final SmuInputHandler smuHandler =
      new SmuInputHandler(SmuState.getInstance());

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    parser.accepts("ip").withRequiredArg().ofType(String.class)
        .defaultsTo(DEFAULT_IP);
    parser.accepts("web");
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      try {
        if (options.has("web")) {
          runSparkServer(WEB_PORT, WEB_IP, WEB_ROOT_URI);
        } else {
          runSparkServer((int) options.valueOf("port"),
              (String) options.valueOf("ip"), DEFAULT_ROOT_URI);
        }

        runRepl();
        General.printInfo("Stopping server...");
        Spark.stop();
      } catch (Exception e) {
        General.printErr(e.getMessage());
        General.printInfo("Shutting down...");
      }
    } else {
      runRepl();
    }
  }

  private void runRepl() {
    List<Commander> commanders = new ArrayList<>();
    commanders.add(smuHandler);
    Repl.run(CommandHandler.getCommands(commanders), System.in);
  }

  private static FreeMarkerEngine createEngine() throws IOException {
    Configuration config = new Configuration(DEFAULT_VERSION);
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      String err = String.format("ERROR: Unable use %s for template loading.%n",
          templates);
      throw new IOException(err);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port, String ip, String rootUri)
      throws Exception {
    Spark.port(port);
    Spark.ipAddress(ip);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    SpotifyAuthentication.setRootUri(rootUri);
    SparkInitializer.setHandlers(freeMarker, "");
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
