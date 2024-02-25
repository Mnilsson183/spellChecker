import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;

public class SpellCheck {
    
    static public Vector<String> check(Path pathToFileToCheck, Path pathToDictionary){
        Dictionary dictionary = new Dictionary(pathToDictionary);
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
            String word = cleanWord(fileInputScanner.next());
            if(!dictionary.findWord(word)){
                misspelledWords.add(word);
            }
        }

        fileInputScanner.close();
        return misspelledWords;
    }

    // TODO impl
    private static String cleanWord(String s){
        return s;
    }

    public static void printMisspelledWords(Vector<String> misspelledWords){
        for(int i = 0; i < misspelledWords.size(); i++){
            System.out.println(misspelledWords.elementAt(i));
        }
    }
}
