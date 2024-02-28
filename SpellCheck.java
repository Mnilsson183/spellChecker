
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class SpellCheck {

    static Dictionary dictionary = null;

    static public void initDictionary(Path pathToDictionary){
        dictionary = new Dictionary(pathToDictionary);
    }
    
    static public Vector<String> check(Path pathToFileToCheck){
        Scanner fileInputScanner = null;
        try {
            fileInputScanner = new Scanner(pathToFileToCheck);
        } catch (Exception e) {
            System.out.println("Unable to read from the input file");
            fileInputScanner.close();
            return null;
        }

        Vector<String> misspelledWords = new Vector<>();
        while(fileInputScanner.hasNext()){
            addIfMisspelledWord(fileInputScanner.next(), misspelledWords);
        }

        fileInputScanner.close();
        return misspelledWords;
    }

    private static void addIfMisspelledWord(String s, Vector<String> misspelledWords){
        String word = "";
        for(int i = 0; i < s.length(); i++){
            if(Character.isLetter(s.charAt(i))){
                word += Character.toLowerCase(s.charAt(i));
            } else if(s.charAt(i) == '\''){
                word += s.charAt(i);
            } else if(s.charAt(i) == '-'){
                if(!dictionary.findWord(word)){
                    misspelledWords.add(word);
                }
                for(int j = i + 1; j < s.length(); j++){

                }
                addIfMisspelledWord(s.substring(i + 1), misspelledWords);
                return;
            }
        }

        if(!dictionary.findWord(word)){
            misspelledWords.add(word);
        }
        
    }

    public static void printMisspelledWords(Vector<String> misspelledWords){
        for(int i = 0; i < misspelledWords.size(); i++){
            System.out.println(misspelledWords.elementAt(i));
        }
    }

    public static void printAlternatives(String misspelled, Vector<String> alternatives){
        System.out.println(misspelled + " -> " + alternatives.get(0));
    }
}
