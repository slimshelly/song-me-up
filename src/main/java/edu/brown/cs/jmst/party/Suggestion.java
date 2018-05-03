package edu.brown.cs.jmst.party;

import edu.brown.cs.jmst.music.Track;

/**
 * @author tvanderm
 */
public class Suggestion implements Comparable<Suggestion> {

  private Track song;
  private Integer age;
  private Integer upVotes;
  private Integer downVotes;
  private Integer score;

  //private Boolean votedOn;

  private static final int UP_VOTE_WEIGHT = 1;
  private static final int DOWN_VOTE_WEIGHT = UP_VOTE_WEIGHT;



  public Suggestion(Track song) {
    this.song = song;
    this.age = 0;
    this.score = UP_VOTE_WEIGHT;
    this.upVotes = 1;
    this.downVotes = 0;
  }

  public Track getSong() { //TODO: does this need to be public?
    return this.song;
  }

  public Integer getScore() {
    return this.score;
  }

  /**
   * The net score is the total up votes minus the total down votes across the
   * entire lifetime of the suggestion. This may be useful for ranking recycled
   * suggestions in the case that new suggestions stop being submitted.
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
    switch (age) {
      case 1: {
        score = (2 * score) / 3;
      }
      case 2: {
        score = (score / 2);
      }
      default: {
        score = 0;
      }
    }
  }

  //TODO: avoid race condition!
  //TODO: make self voting optional!!!! (in case of small parties)
  public void voteUp() {
    this.score += UP_VOTE_WEIGHT;
    this.upVotes += 1;
    this.age = 0;
  }

//  /**
//   * Method to decay the score of a suggestion over time. If a song is voted on
//   * and the song has already decayed at least once, then the song's "age" does
//   * not increase, so the decay penalty does not get harsher. Otherwise, the
//   * song's score is reduced by a greater and greater amount until it has been
//   * passed over by the selection algorithm at least 3 times
//   */
//  private void decayScore() {
//    if (!votedOn) {
//      age += 1;
//    }
//    switch (age) {
//      case 1: {
//        score = (2 *score) / 3;
//      }
//      case 2: {
//        score = (score / 2);
//      }
//      default: {
//        score = 0;
//      }
//    }
//    votedOn = false;
//  }
//
//  //TODO: avoid race condition!
//  public void voteUp() {
//    this.votes += UP_VOTE_WEIGHT;
//    if (this.age != 0) {
//      this.votedOn = true;
//    }
//  }

  //TODO: avoid race condition!
  public void voteDown() {
    this.score -= DOWN_VOTE_WEIGHT;
    this.downVotes += 1;
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
    return this.score - o.score;
  }
}
