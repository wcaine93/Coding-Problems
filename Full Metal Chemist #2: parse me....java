/*
 * https://www.codewars.com/kata/5a529cced8e145207e000010/
 *
 * Identifies numbers of elements in organic molecules based on IUPAC name
 */

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseHer {
    
    //                            Number      :   1       2      3...
    final static private String[] RADICALS    = {"meth", "eth", "prop", "but",   "pent",  "hex",  "hept",  "oct",  "non",  "dec",  "undec",  "dodec",  "tridec",  "tetradec",  "pentadec",  "hexadec",  "heptadec",  "octadec",  "nonadec"},
                                  MULTIPLIERS = {        "di",  "tri",  "tetra", "penta", "hexa", "hepta", "octa", "nona", "deca", "undeca", "dodeca", "trideca", "tetradeca", "pentadeca", "hexadeca", "heptadeca", "octadeca", "nonadeca"},
                                  
                                  SUFFIXES    = {         "ol",      "al", "one", "oic acid", "carboxylic acid",                "oate",             "ether", "amide", "amine", "imine", "benzene", "thiol",    "phosphine", "arsine"},
                                  PREFIXES    = {"cyclo", "hydroxy",       "oxo",             "carboxy",         "oxycarbonyl", "oyloxy", "formyl", "oxy",   "amido", "amino", "imino", "phenyl",  "mercapto", "phosphino", "arsino", "fluoro", "chloro", "bromo", "iodo"};
    
    // Note that alkanes, alkenes alkynes, and akyles aren't present in these lists
    
    String name;
    ArrayList<String> groups = new ArrayList<String>();
    Map<String, Integer> elementCount = new HashMap<>(Map.of("C", 0, "H", 0));
    
    
    public ParseHer(String name) {
      this.name = name;
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
        String root = group.replace(identifyPrefix(group), "");
        root = root.replace(identifySuffix(root), "");
        System.out.println("Root: " + root);
        if (root.equals("")) continue;
        
        for (int i = 1; i < RADICALS.length+1; i++) {
          if (removeMultipliers(root).equals(RADICALS[i-1])) {
            final int addC = i*getMultiplier(root);
            final int addH = (2*i + 2)*getMultiplier(root);
            elementCount.compute("C", (k,v) -> v+addC);
            elementCount.compute("H", (k,v) -> v+addH);
            break;
          }
        }
      }
    }
  
    private int getMultiplier(String group) {
      for (String root : RADICALS) group = group.replace(root, "");
      System.out.println("Multiplier: " + group);
      
      int multiplier = 1;
      for (int i = 2; i < MULTIPLIERS.length+2; i++) {
        String mult = MULTIPLIERS[i-2];
        if (group.indexOf(mult) != -1) {
          multiplier *= i;
          group = group.replaceFirst(mult, "");
          
          i = i-1;
          continue;
        }
      }
      System.out.println("Multiplier: " + multiplier);
      return multiplier;
    }
  
    private String removeMultipliers(String group) {
      for (String mult : MULTIPLIERS) group = group.replace(mult, "");
      return group;
    }
  
    private String identifySuffix(String group) {
      String suffix = group;
      for (String root : RADICALS) suffix = suffix.replace(root, "");
      if (suffix == "") return "";
      System.out.println("Suffix: " + suffix);
      
      int multiplier = getMultiplier(suffix);
      suffix = removeMultipliers(suffix);
      
      int addH = 0;
      // do nothing if suffix is "ane"
      if (suffix.equals("ene") || suffix.equals("yl")) addH -= 2;
      if (suffix.equals("yne")) addH -= 4;
      
      
      final int subH = multiplier*addH;
      elementCount.compute("H", (k,v) -> v+subH);
      System.out.println("Clean suffix: " + suffix);
      return suffix;
    }
  
    private String identifyPrefix(String group) {
      String prefix = group;
      for (String root : RADICALS) prefix = prefix.replace(root, "");
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
