package mistakegeneration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import mistakegeneration.MistakeGenerator.Node;

import org.kohsuke.args4j.Option;

import spellcheck.UtilityHelper;

import com.google.common.io.Files;

public class MistakeGenerationDriver {

    public static class Args {
        @Option(name="--iterations", required=true, usage="The file containing all the words in the dictionary.")
        int numIterations;

        @Option(name="--dictionaryFile", required=true, usage="The file containing all the words in the dictionary.")
        int mutateThreshold;
        
        @Option(name="--dictionaryFile", required=true, usage="The file containing all the words in the dictionary.")
        private File dictionaryFile;
        
        public Args(String[] commandLineArgs) {
            UtilityHelper.initializeArgs4j(commandLineArgs, this);
        }
        
        public int getNumIterations() {
            return numIterations;
        }

        public int getMutateThreshold() {
            return mutateThreshold;
        }

        public File getDictionaryFile() {
            return dictionaryFile;
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
        MistakeGenerator generator = new MistakeGenerator(new Random(), args.getNumIterations(), args.getMutateThreshold());
        List<Node> mistakes = generator.generateMistakes(args.getDictionaryAsList());
        
        for(Node node: mistakes) {
            System.out.println(node.originalWord + "\t" + node.getWord());
        }
    }
}
