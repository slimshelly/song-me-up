package edu.brown.cs.jmst.trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A class to set up a trie data structure. Contains methods to find all words
 * starting with a given prefix.
 *
 * @author mbecker9
 *
 */
public class Trie {

  private TrieNode root = new TrieNode(' ', true, null);

  /**
   * Empty initializer for an object.
   */
  public Trie() {

  }

  /**
   * Insert new word into Trie.
   *
   * @param word
   *          word to be inserted
   */
  public void insert(String word) {
    TrieNode currNode = root;
    int i = 0;
    // go through any part of existing word in trie
    while (i < word.length()) {
      char currLetter = word.charAt(i);
      if (currNode.getChild(currLetter) != null) {
        currNode = currNode.getChild(currLetter);
        i += 1;
      } else {
        break;
      }
    }
    // if word is already in trie, make sure it is marked as a full word
    if (i == word.length()) {
      currNode.setEndOfWord();
      return;
    }
    // if not, add new letters into trie
    while (i < word.length() - 1) {
      char currLetter = word.charAt(i);
      TrieNode newNode = new TrieNode(currLetter, false, currNode);
      currNode.getChildren().put(currLetter, newNode);
      currNode = currNode.getChild(currLetter);
      i += 1;
    }
    // add final character to trie, marking it as a full word
    char currLetter = word.charAt(i);
    TrieNode newNode = new TrieNode(currLetter, true, currNode);
    currNode.getChildren().put(currLetter, newNode);
  }

  /**
   * Return true or false whether or not a string is in the trie.
   *
   * @param str
   *          string to look for in trie
   * @return boolean whether or not str in the trie
   *
   */
  public boolean contains(String str) {
    TrieNode currNode = this.root;
    int i = 0;
    while (i < str.length()) {
      if (currNode.getChild(str.charAt(i)) == null) {
        return false;
      }
      currNode = currNode.getChild(str.charAt(i));
      i += 1;
    }
    return true;
  }

  /**
   * Compile list of strings in trie that start with prefix specified.
   *
   * @param prefix
   *          words must start with prefix
   * @return List<StringBuilder>
   *
   */
  public List<String> wordsWithPrefix(String prefix) {
    if (!contains(prefix)) { // if prefix is not in trie
      return Collections.emptyList();
    }
    StringBuilder wordSoFar = new StringBuilder();
    // otherwise, find last node in prefix
    TrieNode currNode = this.root;
    int i = 0;
    while (i < prefix.length()) {
      wordSoFar.append(prefix.charAt(i)); // building full word
      currNode = currNode.getChild(prefix.charAt(i));
      i += 1;
    }
    // currNode is now the last letter in prefix
    List<String> allWords = new ArrayList<>();
    return findWords(currNode, allWords, wordSoFar);
  }

  /**
   * Find all full words beneath or at a starting node.
   *
   * @param currNode
   *          node to start looking for full words at
   * @param allWords
   *          combines each word (made with a stringbuilder) into a list
   * @param stringBuilder
   *          use to concatenate characters for each word
   * @return List<StringBuilder> for all the full words found
   *
   */
  public List<String> findWords(TrieNode currNode, List<String> allWords,
      StringBuilder stringBuilder) {
    if (currNode.isWord()) {
      allWords.add(stringBuilder.toString());
    }
    // check all children
    for (Map.Entry<Character, TrieNode> entry : currNode.getChildren()
        .entrySet()) {
      currNode = entry.getValue();
      // FIX - currently altering stringbuilder for every child
      stringBuilder.append(currNode.getValue());
      findWords(currNode, allWords, stringBuilder);
      // remove last letter of stringbuilder for next iteration of loop
      stringBuilder.setLength(stringBuilder.length() - 1);
    }
    return allWords;
  }

  /**
   * Getter method for root of trie.
   *
   * @return root of trie
   */
  public TrieNode getRoot() {
    return this.root;
  }

}
