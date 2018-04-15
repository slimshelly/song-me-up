package edu.brown.cs.jmst.general;

/**
 * Measures runtime.
 */
public class CodeTimer {

  private long startTime = System.currentTimeMillis();
  private String name;

  /**
   * @param processName
   *          name of the process
   */
  public CodeTimer(String processName) {
    name = processName;
  }

  /**
   * @param processName
   *          name of the process
   */
  public void toc(String processName) {
    General.printInfo("Finished '" + name + "' in "
        + Long.toString(System.currentTimeMillis() - startTime)
        + " milliseconds.");
    startTime = System.currentTimeMillis();
    name = processName;
  }

}
