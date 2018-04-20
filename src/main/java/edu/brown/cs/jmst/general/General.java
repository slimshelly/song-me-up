package edu.brown.cs.jmst.general;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 * General handles small logical methods that do not pertain to any specific
 * class.
 *
 * @author Samuel Oliphant
 *
 */

public final class General {

  public static final String REG_POSINT = "(\\+?\\d+\\.?)";
  public static final String REG_INT = "([-+]?\\d+\\.?)";
  public static final String REG_POSDOUBLE =
      "((?:\\d*\\.?\\d+)|(?:\\d+\\.?\\d*))";
  public static final String REG_DOUBLE =
      "([-+]?(?:\\d*\\.?\\d+)|(?:\\d+\\.?\\d*))";
  public static final String REG_NAME = "\"([^\"]+)\"";
  public static final String REG_FILE =
      "((?:[a-zA-Z]:)?(?:\\/?[a-zA-Z0-9_.-]+)+)";
  public static final int SIG_FIGS = 5;
  public static final double SIG_BITSHIFT = Math.pow(10, SIG_FIGS);
  public static final double ROUNDING_ERROR = 1.0 / SIG_BITSHIFT;

  private General() {
  }

  /**
   * Turns a string into a list of characters.
   *
   * @param s
   *          string to parse
   * @return list of characters
   */
  public static List<Character> stringToCharacterList(String s) {
    List<Character> list = new ArrayList<>();
    for (Character c : s.toCharArray()) {
      list.add(c);
    }
    return list;
  }

  /**
   * Turns a list of characters into a string.
   *
   * @param list
   *          list of characters to parse
   * @return string with same order as list
   */
  public static String characterListToString(List<Character> list) {
    StringBuilder sb = new StringBuilder();
    for (Character c : list) {
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Prints out an error message in accordance with CS0320's guidelines.
   *
   * @param msg
   *          message to print.
   */
  public static void printErr(String msg) {
    System.out.println("ERROR: " + msg);
  }

  /**
   * Prints out a message that is not an error.
   *
   * @param msg
   *          message to print.
   */
  public static void printInfo(String msg) {
    if (msg != null && !msg.equals("")) {
      System.out.println(msg);
    }
  }

  public static void printVal(String name, String val) {
    if (val != null && !val.equals("")) {
      if (name != null && !name.equals("")) {
        System.out.println(name + ": '" + val + "'");
      } else {
        System.out.println(val);
      }
    }
  }

  /**
   * Takes in a string, type, and variable name. If the string can be parsed as
   * the given type, true is returned. If not, an informative error message is
   * printed and false is returned.
   *
   * @param in
   *          string to check
   * @param type
   *          type to parse the string as
   * @param varName
   *          on error, the variable name will be included to give an
   *          informative error message.
   * @return true if the string input can be parsed properly, false otherwise.
   * @throws DataFormatException
   *           if the in string is not of the given type.
   */
  public static boolean isType(String in, Type type, String varName)
      throws DataFormatException {
    assert in != null;
    assert type != null;
    assert varName != null;
    if (!in.trim().equals(in)) {
      throw new DataFormatException(
          "Input string must not have leading or trailing whitespace.");
    }
    switch (type) {
      case POSINT:
        if (!in.matches(REG_POSINT)) {
          throw new DataFormatException(
              varName + " must be a positive integer.");
        }
        break;
      case INT:
        if (!in.matches(REG_INT)) {
          throw new DataFormatException(varName + " must be an integer.");
        }
        break;
      case POSFLOAT:
      case POSDOUBLE:
        if (!in.matches(REG_POSDOUBLE)) {
          throw new DataFormatException(
              varName + " must be a positive number.");
        }
        break;
      case FLOAT:
      case DOUBLE:
        if (!in.matches(REG_DOUBLE)) {
          throw new DataFormatException(varName + " must be a number.");
        }
        break;
      case NAME:
        if (!in.matches(REG_NAME)) {
          throw new DataFormatException(varName + " is invalid.");
        }
        break;
      default:
        // this should never happen.
        throw new DataFormatException("Type not handled.");
    }
    return true;
  }

  /**
   * Type enum to allow for checking string input for many different types.
   *
   * @author Samuel Oliphant
   *
   */
  public enum Type {
    POSINT, INT, POSDOUBLE, DOUBLE, POSFLOAT, FLOAT, NAME
  }

  /**
   * Compares two string arrays to see if all of their contents are identical.
   *
   * @param a
   *          string array 1
   * @param b
   *          string array 2
   * @return true if the arrays are of the same length and contain the same
   *         strings, false otherwise.
   */
  public static boolean compareStringArrays(String[] a, String[] b) {
    if (a.length == b.length) {
      for (int i = 0; i < a.length; i++) {
        if (!a[i].equals(b[i])) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Return string representation of double array.
   *
   * @param input
   *          double array
   * @return string of numbers separated by commas
   */
  public static String doubleArrayToString(double[] input) {
    String s = Double.toString(input[0]);
    for (int i = 1; i < input.length; i++) {
      s = s.concat(", " + Double.toString(input[i]));
    }
    return s;
  }

  /**
   * Returns true if the string indeed references a valid openable file.
   *
   * @param filename
   *          string path of the file.
   * @return true for valid openable file, false otherwise.
   */
  public static boolean checkFile(String filename) {
    try {
      File file = new File(filename);
      return file.exists() && file.canRead() && !file.isDirectory();
    } catch (SecurityException e) {
      return false;
    }
  }

  /**
   * Takes in a string and replaces all of the non alphabetical characters with
   * spaces, then repeated whitespace with a single space, then makes everything
   * lowercase.
   *
   * @param s
   *          string to scrub
   * @return scrubbed string (or null if s is null)
   */
  public static String scrubString(String s) {
    if (s == null) {
      return null;
    } else {
      return s.replaceAll("[^a-zA-Z]", " ").replaceAll("\\s+", " ")
          .toLowerCase(Locale.ROOT);
    }
  }

  /**
   * Splits up bad input intelligently keeping quoted blocks together.
   *
   * @param badString
   *          string to split up
   * @return list of string parts
   */
  public static List<String> breakBadInput(String badString) {
    List<String> bits = new ArrayList<>();
    Pattern p =
        Pattern.compile("(?:([^\\s\"]+)|(?:(\"[^\"]+\")))" + "(?: (.*))");
    Matcher m = p.matcher(badString);
    String leftover = badString;
    while (m.find() && m.group(3) != null) {
      if (m.group(1) == null) {
        if (leftover.substring(0, m.group(2).length()).equals(m.group(2))) {
          bits.add(m.group(2));
        } else {
          break;
        }
      } else {
        if (m.group(1).equals(leftover.substring(0, m.group(1).length()))) {
          bits.add(m.group(1));
        } else {
          break;
        }
      }
      leftover = m.group(3);
      m = p.matcher(leftover);
    }
    bits.add(leftover);
    return bits;
  }
}
