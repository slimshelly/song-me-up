package edu.brown.cs.jmst.songmeup;

import java.util.ArrayList;
import java.util.List;

public class SmuExecutor {

  public static List<String> getPolos(int numPolos) {
    if (numPolos > 10) {
      throw new IllegalArgumentException("Too many polos! 10 is the max.");
    }
    List<String> polos = new ArrayList<>();
    for (int i = 1; i <= numPolos; i++) {
      polos.add("polo " + Integer.toString(i));
    }
    return polos;
  }

}
