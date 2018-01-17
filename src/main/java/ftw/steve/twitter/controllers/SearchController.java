package ftw.steve.twitter.controllers;

import ftw.steve.twitter.services.NlpService;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    private Twitter twitter;
    private NlpService nlpService;

    @Inject
    public SearchController(Twitter twitter, NlpService nlpService) {
        this.twitter = twitter;
        this.nlpService = nlpService;
    }

    @GetMapping("/{username}")
    public List<String> queryTwitter(@PathVariable String username) {
        String query = String.format("from:%s", username);
        SearchResults searchResults = twitter.searchOperations().search(query);
        List<String> results = new ArrayList<>();
        for (Tweet t : searchResults.getTweets()) {
            int sentiment = nlpService.findSentiment(t.getText());
            results.add(sentiment + "::" + t.getText());
        }
        return results;
    }

}
