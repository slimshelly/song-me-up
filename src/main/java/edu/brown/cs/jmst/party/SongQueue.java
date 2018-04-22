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
