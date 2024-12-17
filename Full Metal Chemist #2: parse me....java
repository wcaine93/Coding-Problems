/*
 * https://www.codewars.com/kata/5a529cced8e145207e000010/
 *
 * Identifies numbers of elements in organic molecules based on IUPAC name
 */

import java.util.Map;
import static java.util.Map.entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseHer {
    
    public static final Map<String, Integer> RADICAL_CARBONS = new HashMap<>(Map.ofEntries(entry("meth", 1), 
                                                                                           entry("eth", 2),
                                                                                           entry("prop", 3),
                                                                                           entry("but", 4),
                                                                                           entry("pent", 5),
                                                                                           entry("hex", 6),
                                                                                           entry("hept", 7),
                                                                                           entry("oct", 8),
                                                                                           entry("non", 9),
                                                                                           entry("dec", 10),
                                                                                           entry("undec", 11),
                                                                                           entry("dodec", 12),
                                                                                           entry("tridec", 13),
                                                                                           entry("tetradec", 14),
                                                                                           entry("pentadec", 15),
                                                                                           entry("hexadec", 16),
                                                                                           entry("heptadec", 17),
                                                                                           entry("octadec", 18),
                                                                                           entry("nonadec", 19)
                                                                                          ));
    public static final Map<Integer, String> MULTIPLIERS = new HashMap<>(Map.ofEntries(entry(2, "di"),
                                                                                       entry(3, "tri"),
                                                                                       entry(4, "tetra"),
                                                                                       entry(5, "penta"),
                                                                                       entry(6, "hexa"),
                                                                                       entry(7, "hepta"),
                                                                                       entry(8, "octa"),
                                                                                       entry(9, "nona"),
                                                                                       entry(10, "deca"),
                                                                                       entry(11, "undeca"),
                                                                                       entry(12, "dodeca"),
                                                                                       entry(13, "trideca"),
                                                                                       entry(14, "tetradeca"),
                                                                                       entry(15, "pentadeca"),
                                                                                       entry(16, "hexadeca"),
                                                                                       entry(17, "heptadeca"),
                                                                                       entry(18, "octadeca"),
                                                                                       entry(19, "nonadeca")
                                                                                     ));
  
    public static final Map<String, HashMap<String, Integer>> SUFFIXES = new HashMap<>(Map.ofEntries(entry("an", new HashMap<>()),
                                                                                                     entry("en", new HashMap<>(Map.of("H", -2))),
                                                                                                     entry("yn", new HashMap<>(Map.of("H", -4))),
                                                                                                     entry("yl", new HashMap<>(Map.of("H", -2))),
                                                                                                     entry("ol", new HashMap<>(Map.of("O", 1))),
                                                                                                     entry("al", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                     entry("one", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                     entry("oic acid", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                     entry("carboxylic acid", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                     entry("oate", new HashMap<>(Map.of("H", -3, "O", 2))),
                                                                                                     entry("ether", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                     entry("amide", new HashMap<>(Map.of("H", -1, "O", 1, "N", 1))),
                                                                                                     entry("imine", new HashMap<>(Map.of("H", -1, "N", 1))),
                                                                                                     entry("thiol", new HashMap<>(Map.of("S", 1))),
                                                                                                     entry("phosphine", new HashMap<>(Map.of("H", 1, "P", 1))),
                                                                                                     entry("arsine", new HashMap<>(Map.of("H", 1, "As", 1)))
                                                                                                   )),
                                                              PREFIXES = new HashMap<>(Map.ofEntries(entry("cyclo", new HashMap<>(Map.of("H", -2))),
                                                                                                     entry("benz", new HashMap<>(Map.of("C", 6, "H", 8))), // extra H
                                                                                                     entry("hydroxy", new HashMap<>(Map.of("O", 1))),
                                                                                                     entry("oxo", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                     entry("carboxy", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                     entry("oxycarbonyl", new HashMap<>(Map.of("H", -4, "O", 2))),
                                                                                                     entry("oyloxy", new HashMap<>(Map.of("H", -4, "O", 2))),
                                                                                                     entry("formyl", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                     entry("oxy", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                     entry("amido", new HashMap<>(Map.of("H", -1, "O", 1, "N", 1))),
                                                                                                     entry("amino", new HashMap<>(Map.of("H", 1, "N", 1))),
                                                                                                     entry("amine", new HashMap<>(Map.of("H", 3, "N", 1))),
                                                                                                     entry("imino", new HashMap<>(Map.of("H", -1, "N", 1))),
                                                                                                     entry("phenyl", new HashMap<>(Map.of("C", 5, "H", 5))),
                                                                                                     entry("mercapto", new HashMap<>(Map.of("S", 1))),
                                                                                                     entry("phosphino", new HashMap<>(Map.of("H", 1, "P", 1))),
                                                                                                     entry("arsino", new HashMap<>(Map.of("H", 1, "As", 1))),
                                                                                                     entry("fluoro", new HashMap<>(Map.of("H", -1, "F", 1))),
                                                                                                     entry("chloro", new HashMap<>(Map.of("H", -1, "Cl", 1))),
                                                                                                     entry("bromo", new HashMap<>(Map.of("H", -1, "Br", 1))),
                                                                                                     entry("iodo", new HashMap<>(Map.of("H", -1, "I", 1)))
                                                                                                   ));
    
    String name;
    Map<String, Integer> elementCount;
    
    
    public ParseHer(String name) {
      this.name = name;
      elementCount = new HashMap<>(Map.of("C", 0, "H", 0));
    }
    
    public Map<String,Integer> parse() {
      System.out.println("3".matches("[\\d,]+"));
      count(name);
      
      return elementCount;
    }
  
    public void count(String compound) {
      Scanner parts = new Scanner(compound).useDelimiter("[-\\[\\]]");
      
      int multiplier = 1;
      boolean multiplierApplied = false;
      boolean afterNumber; // for amine, phosphine & arsine edge cases
      String part = "";
      while (parts.hasNext()) {
        afterNumber = part.isEmpty() ? false : part.matches("[\\d,]+");
        part = parts.next();
        System.out.println("Part: " + part);
        
        while (!part.isEmpty()) {
          if (part.matches("[\\d,]+")) { // if number
            Scanner scnr = new Scanner(part).useDelimiter(",");
            multiplier = 0;
            
            while (scnr.hasNext()) {
              multiplier++;
              scnr.next();
            }
            
            part = "";
            multiplierApplied = false;
          }

          if (multiplier != 1) part = part.replaceFirst(MULTIPLIERS.get(multiplier), "");
          
          for (String prefix : PREFIXES.keySet()) {
            if (part.startsWith(prefix)) {
              System.out.println(prefix);
              if (multiplierApplied) multiplier = 1;
              
              for (String element : PREFIXES.get(prefix).keySet()) {
                elementCount.merge(element, PREFIXES.get(prefix).get(element)*multiplier, Integer::sum);
              }
              
              // edge case
              System.out.println(afterNumber);
              if (afterNumber == true && (prefix.equals("amine") || prefix.equals("phosphine") || prefix.equals("arsine"))) {
                elementCount.merge("H", -2*multiplier, Integer::sum);
              }

              part = part.replaceFirst(prefix, "");
              multiplierApplied = true;
              break;
            }
          }

          for (String root : RADICAL_CARBONS.keySet()) {
            if (part.startsWith(root)) {
              System.out.println(root);
              int carbons = RADICAL_CARBONS.get(root);
              if (multiplierApplied) multiplier = 1;

              final int carbonCount = carbons*multiplier;
              final int hydrogenCount = (2*carbons + 2)*multiplier;

              elementCount.merge("C", carbonCount, Integer::sum);
              elementCount.merge("H", hydrogenCount, Integer::sum);

              part = part.replaceFirst(root, "");
              multiplierApplied = true;
              break;
            }
          }
          
          for (int i = 2; i+2 < MULTIPLIERS.size(); i++) {
            if (part.startsWith(MULTIPLIERS.get(i))) {
              multiplier = i;
              multiplierApplied = false;

              part = part.replaceFirst(MULTIPLIERS.get(i), "");
              break;
            }
          }
          
          for (String suffix : SUFFIXES.keySet()) {
            if (part.startsWith(suffix)) {
              System.out.println(suffix);
              for (String element : SUFFIXES.get(suffix).keySet()) {
                elementCount.merge(element, SUFFIXES.get(suffix).get(element)*multiplier, Integer::sum);
              }
              
              part = part.replaceFirst(suffix, "");
              if (part.equals("e")) part = "";
              break;
            }
          }
          
          System.out.println(elementCount);
          System.out.println("Leftover: " + part);
          System.out.println("After number: " + afterNumber);
        }
      }
    }
}
