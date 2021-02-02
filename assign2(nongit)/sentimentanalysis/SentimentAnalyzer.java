/*
 * Code created for TxState - CS 3354. 
 */
package sentimentanalysis;

import java.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * This class uses the Stanford Natural Language Processing library to perform
 * Sentiment Analysis on a give text input.
 * It depends on the libraries ejml-0.23.jar, stanford-corenlp-3.9.1.jar, and
 * stanford-corenlp-3.9.1-models.jar
 * 
 * @author vmetsis
 */
public class SentimentAnalyzer {
    
    /**
     * A StanfordCoreNLP object needs to be initialized to perform processing.
     * This object has been defined as static since we only need one instance of
     * it to perform all classification tasks.
     */
    private final static StanfordCoreNLP pipeline;
    
    // Initialize StanfordCoreNLP library as a static object
    static {
        //Disable System.err output to prevent showing library messages.
//        PrintStream err = System.err;
//        System.setErr(new PrintStream(new OutputStream() {
//            public void write(int b) {
//            }
//        }));
        
        // Specify the properties of the NLP tool to be applied to the input text
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        pipeline = new StanfordCoreNLP(props);
        
        // Restore System.err
//        System.setErr(err);
    }
    
    /**
     * Accepts a text (multiple sentences) as input and returns the sentiment of each sentence in that text.
     * @param text The input text.
     * @return A list of sentences and their sentiment as CoreMap objects.
     */
    public static List<CoreMap> getSentencesSentiment(String text) {

        // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
        Annotation annotation = new Annotation(text);

        // Run all the selected Annotators on this text
        pipeline.annotate(annotation);

        // This prints out the results of sentence analysis to file(s) in good formats
        //pipeline.prettyPrint(annotation, System.out);
        
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        //CoreMap sentence = sentences.get(0);
        
        return sentences;
    }
    
    

    /**
     * Main method demonstrates an example of how to iterate through a set of sentences
     * that have been annotated by the Stanford NLP library.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String text = "Booz Allen Hamilton has a bad ass homegrown social collaboration platform. Way cool!  #ttiv";
        
        List<CoreMap> sentences = getSentencesSentiment(text);
        //CoreMap sentence = sentences.get(0);
        
        for (CoreMap sentence : sentences) {
            System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class) + ": " +
                    sentence.get(CoreAnnotations.TextAnnotation.class));

            Scanner in = new Scanner(System.in);

            //TweetDatabase /* not sure what here */ = new TweetDatabase();

            String welcomeString = "\n Welcome to Twitter Reader LLC, Please choose one of the following: \n\n"
                    +"\t0. Exit program.\n"
                    +"\t1. Load new tweet text file.\n"
                    +"\t2. Classify tweets using NLP library.\n"
                    +"\t3. Manually change tweet class label.\n"
                    +"\t4. Add new tweets to database.\n"
                    +"\t5. Delete tweet from database (given its id).\n"
                    +"\t7. Search tweets by user, date, flag, or a matching substring.\n";
            String changeLabel = "Please provide a new class label: \n";
            String iDdelete = "Please provide an ID number: \n";

            System.out.println(welcomeString);

        }
    }

}
