package frontier;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This frontier is one that searches the graph from the start node using breath first search.
 * 
 * Please note that this is optimal, but the running time is O(B^d), where B is the branching factor,
 * and d is the depth of the closest solution.
 */
public class BfsFrontier implements Frontier {

    private final Set<Node> visitedOrInFrontier = Sets.newHashSet();
    private final List<Node> frontier = Lists.newArrayList();

    @Override
    public void add(Node node) {
        this.visitedOrInFrontier.add(node);
        this.frontier.add(node);
    }

    @Override
    public Node pop() {
        return this.frontier.remove(0);
    }

    @Override
    public boolean isEmpty() {
        return this.frontier.isEmpty();
    }

    @Override
    public boolean containsOrHasVisited(Node node) {
        return this.visitedOrInFrontier.contains(node);
    }

    @Override
    public boolean containsNodeWithHigherPathCost(Node node) { return false; }

    @Override
    public void replaceNode(Node node) {}
    
    public String toString() {
        return this.frontier.toString();
    }
}
