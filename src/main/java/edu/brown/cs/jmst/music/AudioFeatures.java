package edu.brown.cs.jmst.music;

public class AudioFeatures {

  //0.0 - 1.0 confidence track is acoustic. 1.0 is high confidence
  private Float acousticness;

  //0.0 is least danceable, 1.0 most danceable
  private Float danceability;

  //duration in milliseconds
  private Integer duration_ms;

  //0.0 to 1.0
  private Float energy;

  //the closer to 1.0, the greater likelihood the track contains NO vocal content
  //Above 0.5 is meant to represent instrumental tracks, confidence increases towards 1.0
  private Float instrumentalness;

  //0 = C, 1 = C#/Dflat, 2 = D, and so on (Pitch Class notation)
  private Integer key;

  //Detects presence of audience in the recording. Value above 0.8 provides strong likelihood that the track is live
  private Float liveness;

  //overall loudness of the track in decibels (dB), typically ranging between -60 and 0 dB
  private Float loudness;

  //major = 1, minor = 0
  private Integer mode;

  //detects presence of spoken words.
  //Above 0.66 = probably made entirely of spoken words
  //between 0.33 and 0.66 = may contain both music and speech, either in sections or layered
  private Float speechiness;

  //estimated tempo in beats per minute (BPM)
  private Float tempo;

  //estimated overall time signature for the track (beats per bar/measure)
  private Integer time_signature;

  //0.0 to 1.0; describes "musical positiveness" conveyed by track.
  //High valence tracks sound more happy
  private Float valence;


  /**
   * @return acousticness
   */
  public Float getAcousticness() {
    return acousticness;
  }

  /**
   * @return danceability
   */
  public Float getDanceability() {
    return danceability;
  }

  /**
   * @return duration_ms
   */
  public Integer getDuration_ms() {
    return duration_ms;
  }

  /**
   * @return energy
   */
  public Float getEnergy() {
    return energy;
  }

  /**
   * @return instrumentalness
   */
  public Float getInstrumentalness() {
    return instrumentalness;
  }

  /**
   * @return key
   */
  public Integer getKey() {
    return key;
  }

  /**
   * @return liveness
   */
  public Float getLiveness() {
    return liveness;
  }

  /**
   * @return loudness
   */
  public Float getLoudness() {
    return loudness;
  }

  /**
   * @return mode
   */
  public Integer getMode() {
    return mode;
  }

  /**
   * @return speechiness
   */
  public Float getSpeechiness() {
    return speechiness;
  }

  /**
   * @return tempo
   */
  public Float getTempo() {
    return tempo;
  }

  /**
   * @return time_signature
   */
  public Integer getTime_signature() {
    return time_signature;
  }

  /**
   * @return valence
   */
  public Float getValence() {
    return valence;
  }
}
