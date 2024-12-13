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
    System.out.println(output);
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
      // read first token on line as command
      String command = rawCode.next();
      System.out.println(command);
      
      // read further tokens as arguments
      String line = rawCode.nextLine();
      System.out.println(line);
      // remove comments if they exist
      if (line.indexOf(";") != -1) line = line.substring(0, line.indexOf(";"));
      Scanner args = new Scanner(line).useDelimiter(",");
      
      // interpret command
      switch (command) {
        case ";" -> {}
        case "end" -> {
          executing = false;
          return;
        }
        case "ret" -> { return; }
        case "msg" -> {
          while (args.hasNext()) { // account for unbounded # of args
            if (args.hasNext(".*'.*")) { // single quoted string
              String str = args.next().trim();
              // remove the first single quote and the last
              stdout.add(str.substring(1, str.length() - 1));
            } else { // handle registers
              stdout.add(registers.get(args.next().trim()).toString());
            }
          }
        }
        
        case "mov" -> {
          String toReg = args.next().trim();
          String copyReg = args.next().trim();
          
          // if the input is not a register, it is a number
          int val = registers.getOrDefault(copyReg, Integer.parseInt(copyReg));
          
          registers.put(toReg, val);
          System.out.println(registers);
        }
        case "inc" -> registers.compute(args.next().trim(), (k,v) -> v+1);
        case "dec" -> registers.compute(args.next().trim(), (k,v) -> v-1);
        
        default -> System.out.println("Invalid command");
      }
    }
  }
  
  public static void terminate() {
    registers.clear();
    labels.clear();
    stdout.clear();
  }
}
