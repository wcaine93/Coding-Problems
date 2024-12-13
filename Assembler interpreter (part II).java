/* 
 * https://www.codewars.com/kata/58e61f3d8ff24f774400002c
 *
 * Accepts a string of assembly language as input and returns the output. See kata for more details.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Scanner;

public class AssemblerInterpreter {
  static Map<String, Integer> registers = new HashMap<>();
  static Map<String, Integer> labels = new HashMap<>(); // links register names to line numbers
  
  static ArrayList<String> stdout = new ArrayList<>();
  static boolean executing = true;
    
  public static String interpret(final String input) {
    label(input);
    
    interpret(input, 0);
    if (executing) return null; // return null if execution ends without end statement
    
    String output = "";
    for (String s : stdout.toArray(new String[0])) output += s;
    terminate();
    return output;
  }
  
  public static void label(String input) {
    Scanner rawCode = new Scanner(input);
    
    int lineNum = 0;
    while (rawCode.hasNext()) {
      String line = rawCode.nextLine();
      if (line.indexOf(':') != -1) {
        labels.put(line.substring(0, line.indexOf(':')), lineNum);
        continue;
      }
      
      lineNum++;
    }
  }
  
  public static void interpret(final String input, int callPos) {
    if (!executing) return;
    
    Scanner rawCode = new Scanner(input);
    for (int i = 0; i < callPos; i++) rawCode.nextLine(); // start on line callPos
    
    while (rawCode.hasNext()) {
      String read = rawCode.next();
      switch (read) {
          case ";" -> break;
          case "end" -> {
            executing = false;
            return;
          }
          case "ret" -> return;
          case "msg" -> {
            // single quote output
            if rawCode.hasNext("'\\.\\*"); // /'.*/
          }
          default -> throw new Exception("Invalid command");
      }
      
      rawCode.nextLine();
    }
  }
  
  public static void terminate() {
    registers.clear();
    labels.clear();
    stdout.clear();
  }
}
