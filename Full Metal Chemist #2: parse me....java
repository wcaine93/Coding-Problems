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
    
    Scanner name;
    Map<String, Integer> elementCount = new HashMap<>(Map.of("C", 0, "H", 0));
    
    
    public ParseHer(String name) {
      this.name = new Scanner(name).useDelimiter("-?[\\d,]*-");
    }
    
    public Map<String,Integer> parse() {
      while (name.hasNext()) {
        String section = name.next();
        
        if (section.matches(".*yl.*")) {
          Scanner parts = new Scanner(section).useDelimiter("yl");
          section = parts.next();
          
          for (int i = 1; i < RADICALS.length + 1; i++) {
            if (section.equals(RADICALS[i-1])) {
              final int C = i, H = 2*i;
              elementCount.compute("C", (k,v) -> v+=C);
              elementCount.compute("H", (k,v) -> v+=H);
            }
          }
          section = parts.next();
        } if (section.matches(".*ane")) {
          section = section.replace("ane", "");
          
          for (int i = 1; i < RADICALS.length + 1; i++) {
            if (section.equals(RADICALS[i-1])) {
              final int C = i, H = 2*i+2;
              elementCount.compute("C", (k,v) -> v+=C);
              elementCount.compute("H", (k,v) -> v+=H);
            }
          }
        } else System.out.println("unknown: " + section);
      }
      
      return elementCount;
    }    
}
