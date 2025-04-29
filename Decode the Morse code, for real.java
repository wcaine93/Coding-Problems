/*
 * https://www.codewars.com/kata/54acd76f7207c6a2880012bb
 *
 * decode Morse Code from 'real-life' scenarios (varying sampling rates and blip/space lengths)
 */

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MorseCodeDecoder {
    /**
     * Given a string in Morse Code, returns the English translation.
     *
     * Accept dots, dashes and spaces, returns human-readable message.
     */
    public static String decodeMorse(String morseCode) {
        //TODO
      return "";
    }
    
    /**
     * Given a string of bits, which may or may not begin or end with '0's,
     * and which may have some variation in the length of the time unit used,
     * returns the Morse Code translation of this message.
     *
     * Accepts 0s and 1s, return dots, dashes and spaces
     *
     */
    public static String decodeBitsAdvanced(String bits) {
      MorseC code = new MorseC(bits);
      
      
      // decide which lengths are long/short using simplified implementation of k-means
      int min = 100000; // arbitrarily large number
      int max = -1;
      for (Integer i : code.getLengths()) {
        if (i < min) min = i;
        if (i > max) max = i;
      }
      double[] centroids = {min, (min + max)/2, max}; // dot length, dash length, word space length
      for (int i = 0; i < 10; i++) kMeans.clusterLengths(centroids, bits);
      
      // TODO: edit k-means model to use MorseC Class,
      // use centroids to assign tokens to values and begin decoding
      
      
      return "";
    }
}

class MorseC {
    String bits;
    private List<String> tokenizedCode = new ArrayList<>();
    private Map<Integer, Integer> lengthCounts = new HashMap<>();

    MorseC(String bits) {
      this.bits = bits;
      
      tokenize(); // must be called before calcLengths
      calcLengths();

      //for (String s : tokenizedCode) System.out.println(s + ": " + s.length());
      lengthCounts.forEach((k, v) -> System.out.println(k + ": " + v));
    }
  
    private void tokenize() {
      String curr = "" + bits.charAt(0);
      bits = bits.substring(1);

      for (char c : bits.toCharArray()) {
        if (curr.charAt(0) == c) curr += c;
        else {
          tokenizedCode.add(curr);
          curr = "" + c;
        }
      }
    }

    private void calcLengths() {
      // must be called after tokenize
      
      // counts the lengths of consecutive 1s and 0s
      tokenizedCode.forEach((t) -> lengthCounts.merge(t.length(), 1, Integer::sum));
    }

    public List<String> getTokens() { return tokenizedCode; }
    public Set<Integer> getLengths() { return lengthCounts.keySet(); }
    public Map<Integer, Integer> getLengthCounts() { return lengthCounts; }
}

class kMeans {
    public static void clusterLengths(double[] means, String bits) {
      // simplified 1-dimensional implementation of k-means
      
      double[] classes = new double[means.length];
      for (int i = 0; i < classes.length; i++) classes[i] = 1;
      
      // iterate through bits and change means according to lengths
      char prev = ' ';
      int count = 0;
      for (char c : bits.toCharArray()) {
        if (prev == c) count++;
        else if (count != 0) {
          // identify the nearest class
          int closest = 0;
          double minDist = 1000000; // arbitrarily large number
          for (int i = 0; i < means.length; i++) {
            double currDist = Math.abs(count - means[i]);
            if (currDist < minDist) {
              closest = i;
              minDist = currDist;
            }
          }
          
          // update the mean of the nearest class
          means[closest] *= classes[closest];
          means[closest] += count;
          classes[closest]++;
          means[closest] /= classes[closest];
          
          count = 0;
        }
        prev = c;
      }
      
      for (double d : means) System.out.println(d);
    }
}
