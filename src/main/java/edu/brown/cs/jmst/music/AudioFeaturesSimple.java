package edu.brown.cs.jmst.music;

public class AudioFeaturesSimple {

  private String id;
  //0.0 is least danceable, 1.0 most danceable
  private Float danceability;
  //0.0 to 1.0
  private Float energy;
  //0.0 to 1.0; describes "musical positiveness" conveyed by track.
  //High valence tracks sound more happy
  private Float valence;

  public AudioFeaturesSimple(String id, Float danceability, Float energy,
                             Float valence) {
    this.id = id;
    this.danceability = danceability;
    this.energy = energy;
    this.valence = valence;
  }

  public String getId() {
    return this.id;
  }

  public Float getDanceability() {
    return this.danceability;
  }

  public Float getEnergy() {
    return this.energy;
  }

  public Float getValence() {
    return this.valence;
  }

}
