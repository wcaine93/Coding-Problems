/*
 * https://www.codewars.com/kata/59de9f8ff703c4891900005c/java
 *
 * Implementation of the game of go. See kata for details
 */

import java.util.Map;
import java.util.HashMap;

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
    return switch (c) {
      case WHITE -> BLACK;
      case BLACK -> WHITE;
    };
  }
}

public class Go {
  private Map<String, Integer> size = new HashMap<>();
  private char[][] board;
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
}
