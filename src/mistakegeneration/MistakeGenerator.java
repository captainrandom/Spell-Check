package mistakegeneration;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import spellcheck.SpellCheck;
import spellcheck.UtilityHelper;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The mistake generator is based off of a genetic algorithm.
 */
public class MistakeGenerator {

    public final int MUTATE_BOUNDS = 100;
    
    private final Random numGen;
    private final int numIterations;
    private final int mutateThreshold;

    public MistakeGenerator(Random numGen, int numIterations, int mutateThreshold) {
        this.numGen = numGen;
        this.numIterations = numIterations;
        this.mutateThreshold = mutateThreshold;
    }

    public List<Node> generateMistakes(List<String> priorPopulation) {
        List<Node> population = Lists.transform(priorPopulation, new Function<String,Node>() {
            @Override
            public Node apply(String word) {
                return Node.createOriginalNode(word);
            }
        });

        for(int i = 0; i < this.numIterations; i++) {
            for(int j = 0, populationSize = population.size(); j < populationSize; j++) {
                Node x = randomlySelect(population);
                Node y = randomlySelect(population);
                Node child = crossover(x,y);
                
                if(this.mutateThreshold < this.numGen.nextInt(MUTATE_BOUNDS)) {
                    child = mutate(child);
                }
                population.add(child);
            }
        }

        return population;
    }
    
    public int fittnessFunction(Node node) {
        return UtilityHelper.countDuplicates(node.getWord());
    }
    
    public Node randomlySelect(List<Node> population) {
    }

    /**
     * This is also known as the reproduction function
     * @param x
     * @param y
     * @return
     */
    private Node crossover(Node x, Node y) {
        String doubledLetters = doubleLetters(x.getWord(),y.getWord());
        return new Node(y.getOriginalWord(), doubledLetters);
    }
    public Node mutate(Node child) {
        return new Node(child.getOriginalWord(), changeVowel(capitalize(child.getWord())));
    }
    
//errors
    
    private String capitalize(String word) {
        StringBuilder sb = new StringBuilder();
        int randomNum = this.numGen.nextInt(word.length());
        sb.setCharAt(randomNum, Character.toUpperCase(word.charAt(randomNum)));
        return sb.toString();
    }
    
    private String changeVowel(String word) {
        StringBuilder sb = new StringBuilder();
        int randomPosition = this.numGen.nextInt(word.length());
        int randomVowel = this.numGen.nextInt(SpellCheck.VOWELS.length);
        sb.setCharAt(randomPosition, SpellCheck.VOWELS[randomVowel]);
        return sb.toString();
    }
    
    private String doubleLetters(String word1, String word2) {
        HashMap<Character, Integer> wordMap = Maps.newHashMap();
        for(char c: word1.toCharArray()) {
            Integer count = wordMap.get(c);
            wordMap.put(c, count == null ? 1 : count + 1);
        }
        
        StringBuilder strBuilder = new StringBuilder();
        for(char c: word2.toCharArray()) {
            if(wordMap.containsKey(c)) {
                for(int i = 0; i < wordMap.get(c); i++) {
                    strBuilder.append(c);
                }
            } else {
                strBuilder.append(c);
            }
        }
        return strBuilder.toString();
    }
    
    public static class Node {
        public final String originalWord;
        public String word;
        
        public static Node createOriginalNode(String word) {
            return new Node(word,word);
        }

        public Node(String originalWord, String word) {
            this.originalWord = originalWord;
            this.word = word;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getOriginalWord() {
            return originalWord;
        }
        
    }
}
