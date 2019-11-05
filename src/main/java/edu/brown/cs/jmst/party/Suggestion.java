package edu.brown.cs.jmst.party;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;

public class Suggestion implements Comparable<Suggestion> {

  private Track song;
  private Integer age;
  private Integer upVotes;
  private Integer downVotes;
  private Integer score;
  private Integer order;
  private int popularity;
  private Float valence;
  private Float danceability;
  private Float energy;



  private Map<String, Integer> userVoteMap;
  private Set<String> userSubmittedSet;

  private static final int UP_VOTE_WEIGHT = 1;
  private static final int DOWN_VOTE_WEIGHT = UP_VOTE_WEIGHT;

  Suggestion(Track song, String userId, AudioFeaturesSimple features,
             Integer order) {

    this.song = song;
    this.age = 0;
    this.score = UP_VOTE_WEIGHT;
    this.upVotes = 1;
    this.downVotes = 0;
    this.userVoteMap = new ConcurrentHashMap<>();
    this.userVoteMap.put(userId, 1);
    this.userSubmittedSet = Collections.synchronizedSet(new HashSet<>()); //TODO: make a Suggestion inherently thread-safe, and simply synchronize on that
    userSubmittedSet.add(userId);

    try {
      this.popularity = song.getPopularity();
    } catch (Exception e) {
      System.err.println("EXCEPTION THROWN! (Suggestion, line 59)");
      e.printStackTrace();
      this.popularity = 0;
    }

    this.valence = features.getValence();
    this.danceability = features.getDanceability();
    this.energy = features.getEnergy();
    this.order = order;
  }

  public JsonObject toJson() throws Exception {
    JsonObject jo = new JsonObject();
    jo.addProperty("song_id", song.getId());
    jo.addProperty("song_name", song.getName());
    JsonArray artistIds = new JsonArray();
    for (String artist_id : song.getArtistIds()) {
      artistIds.add(artist_id);
    }
    
    jo.add("artist_ids", artistIds);
    
    JsonArray artistNames = new JsonArray();
    for (String artist_name : song.getArtistNames()) {
      artistNames.add(artist_name);
    }
    
    jo.addProperty("album_cover", song.getAlbumArt());
    
    jo.add("artist_names", artistNames);
    jo.addProperty("duration_ms", song.getDuration_ms());
    jo.addProperty("uri", song.getUri());
    jo.addProperty("score", score);
    return jo;
  }

  public JsonObject toJson(String userId) throws Exception {
    JsonObject jo = this.toJson();
    jo.addProperty("user_vote_status", this.getUserVoteStatus(userId));
    return jo;
  }

  public Track getSong() { // TODO: does this need to be public?
    return this.song;
  }

  public Integer getScore() {
    return this.score;
  }

  /**
   * The net score is the total up votes minus the total down votes across the
   * entire lifetime of the suggestion. This may be useful for ranking recycled
   * suggestions in the case that new suggestions stop being submitted.
   *
   * @return the combined weight of the lifetime total up votes minus the total
   *         down votes (equivalent to the total score accumulated if the score
   *         didn't decay between rounds of voting).
   */
  public Integer getNetScore() {
    return (upVotes * UP_VOTE_WEIGHT) - (downVotes * DOWN_VOTE_WEIGHT);
  }

  /**
   * Method to decay the score of a suggestion over time. If a song is voted on
   * then the song's "age" is reset to 0, as though a new suggestion were made.
   * Otherwise, the suggestion's score is reduced by a greater and greater
   * amount until it has not been voted on for 3 consecutive rounds.
   * The decay equations are designed so that a score of 1 always decays to 0,
   * which means that new suggestions are always higher rated than suggestions
   * that were not voted on after being submitted.
   */
  void decayScore() {
    this.age += 1;
    if (this.score > 0) {
      switch (age) {
      case 1: {
        score = (2 * score) / 3;
        break;
      }
      case 2: {
        score = (score / 2);
        break;
      }
      default: {
        score = 0; //TODO: maybe this should be -1?
      }
    } 
    }
  }

  // TODO: avoid race condition!
  private void doVote(String userId, boolean isUpVote) {
    if (isUpVote) {
      this.score += UP_VOTE_WEIGHT;
      this.upVotes += 1;
      userVoteMap.put(userId, 1);
    } else {
      this.score -= DOWN_VOTE_WEIGHT;
      this.downVotes += 1;
      userVoteMap.put(userId, -1);
    }
  } //TODO: if something was voted on (and not undone), set age to 0 before vote decay occurs

