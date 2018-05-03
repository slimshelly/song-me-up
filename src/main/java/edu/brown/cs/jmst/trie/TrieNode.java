package edu.brown.cs.jmst.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for TrieNodes that will populate the trie.
 *
 * @author mbecker9
 *
 */
public class TrieNode {

  private char value;
  private boolean endOfWord = false;
  private TrieNode parent;
  private Map<Character, TrieNode> children = new HashMap<>();

  /**
   * Constructor for class.
   *
   * @param value
   *          char value of node
   * @param isEndOfWord
   *          boolean whether or not the node is a full word
   * @param parent
   *          the parent node
   */
  public TrieNode(char value, boolean isEndOfWord, TrieNode parent) {
    this.value = value;
    this.endOfWord = isEndOfWord;
    this.parent = parent;
  }

  /**
   * Determine whether or not the current node is the end of a word.
   *
   * @return true if end of word, false if not
   */
  public boolean isWord() {
    return this.endOfWord;
  }

  /**
   * Getter method for value of node.
   *
   * @return char (letter) of the node
   */
  public char getValue() {
    return this.value;
  }

  /**
   * Getter method for parent of the node.
   *
   * @return parent of the node
   */
  public TrieNode getParent() {
    return this.parent;
  }

  /**
   * Getter method for map of the node's children.
   *
   * @return the map
   */
  public Map<Character, TrieNode> getChildren() {
    return this.children;
  }

  /**
   * Determine whether or not the node has a child with the given character.
   *
   * @param c
   *          look for this character among the node's children
   * @return boolean whether or not the node has this character among its
   *         children
   */
  public boolean hasChild(char c) {
    if (children.get(c) != null) {
      return true;
    }
    return false;
  }

  /**
   * Getter method for the child of the node corresponding to character c.
   *
   * @param c
   *          the character to look for
   * @return the node corresponding to the character c, or null if there is not
   *         child with c
   */
  public TrieNode getChild(char c) {
    return children.get(c);
  }

  /**
   * Mark the node as being a full word.
   */
  public void setEndOfWord() {
    this.endOfWord = true;
  }

}
