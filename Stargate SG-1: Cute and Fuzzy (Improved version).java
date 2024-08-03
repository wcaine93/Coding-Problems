/**
 * https://www.codewars.com/kata/59669eba1b229e32a300001a/java
 * 
 * Full implementation of A* algorithm.
 */

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;

public class SG1 {
  private static char[][] wireMatrx;
  private static double[][] costMap;
  public static SortedMap<Double, Path> paths;
  static String solvedDiagram;
  
  public static void print(Path path) {
    // create a deep copy of wireMatrx
    char[][] printMatrx = Arrays.stream(wireMatrx)
                                .map(char[]::clone)
                                .toArray(char[][]::new);
    
    ListIterator i = path.getPath();
    i.forEachRemaining((pos) -> {
      Pos p = (Pos) pos;
      if (printMatrx[p.y][p.x] != 'S') printMatrx[p.y][p.x] = 'P';
      System.out.printf("(%d, %d)\n", p.x, p.y);
    });
    
    for (int j = 0; j < printMatrx.length; j++) {
      for (int k = 0; k < printMatrx[j].length; k++) {
        System.out.print(printMatrx[j][k]);
      }
      System.out.println();
    }
    System.out.println();
  }
  
  public static String wireDHD(String exWires) {
    solvedDiagram = "";
    paths = new TreeMap<>();
    
    generateWireMatrx(exWires);
    generateCostMap(exWires);
    
    int rowLength = exWires.indexOf("\n") + 1;
    Pos startPos = new Pos(exWires.indexOf('S') % rowLength, exWires.indexOf('S') / rowLength);
    Path startPath = new Path(startPos, 0);
    paths.put(costMap[startPos.y][startPos.x], startPath);
    Path path = paths.get(paths.firstKey());
    
    while (!paths.isEmpty() && solvedDiagram == "") { // no more paths, all dead ends
      pathfinder();
    }
    
    if (solvedDiagram == "") return "Oh for crying out loud...";
    else return solvedDiagram;
  }
  
  public static void generateWireMatrx(String wireDiagram) {
    int rowLength = wireDiagram.indexOf("\n");
    int rowNum = (wireDiagram.length() + 1) / (rowLength + 1);
    
    char[][] matrx = new char[rowNum][rowLength];
    for (int i = 0; i < rowNum; i++) {
      for (int j = 0; j < rowLength; j++) {
        matrx[i][j] = wireDiagram.charAt(i*(rowLength+1) + j);
      }
    }
    
    wireMatrx = matrx;
  }
  
  public static void generateCostMap(String wireDiagram) {
    costMap = new double[wireMatrx.length][wireMatrx[0].length];
    
    int rowLength = wireDiagram.indexOf("\n") + 1;
    Pos startPos = new Pos(wireDiagram.indexOf('S') % rowLength, wireDiagram.indexOf('S') / rowLength);
    Pos goalPos = new Pos(wireDiagram.indexOf('G') % rowLength, wireDiagram.indexOf('G') / rowLength);
    double regression = (double) (goalPos.y - startPos.y) / (goalPos.x - startPos.x);
    
    for (int i = 0; i < costMap.length; i++) {
      for (int j = 0; j < costMap[0].length; j++) {
        // Euclidian distance from goal
        double distance = Math.sqrt(Math.pow(i - goalPos.y, 2) + Math.pow(j - goalPos.x, 2));
        
        if (Double.isNaN(regression) || Double.isInfinite(regression)) {
          costMap[i][j] = distance;
          continue;
        }
        // orthonormal distance of (i, j) from the regression line (i.e. minimal distance to line)
        double deviation = ((i - goalPos.y) - regression*(j - goalPos.x)) / Math.sqrt(Math.pow(regression, 2) + 1);
        deviation *= i - goalPos.y < regression*(j - goalPos.x) ? -1 : 1;
        
        costMap[i][j] = (double) 1/2*(deviation + distance);
      }
    }
  }
  
