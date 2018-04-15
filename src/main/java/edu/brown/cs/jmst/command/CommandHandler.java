package edu.brown.cs.jmst.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.brown.cs.jmst.io.BadInputHandler;

/**
 * Class for working with command objects.
 *
 * @author Samuel Oliphant
 *
 */
public final class CommandHandler {

  private CommandHandler() {
  }

  /**
   * Returns a list of all commands from the given commanders.
   *
   * @param cmdrs
   *          commanders from which commands are found
   * @return list of all commands
   */
  public static List<Command> getCommands(List<Commander> cmdrs) {
    List<Command> cmds = new ArrayList<>();
    for (Commander cmdr : cmdrs) {
      for (Command c : cmdr.getCommands()) {
        cmds.add(c);
      }
    }
    return cmds;
  }

  /**
   * Takes in an input and list of commands. Then runs the input on a command
   * that can handle it, and prints the result.
   *
   * @param input
   *          input string
   * @param cmds
   *          command list
   * @throws Exception
   *           if something goes wrong with the command execution
   */
  public static void execPrint(String input, List<Command> cmds)
      throws Exception {
    List<String> bits = new ArrayList<>();
    Pattern p;
    Matcher m;
    for (Command c : cmds) {
      p = Pattern.compile(c.getRegex());
      m = p.matcher(input);
      if (m.find()) {
        for (int i = 1; i <= m.groupCount(); i++) {
          bits.add(m.group(i));
        }
        c.execute(bits);
        c.print();
        return;
      }
    }
    throw new Exception(BadInputHandler.makeErrMessage(input, false));
  }

  /**
   * Returns a command that can handle the input string. Null if no commands can
   * be found.
   *
   * @param input
   *          input string
   * @param cmds
   *          list of commands
   * @return command that can run on the input string
   */
  public static Command getValidCommand(String input, List<Command> cmds) {
    Pattern p;
    Matcher m;
    for (Command c : cmds) {
      p = Pattern.compile(c.getRegex());
      m = p.matcher(input);
      if (m.find()) {
        return c;
      }
    }
    return null;
  }

  /**
   * Executes a given command on given input. Does nothing if the input doesn't
   * match the command format.
   *
   * @param input
   *          input string for command
   * @param cmd
   *          command to execute on the string
   * @throws Exception
   *           if something goes wrong with the command execution
   */
  public static void execute(String input, Command cmd) throws Exception {
    List<String> toks = new ArrayList<>();
    Pattern p = Pattern.compile(cmd.getRegex());
    Matcher m = p.matcher(input);
    if (m.find()) {
      for (int i = 1; i <= m.groupCount(); i++) {
        toks.add(m.group(i));
      }
      cmd.execute(toks);
    }
  }

}
