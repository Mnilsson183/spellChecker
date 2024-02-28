import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Path file = Paths.get(args[0]);
        Path dictionary = Paths.get(args[1]);
        SpellCheck.initDictionary(dictionary);
        Vector<String> misspelled = SpellCheck.check(file);
        SpellCheck.printMisspelledWords(misspelled);
        System.out.println("Alts");
        for(int i = 0; i < misspelled.size(); i++){
            SpellCheck.printAlternatives(misspelled.get(i), SpellCheck.dictionary.findAlternatives(misspelled.get(i)));
        }
        // SpellCheck.printAlternatives(misspelled.get(0), SpellCheck.dictionary.findAlternatives(misspelled.get(0)));
        Vector<String> suggest = SpellCheck.dictionary.autoCompleteSuggest("osc");
        if(suggest == null){
            System.out.println("No results found");
            return;
        }
        for(int i = 0; i < suggest.size(); i++){
            System.out.println(suggest.get(i));
        }
    }
}
