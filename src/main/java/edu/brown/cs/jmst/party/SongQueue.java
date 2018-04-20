package edu.brown.cs.jmst.party;

import java.util.PriorityQueue;

public class SongQueue {
  //TODO: ordered collection of Suggestions (most basic version, ordered only on number of votes)
  private PriorityQueue<Suggestion> queue;

  public SongQueue() {
    this.queue = new PriorityQueue<>();
  }

  public PriorityQueue<Suggestion> getQueue() {
    return queue;
  }

  //TODO: get next suggestion (pop from queue)
}
