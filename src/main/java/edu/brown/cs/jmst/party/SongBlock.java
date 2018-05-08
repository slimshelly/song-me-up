package edu.brown.cs.jmst.party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;

/**
 * The basis for selecting songs to play. Exactly 3 should exist simultaneously.
 * The lifecycle of a SongBlock has three parts:
 * 1. partygoers add song suggestions
 * 2. partygoers vote on songs added in step 1. Suggestions with sufficiently
 *      negative votes are removed entirely
 * 3. the top X songs are played in an order determined by a sorting algorithm
 *      designed to optimize the consecutive ordering of songs. Suggestions not
 *      played during this step are transferred to the suggestion set of the
 *      next song block, but the number of (positive) votes on the suggestion
 *      decreases (perhaps inversely proportional to amount of votes already
 *      accumulated so that less popular suggestions decay more rapidly)
 * @author tvanderm
 */
class SongBlock {

  private PriorityBlockingQueue<Suggestion> suggestions;
  private List<Suggestion> songsToPlay;
  private SongBlock nextBlock;
  private SongBlock prevBlock;

  protected int state;

  private static final int SUGGESTING = 1;
  private static final int VOTING = 2;
  private static final int PLAYING = 3;

  // Unsure which method is better. Numbers subject to change
  private static final int BLOCK_LENGTH_SONGS = 6;
  private static final int BLOCK_LENGTH_MS = 900000; // 15 minutes

  private static final int VOTES_ONLY = 0;
  private static final int BALANCED = 1;
  private static final int SMOOTHED = 2;
  private static final int NO_VOTES = 3;
  private static final int ORDER = 4;

  SongBlock(int state) {
    this.suggestions = new PriorityBlockingQueue<>();
    this.songsToPlay = new ArrayList<>();
    this.state = state;
  }

  protected SongBlock getNextBlock() {
    return this.nextBlock;
  }

  protected void setNextBlock(SongBlock nextBlock) {
    this.nextBlock = nextBlock;
  }

  protected SongBlock getPrevBlock() {
    return this.prevBlock;
  }

  protected void setPrevBlock(SongBlock prevBlock) {
    this.prevBlock = prevBlock;
  }

  protected PriorityBlockingQueue<Suggestion> getSuggestions() {
    return new PriorityBlockingQueue<>(this.suggestions);
  }

  int size() {
    return this.suggestions.size();
  }

  Suggestion getSuggestionByTrack(Track song) throws PartyException {
    for (Suggestion s : suggestions) {
      if (s.getSong().equals(song)) {
        return s;
      }
    }
    return null;
    // throw new PartyException("No matching song found in this block.");
  }

  Suggestion getSuggestionById(String songId) throws PartyException {
    for (Suggestion s : suggestions) {
      if (s.getSong().getId().equals(songId)) {
        return s;
      }
    }
    return null;
    // throw new PartyException("No song found in this block with ID [" + songId
    // + "].");
  }

  /*
   * TODO: after making a suggestion, maybe tell a user
   * "Other users can vote on your suggestion in XX:XX minutes!"
   */
  /**
   * Method for adding a duplicate song suggestion to this block.
   *
   * @param existingSuggestion the Suggestion that already existed
   * @param userId the id String of the user making the suggestion
   * @throws PartyException if the user has suggested this song already
   */
  protected synchronized void suggestDuplicate(Suggestion existingSuggestion,
      String userId) throws PartyException {
    if (existingSuggestion.hasBeenSubmittedByUser(userId)) {
      throw new PartyException(
          "User may not submit the same suggestion more than once.");
    }
    if (!existingSuggestion.hasBeenUpVotedByUser(userId)) {
      vote(existingSuggestion, userId, true);
    }
    existingSuggestion.addSubmitter(userId);
  }

  /**
   * Method for adding a unique, non-duplicate song suggestion to this block.
   *
   * @param song
   *          a Track object to suggest
   * @param userId
   *          the id String of the user making the suggestion
   * @return the new Suggestion that was added to the block's suggestion queue
   */
  protected Suggestion suggestUnique(Track song, String userId,
                                     AudioFeaturesSimple features,
                                     Integer order) {
    Suggestion suggested = new Suggestion(song, userId, features, order);
    // TODO: lock the queue to make this thread-safe
    suggestions.add(suggested);
    // TODO: unlock the queue
    return suggested;
  }

  /**
   * Method for updating the score of an existing Suggestion in this block.
   * @param voteOn the Suggestion to vote on
   * @param userId the id String of the user making the vote
   * @param isUpVote a boolean; true indicates an up-vote, false indicates a
   *                 down-vote
   * @return the updated score of the Suggestion after the vote is made
   */
  protected int vote(Suggestion voteOn, String userId, boolean isUpVote) {
    assert suggestions.contains(voteOn);
    // TODO: lock the queue to make it thread-safe
    suggestions.remove(voteOn); // updating ordering
    int updatedScore = voteOn.vote(userId, isUpVote);
    suggestions.add(voteOn); // updating ordering
    // TODO: unlock the queue;
    return updatedScore;
  }

  /**
   * @return A list of songs in the order they should be played
   */
  List<Suggestion> getSongsToPlay() {
    return this.songsToPlay;
  }

  protected Collection<Suggestion> topSuggestionsDuration() throws Exception {
    PriorityBlockingQueue<Suggestion> toPlay = new PriorityBlockingQueue<>();
    int totalLengthMs = 0;
    while (totalLengthMs < BLOCK_LENGTH_MS) {
      Suggestion next = suggestions.poll();
      if (next != null) {
        int duration = next.getSong().getDuration_ms();
        if ((totalLengthMs + duration) - BLOCK_LENGTH_MS > BLOCK_LENGTH_MS
            - totalLengthMs) {
          break; // don't add another song, because that gets us further from
                 // target
        }
        toPlay.add(next);
        totalLengthMs += duration;
      } else {
        break;
      }
    }
    return toPlay;
  }

