/*
 * https://www.codewars.com/kata/59de9f8ff703c4891900005c/java
 *
 * Implementation of the game of go. See kata for details
 */

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
  private List<char[][]> boardStates = new ArrayList<>();
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
  public char getPosition(String pos) {
    int[] coords = find(pos);
    return board[coords[0]][coords[1]];
  }
  
  private void createBoard() throws IllegalArgumentException {
    if (size.get("height") <= 0) throw new IllegalArgumentException("Board height must be positive");
    if (size.get("width") <= 0) throw new IllegalArgumentException("Board width must be positive");
    if (size.get("height") > 19) throw new IllegalArgumentException("Maximum board height is 19 spaces");
    if (size.get("width") > 19) throw new IllegalArgumentException("Maximum board width is 19 spaces");
    
    // create empty board of size height x width
    board = new char[size.get("height")][size.get("width")];
    for (int row = 0; row < size.get("height"); row++) {
      for (int col = 0; col < size.get("width"); col++) {
        // . represents empty spaces
        board[row][col] = '.';
      }
    }
  }
  
  private int[] find(String pos) throws IllegalArgumentException {
    System.out.println(pos);
    // returns the coordinates of a certain location (e.g., 1A, 6K) on the board
    // the board is numbered with descending numbers for rows and ascending letters for columns
    // the letter I is excluded from lettering
    
    if (pos.split("\\D").length == 0) throw new IllegalArgumentException("Board position must have a number");
    if (pos.split("\\d+").length == 0) throw new IllegalArgumentException("Board position must have a letter");
    
    int row = size.get("height") - Integer.valueOf(pos.split("\\D")[0]); // reverse the numbering of rows
    
    char letter = pos.split("\\d+")[1].charAt(0);
    int col = Integer.valueOf(letter);
    col -= letter > 73 ? 66 : 65; // 73 is the offset of I in ascii, 65 the offset of A
    
    return new int[] {row, col};
  }
  
  private void placeStone(int[] coords) throws IllegalArgumentException {
    int row = coords[0];
    int col = coords[1];
    
    if (row > size.get("height") || col > size.get("width")) throw new IllegalArgumentException("Stones may not be placed out of bounds");
    if (board[row][col] != '.') throw new IllegalArgumentException("Stones cannot be placed on top of other stones");
    
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
      placeStone(stonePlacements[i]);
    }
  }
  
  private void turn() { this.turn = Color.change(this.turn); }
  public void passTurn() { turn(); }
  
  public void move(String... poses) {
    for (String pos : poses) move(pos);
  }
  
  private void move(String pos) {
    placeStone(find(pos));
    turn();
    
    // roll back moves that yield a previous board state (KO rule)
    if (!boardStates.isEmpty() && boardStates.subList(0, boardStates.size()-1).contains(board)) {
      rollBack(1);
      turn();
    }
  }
  
  public void rollBack(int moves) throws IllegalArgumentException {
    // rolls the board state back moves number of moves
    
    if (moves > boardStates.size()) {
      reset();
      return;
    }
    
    boardStates.subList(boardStates.size() - moves, boardStates.size()).clear();
    board = boardStates.get(boardStates.size() - 1);
    
    if (moves % 2 != 0) turn();
  }
  
  public void reset() {
    createBoard();
    boardStates.clear();
    turn = Color.BLACK;
  }
}
