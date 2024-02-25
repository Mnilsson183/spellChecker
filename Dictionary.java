import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class Dictionary {

    CharacterNode characterNodeStart = new CharacterNode();

    public Dictionary(Path path){
        Scanner dictionaryInputScanner = null;
        try {
            dictionaryInputScanner = new Scanner(path);
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

    public Vector<String> findAlternatives(String s){

    }
}
