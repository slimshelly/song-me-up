package edu.brown.cs.jmst.music;

public class AudioFeatures {

  //0.0 - 1.0 confidence track is acoustic. 1.0 is high confidence
  Float acousticness;

  //0.0 is least danceable, 1.0 most danceable
  Float danceability;

  //duration in milliseconds
  Integer duration_ms;

  //0.0 to 1.0
  Float energy;

  //the closer to 1.0, the greater likelihood the track contains NO vocal content
  //Above 0.5 is meant to represent instrumental tracks, confidence increases towards 1.0
  Float instrumentalness;

  //0 = C, 1 = C#/Dflat, 2 = D, and so on (Pitch Class notation)
  Integer key;

  //Detects presence of audience in the recording. Value above 0.8 provides strong likelihood that the track is live
  Float liveness;

  //overall loudness of the track in decibels (dB), typically ranging between -60 and 0 dB
  Float loudness;

  //major = 1, minor = 0
  Integer mode;

  //detects presence of spoken words.
  //Above 0.66 = probably made entirely of spoken words
  //between 0.33 and 0.66 = may contain both music and speech, either in sections or layered
  Float speechiness;

  //estimated tempo in beats per minute (BPM)
  Float tempo;

  //estimated overall time signature for the track (beats per bar/measure)
  Integer time_signature;

  //0.0 to 1.0; describes "musical positiveness" conveyed by track.
  //High valence tracks sound more happy
  Float valence;


}
