import java.util.HashMap;
import java.util.Vector;

public class LookupTable {
    static private HashMap<String, Vector<String>> misspelledHashMap = new HashMap<>();

    static public Vector<String> getLookup(String s){
        return misspelledHashMap.get(s);
    }

    static public void addMisspelled(String misspelled, Vector<String> result){
        misspelledHashMap.put(misspelled, result);
    }
}
