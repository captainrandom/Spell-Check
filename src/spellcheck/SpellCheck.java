package spellcheck;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import frontier.Frontier;
import frontier.FrontierFactory;
import frontier.Node;

/**
 * This uses ai search methodologies to find potential correct spellings. 
 */
public class SpellCheck {
    public static final Character[] VOWELS = new Character[] {'a', 'e', 'i', 'o', 'u'};
    public static final Set<Character> VOWEL_SET = ImmutableSet.<Character>builder().add(VOWELS).build();

    private final Multimap<String,String> capitalizationErrors;
    private final FrontierFactory frontierFactory;
    
    public SpellCheck(List<String> dictionary, FrontierFactory frontierFactory) {
        this.capitalizationErrors = UtilityHelper.createMapFromDictionary(dictionary);
        this.frontierFactory = Preconditions.checkNotNull(frontierFactory);
    }

    public Optional<Collection<String>> findSuggestions(String misspelledWord) {
        // want to solve the problem in lower case space first.
        // later we will decide if the solution should be capitalized or not.
        String mispelledWordInLowerCase = misspelledWord.toLowerCase();
        Node startNode = new Node(mispelledWordInLowerCase, 0);

        // create the frontier and add the starting node.
        Frontier frontier = this.frontierFactory.createFrontier();
        frontier.add(startNode);

        while(!frontier.isEmpty()) {
            //System.out.println(frontier);
            // pop node off of the frontier
            Node node = frontier.pop();

            // check for solution
            if(isSolution(node.getWord())) {
                return Optional.of(this.capitalizationErrors.get(node.getWord()));
            }

            // expand frontier
            addChildrenToFrontier(frontier, node);
        }

        return Optional.absent();
    }

    private void addChildrenToFrontier(Frontier frontier, Node word) {
        for(Node child: expandChildren(word)) {
            if(!frontier.containsOrHasVisited(child)) {
                frontier.add(child);
            } else if(frontier.containsNodeWithHigherPathCost(child)) {
                frontier.replaceNode(child);
            }
        }
    }

    private List<Node> expandChildren(Node word) {
        List<Node> children = changeVowel(word);
        children.addAll(removeRepeatedCharacter(word));
        return children;
    }

    @VisibleForTesting
    public List<Node> changeVowel(Node node) {
        String word = node.getWord();
        List<Node> wordsWithSingleVowelRemoved = Lists.newArrayList();
        for(int i = 0; i < word.length(); i++) {
            if(VOWEL_SET.contains(word.charAt(i))) {
                for(Character vowel: VOWELS) {
                    String wordWithRemovedVowel = replaceCharacter(word, vowel, i);
                    wordsWithSingleVowelRemoved.add(Node.createChild(node,wordWithRemovedVowel));
                }
            }
        }
        return wordsWithSingleVowelRemoved;
    }
    
    private String replaceCharacter(String word, char replacement, int index) {
        StringBuilder strBuilder = new StringBuilder(word);
        strBuilder.setCharAt(index, replacement);
        return strBuilder.toString();
    }

    @VisibleForTesting
    public List<Node> removeRepeatedCharacter(Node node) {
        String word = node.getWord();
        List<Node> wordsWithSingleDuplicateRemoved = Lists.newArrayList();
        for(int i = 0, j = 1; j < word.length(); i++, j++) {
            if(word.charAt(i) == word.charAt(j)) {
                String wordWithDuplicateCharRemoved = removeCharacter(word,j);
                wordsWithSingleDuplicateRemoved.add(Node.createChild(node,wordWithDuplicateCharRemoved));
            }
        }
        return wordsWithSingleDuplicateRemoved;
    }
    
    private String removeCharacter(String word, int removeIndex) {
        return word.substring(0, removeIndex) + word.substring(removeIndex+1, word.length());
    }
    
    private boolean isSolution(String word) {
        return this.capitalizationErrors.containsKey(word);
    }
}
