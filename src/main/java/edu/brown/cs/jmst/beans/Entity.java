package edu.brown.cs.jmst.beans;

import java.util.Objects;

/**
 * An Entity is an object that defines its equality based on a field called id.
 * @author tvanderm
 */
public abstract class Entity {
  protected String id;
  /**
   * @return this.id
   */
  //has an ID, equality is based on the ID
  public String getId() {
    return this.id;
  }
  /**
   * @param o an object to check equality against
   * @return true if and only if this has the same id as o
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Entity)) {
      return false;
    }
    return this.getId().equals(((Entity) o).getId());
  }
  /**
   * @return the hash of the id
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
