/*
 * https://www.codewars.com/kata/5a529cced8e145207e000010/
 *
 * Identifies numbers of elements in organic molecules based on IUPAC name
 */

import java.util.Map;
import static java.util.Map.entry;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseHer {
    
    final static private Map<String, Integer> RADICALS = new HashMap<>(Map.ofEntries(entry("meth", 1), 
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
                                                                                    )),
                                              MULTIPLIERS = new HashMap<>(Map.ofEntries(entry("di", 2),
                                                                                        entry("tri", 3),
                                                                                        entry("tetra", 4),
                                                                                        entry("penta", 5),
                                                                                        entry("hexa", 6),
                                                                                        entry("hepta", 7),
                                                                                        entry("octa", 8),
                                                                                        entry("nona", 9),
                                                                                        entry("deca", 10),
                                                                                        entry("undeca", 11),
                                                                                        entry("dodeca", 12),
                                                                                        entry("trideca", 13),
                                                                                        entry("tetradeca", 14),
                                                                                        entry("pentadeca", 15),
                                                                                        entry("hexadeca", 16),
                                                                                        entry("heptadeca", 17),
                                                                                        entry("octadeca", 18),
                                                                                        entry("nonadeca", 19)
                                                                                      ));
    final static private Map<String, HashMap<String, Integer>> SUFFIXES = new HashMap<>(Map.ofEntries(entry("ol", new HashMap<>(Map.of("O", 1))),
                                                                                                      entry("al", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                      entry("one", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                      entry("oic acid", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                      entry("carboxylic acid", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                      entry("oate", new HashMap<>(Map.of("H", -3, "O", 2))),
                                                                                                      entry("ether", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                      entry("amide", new HashMap<>(Map.of("H", -1, "O", 1, "N", 1))),
                                                                                                      entry("amine", new HashMap<>(Map.of("H", 1, "N", 1))),
                                                                                                      entry("imine", new HashMap<>(Map.of("H", -1, "N", 1))),
                                                                                                      entry("benzene", new HashMap<>(Map.of("C", 6, "H", 6))),
                                                                                                      entry("thiol", new HashMap<>(Map.of("S", 1))),
                                                                                                      entry("phosphine", new HashMap<>(Map.of("H", 1, "P", 1))),
                                                                                                      entry("arsine", new HashMap<>(Map.of("H", 1, "As", 1)))
                                                                                                    )),
                                                                PREFIXES = new HashMap<>(Map.ofEntries(entry("cyclo", new HashMap<>(Map.of("H", -2))),
                                                                                                       entry("hydroxy", new HashMap<>(Map.of("O", 1))),
                                                                                                       entry("oxo", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                       entry("carboxy", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                       entry("oxycarbonyl", new HashMap<>(Map.of("H", -3, "O", 2))),
                                                                                                       entry("oyloxy", new HashMap<>(Map.of("H", -3, "O", 2))),
                                                                                                       entry("formyl", new HashMap<>(Map.of("H", -2, "O", 2))),
                                                                                                       entry("oxy", new HashMap<>(Map.of("H", -2, "O", 1))),
                                                                                                       entry("amido", new HashMap<>(Map.of("H", -1, "O", 1, "N", 1))),
                                                                                                       entry("amino", new HashMap<>(Map.of("H", 1, "N", 1))),
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
    // Note that alkanes, alkenes alkynes, and akyles aren't present in these lists
    
    String name;
    ArrayList<String> groups;
    Map<String, Integer> elementCount;
    
    
    public ParseHer(String name) {
      this.name = name;
      groups = new ArrayList<String>();
      elementCount = new HashMap<>(Map.of("C", 0, "H", 0));
    }
    
    public Map<String,Integer> parse() {
      (new Scanner(name)).useDelimiter("-?[\\d,]*-").forEachRemaining(groups::add);
      
      separate();
      
      System.out.println(groups);
      count();
      
      return elementCount;
    }
  
    public void separate() {
      for (int i = 0; i < groups.size(); i++) {
        String group = groups.get(i);
        int index = group.indexOf("yl");
        
        if (index != -1) {
          groups.remove(i);
          
          String pre = group.substring(0, index+2);
          String post = group.substring(index+2);
          
          if (!post.isEmpty()) groups.add(post);
          groups.add(i, pre);
        }
      }
    }
  
    public void count() {
      for (String group : groups) {
        System.out.println("Original: " + elementCount);
        String root = group.replace(identifyPrefix(group), "");
        System.out.println("After prefix: " + elementCount);
        root = root.replace(identifySuffix(root), "");
        System.out.println("After suffix: " + elementCount);
        System.out.println("Root: " + root);
        if (root.equals("")) continue;
        
        for (String radical : RADICALS.keySet()) {
          if (removeMultipliers(root).equals(radical)) {
            int carbons = RADICALS.get(radical);
            final int addC = carbons*getMultiplier(radical);
            final int addH = (2*carbons + 2)*getMultiplier(radical);
            elementCount.compute("C", (k,v) -> v+addC);
            elementCount.compute("H", (k,v) -> v+addH);
            break;
          }
        }
        
        System.out.println("Final: " + elementCount);
      }
    }
  
    private int getMultiplier(String group) {
      for (String root : RADICALS.keySet()) group = group.replace(root, "");
      System.out.println("Multiplier: " + group);
      
      int product = 1;
      for (String multiplier : MULTIPLIERS.keySet()) {
        if (group.indexOf(multiplier) != -1) {
          product *= MULTIPLIERS.get(multiplier);
          group = group.replaceFirst(multiplier, "");
          
          break;
        }
      }
      if (product == 1) return 1;
      
      System.out.println("Multiplier: " + product);
      int nextMultiplier = getMultiplier(group);
      return nextMultiplier == 1 ? product : getMultiplier(group);
    }
  
    private String removeMultipliers(String group) {
      for (String multiplier : MULTIPLIERS.keySet()) group = group.replace(multiplier, "");
      return group;
    }
  
    private String identifySuffix(String group) {
      String suffix = group.replace("meth", "");
      for (String root : RADICALS.keySet()) suffix = suffix.replace(root, "");
      if (suffix == "") return "";
      System.out.println("Suffix: " + suffix);
      
      int multiplier = getMultiplier(suffix);
      suffix = removeMultipliers(suffix);
      countSuffix(suffix, multiplier);
      
      System.out.println("Clean suffix: " + suffix);
      return suffix;
    }
  
    private void countSuffix(String suffix, final int multiplier) {
      HashMap<String, Integer> changeBy = new HashMap<String, Integer>(Map.of("H", 0));
      
      for (String s : SUFFIXES.keySet()) {
        if (suffix.endsWith(s)) {
          suffix = suffix.substring(0, suffix.indexOf(s)) + "e";
          
          for (String element : SUFFIXES.get(s).keySet()) {
            int change = SUFFIXES.get(s).get(element);
            changeBy.merge(element, change, Integer::sum);
          }
          
          break;
        }
      }
      // do nothing if suffix is "ane"
      if (suffix.equals("ene") || suffix.equals("yl")) changeBy.compute("H", (k,v) -> v-2);
      if (suffix.equals("yne")) changeBy.compute("H", (k,v) -> v-4);;
      
      
      System.out.println("multiplier: " + multiplier + ", changeBy: " + changeBy);
      for (String element : changeBy.keySet()) {
        final int change = changeBy.get(element)*multiplier;
        System.out.println("Element: " + element + ", change: " + change);
        System.out.println("Pre-suffix: " + elementCount.get(element));
        elementCount.merge(element, change, Integer::sum);
        System.out.println("Post-suffix: " + elementCount.get(element));
      }
    }
  
    private String identifyPrefix(String group) {
      String prefix = group.replace("meth", "");;
      for (String root : RADICALS.keySet()) prefix = prefix.replace(root, "");
      if (prefix == "") return "";
      System.out.println("Prefix: " + prefix);
      
      int multiplier = getMultiplier(prefix);
      prefix = removeMultipliers(prefix);
      
      
      int addH = 0;
      if (prefix.startsWith("cyclo")) {
        addH -= 2;
        prefix = "cyclo";
      }
      
      if (addH == 0) return "";
      
      final int subH = multiplier*addH;
      elementCount.compute("H", (k,v) -> v+subH);
      System.out.println("Clean prefix: " + prefix);
      return prefix;
    }
}
