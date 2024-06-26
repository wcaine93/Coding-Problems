import java.util.ArrayList;
import java.util.List;

public class RailFenceCipher {
    
    static String encode(String plainText, int railNum) {
      // assign characters to rail lists
      List<ArrayList<Character>> railList = new ArrayList<ArrayList<Character>>(railNum);
      List<Character> currentRail = new ArrayList<Character>();
      
      int setRail = 0;
      // choose characters for rail based on number of rails above and below
      for (int i = 0; i < railNum; i++) {
        int assignChar = setRail; // add characters to rail beginning at the same rail number
        int lowerOffset = 2*(railNum - setRail) - 2;
        int upperOffset = 2*setRail;
        for (int j = 0; assignChar < plainText.length(); j++) {
          currentRail.add(plainText.charAt(assignChar));
          
          // alternate traversal of upper and lower offset, always begin with lower
          // only when not on the first or last rails, which have no rails above/below
          if (setRail == 0) assignChar += lowerOffset;
          else if (setRail == railNum - 1) assignChar += upperOffset;
          else if (j % 2 == 0) {
            assignChar += lowerOffset;
          } else {
            assignChar += upperOffset;
          }
        }
        
        // move to the next rail
        railList.add(new ArrayList<>(currentRail));
        currentRail.clear();
        setRail++;
      }
      
      // concatenate rails to output string
      String encodedText = "";
      for (int i = 0; i < railNum; i++) {
        for (char c : railList.get(i)) {
          encodedText += c;
        }
      }
      
      return encodedText;
    }
    
    static String decode(String encodedText, int railNum) {
      char[] decodedText = new char[encodedText.length()]; // array indices used as location markers
      
      // reverse the encoding (choose index by calculating offset, position character, move to next)
      int[] railIndex = new int[2]; // index 0 static, index 1 as a counter
      int positionChar;
      for (int currentRail = 0; currentRail < railNum; currentRail++) {
        positionChar = currentRail;
        int lowerOffset = 2*(railNum - currentRail) - 2;
        int upperOffset = 2*currentRail;
        
        for (int i = 0; positionChar < encodedText.length(); i++) {
          decodedText[positionChar] = encodedText.charAt(i + railIndex[0]);
          
          // alternating offsets, excluding first and last rails
          if (currentRail == 0) positionChar += lowerOffset;
          else if (currentRail == railNum - 1) positionChar += upperOffset;
          else if (i % 2 == 0) {
            positionChar += lowerOffset;
          } else {
            positionChar += upperOffset;
          }
          railIndex[1]++;
        }
        
        // begin next iteration at the next character
        railIndex[0] = railIndex[1];
      }
      
      // concatenate character array to output string
      String plainText = "";
      for (char c : decodedText) {
        plainText += c;
      }
      
      return plainText;
    }
}
