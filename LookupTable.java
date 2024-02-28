import java.util.HashMap;

public class LookupTable {
    static private HashMap<String, String> misspelledHashMap = new HashMap<>();

    static public String getLookup(String s){
        return misspelledHashMap.get(s);
    }

    static public void addMisspelled(String misspelled, String result){
        misspelledHashMap.put(misspelled, result);
    }
}
