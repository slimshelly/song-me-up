package edu.brown.cs.jmst.music;

/**
 * Entity Interface.
 */
public interface Entity {

  /**
   * Returns ID of entity.
   *
   * @return ID
   */
  String getId();

  @Override
  int hashCode();

  @Override
  boolean equals(Object obj);

}
