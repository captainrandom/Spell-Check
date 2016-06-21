package spellcheck;
import java.io.Console;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;


public class UtilityHelper {

    public static void initializeArgs4j(String[] commandLineArgs, Object objectToParse) {
        CmdLineParser parser = new CmdLineParser(objectToParse);
        try {
            parser.parseArgument(commandLineArgs);
        } catch(CmdLineException e) {
            System.err.println("Application failed to interpret command-line: " + e.getMessage());
            parser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }
    }
    
    public static Multimap<String, String> createMapFromDictionary(List<String> dictionary) {
        ImmutableSetMultimap.Builder<String,String> dictionaryBuilder = ImmutableSetMultimap.<String,String>builder();
        for(String word: dictionary) {
            dictionaryBuilder.put(word.toLowerCase(), word);
        }
        return dictionaryBuilder.build();
    }
    
    public static Console getConsole() {
        Console console = System.console();
        if(console == null) {
            System.err.println("No console.");
            System.exit(1);
            
        }
        return console;
    }
    
    public static int countDuplicates(String word) {
        int duplicates = 0;
        for(int i = 0, j = 1; j < word.length(); i++, j++) {
            if(word.charAt(i) == word.charAt(j)) {
                duplicates++;
            } else if(SpellCheck.VOWEL_SET.contains(word.charAt(i)) &&
                    SpellCheck.VOWEL_SET.contains(word.charAt(j))) {
                duplicates +=2;
            }
        }
        return duplicates;
    }
    
}
