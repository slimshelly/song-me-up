package edu.brown.cs.jmst.beans;

import java.util.Objects;

/**
 * @author tvanderm
 * @param <B> The type of EntityBean a child class will be a proxy for.
 * The creator of a concrete proxy instance will provide the means
 *           of filling the bean through the functional interface
 *           'filler'.
 */
public abstract class EntityProxy<B extends EntityBean> extends Entity {
  protected B bean;
  protected FillBean<EntityProxy<B>, B> filler;
  protected void fill() throws Exception {
    this.bean = this.filler.fillBean(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EntityProxy)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    EntityProxy<?> that = (EntityProxy<?>) o;
    return Objects.equals(this.id, that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
