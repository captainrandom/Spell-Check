package frontier;

/**
 * This represents the datastructure that tells the generic search algorithm
 * which node to visit next. It also keeps track of which nodes have been
 * visited and which nodes are already in the frontier.
 */
public interface Frontier {

    /**
     * Adds the node to the frontier.
     * @param node
     */
    public void add(Node node);

    /**
     * The next node depends on the type of frontier used. Namely the data structures used.
     *
     * @return The next node to be visited from the frontier. 
     */
    public Node pop();

    /**
     * @return True if there are no more nodes to pop in the frontier. 
     */
    public boolean isEmpty();
    
    /**
     * @param node
     * @return true if the node is in the frontier or if it has been visited before. False otherwise.
     */
    public boolean containsOrHasVisited(Node node);
    
    public boolean containsNodeWithHigherPathCost(Node node);
    
    public void replaceNode(Node node);
}