  private void undoVote(String userId, boolean isUpVote) {
    if (isUpVote) {
      this.score -= UP_VOTE_WEIGHT;
      this.upVotes -= 1;
    } else {
      this.score += DOWN_VOTE_WEIGHT;
      this.downVotes -= 1;
    }
    userVoteMap.put(userId, 0);
  }

  public int vote(String userId, boolean isUpVote) {
    if (!userVoteMap.containsKey(userId) || userVoteMap.get(userId) == 0) {
      doVote(userId, isUpVote);
    } else {
      boolean existingVoteIsUpVote = userVoteMap.get(userId).equals(1);
      undoVote(userId, existingVoteIsUpVote);
      if (!(existingVoteIsUpVote == isUpVote)) {
        doVote(userId, isUpVote);
      }
    }
    return this.score;
  }

  public int getUserVoteStatus(String userId) {
    return userVoteMap.getOrDefault(userId, 0);
  }

  public boolean hasBeenVotedOnByUser(String userId) {
    return userVoteMap.containsKey(userId) && userVoteMap.get(userId) != 0;
  }

  public boolean hasBeenUpVotedByUser(String userId) {
    return userVoteMap.get(userId).equals(1);
  }

  public boolean hasBeenSubmittedByUser(String userId) {
    return userSubmittedSet.contains(userId);
  }


  public void addSubmitter(String userId) {
    userSubmittedSet.add(userId);
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   * <p>
   * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)
   * <p>
   * <p>The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.
   * <p>
   * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.
   * <p>
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   * <p>
   * <p>In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override
  public int compareTo(Suggestion o) {
    int scoreComp = o.score - this.score;
    if (scoreComp == 0) {
      int popComp = o.popularity - this.popularity;
      if (popComp == 0) {
        return this.order - o.order;
      }
      return popComp;
    }
    return scoreComp;
  }

  public int compareToScoreAndOrder(Suggestion o) {
    int scoreComp = o.score - this.score;
    if (scoreComp == 0) {
      return this.order - o.order;
    }
    return scoreComp;
  }

  public int compareToScoreOnly(Suggestion o) {
    return o.score - this.score;
  }

  public int compareToOrderOnly(Suggestion o) {
    return this.order - o.order;
  }

  /**
   * Method for finding the 'distance' to another Suggestion. Used as a measure
   * of how similar the two tracks are.
   * @param that a Suggestion to find the 'distance' to
   * @return the 'distance' between this Suggestion object and the given
   *         Suggestion, as a Double.
   */
  Double distanceTo(Suggestion that) {
    Float vDif = this.valence - that.valence; //positive means THIS > that
    Float dDif = this.danceability - that.danceability; //positive means THIS > that
    Float eDif = this.energy - that.energy; //positive means THIS > that
    //TODO: consider weighting the different things! (like multiply vDif squared by 2)
    Float sum = (vDif * vDif) + (dDif * dDif) + (eDif * eDif);
    return Math.sqrt(sum);

  }

  /**
   * Method for finding the 'distance' to another Suggestion. Used as a measure
   * of how similar the two tracks are. Takes into account the inverse of
   * popularity, so that there's a rough alternation between more and less
   * popular songs.
   * @param that a Suggestion to find the 'distance' to
   * @return the 'distance' between this Suggestion object and the given
   *         Suggestion, as a Double.
   */
  public Double distanceToInversePopularity(Suggestion that) {
    Float vDif = this.valence - that.valence; //positive means THIS > that
    Float dDif = this.danceability - that.danceability; //positive means THIS > that
    Float eDif = this.energy - that.energy; //positive means THIS > that
    Float pDif = ((Double) (1.0 / this.popularity)).floatValue()
            - ((Double) (1.0 / this.popularity)).floatValue();
    if (pDif < 0) {
      pDif *= -1;
    }
    pDif = ((Double) (1.0 - pDif)).floatValue();
    //TODO: consider weighting the different things! (like multiply vDif squared by 2)
    Float sum = (vDif * vDif) + (dDif * dDif) + (eDif * eDif) + (pDif * pDif);
    return Math.sqrt(sum);
  }

}
