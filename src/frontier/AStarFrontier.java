package frontier;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class AStarFrontier implements Frontier {

    private final PriorityQueue<Node> queue;
    private final Set<Node> visitedOrInFrontier;
    private final Map<Node,Node> inFrontierOnly;
    
    public AStarFrontier() {
        this.queue = new PriorityQueue<>();
        this.visitedOrInFrontier = Sets.newHashSet();
        this.inFrontierOnly = Maps.newHashMap();
    }

    @Override
    public void add(Node node) {
        this.queue.add(node);
        this.visitedOrInFrontier.add(node);
        this.inFrontierOnly.put(node,node);
    }

    @Override
    public Node pop() {
        Node node = this.queue.poll();
        this.inFrontierOnly.remove(node);
        return node;
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public boolean containsOrHasVisited(Node node) {
        return this.visitedOrInFrontier.contains(node);
    }

    @Override
    public boolean containsNodeWithHigherPathCost(Node node) {
        return this.inFrontierOnly.containsKey(node) && this.inFrontierOnly.get(node).compareTo(node) > 0;
    }

    @Override
    public void replaceNode(Node node) {
        Node highCostNode = this.inFrontierOnly.get(node);
        this.queue.remove(highCostNode);
        add(node);
    }

}
