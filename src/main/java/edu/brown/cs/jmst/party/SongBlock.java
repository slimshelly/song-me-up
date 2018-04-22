package edu.brown.cs.jmst.party;

import java.util.Collection;
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
 */
class SongBlock {


  private PriorityBlockingQueue<Suggestion> suggestions = new PriorityBlockingQueue<>();
  private SongBlock nextBlock;
  private SongBlock prevBlock;

  //Unsure which method is better. Numbers subject to change
  private static final int BLOCK_LENGTH_SONGS = 5;
  private static final int BLOCK_LENGTH_MS = 900000; //15 minutes

  protected void suggest(Suggestion song) {
    if (nextBlock.suggestions.contains(song)) {
      song.voteUp();
    } else  if (suggestions.contains(song)) {
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

  protected void passSuggestions() {
    //TODO: let current suggestions decay
    for (Suggestion s: this.suggestions) {
      s.decayScore();
    }
    suggestions.removeIf((Suggestion s) -> s.getScore() <= 0);
    //TODO: make thread-safe by ensuring new suggestions go to the correct block
    //TODO: maybe have a single suggestion receiver that passes suggestions to block currently in suggestion phase, so there is no apparent downtime
    suggestions.drainTo(nextBlock.suggestions);
    assert(suggestions.isEmpty()); //TODO: temporary!
  }

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
