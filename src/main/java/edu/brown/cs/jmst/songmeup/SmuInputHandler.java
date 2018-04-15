package edu.brown.cs.jmst.songmeup;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.jmst.command.Command;
import edu.brown.cs.jmst.command.Commander;
import edu.brown.cs.jmst.general.General;

/**
 * Handles REPL input
 *
 * @author Samuel Oliphant
 *
 */
public class SmuInputHandler implements Commander {

  private SmuState state;

  public SmuInputHandler(SmuState smuState) {
    state = smuState;
  }

  @Override
  public List<Command> getCommands() {
    List<Command> commands = new ArrayList<>();
    commands.add(new MarcoPoloCommand());
    return commands;
  }

  /**
   * This is just an example command. The class defines the regex to match in
   * the constructor and performs execution in the execute method. If the
   * execute method is called then the print method, the print method should
   * print the results.
   *
   * @author Samuel Oliphant
   *
   */
  private class MarcoPoloCommand extends Command {

    public MarcoPoloCommand() {
      super("marco " + General.REG_POSINT + "$");
    }

    @Override
    public void execute(List<String> toks) throws Exception {
      assert toks.size() == 1;
      int numPolos = Integer.parseInt(toks.get(0));
      state.setListMessage(SmuExecutor.getPolos(numPolos));
    }

    @Override
    public void print() {
      for (String p : state.getListMessage()) {
        General.printInfo(p);
      }
    }
  }

}
