import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path file = Paths.get(args[0]);
        Path dictionary = Paths.get(args[1]);
        SpellCheck.printMisspelledWords(SpellCheck.check(file, dictionary));
    }
}
