package edu.brown.cs.jmst.party;

import edu.brown.cs.jmst.music.Track;

import java.util.Collection;
import java.util.List;

/**
 * @author tvanderm
 */
public class SongQueue {

  private SongBlock blockA;
  private SongBlock blockB;
  private SongBlock blockC;
  private SongBlock suggestingBlock;
  private SongBlock votingBlock;
  private SongBlock playingBlock;

  SongQueue() {
    this.blockA = new SongBlock();
    this.blockB = new SongBlock();
    this.blockC = new SongBlock();
    blockA.setNextBlock(blockB);
    blockA.setPrevBlock(blockC);
    blockB.setNextBlock(blockC);
    blockB.setPrevBlock(blockA);
    blockC.setNextBlock(blockA);
    blockC.setPrevBlock(blockB);
    this.suggestingBlock = blockA;
    this.votingBlock = blockC;
    this.playingBlock = blockB;
  }

  /**
   * @param song A Track to add to the current pool of suggestions
   * @param userId the ID string of the user submitting the suggestion
   */
  public void suggest(Track song, String userId) throws PartyException {
    suggestingBlock.suggest(song, userId);
  }

  /**
   * @param song A Suggestion to vote on
   * @param userId the ID string of the user voting on the suggestion
   * @param isUpVote true indicates an up-vote, false indicates a down-vote
   */
  public int vote(Suggestion song, String userId, boolean isUpVote) {
    return votingBlock.vote(song, userId, isUpVote);
  }

  /**
   * @return a PriorityBlockingQueue of Suggestions that should be displayed for
   *         voting on. They are ordered based on number of votes
   */
  public Collection<Suggestion> getSongsToVoteOn() {
    return votingBlock.getSuggestions();
  }

  /**
   * @return A List of Suggestions in the order they should be played
   * @throws Exception if an error occurs while getting the audioFeatures info
   *                   about the track
   */
  public List<Suggestion> getSongsToPlay() throws Exception {
    return playingBlock.getSongs();
  }

  /**
   * This method should be called EVERY time the current block of songs is
   * ending and the next block is needed, including the case when the host skips
   * the last song in the block.
   */
  public void requestNewBlock() {
    this.playingBlock.passSuggestions(); //decay scores, add to suggestion queue
    Cycle(); //
  }

  public void Play() {
    this.playingBlock.passSuggestions();
    //TODO: play songs from playingBlock
    //TODO: while a song is playing, if there are enough vetoes then stop the
    //todo~  current song and move on to the next.
    Cycle();
  }

  private void Cycle() {
    //TODO: while cycling, some blocks will temporarily have two roles. Need to make sure that this does not cause problems
    this.suggestingBlock = suggestingBlock.getNextBlock();
    this.votingBlock = votingBlock.getNextBlock();
    this.playingBlock = playingBlock.getNextBlock();
  }

  //  //TODO: ordered collection of Suggestions (most basic version, ordered only on number of votes)
//  private PriorityQueue<Suggestion> queue;
//
//  public SongQueue() {
//    this.queue = new PriorityQueue<>();
//  }
//
//  public PriorityQueue<Suggestion> getQueue() {
//    return queue;
//  }
//
//  public void MakeSuggestion(Suggestion suggestion) {
//    this.queue.add(suggestion);
//  }


  //TODO: get next suggestion (pop from queue)


  //TODO: make this more sophisticated:
  // determine the importance of the various audio features in terms of similarness between songs

  //Important!
  // valence, danceability

  //look at all songs currently in pool of suggestions
  //baseline ordering: time added (first in first out)
  //make adjustments based on votes: X vetoes (or Y% of partygoers) negatively impacts suggestion's standing
  //I'm beginning to suspect that the highest priority (after making sure that
  //  people feel like their input matters in a FAIR and reasonable way), is to
  //  ensure that there are no jarring changes in VALENCE (particularly high to low??)

  //It's probably preferred to have high danceability songs, most of the time. But maybe not always.

  //What would be super cool is if I can keep track of whioh songs are being
  // suggested, which songs are being voted up, and which are being voted down,
  // then use the data to adjust the algorithm's priorities in real time.

  //ASSUMPTION: it is likely that if a person makes a suggestion Y during song X,
  // they would be happiest if Y plays immediately after X

  //IDEA: what if, when making a suggestion, you could flag it as "low priority",
  // meaning you would like to hear this song EVENTUALLY, as opposed to immediately after the current song
  // -Alternatively, flag suggestion as "eventually", "soon", or "next"
  //  * limited number of "next" uses, because it would give the song significantly more weight
  //  * maybe "soon" could become disabled once there are already too many songs to pick from
  //    ~ or just have it default to "soon" without making it a visible option.
  //    ~ allow people who don't really care that much to pick "eventually" (with perhaps the [hidden] promise that the song will be played at some point, no matter what)
  //    ~ allow people who want their song NOW to pick "next", with maybe special considerations to deal with the case where a lot of people want a song next
  //      + vote on X suggestions to get back the ability to make a song higher priority
  //      + have Y people vote your suggestion up (while in queue or while playing) to get back "next" ability, where Y is lower than X

  //IDEA: while a song is playing, let people express their opinion about the current song

  //For now, assume that songs are only voted on in the queue

  //1. Order added to queue
  //2. Most positive votes
  //3. Attempt to eliminate large jumps in valence [best guess right now: ~5? Could be lower if the TWO examples I have now are on the extreme end (need more data!)]
  //4. Avoid consecutive songs with "low" danceability
  //5.

}
