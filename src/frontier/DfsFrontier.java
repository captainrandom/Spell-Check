package frontier;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * This frontier allows the algorithm search the graph from start using depth first search.
 *
 * Please note DFS is NOT optimal.  This means that if there exists an ambiguity (2 possible paths that
 * lead to different words), then it is possible to get the word that is furthest away from the misspelled word.
 *
 * Ex: weke => wiki instead of weke => wake
 *
 * There are ways to make dfs optimal (look at iterative deepening search using dfs).
 */
public class DfsFrontier implements Frontier {
    
    private final Set<Node> visitedOrInFrontier = Sets.newHashSet();
    private final List<Node> frontier = Lists.newArrayList();

    @Override
    public void add(Node node) {
        this.visitedOrInFrontier.add(node);
        this.frontier.add(node);
    }

    @Override
    public Node pop() {
        return this.frontier.remove(this.frontier.size()-1);  // <-- this is the main difference between dfs and bfs
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
    public boolean containsNodeWithHigherPathCost(Node node) {
        return false;
    }

    @Override
    public void replaceNode(Node node) {}
}
