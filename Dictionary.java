import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class Dictionary {

    CharacterNode characterNodeStart = new CharacterNode('\u0000');
    Path sourcePath = null;
    final int maxDistanceValue = 1;

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

        while(dictionaryInputScanner.hasNextLine()){
            CharacterNode currentCharacterNode = characterNodeStart;
            String word = dictionaryInputScanner.nextLine();
            for(int i = 0; i < word.length(); i++){
                char c = Character.toLowerCase(word.charAt(i));
                try {
                    if(c == '\''){
                        if(currentCharacterNode.chars[26] == null){
                            currentCharacterNode.chars[26] = new CharacterNode(word.charAt(i));
                        }
                        currentCharacterNode = currentCharacterNode.chars[26];
                    } else if(Character.isLetterOrDigit(c)){
                        if(currentCharacterNode.chars[c - 97] == null){
                            currentCharacterNode.chars[c - 97] = new CharacterNode(word.charAt(i));
                        }
                        currentCharacterNode = currentCharacterNode.chars[c - 97];
                    }
                } catch (Exception e) {
                    System.out.println("Bad value in the dictionary " + "\"" + c + "\"" + " in word " + word);
                    System.exit(0);
                }
                if(i == word.length() - 1){
                    currentCharacterNode.wordExists = true;
                }
            }
        }

        dictionaryInputScanner.close();
    }

    public void addNode(String word){
        CharacterNode currentCharacterNode = characterNodeStart;
        for(int i = 0; i < word.length(); i++){
            char c = Character.toLowerCase(word.charAt(i));
            try {
                if(c == '\''){
                    if(currentCharacterNode.chars[26] == null){
                        currentCharacterNode.chars[26] = new CharacterNode(word.charAt(i));
                    }
                    currentCharacterNode = currentCharacterNode.chars[26];
                } else if(Character.isLetterOrDigit(c)){
                    if(currentCharacterNode.chars[c - 97] == null){
                        currentCharacterNode.chars[c - 97] = new CharacterNode(word.charAt(i));
                    }
                    currentCharacterNode = currentCharacterNode.chars[c - 97];
                }
            } catch (Exception e) {
                System.out.println("Bad value in the dictionary " + "\"" + c + "\"" + " in word " + word);
                System.exit(0);
            }
            if(i == word.length() - 1){
                currentCharacterNode.wordExists = true;
            }
        }
    }

    public boolean findWord(String word){
        CharacterNode currentCharacterNode = characterNodeStart;
        for(int i = 0; i < word.length(); i++){
            if(currentCharacterNode == null){
                return false;
            }
            char c = '\u0000';
            try {
                c = Character.toLowerCase(word.charAt(i));
                if(c == '\''){
                    currentCharacterNode = currentCharacterNode.chars[26];
                } else{
                    currentCharacterNode = currentCharacterNode.chars[c - 97];
                }   
            } catch (Exception e) {
                System.out.println("Bad value in the source file " + "\"" + c + "\"" + " in word" + word);
                System.exit(0);
            }
        }

        if(currentCharacterNode == null){
            return false;
        }

        return currentCharacterNode.wordExists;
    }

    private int levenshteinDistance(String s1, String s2, int m, int n){
        // str1 is empty
        if (m == 0) {
            return n;
        }
        // str2 is empty
        if (n == 0) {
            return m;
        }
        // if same
        if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
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

    private int levenshteinMatrixDistance(String s1, String s2){
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Fill in the rest of the matrix
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // No operation required
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]); // Insertion, deletion, or substitution
                }
            }
        }

        // Return the distance
        return dp[m][n];
    }

    private int damerauLevenshteinDistance(String s1, String s2){
        int lenS1 = s1.length();
        int lenS2 = s2.length();

        int[][] distanceMatrix = new int[lenS1 + 1][lenS2 + 1];

        for (int i = 0; i <= lenS1; i++) {
            distanceMatrix[i][0] = i;
        }
        for (int j = 0; j <= lenS2; j++) {
            distanceMatrix[0][j] = j;
        }

        // Fill in the matrix
        for (int i = 1; i <= lenS1; i++) {
            for (int j = 1; j <= lenS2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                distanceMatrix[i][j] = Math.min(
                        distanceMatrix[i - 1][j] + 1, // deletion
                        Math.min(
                                distanceMatrix[i][j - 1] + 1, // insertion
                                distanceMatrix[i - 1][j - 1] + cost // substitution
                        )
                );

                // Transposition check
                if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2) && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    distanceMatrix[i][j] = Math.min(
                            distanceMatrix[i][j],
                            distanceMatrix[i - 2][j - 2] + cost
                    );
                }
            }
        }

        // Return the distance between the two strings
        return distanceMatrix[lenS1][lenS2];
    }

    private Vector<String> readDictionaryToArray(){
        Scanner dictionarySourceInput = null;
        Vector<String> dictionaryVector = new Vector<>();
        try {
            dictionarySourceInput = new Scanner(this.sourcePath);
        } catch (Exception e) {
            System.out.println("Error reading from dictionary");
            dictionarySourceInput.close();
            return null;
        }
        while (dictionarySourceInput.hasNext()) {
            dictionaryVector.add(dictionarySourceInput.next());
        }
        dictionarySourceInput.close();
        return dictionaryVector;
    }

    public Vector<String> findAlternatives(String s){
        Vector<String> closeStrings = new Vector<>();
        if(LookupTable.getLookup(s) != null){
            closeStrings.add(LookupTable.getLookup(s));
            return closeStrings;
        }

        String closestString = "";
        int closestDistance = Integer.MAX_VALUE;
        Vector<String> dictionaryVector = readDictionaryToArray();
        for(int i = 0; i < dictionaryVector.size(); i++){
            //int currentDistance = levenshteinDistance(s, dictionaryWord, s.length(), dictionaryWord.length());
            //int currentDistance = damerauLevenshteinDistance(s, dictionaryWord);
            int currentDistance = levenshteinMatrixDistance(s, dictionaryVector.get(i));
            if(currentDistance < closestDistance){
                closestString = dictionaryVector.get(i);
                closestDistance = currentDistance; 
            }
        }
        LookupTable.addMisspelled(s, closestString);
        closeStrings.add(closestString);
        LookupTable.addMisspelled(s, closestString);
        return closeStrings;
    }

    public Vector<String> autoCompleteSuggest(String wordPartial){
        CharacterNode currentCharacterNode = characterNodeStart;
        for(int i = 0; i < wordPartial.length(); i++){
            if(currentCharacterNode == null){
                return null;
            }
            char c = Character.toLowerCase(wordPartial.charAt(i));
            currentCharacterNode = currentCharacterNode.chars[c - 97];
        }

        if(currentCharacterNode == null){
            return null;
        }
        wordPartial = wordPartial.substring(0,wordPartial.length() - 1);
        dumpTreeStructure(currentCharacterNode, wordPartial);
        return sortTheDumpedTreeStructureByStringSize(freeUseVector);
    }

    // clear before use
    // Vector for local use
    // 
    private Vector<String> freeUseVector = new Vector<>();

    /* 
     * Dump the characterNode tree structure into the free use vector
    */
    private void dumpTreeStructure(CharacterNode characterNodeHead, String additional){
        if(characterNodeHead.wordExists == true){
            freeUseVector.add(additional + characterNodeHead.myChar);
        }
        for(int i = 0; i < characterNodeHead.chars.length; i++){
            if(characterNodeHead.chars[i] != null){
                dumpTreeStructure(characterNodeHead.chars[i], additional+characterNodeHead.myChar);
            }
        }
    }

    private Vector<String> sortTheDumpedTreeStructureByStringSize(Vector<String> treeDump){
        quickSortOnCharacterLength(treeDump, 0, treeDump.size() - 1);

        Vector<String> rets = new Vector<>();
        for(int i = 0; i < treeDump.size(); i++){
            rets.add(treeDump.get(i));
        }
        return rets;
    }

    private static void swap(Vector<String> arr, int i, int j)
    {
        String temp = arr.get(i);
        arr.setElementAt(arr.get(j), i);
        arr.setElementAt(temp, j);
    }
 
    private static int partition(Vector<String> arr, int low, int high)
    {
        int pivot = arr.get(high).length();
 
        int i = (low - 1);
 
        for (int j = low; j <= high - 1; j++) {
 
            if (arr.get(j).length() < pivot) {
 
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }
 
    private static void quickSortOnCharacterLength(Vector<String> arr, int low, int high)
    {
        if (low < high) {
 
            int pi = partition(arr, low, high);
 
            quickSortOnCharacterLength(arr, low, pi - 1);
            quickSortOnCharacterLength(arr, pi + 1, high);
        }
    }

    public void printDictionary(){
        characterNodeStart.printNodesFromPointCaller();
    }
}
