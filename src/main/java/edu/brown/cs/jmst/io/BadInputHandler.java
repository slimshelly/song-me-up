package edu.brown.cs.jmst.io;

import java.util.List;
import java.util.zip.DataFormatException;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.general.General.Type;

/**
 * Handles input that is not valid and returns an informative error message.
 *
 * @author Samuel Oliphant
 *
 */

public final class BadInputHandler {

  private static final String INVALID =
      "Invalid input handler received valid input.";
  private static final String MALFORMED =
      "Malformed command. Valid command is: ";

  private BadInputHandler() {
  }

  /**
   * Takes in invalid input and returns an error message.
   *
   * @param badString
   *          invalid input
   * @param web
   *          whether this message will be printed on a website
   * @return error message string
   */
  public static String makeErrMessage(String badString, boolean web) {
    String lb;
    if (web) {
      lb = "<br>";
    } else {
      lb = " ";
    }
    if (!badString.equals(badString.trim())) {
      return "Trailing or leading whitespace.";
    }
    String errmsg = "";
    List<String> bits = General.breakBadInput(badString);
    if (bits.size() == 0) {
      return "Blank input.";
    }
    try {
      switch (bits.get(0)) {
        case "marco":
          if (bits.size() == 2) {
            General.isType(bits.get(1), Type.POSINT, "number of polos");
          } else {
            errmsg = MALFORMED + "marco <integer>";
          }
        default:
          throw new DataFormatException("Invalid command." + lb
              + "Valid commands include:" + lb + "marco <integer>");
      }
    } catch (DataFormatException e) {
      errmsg = e.getMessage();
    }
    if (errmsg.equals("")) {
      errmsg = INVALID;
    }
    return errmsg;
  }
}
