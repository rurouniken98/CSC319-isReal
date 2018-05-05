/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Acer
 */
public class Tweet {
    
    TwitterUser tu = new TwitterUser();
    
    public void pullTwitter() throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("HOzjKqyUq1ZdUVfyEuI0IQ8op")
        .setOAuthConsumerSecret("NtSENmTOOqVRkPoeAo74o5elX8gQjSmTbWkMgMZttrXa5eSTMQ")
        .setOAuthAccessToken("890151296342171648-FcA28BMXKSJtX5spD0xh0xWiwYJmCz1")
        .setOAuthAccessTokenSecret("nw43sCMpSps0zH1N0YTn0QzpVTl8DaJo2FEjGshi562hW");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        //Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        System.out.println("Showing home timeline.");
        for (Status status : statuses) {
            System.out.println(status.getUser().getName() + ":"
                    + status.getText());
        }
    }

    public void searchTweet(String input) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("HOzjKqyUq1ZdUVfyEuI0IQ8op")
        .setOAuthConsumerSecret("NtSENmTOOqVRkPoeAo74o5elX8gQjSmTbWkMgMZttrXa5eSTMQ")
        .setOAuthAccessToken("890151296342171648-FcA28BMXKSJtX5spD0xh0xWiwYJmCz1")
        .setOAuthAccessTokenSecret("nw43sCMpSps0zH1N0YTn0QzpVTl8DaJo2FEjGshi562hW");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(input);
        QueryResult result;
        try {
            result = twitter.search(query);
            do {
                for (Status status : result.getTweets()) {
                    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                }
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
