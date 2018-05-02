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
  private static final Version DEFAULT_VERSION = new Version(2, 3, 20);
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
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      try {
        runSparkServer((int) options.valueOf("port"));
        runRepl();
        General.printInfo("Stopping server...");
        Spark.stop();
      } catch (IOException e) {
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

  private void runSparkServer(int port) throws IOException {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    SparkInitializer.setHandlers(freeMarker);
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
