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
  
  // assign a code to each command
  static final Map<String, Integer> commands = Map.ofEntries(
    // x is the first input, y is the second input
    entry("mov", 12), // copies x into register y
    entry("inc", 11), // register x++
    entry("dec", 21), // register x--
    entry("add", 22), // register x += y
    entry("sub", 32), // register x -= y
    entry("mul", 42), // register x *= y
    entry("div", 52), // register x /= y
    entry(":", 10), // label
    entry("jmp", 31), // jumps to label x
    entry("cmp", 62), // compares x and y (see cmp codes below)
    entry("jne", 0), // jumps to x if cmp value is 0
    entry("je", 1), // jumps to x if cmp value is 1
    entry("jge", 2), // jumps to x if cmp value is 2
    entry("jg", 3), // jumps to x if cmp value is 3
    entry("jle", 4), // jumps to x if cmp value is 4
    entry("jl", 5), // jumps to x if cmp value is 5
    entry("call", 41), // calls subroutine x
    entry("ret", 20), // returns back to the call
    entry("msg", 100), // output to stdout
    entry("end", 30), // terminates execution
    entry(";", 40) // begin a line comment
  );
  
  static ArrayList<String> stdout = new ArrayList<>();
    
  public static String interpret(final String input) {
    label(input);
    
    interpret(input, 0);
    
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
    Scanner rawCode = new Scanner(input);
    
    
  }
  
  public static void terminate() {
    registers.clear();
    labels.clear();
    stdout.clear();
  }
}
