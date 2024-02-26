import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class Dictionary {

    CharacterNode characterNodeStart = new CharacterNode();
    Path sourcePath = null;

    public Dictionary(Path path){
        Scanner dictionaryInputScanner = null;
        try {
            dictionaryInputScanner = new Scanner(path);
            this.sourcePath = path;
        } catch (Exception e) {
            System.out.println("Cannot open the dictionary given");
            dictionaryInputScanner.close();
            return;
        }

        while(dictionaryInputScanner.hasNext()){
            CharacterNode currentCharacterNode = characterNodeStart;
            String word = dictionaryInputScanner.next();
            for(int i = 0; i < word.length(); i++){
                char c = Character.toLowerCase(word.charAt(i));
                currentCharacterNode.chars[c - 97] = new CharacterNode();
                currentCharacterNode = currentCharacterNode.chars[c - 97];
                if(i == word.length() - 1){
                    currentCharacterNode.wordExists = true;
                }
            }
        }

        dictionaryInputScanner.close();
    }

    public boolean findWord(String word){
        CharacterNode currentCharacterNode = characterNodeStart;
        for(int i = 0; i < word.length(); i++){
            if(currentCharacterNode == null){
                return false;
            }
            char c = Character.toLowerCase(word.charAt(i));
            currentCharacterNode = currentCharacterNode.chars[c - 97];
        }

        if(currentCharacterNode == null){
            return false;
        }

        return currentCharacterNode.wordExists;
    }
    /*
    function LevenshteinDistance(char s[0..m-1], char t[0..n-1]):
    // create two work vectors of integer distances
    declare int v0[n + 1]
    declare int v1[n + 1]

    // initialize v0 (the previous row of distances)
    // this row is A[0][i]: edit distance from an empty s to t;
    // that distance is the number of characters to append to  s to make t.
    for i from 0 to n:
        v0[i] = i

    for i from 0 to m - 1:
        // calculate v1 (current row distances) from the previous row v0

        // first element of v1 is A[i + 1][0]
        //   edit distance is delete (i + 1) chars from s to match empty t
        v1[0] = i + 1

        // use formula to fill in the rest of the row
        for j from 0 to n - 1:
            // calculating costs for A[i + 1][j + 1]
            deletionCost := v0[j + 1] + 1
            insertionCost := v1[j] + 1
            if s[i] = t[j]:
                substitutionCost := v0[j]
            else:
                substitutionCost := v0[j] + 1

            v1[j + 1] := minimum(deletionCost, insertionCost, substitutionCost)

        // copy v1 (current row) to v0 (previous row) for next iteration
        // since data in v1 is always invalidated, a swap without copy could be more efficient
        swap v0 with v1
    // after the last swap, the results of v1 are now in v0
    return v0[n]
     */


    private int levenshteinDistance(String s1, String s2, int m, int n){
        // str1 is empty
        if (m == 0) {
            return n;
        }

        // str2 is empty
        if (n == 0) {
            return m;
        }
        if (s1.charAt(m - 1) == s1.charAt(n - 1)) {
            return levenshteinDistance(s1, s2, m - 1, n - 1);
        }
        return 1 + Math.min(
            // Insert
            levenshteinDistance(s1, s2, m, n - 1),
            Math.min(
            // Remove
            levenshteinDistance(s1, s2, m - 1, n),

            // Replace
            levenshteinDistance(s1, s2, m - 1, n - 1)
            )
        );
    }

    public Vector<String> findAlternatives(String s){
        Vector<String> closeStrings = new Vector<>();
        Scanner sourceInput = null;
        try {
            sourceInput = new Scanner(this.sourcePath);
        } catch (Exception e) {
            System.out.println("Error reading from dictionary");
            sourceInput.close();
            return null;
        }

        String closestString = "";
        int closestDistance = Integer.MAX_VALUE;
        while (sourceInput.hasNext()) {
            String dictionaryWord = sourceInput.next();
            int currentDistance = levenshteinDistance(s, dictionaryWord, s.length(), dictionaryWord.length());
            System.out.println(dictionaryWord + currentDistance);
            if(currentDistance < closestDistance){
                closestString = dictionaryWord;
                closestDistance = currentDistance; 
            }
        }
        sourceInput.close();
        closeStrings.add(closestString);
        return closeStrings;

    }
}
