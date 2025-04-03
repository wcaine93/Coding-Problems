/*
 * https://www.codewars.com/kata/59de9f8ff703c4891900005c/java
 *
 * Implementation of the game of go. See kata for details
 */

import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;

enum Color {
  WHITE('o'),
  BLACK('x');
  
  private char stone;
  
  Color(char stone) { this.stone = stone; }
  
  public char getStone() { return this.stone; }
  
  @Override
  public String toString() {
    return switch (this) {
      case BLACK -> "black";
      case WHITE -> "white";
    };
  }
  
  public static Color change(Color c) {
    // returns the opposite color from the one passed
    return switch (c) {
      case WHITE -> BLACK;
      case BLACK -> WHITE;
    };
  }
}

public class Go {
  private Map<String, Integer> size = new HashMap<>();
  private char[][] board;
  private TreeSet<char[][]> boardStates = new TreeSet<>();
  private Color turn = Color.BLACK;
  
  Go(int size) { // create square board
    this.size.put("height", size);
    this.size.put("width", size);
    
    createBoard();
  }
  Go(int height, int width) {
    size.put("height", height);
    size.put("width", width);
    
    createBoard();
  }
  
  public Map getSize() { return this.size; }
  public char[][] getBoard() { return this.board; }
  public String getTurn() { return this.turn.toString(); }
  public char getPosition(String pos) { return find(pos); }
  
  private char find(String pos) {
    // returns the character in a certain location (e.g., A1, K6) on the board
    // the board is numbered with descending numbers for rows and ascending letters for columns
    // the letter I is excluded from lettering
    
    char letter = pos.charAt(0);
    int col = Integer.valueOf(letter);
    col -= letter > 73 ? 66 : 65; // 73 is the offset of I in ascii, 65 the offset of A
    
    pos = pos.substring(1);
    int row = size.get("height") - Integer.valueOf(pos); // reverse the numbering of rows
    
    return board[row][col];
  }
  
  private void createBoard() throws IllegalArgumentException {
    if (size.get("height") <= 0) throw new IllegalArgumentException("Board height must be positive");
    if (size.get("width") <= 0) throw new IllegalArgumentException("Board width must be positive");
    
    // create empty board of size height x width
    board = new char[size.get("height")][size.get("width")];
    for (int row = 0; row < size.get("height"); row++) {
      for (int col = 0; col < size.get("width"); col++) {
        // . represents empty spaces
        board[row][col] = '.';
      }
    }
  }
  
  private void placeStone(int row, int col) throws IllegalStateException {
    if (board[row][col] != '.') throw new IllegalStateException("Stones cannot be placed on top of other stones.");
    
    board[row][col] = turn.getStone();
  }
  
  public void handicapStones(int num) throws IllegalStateException, IllegalArgumentException {
    switch (size.get("height")) {
      case 9: case 13: case 19: break;
      default: throw new IllegalStateException("Handicap stones can only be placed on 9x9, 13x13 and 19x19 boards." +
                                              " The height of this board is " + size.get("height"));
    }
    switch (size.get("width")) {
      case 9: case 13: case 19: break;
      default: throw new IllegalStateException("Handicap stones can only be placed on 9x9, 13x13 and 19x19 boards." +
                                              " The height of this board is " + size.get("width"));
    }
    
    if (boardStates.size() != 0) throw new IllegalStateException("Handicap stones may only be placed before the first move");
    if (size.get("height") > 9 && num > 5) throw new IllegalArgumentException("Only 5 handicap stones may be placed on a 9x9 board");
    else if (num > 9) throw new IllegalArgumentException("At most 9 handicap stones may be placed on a given board");
    
    // see kata for handicap details
    int[][] stonePlacements = switch (size.get("height")) {
      case 9 -> new int[][] {{2, 6}, {6, 2}, {6, 6}, {2, 2}, {4, 4}};
      case 13 -> new int[][] {{3, 9}, {9, 3}, {9, 9}, {3, 3}, {6, 6}, {6, 3}, {6, 9}, {3, 6}, {9, 6}};
      case 19 -> new int[][] {{3, 15}, {15, 3}, {15, 15}, {3, 3}, {9, 9}, {9, 3}, {9, 15}, {3, 9}, {15, 9}};
      default -> new int[0][0];
    };
    
    for (int i = 0; i < num; i++) {
      // array index exception handling done above
      placeStone(stonePlacements[i][0], stonePlacements[i][1]);
    }
  }
  
  private void turn() { this.turn = Color.change(this.turn); }
  public void passTurn() { turn(); }
}
