/**
 * https://www.codewars.com/kata/571ec81d7e8954ce1400014f/java
 */

public class BF {
  int[][] field;

  public BF(int[][] field) {
    this.field = field;
  }

  public boolean validate() {
    // verify that exactly 20 cells are occupied by ships
    int shipCellCount = 0;
    for (int[] row : field) for (int cell : row) if (cell == 1) shipCellCount++;
    if (shipCellCount != 20) return false;
    
    return false;
  }
}
