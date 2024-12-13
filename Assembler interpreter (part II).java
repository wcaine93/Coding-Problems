/* 
 * https://www.codewars.com/kata/58e61f3d8ff24f774400002c
 *
 * Accepts a string of pre-validated assembly language as input and returns the output. See kata for more details.
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
  static boolean executing;
  static boolean terminated;
    
  public static String interpret(final String input) {
    flush();
    executing = true;
    terminated = false;
    
    label(input);
    interpret(input, 0);
    // return null if execution ends without end statement
    if (executing || terminated) return null;
    
    String output = "";
    for (String s : stdout.toArray(new String[0])) output += s;
    return output;
  }
  
  public static void label(String input) {
    Scanner rawCode = new Scanner(input);
    
    int lineNum = 0;
    while (rawCode.hasNext()) {
      String line = rawCode.nextLine();
      if (line.contains(":")) {
        labels.put(line.substring(0, line.indexOf(':')), lineNum);
      }
      
      lineNum++;
    }
  }
  
  public static void interpret(final String input, int callPos) {
    if (!executing || terminated) return;
    
    Scanner rawCode = new Scanner(input);
    for (int i = 0; i <= callPos; i++) rawCode.nextLine(); // start on line after callPos
    
    int compare = 0; // for use by cmp
    while (rawCode.hasNext()) {
      // read first token on line as command
      String command = rawCode.next();
      
      // read further tokens as arguments
      String line = rawCode.nextLine();
      // remove comments if they exist
      if (line.contains(";")) line = line.substring(0, line.indexOf(";"));
      Scanner args = new Scanner(line).useDelimiter(",");
      
      // interpret command
      switch (command) {
        case ";" -> {}
        case "end" -> {
          executing = false;
          return;
        }
        case "ret" -> { return; }
        case "call" -> {
          interpret(input, labels.get(args.next().trim()));
        }
        case "jmp" -> {
          interpret(input, labels.get(args.next().trim()));
          return;
        }
        case "msg" -> {
          while (args.hasNext()) { // account for unbounded # of args
            if (args.hasNext(".*'.*")) { // single quoted string
              String str = args.next().trim();
              
              // validate that str is the whole quoted portion
              if (str.length() == 1 || !str.endsWith("'")) {
                // if a comma is in a quoted region, adds the latter half of the region
                str += "," + args.next();
                str = str.trim();
              }
              // remove the first single quote and the last
              stdout.add(str.substring(1, str.length() - 1));
            } else { // handle registers
              stdout.add(registers.getOrDefault(args.next().trim(), 0).toString());
            }
          }
        }

        case "mov" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.put(storeReg, val);
        }
        case "inc" -> registers.compute(args.next().trim(), (k,v) -> v+1);
        case "dec" -> registers.compute(args.next().trim(), (k,v) -> v-1);
        case "add" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.compute(storeReg, (k, v) -> v+val);
        }
        case "sub" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.compute(storeReg, (k, v) -> v-val);
        }
        case "mul" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.compute(storeReg, (k, v) -> v*val);
        }
        case "div" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.compute(storeReg, (k, v) -> (int) (v/val));
        }
          
        case "cmp" -> {
          String reg1 = args.next().trim();
          String reg2 = args.next().trim();
          
          // if the input is not a register, it is a number
          int val1 = registers.keySet().contains(reg1) ? registers.get(reg1) : Integer.parseInt(reg1);
          int val2 = registers.keySet().contains(reg2) ? registers.get(reg2) : Integer.parseInt(reg2);
          
          // store result as -1 (less than), 0 (equal), 1 (greater than)
          compare = val1 > val2 ? 1 : val1 < val2 ? -1 : 0;
        }
        case "je" -> {
          if (compare == 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }
        case "jne" -> {
          if (compare != 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }
        case "jge" -> {
          if (compare >= 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }
        case "jle" -> {
          if (compare <= 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }
        case "jg" -> {
          if (compare > 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }
        case "jl" -> {
          if (compare < 0) {
            interpret(input, labels.get(args.next().trim()));
            return;
          }
        }

        default -> System.out.println("Invalid command: " + command);
      }
    }
    
    // end of execution reached without end
    terminated = true;
  }
  
  public static void flush() {
    registers.clear();
    labels.clear();
    stdout.clear();
  }
}
