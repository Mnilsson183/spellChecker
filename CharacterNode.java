
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

    public String toString(){
        if(wordExists){
            return "" + Character.toUpperCase(myChar);
        } else {
            return "" + myChar;
        }
    }

    public void printNodesFromPointCaller(){
        printNodesFromPoint(0, this);
    }

    public void printNodesFromPoint(int indentLevel, CharacterNode characterNode){
        for(int i = 0; i < indentLevel; i++){
            System.out.print("\t");
        }
        System.out.println(characterNode.toString());
        for(int i = 0; i < chars.length; i++){
            if(characterNode.chars[i] != null){
                printNodesFromPoint(indentLevel + 1, characterNode.chars[i]);
            }
        }
    }
}