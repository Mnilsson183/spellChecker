import java.util.Vector;

public class Definition {
    private String definition;
    private Vector<String> alternatives;

    public Definition(String definition, Vector<String> alternatives){

    }

    public String getDefinition() {
        return definition;
    }

    public Vector<String> getAlternatives() {
        return alternatives;
    }

    public void addAlternatives(String alternative){
        this.alternatives.add(alternative);
    }
}
