package edu.brown.cs.jmst.command;

import java.util.List;

/**
 * Interface for all classes that can handle commands.
 *
 * @author Samuel Oliphant
 *
 */
public interface Commander {
  /**
   * Returns a list of all the commands this commander can handle.
   *
   * @return list of commands
   */
  List<Command> getCommands();
}
