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
    
  public static String interpret(final String input) {
    label(input);
    System.out.println(labels);
    
    executing = true;
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
    for (int i = 0; i <= callPos; i++) rawCode.nextLine(); // start on line after callPos
    
    int compare = 0; // for use by cmp
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
        case "call", "jmp" -> {
          interpret(input, labels.get(args.next().trim()));
        }

        case "mov" -> {
          String storeReg = args.next().trim();
          String inputReg = args.next().trim();

          // if the input is not a register, it is a number
          int val = registers.keySet().contains(inputReg) ? registers.get(inputReg) : Integer.parseInt(inputReg);

          registers.put(storeReg, val);
          System.out.println(registers);
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
          if (compare == 0) interpret(input, labels.get(args.next().trim()));
        }
        case "jne" -> {
          if (compare != 0) interpret(input, labels.get(args.next().trim()));
        }
        case "jge" -> {
          if (compare >= 0) interpret(input, labels.get(args.next().trim()));
        }
        case "jle" -> {
          if (compare <= 0) interpret(input, labels.get(args.next().trim()));
        }
        case "jg" -> {
          if (compare > 0) interpret(input, labels.get(args.next().trim()));
        }
        case "jl" -> {
          if (compare < 0) interpret(input, labels.get(args.next().trim()));
        }

        default -> System.out.println("Invalid command: " + command);
      }
    }
  }
  
  public static void terminate() {
    registers.clear();
    labels.clear();
    stdout.clear();
  }
}
