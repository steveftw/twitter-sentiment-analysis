package ftw.steve.twitter.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NlpService {

    private StanfordCoreNLP pipeline;

    @PostConstruct
    public void init() {
        pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public int findSentiment(String tweetData) {
        int mainSentiment = 0;
        String cleanTweet = cleanTweet(tweetData);
        if (cleanTweet != null && cleanTweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(cleanTweet);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }

    private String cleanTweet(String tweetData) {
        // make tweet lower case
        tweetData = tweetData.toLowerCase();
        // clean the tweets data
        tweetData = tweetData.replaceAll("(@[A-Za-z0-9_]+)|([^0-9A-Za-z \\t])|(\\w+:\\/\\/\\S+)", " ");
        System.out.println(tweetData);
        return tweetData;
    }

}
