package edu.brown.cs.jmst.party;

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

  public SongQueue() {
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

  public void Suggest(Suggestion song) {
    suggestingBlock.suggest(song);
  }

  public void Vote(Suggestion song, Boolean isUpVote) {
    votingBlock.vote(song, isUpVote);
  }

  public void Play() {
    this.playingBlock.passSuggestions();
    //TODO: play songs from playingBlock
    Cycle();
  }

  private void Cycle() {
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


  /*
  *
  Bleeding Love
  {
  //TODO: "danceability": 0.638,
  "energy": 0.656,
  "key": 5,
  "loudness": -5.886,
  "mode": 1,
  "speechiness": 0.0357,
  "acousticness": 0.188,
  "instrumentalness": 0,
  "liveness": 0.146,
  TODO: "valence": 0.225,
  "tempo": 104.036,
  "type": "audio_features",
  "id": "7wZUrN8oemZfsEd1CGkbXE",
  "uri": "spotify:track:7wZUrN8oemZfsEd1CGkbXE",
  "track_href": "https://api.spotify.com/v1/tracks/7wZUrN8oemZfsEd1CGkbXE",
  "analysis_url": "https://api.spotify.com/v1/audio-analysis/7wZUrN8oemZfsEd1CGkbXE",
  "duration_ms": 262467,
  "time_signature": 4
  }


  Broccoli
  {
  //TODO: "danceability": 0.886,
  "energy": 0.525,
  "key": 8,
  "loudness": -7.39,
  "mode": 1,
  "speechiness": 0.132,
  "acousticness": 0.236,
  "instrumentalness": 0,
  "liveness": 0.057,
  TODO: "valence": 0.708,
  "tempo": 145.99,
  "type": "audio_features",
  "id": "3rZhRBdVQ2fTEM2ULOAwUL",
  "uri": "spotify:track:3rZhRBdVQ2fTEM2ULOAwUL",
  "track_href": "https://api.spotify.com/v1/tracks/3rZhRBdVQ2fTEM2ULOAwUL",
  "analysis_url": "https://api.spotify.com/v1/audio-analysis/3rZhRBdVQ2fTEM2ULOAwUL",
  "duration_ms": 225205,
  "time_signature": 4
  }


  Runaway
  {
  //TODO: "danceability": 0.552,
  "energy": 0.577,
  "key": 1,
  "loudness": -3.724,
  "mode": 0,
  "speechiness": 0.0725,
  "acousticness": 0.195,
  "instrumentalness": 0.00257,
  "liveness": 0.527,
  //TODO: "valence": 0.107,
  "tempo": 86.966,
  "type": "audio_features",
  "id": "3DK6m7It6Pw857FcQftMds",
  "uri": "spotify:track:3DK6m7It6Pw857FcQftMds",
  "track_href": "https://api.spotify.com/v1/tracks/3DK6m7It6Pw857FcQftMds",
  "analysis_url": "https://api.spotify.com/v1/audio-analysis/3DK6m7It6Pw857FcQftMds",
  "duration_ms": 547733,
  "time_signature": 4
  }


  Gold Digger
  {
  //TODO: "danceability": 0.638,
  "energy": 0.699,
  "key": 1,
  "loudness": -5.54,
  "mode": 0,
  "speechiness": 0.384,
  "acousticness": 0.0223,
  "instrumentalness": 0,
  "liveness": 0.0917,
  //TODO: "valence": 0.66,
  "tempo": 92.939,
  "type": "audio_features",
  "id": "1PS1QMdUqOal0ai3Gt7sDQ",
  "uri": "spotify:track:1PS1QMdUqOal0ai3Gt7sDQ",
  "track_href": "https://api.spotify.com/v1/tracks/1PS1QMdUqOal0ai3Gt7sDQ",
  "analysis_url": "https://api.spotify.com/v1/audio-analysis/1PS1QMdUqOal0ai3Gt7sDQ",
  "duration_ms": 207627,
  "time_signature": 4
  }
  * */



}
