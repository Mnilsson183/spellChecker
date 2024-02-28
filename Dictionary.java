import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class Dictionary {

    CharacterNode characterNodeStart = new CharacterNode('\u0000');
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
                if(currentCharacterNode.chars[c - 97] == null){
                    currentCharacterNode.chars[c - 97] = new CharacterNode(word.charAt(i));
                }
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

    public Vector<String> findAlternatives(String s){
        Vector<String> closeStrings = new Vector<>();
        Scanner dictionarySourceInput = null;
        try {
            dictionarySourceInput = new Scanner(this.sourcePath);
        } catch (Exception e) {
            System.out.println("Error reading from dictionary");
            dictionarySourceInput.close();
            return null;
        }

        String closestString = "";
        int closestDistance = Integer.MAX_VALUE;
        while (dictionarySourceInput.hasNext()) {
            String dictionaryWord = dictionarySourceInput.next();
            int currentDistance = levenshteinDistance(s, dictionaryWord, s.length(), dictionaryWord.length());
            if(currentDistance < closestDistance){
                closestString = dictionaryWord;
                closestDistance = currentDistance; 
            }
        }
        dictionarySourceInput.close();
        closeStrings.add(closestString);
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
