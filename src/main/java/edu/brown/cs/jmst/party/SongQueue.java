package edu.brown.cs.jmst.party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import edu.brown.cs.jmst.music.AudioFeaturesSimple;
import edu.brown.cs.jmst.music.Track;
import edu.brown.cs.jmst.party.SuggestResult.STATUS_TYPE;


public class SongQueue {

  private SongBlock blockA;
  private SongBlock blockB;
  private SongBlock blockC;
  private SongBlock suggestingBlock;
  private SongBlock votingBlock;
  private SongBlock playingBlock;

  private static final int SUGGESTING = 1;
  private static final int VOTING = 2;
  private static final int PLAYING = 3;

  private static final int MIN_VOTE_BLOCK_SIZE = 15;

  private static int order = 0;

  SongQueue() {
    this.blockA = new SongBlock(SUGGESTING);
    this.blockB = new SongBlock(VOTING);
    this.blockC = new SongBlock(PLAYING);
    this.suggestingBlock = blockA;
    this.votingBlock = blockB;
    this.playingBlock = blockC;
    this.suggestingBlock.setPrevBlock(this.votingBlock);
    this.votingBlock.setPrevBlock(this.playingBlock);
    this.playingBlock.setPrevBlock(this.suggestingBlock);
    this.suggestingBlock.setNextBlock(this.playingBlock);
    this.votingBlock.setNextBlock(this.suggestingBlock);
    this.playingBlock.setNextBlock(this.votingBlock);
  }

  /**
   * @param song A Track to add to the current pool of suggestions
   * @param userId the ID string of the user submitting the suggestion
   * @return the Suggestion object added to the suggestions, or null if it was
   *         a duplicate suggestion
   */
  public SuggestResult suggest(Track song, String userId,
                               AudioFeaturesSimple features)
      throws PartyException {
    Suggestion existingSuggestion;
    if ((existingSuggestion = votingBlock.getSuggestionByTrack(song)) != null) {
      votingBlock.suggestDuplicate(existingSuggestion, userId);
      return new SuggestResult(STATUS_TYPE.VOTE);
    } else if (votingBlock.size() < MIN_VOTE_BLOCK_SIZE) {
      order++;
      votingBlock.suggestUnique(song, userId, features, order); //TODO: return value unneeded?
      return new SuggestResult(STATUS_TYPE.VOTE);
    } else if ((existingSuggestion =
        suggestingBlock.getSuggestionByTrack(song)) != null) {
      suggestingBlock.suggestDuplicate(existingSuggestion, userId);
      return new SuggestResult(STATUS_TYPE.DUPLICATE_SUGG);
    } else {
      order++;
      return new SuggestResult(STATUS_TYPE.UNIQUE_SUGG,
          suggestingBlock.suggestUnique(song, userId, features, order));
    }
  }

  // TODO: possible actions that front end might need to take:
  // 1. append song to suggestion block's suggestion queue (no refresh)
  // 2. refresh suggestion blocks' suggestion queue
  // 3. refresh voting block's suggestion queue
  // 4. refresh playing block's playing list
  // 5. refresh everything simultaneously

  /**
   * @param song A Suggestion to vote on
   * @param userId the ID string of the user voting on the suggestion
   * @param isUpVote true indicates an up-vote, false indicates a down-vote
   * @return the ordered Collection of Suggestions that are being voted on
   */
  public Collection<Suggestion> vote(Suggestion song, String userId,
      boolean isUpVote) {
    votingBlock.vote(song, userId, isUpVote);
    return votingBlock.getSuggestions();
  }

  public Suggestion getSuggestionInVoteBlockById(String songId)
      throws PartyException {
    Suggestion toReturn = votingBlock.getSuggestionById(songId);
    if (toReturn == null) {
      throw new PartyException(
          "No song found in current voting block with ID '" + songId + "'.");
    }
    return toReturn;
  }

  /**
   * @return A Collection of Suggestions ordered based on score, with order of
   *         submission settling any ties.
   */
  public Collection<Suggestion> getSuggestedSongs() {
    return suggestingBlock.getSuggestions();
  }

  /**
   * @return a PriorityBlockingQueue of Suggestions that should be displayed for
   *         voting on. They are ordered based on score, with order of
   *         submission settling any ties.
   */
  public PriorityBlockingQueue<Suggestion> getSongsToVoteOn() {
    return votingBlock.getSuggestions();
  }

  /**
   * @return A List of Suggestions in the order they should be played
   */
  public List<Suggestion> getSongsToPlaySoon() {
    return playingBlock.getSongsToPlay();
  }

  /**
   * @return the Suggestion to play next
   * @throws PartyException when the voting block is empty and there are no
   *                        songs left to play
   */
  public Suggestion getNextSongToPlay(Suggestion prevPlayed) throws PartyException {
    if (playingBlock.getSongsToPlay().size() != 0) {
      return playingBlock.getNextSongToPlay();
    }
    cycle(prevPlayed); //TODO: need to tell front end to update everything!
    if (playingBlock.getSongsToPlay().size() != 0) {
      return playingBlock.getNextSongToPlay();
    } else {
      throw new PartyException(
              "Voting queue was empty; could not select song to play next.");
    }
  }

  private void cycle(Suggestion prevPlayed) {
    // TODO: while cycling, some blocks will temporarily have two roles. Need to
    // make sure that this does not cause problems
    System.out.println("b sugg: " + suggestingBlock.state);
    System.out.println("b vote: " + votingBlock.state);
    System.out.println("b play: " + playingBlock.state);
    this.playingBlock.becomeSuggBlock();
    this.suggestingBlock.becomeVoteBlock();
    this.votingBlock.becomePlayBlock(prevPlayed);

    this.suggestingBlock = suggestingBlock.getNextBlock();
    this.votingBlock = votingBlock.getNextBlock();
    this.playingBlock = playingBlock.getNextBlock();
    System.out.println("a sugg: " + suggestingBlock.state);
    System.out.println("a vote: " + votingBlock.state);
    System.out.println("a play: " + playingBlock.state);
  }

}
