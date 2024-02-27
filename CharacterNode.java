
public class CharacterNode{
    // 26 characters + '
    CharacterNode chars[] = new CharacterNode[27];
    boolean wordExists = false;
    char myChar;

    public CharacterNode(char c){
        this.myChar = c;
    }

    public CharacterNode getNextNode(char c){
        if(c == '\''){
            return chars[chars.length-1];
        }
        c = Character.toLowerCase(c);
        return chars[c-97];
    }
}