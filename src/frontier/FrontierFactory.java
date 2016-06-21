package frontier;

/**
 * This is a utility class that allows for easy creation of the Frontier object.
 * It also decides which algorithm type will be used.
 */
public class FrontierFactory {

    /**
     * By changing the frontier type, namely the datastructure that tells us which node to visit next,
     * it changes the algorithm.  This provides an easy way toggle the algorithm.  
     */
    public enum FrontierAlgorithim {
        BFS, DFS, A_STAR
    }

    private final FrontierAlgorithim algorithmType;

    public FrontierFactory(FrontierAlgorithim algorithmType) {
        this.algorithmType = algorithmType;
    }

    public Frontier createFrontier() {
        switch(this.algorithmType) {
        case BFS:
            return new BfsFrontier();
        case DFS:
            return new DfsFrontier();
        case A_STAR:
            return new AStarFrontier();
            default:
                throw new UnsupportedOperationException("Algorithm " + this.algorithmType +
                        " is not supported at this time");
        }
    }
}
