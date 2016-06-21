package spellcheck;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Option;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import frontier.FrontierFactory;
import frontier.FrontierFactory.FrontierAlgorithim;

public class SpellCheckDriver {

    public static final String NO_SUGGESTION = "NO SUGGESTION";
    public static final FrontierAlgorithim DEFAULT_ALGORITHM = FrontierAlgorithim.BFS;

    public static class Args {
        
        @Option(name="--dictionaryFile", required=true, usage="The file containing all the words in the dictionary.")
        private File dictionaryFile;
        
        @Option(name="--algorithm", required=false, usage="The file containing all the words in the dictionary.")
        private FrontierAlgorithim algorithm;

        @Option(name="--word", required=false, usage="A word that is misspelled. Can be a comma delimited list of words as well.")
        private String misspelledWord;

        public Args(String[] commandLineArgs) {
            UtilityHelper.initializeArgs4j(commandLineArgs, this);
        }
        
        public FrontierAlgorithim getAlgorithm() {
            if(this.algorithm == null) {
                return DEFAULT_ALGORITHM;
            } else {
                return this.algorithm;
            }
        }

        public boolean haveMisspelledWords() {
            return Strings.emptyToNull(this.misspelledWord) != null; 
        }
        public String[] getMisspelledWords() {
            return this.misspelledWord.split(",");
        }

        public List<String> getDictionaryAsList() {
            try {
                return Files.readLines(this.dictionaryFile, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] cliArgs) {
        Args args = new Args(cliArgs);
        FrontierFactory frontierFactory = new FrontierFactory(args.getAlgorithm());
        SpellCheck spellChecker = new SpellCheck(args.getDictionaryAsList(), frontierFactory);

        if(args.haveMisspelledWords()) {
            for(String misspelledWord: args.getMisspelledWords()) {
                runSpellChecker(spellChecker, misspelledWord);
            }
        } else { // read from standard input.
            Console console = UtilityHelper.getConsole();
            while(true) {
                String mispelledWord = console.readLine(">");
                runSpellChecker(spellChecker, mispelledWord);
            }
        }
    }

    private static void runSpellChecker(SpellCheck spellChecker, String misspelledWord) {
        Optional<Collection<String>> potentialSuggestions = spellChecker.findSuggestions(misspelledWord);
        printOutsuggestions(potentialSuggestions);
    }

    private static void printOutsuggestions(Optional<Collection<String>> potentialSuggestions) {
        if(potentialSuggestions.isPresent()) {
            for(String suggestion: potentialSuggestions.get()) {
                System.out.println(suggestion);
            }
        } else {
            System.out.println(NO_SUGGESTION);
        }
    }
}
