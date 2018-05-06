package edu.brown.cs.jmst.music;

/**
 * @author tvanderm
 */
public class AudioFeatures {

  private String id;
  //0.0 is least danceable, 1.0 most danceable
  private Float danceability;

  //0.0 to 1.0
  private Float energy;

  private Float valence;

  public AudioFeatures() {};
  
  public AudioFeatures(String id, Float da, Float energy, Float valence) {
      this.id = id;
      this.danceability = da;
      this.energy = energy;
      this.valence = valence;
      
  }


  /**
   * @return danceability
   */
  public Float getDanceability() {
    return danceability;
  }

  /**
   * @return energy
   */
  public Float getEnergy() {
    return energy;
  }

  /**

  /**
   * @return valence
   */
  public Float getValence() {
    return valence;
  }
}
