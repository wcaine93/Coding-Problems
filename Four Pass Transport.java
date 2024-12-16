/*
 * https://www.codewars.com/kata/5aaa1aa8fd577723a3000049/
 *
 * Another implementation of A*, this time a segmented implementation going between checkpoints ("stations").
 */

import java.util.List;
import java.util.ArrayList;

public class FPT {
  ArrayList<Path> paths;
  int[] stations;
  Path solution;

  public FPT(int[] stations) {
    this.stations = stations;
    paths = new ArrayList<>();
    solution = null;
  }

  public List<Integer> solve() {
    paths.add(new Path(stations[0], 1));
    while (solution == null) {
      pathfind();
      if (paths.isEmpty()) return null;
      paths.sort(null); // use natural sorting
    }
    return solution.path;
  }
  
  private void pathfind() {
    Path currPath = paths.get(0);
    paths.remove(currPath);
    
    int currPlace = currPath.getPlace();
    int[] nextPlaces = {currPlace + 1, currPlace - 1, currPlace + 10, currPlace - 10};
    
    for (Integer nextPlace : nextPlaces) {
      if (currPath.path.contains(nextPlace)) continue;
      
      paths.add(currPath.moveTo(nextPlace, nextPlace == stations[currPath.nextStation]));
    }
  }
}

/*
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
class Path implements Comparable {
  ArrayList<Integer> path = new ArrayList<>();
  int nextStation = 0;
  private double cost = 0;
  
  Path() {}
  Path(int currPos, int nextStation) {
    path.add(currPos);
    this.nextStation = nextStation;
    // euclidian distance of current location from station
    cost = Math.sqrt(Math.pow(locate(nextStation)[0] - locate(currPos)[0], 2) + Math.pow(locate(nextStation)[1] - locate(currPos)[1], 2));
  }
  
  public double getCost() { return cost; }
  public int getPlace() { return path.get(path.size() - 1); }
  
  public void addCost(double cost) { this.cost += cost; }
  
  @Override
  public int compareTo(Object o) {
    if (o == null) throw new java.lang.NullPointerException("Type " + o.getClass() + " cannot be compared to type Path");
    if (!(o instanceof Path)) throw new java.lang.ClassCastException("Type " + o.getClass() + " cannot be cast to type Path");
    Path other = (Path) o; 
    
    if (cost == other.getCost()) return 0;
    if (cost > other.getCost()) return 1;
    else return -1;
  }
  
  static int[] locate(int place) {
    int[] location = {place % 10, place / 10}; // {x, y}
    return location;
  }
  
  public Path moveTo(int place, boolean isStation) {
    int station = nextStation + (isStation ? 1 : 0);
    
    Path newPath = new Path(place, station);
    newPath.addCost(isStation ? -10 : cost);
    return newPath;
  }
}
