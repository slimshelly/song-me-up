package edu.brown.cs.jmst.music;

import java.util.List;

import edu.brown.cs.jmst.beans.EntityProxy;
import edu.brown.cs.jmst.beans.FillBean;

public class TrackProxy extends EntityProxy<TrackBean> implements Track {

  /**
   * @param id
   *          the ID of a Track
   * @param filler
   *          the method used to fill the bean. May throw exceptions
   */
  TrackProxy(String id, FillBean<EntityProxy<TrackBean>, TrackBean> filler) {
    this.id = id;
    this.filler = filler;
    this.bean = null;
  }

  @Override
  public String getAlbumId() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getAlbumId();
  }

  @Override
  public List<String> getArtistIds() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getArtistIds();
  }

  @Override
  public boolean isExplicit() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.isExplicit();
  }

  // @Override
  // public boolean isPlayable() throws Exception {
  // if (this.bean == null) {
  // this.fill();
  // }
  // return bean.isPlayable();
  // }

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
  public String getUri() throws Exception {
    if (this.bean == null) {
      this.fill();
    }
    return bean.getUri();
  }

  @Override
  public String toString() {
    if (this.bean == null) {
      try {
        this.fill();
      } catch (Exception e) {
        return "ERROR: " + e.getMessage();
      }
    }
    return bean.toString();
  }

@Override
public String getAlbumArt() throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}