  public static void generateSolutionString(Path solutionPath) {
    String solution = "";
    
    ListIterator i = solutionPath.getPath();
    i.forEachRemaining((pos) -> {
      Pos p = (Pos) pos;
      if (wireMatrx[p.y][p.x] != 'S') wireMatrx[p.y][p.x] = 'P';
    });
    
    
    for (int j = 0; j < wireMatrx.length; j++) {
      for (int k = 0; k < wireMatrx[j].length; k++) {
        solution += wireMatrx[j][k];
      }
      if (j == wireMatrx.length - 1) break;
      solution += '\n';
    }
    
    solvedDiagram = solution;
  }
  
  private static void pathfinder() {
    Path currentPath = paths.get(paths.firstKey());
    // print(currentPath);
    paths.remove(paths.firstKey());
    
    Pos currentPos = currentPath.getPosition();
    
    Pos[] adjacents = new Pos[8];
    int[][] offsets = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    for (int i = 0; i < adjacents.length; i++) {
      Pos adjPos = currentPos.move(offsets[i][0], offsets[i][1]);
      if (adjPos.y < 0 || adjPos.y >= wireMatrx.length) continue;
      if (adjPos.x < 0 || adjPos.x >= wireMatrx[0].length) continue;
      if (wireMatrx[adjPos.y][adjPos.x] == 'X') continue;
      // don't allow positions already in the path
      ListIterator path = currentPath.getPath();
      boolean blocked = false;
      while(path.hasNext()) {
        if (path.next().equals(adjPos)) blocked = true;
      }
      if (blocked) continue;
      
      // test for completion
      if (wireMatrx[adjPos.y][adjPos.x] == 'G') {
        generateSolutionString(currentPath);
        return;
      } else adjacents[i] = adjPos;
    }
    
    for (int i = 0; i < adjacents.length; i++) {
      if (adjacents[i] == null) continue;
      Pos newPos = adjacents[i];
      
      double moveCost = Math.sqrt(Math.pow(offsets[i][0], 2) + Math.pow(offsets[i][1], 2));
      Path newPath = currentPath.subPath(newPos, moveCost);
      double totalCost = newPath.cost + costMap[newPos.y][newPos.x];
      
      // ensure not to duplicate (overwrite) in paths
      while (paths.containsKey(totalCost)) totalCost += .000000001;
      
      // do not record if there is another path reaching the same end with lesser cost
      Boolean[] longer = {false};
      paths.headMap(totalCost).forEach((k, v) -> longer[0] = !longer[0] && v.getPosition().equals(newPos) ? true : false);
      if (longer[0]) continue;
      
      // remove any paths reaching the same end with higher cost
      List<Double> duplicates = new LinkedList<>();
      Double newCost = totalCost;
      paths.tailMap(totalCost)
        .forEach((k, v) -> {
          if (v.getPosition().equals(newPos)) duplicates.add(k);
        });
      duplicates.listIterator().forEachRemaining((k) -> paths.remove(k));
      
      // record in paths
      paths.put(totalCost, newPath);
    }
  }
}

class Pos {
  int x;
  int y;
  
  public Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public Pos move(int offset_x, int offset_y) {
    return new Pos(this.x + offset_x, this.y + offset_y);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    
    if (obj instanceof Pos) {
      Pos pos = (Pos) obj;
      if (this.x == pos.x && this.y == pos.y) return true;
    }
    
    return false;
  }
}

class Path {
  private LinkedList<Pos> path = new LinkedList<Pos>();
  double cost;
  
  public Path(Pos pos, double pathCost) {
    path.add(pos);
    cost = pathCost;
  }
  
  public Path subPath(Pos pos, double moveCost) {
    Path subPath = new Path(pos, this.cost + moveCost);
    subPath.path.addAll(0, this.path);
    return subPath;
  }
  
  public ListIterator getPath() {
    return path.listIterator();
  }
  
  public void appendPath(Pos pos) {
    path.addLast(pos);
  }
  
  public Pos getPosition() {
    return path.getLast();
  }
}
