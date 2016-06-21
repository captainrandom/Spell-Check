package frontier;

import spellcheck.UtilityHelper;


public class Node implements Comparable<Node> {

    private final String word;
    private final int g;
    private final int heuristic;
    
    public static Node createChild(Node node, String childWord) {
        return new Node(childWord, node.getActualDistance()+1);
    }

    public Node(String value, int g) {
        this.word = value;
        this.g = g;
        this.heuristic = UtilityHelper.countDuplicates(value);
    }
    
    public String getWord() {
        return this.word;
    }
    
    public int getActualDistance() {
        return this.g;
    }

    public int getDistance() {
        return this.g + this.heuristic;
    }

    @Override
    public int hashCode() {
        return this.word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) { return false; }
        else if(obj == this) { return true; }
        else if(!(obj instanceof Node)) { return false; }
        Node node = (Node) obj;
        return this.word.equals(node.getWord());
    }

    @Override
    public int compareTo(Node node) {
        return this.getDistance() - node.getDistance();
    }
}
