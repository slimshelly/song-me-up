package edu.brown.cs.jmst.music;

/**
 * Abstract Entity Bean.
 */
public abstract class EntityBean {

  private String id;

  /**
   * Constructor for a bean.
   *
   * @param id
   *          string Id.
   */
  public EntityBean(String id) {
    this.id = id;
  }
}
