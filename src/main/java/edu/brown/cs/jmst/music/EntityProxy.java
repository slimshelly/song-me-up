package edu.brown.cs.jmst.music;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity Proxy abstract class.
 *
 * @param <E>
 *          data type
 */
public abstract class EntityProxy<E extends Entity> implements Entity {

  protected String id;
  protected E data;

  private static Map<String, Entity> cash = new HashMap<>();

  /**
   * Update cash.
   *
   * @param id
   *          string id
   * @param now
   *          this entity
   */
  public static void addtoCache(String id, Entity now) {
    cash.put(id, now);
  }

  /**
   * To check if cache is used.
   *
   * @param id
   *          string
   * @return boolean
   */
  public static boolean inCache(String id) {
    if (cash.containsKey(id)) {
      return true;
    }
    return false;
  }

  /**
   * Returns internal Info.
   *
   * @return data
   */
  public E getData() {
    return this.data;
  }

  /**
   * Constructor.
   *
   * @param id
   *          string id
   */
  public EntityProxy(String id) {
    this.id = id;
    fillMeUpWithCash();
  }

  /**
   * Getter for ID.
   *
   * @return id string
   */
  public String getId() {
    return this.id;
  }

  private void fillMeUpWithCash() {
    if (data != null) {
      // already filled, return
      return;
    }
    if (cash.containsKey(id)) {
      data = (E) cash.get(id);
    }
  }

  /**
   * Fill function.
   */
  public void fill() {
    fillMeUpWithCash();
    if (data != null) {
      // already filled
      return;
    }
    // set up connection to query
    try {
      fillBean();
    } catch (SQLException e) {
      System.out.println("ERROR: unable to fill bean.");
    }
    cash.put(id, data);

  }

  abstract void fillBean() throws SQLException;

}
