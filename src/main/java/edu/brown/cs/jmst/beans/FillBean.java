package edu.brown.cs.jmst.beans;

/**
 * May throw an exception.
 * @param <P> a EntityProxy subclass for the given EntityBean class
 * @param <B> a EntityBean class that this will be a proxy of
 */
public interface FillBean<P extends EntityProxy<B>, B extends EntityBean> {
  /**
   * @param proxy the proxy to fill
   * @return B, the bean to fill the proxy with
   * @throws Exception if something goes wrong. An example might be filling a
   *         bean with information from a database that causes an SQLException.
   */
  B fillBean(P proxy) throws Exception;
}
