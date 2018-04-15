package edu.brown.cs.jmst.command;

import java.util.List;

/**
 * Class for wrapping all valid commands. Allows them to be executed.
 *
 * @author Samuel Oliphant
 *
 */
public abstract class Command {

  private String regex;

  /**
   * Make a new command that recognizes the given regex.
   *
   * @param regex
   *          regex to recognize.
   */
  public Command(String regex) {
    this.regex = regex;
  }

  /**
   * Returns the command's regex.
   *
   * @return regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * Is run when command is called.
   *
   * @param toks
   *          input for command
   * @throws Exception
   *           if something goes wrong
   */
  public abstract void execute(List<String> toks) throws Exception;

  /**
   * Prints data logged by execute.
   */
  public abstract void print();

}
