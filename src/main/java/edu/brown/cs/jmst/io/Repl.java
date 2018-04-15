package edu.brown.cs.jmst.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import edu.brown.cs.jmst.command.Command;
import edu.brown.cs.jmst.command.CommandHandler;
import edu.brown.cs.jmst.general.General;

/**
 * Runs a read-eval-print-loop. Ensures that each input is well-formed using the
 * input commands. If it is not, an informative error message is printed by the
 * BadInputHandler.
 *
 * @author Samuel Oliphant
 *
 */

public final class Repl {

  private Repl() {
  }

  /**
   * Runs the repl.
   *
   * @param cmds
   *          list of valid commands
   * @param is
   *          input stream to watch
   */
  public static void run(List<Command> cmds, InputStream is) {
    try (BufferedReader br =
        new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.equals("exit") || line.equals("quit")) {
          break;
        }
        try {
          CommandHandler.execPrint(line, cmds);
        } catch (Exception e) {
          General.printErr(e.getMessage());
        }
      }
    } catch (IOException ioe) {
      // Not possible. No error message can make sense of this.
      ioe.printStackTrace();
    }
  }

}
