/*
 * https://www.codewars.com/kata/59de9f8ff703c4891900005c/java
 *
 * Implementation of the game of go. See kata for details
 */

import java.util.Map;
import java.util.HashMap;

enum Color {
  WHITE('o', "white"),
  BLACK('x', "black");
  
  private char stone;
  private String name;
  
  Color(char stone, String name) {
    this.stone = stone;
    this.name = name;
  }
  
  public char getStone() { return this.stone; }
  public String getName() { return this.name; }
  
  public static Color change(Color c) {
    switch (c) {
      case WHITE:
        return BLACK;
      default:
        return WHITE;
    }
  }
}

public class Go {
  private Map<String, Integer> size = new HashMap<>();
  private char[][] board;
  
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
