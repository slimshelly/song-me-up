package edu.brown.cs.jmst.music;

import edu.brown.cs.jmst.beans.EntityProxy;
import edu.brown.cs.jmst.beans.FillBean;

import java.util.List;

public class TrackProxy extends EntityProxy<TrackBean> implements Track {

  /**
   * @param id the ID of a Track
   * @param filler the method used to fill the bean. May throw exceptions
   */
  TrackProxy(String id,
               FillBean<EntityProxy<TrackBean>, TrackBean> filler) {
    this.id = id;
    this.filler = filler;
    this.bean = null;
  }

  @Override
  public Album getAlbum() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getAlbum();
  }

  @Override
  public List<String> getArtistIds() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getArtistIds();
  }

  @Override
  public Boolean isExplicit() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.isExplicit();
  }

  @Override
  public Boolean isPlayable() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.isPlayable();
  }

  @Override
  public int getPopularity() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getPopularity();
  }

  @Override
  public int getDuration_ms() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getDuration_ms();
  }

  @Override
  public String getName() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getName();
  }

  @Override
  public String getUrl() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getUrl();
  }
}
