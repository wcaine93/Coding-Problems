/*
 * https://www.codewars.com/kata/59de9f8ff703c4891900005c/java
 *
 * Implementation of the game of go. See kata for details
 */

import java.util.Map;
import java.util.HashMap;

public class Go {
  private Map<String, Integer> size = new HashMap<>();
  private int height;
  private int width;
  private char[][] board;
  
  Go(int size) { // create square board
    this.size.put("height", size);
    this.size.put("width", size);
    
    createBoard();
  }
  Go(int height, int width) {
    size.put("height", height);
    size.put("width", width);
  }
  
  public char[][] getBoard() { return this.board; }
  
  private void createBoard() throws IllegalArgumentException {
    if (height <= 0) throw new IllegalArgumentException("Board height must be positive");
    if (width <= 0) throw new IllegalArgumentException("Board width must be positive");
    
    // create empty board of size height x width
    board = new char[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        // . represents empty spaces
        board[row][col] = '.';
      }
    }
  }
}
