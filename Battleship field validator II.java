/**
 * https://www.codewars.com/kata/571ec81d7e8954ce1400014f/java
 *
 * Generates all possible placements of ships on a given field and determines if any are valid.
 */

import java.util.ArrayList;

public class BF {
  // set ships for a given field are associated by index
  ArrayList<int[][]> possibleFields = new ArrayList<>();
  ArrayList<int[]> setShips = new ArrayList<>(); // number of ships set on a field
  boolean solution = false; // true when a valid solution has been found for the field

  public BF(int[][] field) {
    possibleFields.add(field);
    setShips.add(new int[4]);
  }

  public boolean validate() {
    int[][] field = possibleFields.get(0);
    
    // verify that exactly 20 cells are occupied by ships
    int shipCellCount = 0;
    for (int[] row : field) for (int cell : row) if (cell == 1) shipCellCount++;
    if (shipCellCount != 20) return false;
    
    while (!solution) {
      if (!possibleFields.isEmpty()) branch();
      else return false;
    }
    return true;
  }
  
  public void branch() {
    // default call
    int[][] field = possibleFields.get(0);
    branch(field, field, setShips.get(0), false);
  }
  
  public void branch(int[][] field, int[][] currField, int[] shipCount, boolean branch) {
    // recursively generate all the possible ships orientations for a given field
    // starts generation with the largest ship and goes to the smallest possible
    int searchSize = 0;
    if (shipCount[0] < 1) searchSize = 4; // battleship (1x size 4)
    else if (shipCount[1] < 2) searchSize = 3; // cruiser (2x size 3)
    else if (shipCount[2] < 3) searchSize = 2; // destroyer (3x size 2)
    else if (shipCount[3] < 4) searchSize = 1; // submarine (4x size 1)
    else { // means all ships have a valid position
      solution = true;
      return;
    }
    
    
    // rowwise search
    int size = 0; // current contiguous size
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (currField[row][col] != 1) {
          size = 0;
          continue;
        } // reset and do nothing if there is no ship in the cell
        
        size++;
        if (size == searchSize) {
          // mark the orientation with X and add to the possible fields with updated set ship count
          int[][] newField = fieldClone(field);
          for (int i = 0; i < searchSize; i++) newField[row][col-i] = -1;
          possibleFields.add(newField);
          int[] newCount = shipCount.clone();
          newCount[4-searchSize]++;
          setShips.add(newCount);
          
          // branch with each of the possible cells removed to force generation of other orientations, if they exist
          for (int i = 0; i < searchSize; i++) {
            newField = fieldClone(currField);
            newField[row][col-i] = -1;
            branch(field, newField, shipCount, true);
          }
        }
      }
    }
    
    // column search
    size = 0;
    for (int col = 0; col < 10; col++) {
      for (int row = 0; row < 10; row++) {
        if (currField[row][col] != 1) {
          size = 0;
          continue;
        } // reset and do nothing if there is no ship in the cell
        
        size++;
        if (size == searchSize) {
          // mark the orientation with X and add to the possible fields with updated set ship count
          int[][] newField = fieldClone(field);
          for (int i = 0; i < searchSize; i++) newField[row-i][col] = -1;
          possibleFields.add(newField);
          int[] newCount = shipCount.clone();
          newCount[4-searchSize]++;
          setShips.add(newCount);
          
          // branch with each of the possible cells removed to force generation of other orientations, if they exist
          for (int i = 0; i < searchSize; i++) {
            newField = fieldClone(currField);
            newField[row-i][col] = -1;
            branch(field, newField, shipCount, true);
          }
        }
      }
    }
    
    // if no possible orientations exist or options have been exhausted
    if (!branch) {
      possibleFields.remove(0);
      setShips.remove(0);
    } else return; // destroy the branch if this is a branch
  }
  
  public int[][] fieldClone(int[][] field) {
    // deep array clone method for ints
    
    int[][] clone = new int[10][10];
    
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[0].length; j++) {
        clone[i][j] = field[i][j];
      }
    }
    
    return clone;
  }
}
