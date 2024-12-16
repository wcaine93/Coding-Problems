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
        String root = group.replace(identifySuffix(group), "");
        if (root.equals("")) continue;
        
        for (int i = 1; i < RADICALS.length+1; i++) {
          if (root.equals(RADICALS[i-1])) {
            final int addC = i;
            final int addH = 2*i+2;
            elementCount.compute("C", (k,v) -> v+addC);
            elementCount.compute("H", (k,v) -> v+addH);
            break;
          }
        }
      }
    }
  
    private String identifySuffix(String group) {
      String suffix = group;
      for (String root : RADICALS) suffix = suffix.replace(root, "");
      System.out.println("Suffix: " + suffix);
      
      // do nothing if suffix is "ane"
      if (suffix.equals("ene") || suffix.equals("yl")) elementCount.compute("H", (k,v) -> v-2);
      
      return suffix;
    }
}
