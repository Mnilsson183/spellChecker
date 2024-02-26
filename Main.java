import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Path file = Paths.get(args[0]);
        Path dictionary = Paths.get(args[1]);
        SpellCheck.initDictionary(dictionary);
        System.out.println(SpellCheck.dictionary.findAlternatives("egs").get(0));
        // Vector<String> misspelled = SpellCheck.check(file);
        // SpellCheck.printMisspelledWords(misspelled);
        // System.out.println("Alts");
        // SpellCheck.printMisspelledWords(SpellCheck.dictionary.findAlternatives(misspelled.get(0)));

    }
}