  // returns the top X songs in order of votes (increasing or decreasing?)
  private List<Suggestion> topSuggestionsQuantity() {
    List<Suggestion> toPlay = new ArrayList<>();
    while (toPlay.size() < BLOCK_LENGTH_SONGS) {
      Suggestion next = suggestions.poll();
      if (next != null) {
        toPlay.add(next);
      } else {
        break;
      }
    }
    return toPlay;
  }

  // TODO: put a collection of songs in an order that makes sense
  // given the Collection of suggestions from topSuggestions, and with the
  // knowledge of the previous song played, get an acceptable choice for the
  // next song to play (out of the collection of top songs)
  Suggestion getNextSongToPlay() {
    assert this.state == PLAYING;
    return this.songsToPlay.remove(0);
  }

  protected List<Suggestion> updateSongsToPlay2(int sortMode, Suggestion prev) {
    List<Suggestion> topSongs = new ArrayList<>(topSuggestionsQuantity());
    assert !topSongs.isEmpty();
    switch (sortMode) {
      case VOTES_ONLY: {
        this.songsToPlay = topSongs;
        return this.songsToPlay;
      }
      case BALANCED: {

        // TODO: find shortest ordering of top, using the first element as the starting point
      }
      case SMOOTHED: {
        // TODO: find shortest ordering of top, using the previously played song as the starting point
      }
      case NO_VOTES: {
        // TODO: find shortest ordering of suggestions, and take the top from there
        // NOTE: this option probably won't be implemented
      }
      case ORDER: {
        // TODO: just play things in the order they were submitted
        // NOTE: this option probably won't be implemented
      }
      default: {
        // TODO:
      }
    }
    // First song: most popular suggestion
    // Next song: (does greedy nearest neighbor work here? Probably not really,
    // but actually no one will notice)
    //
    return null;
  }

  // Smoothest: ignore voting; from each set of suggestions take the 5 that will
  // make shortest path
  // Smooth: order the top 5 voted from suggestions to minimize path, starting
  // from previous song played
  // Balanced: order the top 5 voted from suggestions to minimize path, starting
  // from top voted suggestion
  // Voting only: order the top 5 voted songs by number of votes

  // maybe find a path between songs that minimizes the total distance traveled

  // Method that decays the votes on the not-selected suggestions, then adds the
  // suggestions to the next block's collection of suggestions.

  protected void becomePlayBlock(Suggestion prevPlayed) {
    assert this.state == VOTING;
    assert this.songsToPlay.isEmpty();
    updateSongsToPlay(prevPlayed);
    // this.songsToPlay.addAll(topSuggestionsQuantity());
    for (Suggestion s : this.suggestions) {
      s.decayScore();
    }
    this.suggestions.removeIf((Suggestion s) -> s.getScore() <= 0);  //FIXME: < 0 or <= 0? Ensure consistency with intent from the decayScore() method
    this.suggestions.drainTo(nextBlock.suggestions);
    this.state = PLAYING;
    assert this.suggestions.isEmpty(); // TODO: temporary!
  }

  protected void becomeSuggBlock() {
    assert this.state == PLAYING;
    assert this.suggestions.isEmpty();
    this.state = SUGGESTING;
  }

  protected void becomeVoteBlock() {
    assert this.state == SUGGESTING;
    this.state = VOTING;
  }

  private void updateSongsToPlay(Suggestion prevPlayed) {
    this.songsToPlay = topSuggestionsQuantity();
//    this.songsToPlay = getAllPermutations(prevPlayed, new ArrayList<>(),
//            topSuggestionsQuantity());
  }

//  private List<Suggestion> getAllPermutations(Suggestion start,
//                                              List<Suggestion> prefix,
//                                              List<Suggestion> suggs) {
//    int n = suggs.size();
//    if (n == 0) { //Base case
//      return prefix;
//    } else {
//      List<Suggestion> newPrefix = new ArrayList<>(prefix);
//      List<Suggestion> newSugs = new ArrayList<>(suggs);
//      Suggestion s = newSugs.remove(0);
//      newPrefix.add(s);
//      List<Suggestion> min_list = getAllPermutations(start, newPrefix, newSugs);
//      for (int i = 1; i < n; i++) {
//        List<Suggestion> newPrefix2 = new ArrayList<>(prefix);
//        List<Suggestion> newSugs2 = new ArrayList<>(suggs);
//        Suggestion s2 = newSugs2.remove(i);
//        newPrefix2.add(s2);
//        List<Suggestion> newList = getAllPermutations(start, newPrefix2,
//                newSugs2);
//        if(getTotalDistance(start, newList) < getTotalDistance(start, min_list)){
//          min_list = newList;
//        }
//      }
//      return min_list;
//    }
//  }
//
//  private double getTotalDistance(Suggestion start, List<Suggestion> suggs){
//    List<Suggestion> copy = new ArrayList<>(suggs);
//    double distance = 0.0;
//    Suggestion prev = start;
//    while (!copy.isEmpty()) {
//      Suggestion next = copy.remove(0);
//      distance += prev.distanceToInversePopularity(next);
//      prev = next;
//    }
//    return distance;
//  }

  // Add Vote Play
  // A
  // B - A
  // C - B <- A
  // A - C <- B
  // B - A <- C
  // C - B <- A
  // A - C <- B
  // B - A <- C
  // C - B <- A
}
