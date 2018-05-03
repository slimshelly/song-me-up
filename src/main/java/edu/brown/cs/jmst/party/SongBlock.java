package edu.brown.cs.jmst.party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

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

  private PriorityBlockingQueue<Suggestion> suggestions = new PriorityBlockingQueue<>();
  private SongBlock nextBlock;
  private SongBlock prevBlock;

  //Unsure which method is better. Numbers subject to change
  private static final int BLOCK_LENGTH_SONGS = 5;
  private static final int BLOCK_LENGTH_MS = 900000; //15 minutes

  private static final int VOTES_ONLY = 0;
  private static final int BALANCED = 1;
  private static final int SMOOTHED = 2;
  private static final int NO_VOTES = 3;
  private static final int ORDER = 4;

  //TODO: after making a suggestion, maybe tell a user "Other users can vote on your suggestion in XX:XX minutes!"
  protected void suggest(Suggestion song) {
    if (nextBlock.suggestions.contains(song)) {
      song.voteUp();
    } else if (suggestions.contains(song)) {
      song.voteUp();
    } else {
      suggestions.add(song); //TODO: use add, or use one of the blocking alternatives?
    }
  }

  protected SongBlock getNextBlock() {
    return this.nextBlock;
  }

  protected SongBlock getPrevBlock() {
    return this.prevBlock;
  }

  protected void setNextBlock(SongBlock nextBlock) {
    this.nextBlock = nextBlock;
  }

  protected void setPrevBlock(SongBlock prevBlock) {
    this.prevBlock = prevBlock;
  }

  protected Collection<Suggestion> getSuggestions() {
    return this.suggestions;
  }

  protected void vote(Suggestion song, Boolean isUpVote) {
    assert suggestions.contains(song);
    if (isUpVote) {
      song.voteUp();
    } else {
      song.voteDown();
    }
  }

  protected Collection<Suggestion> topSuggestionsDuration() throws Exception {
    PriorityBlockingQueue<Suggestion> toPlay = new PriorityBlockingQueue<>();
    int totalLengthMs = 0;
    while (totalLengthMs < BLOCK_LENGTH_MS) {
      Suggestion next = suggestions.poll();
      if (next != null) {
        int duration = next.getSong().getDuration_ms();
        if ((totalLengthMs + duration) - BLOCK_LENGTH_MS
                > BLOCK_LENGTH_MS - totalLengthMs ) {
          break; //don't add another song, because that gets us further from target
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
  protected Collection<Suggestion> topSuggestionsQuantity() throws Exception {
    PriorityBlockingQueue<Suggestion> toPlay = new PriorityBlockingQueue<>();
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

  //TODO: put a collection of songs in an order that makes sense
  // given the Collection of suggestions from topSuggestions, and with the
  // knowledge of the previous song played, get an acceptable choice for the
  // next song to play (out of the collection of top songs)

  protected List<Suggestion> getSongs() throws Exception {
    return new ArrayList<>(topSuggestionsQuantity());
  }

  protected Suggestion nextSong(Suggestion prev, Collection<Suggestion> top, int sortMode) {
    switch (sortMode) {
      case VOTES_ONLY: {
        //TODO: top (which is the collection returned by topSuggestions) is already in the desired order
      }
      case BALANCED: {
        //TODO: find shortest ordering of top, using the first element as the starting point
      }
      case SMOOTHED: {
        //TODO: find shortest ordering of top, using the previously played song as the starting point
      }
      case NO_VOTES: {
        //TODO: find shortest ordering of suggestions, and take the top from there
        //NOTE: this option probably won't be implemented
      }
      case ORDER: {
        //TODO: just play things in the order they were submitted
        //NOTE: this option probably won't be implemented
      }
      default: {
        //TODO:
      }
    }
    //First song: most popular suggestion
    //Next  song: (does greedy nearest neighbor work here? Probably not really, but actually no one will notice)
    //
    return null;
  }

  // Smoothest: ignore voting; from each set of suggestions take the 5 that will make shortest path
  // Smooth: order the top 5 voted from suggestions to minimize path, starting from previous song played
  // Balanced: order the top 5 voted from suggestions to minimize path, starting from top voted suggestion
  // Voting only: order the top 5 voted songs by number of votes


  //maybe find a path between songs that minimizes the total distance traveled
  //playing music with friends:

  // Method that decays the votes on the not-selected suggestions, then adds the
  //  suggestions to the next block's collection of suggestions.
  protected void passSuggestions() {
    for (Suggestion s: this.suggestions) {
      s.decayScore();
    }
    suggestions.removeIf((Suggestion s) -> s.getScore() <= 0);
    //TODO: make thread-safe by ensuring new suggestions go to the correct block
    //TODO: maybe have a single suggestion receiver that passes suggestions to block currently in suggestion phase, so there is no apparent downtime
    suggestions.drainTo(nextBlock.suggestions);
    assert(suggestions.isEmpty()); //TODO: temporary!
  }

  //want an 'ordered' but not sorted data structure--alternating

  //  Add Vote Play
  //  A
  //  B - A
  //  C - B <- A
  //  A - C <- B
  //  B - A <- C
  //  C - B <- A
  //  A - C <- B
  //  B - A <- C
  //  C - B <- A
}
