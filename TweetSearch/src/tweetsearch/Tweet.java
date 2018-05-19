/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Location;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Acer
 */
public class Tweet extends TwitterSearchGUI{
    Scanner sc = new Scanner(System.in);
    TwitterUser tu = new TwitterUser();
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Twitter twitter;
    List<Status> tweets = new ArrayList<>();
    TwitterFactory tf ;
    List<String> allResult;
    String description = "";
    
   public List hashtag(String input) {
        description = "Search tweets using Hashtag";
        allResult = new ArrayList<>();
        String hashTag = "#" + input;
        int count = 100;
        long sinceId = 0;
        long numberOfTweets = 0;
        cb = tu.createOauth(cb);
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance(); 
        Query queryMax = new Query(hashTag);
        queryMax.setCount(count);
      
        getTweets(queryMax, twitter, "maxId", sinceId, numberOfTweets);
        queryMax = null;
        do {
            Query querySince = new Query(hashTag);
            querySince.setCount(count);
            querySince.setSinceId(sinceId);
            getTweets(querySince, twitter, "sinceId", sinceId, numberOfTweets);
            querySince = null;
        } while (checkIfSinceTweetsAreAvaliable(twitter, input, count, sinceId));
        return allResult;
    }

    public  boolean checkIfSinceTweetsAreAvaliable(Twitter twitter, String hashTag, int count, long sinceId){
        Query query = new Query(hashTag);
        query.setCount(count);
        query.setSinceId(sinceId);
        try {
            QueryResult result = twitter.search(query);
            if (result.getTweets() == null || result.getTweets().isEmpty()) {
                return false;
            }
        } catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
            System.exit(-1);
        }
       return true;
    }

    public  List getTweets(Query query, Twitter twitter, String mode, long sinceId, long numberOfTweets) {
        
        long maxId = 0;
        long whileCount = 0;
        boolean getTweets = true;
        while (getTweets) {
            try {
                QueryResult result = twitter.search(query);
                if (result.getTweets() == null || result.getTweets().isEmpty()) {
                   getTweets = false;
                } else {
                   
                    int forCount = 0;
                    for (Status status : result.getTweets()) {
                        if (whileCount == 0 && forCount == 0) {
                            sinceId = status.getId();
                        }
                        allResult.add("@" + status.getUser().getScreenName() + " : " + status.getUser().getName() + "--------" + status.getText());
                        if (forCount == result.getTweets().size() - 1) {
                            maxId = status.getId();
                        }
                        forCount++;
                    }
                    numberOfTweets = numberOfTweets + result.getTweets().size();
                    query.setMaxId(maxId - 1);                    
                }
            } catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e);
                System.exit(-1);
            }
            whileCount++;
            
        }
        return allResult;
       // System.out.println("Total tweets count=======" + numberOfTweets);
        
      //System.exit(0);
        
    }    
    

    //Search all tweets of a user
    public void searchUserTweets(String input) {
        cb = tu.createOauth(cb);
        int wantedTweets = 112;
        long firstQueryID = Long.MAX_VALUE;
        int remainingTweets = wantedTweets;
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        Query query = new Query(input);
        try {
            
            while (remainingTweets > 0) {
                remainingTweets = wantedTweets - tweets.size();
                if (remainingTweets > 100) {
                    query.count(100);
                } else {
                    query.count(remainingTweets);
                }
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                Status s = tweets.get(tweets.size() - 1);
                firstQueryID = s.getId();
                query.setMaxId(firstQueryID);
                remainingTweets = wantedTweets - tweets.size();
                int count = 0;
                do {
                    for (Status status : result.getTweets()) {
                        System.out.print("@" + status.getUser().getScreenName() + ":" + status.getText());
                        count++;
                        System.out.print(count);
                    }
                } while ((query = result.nextQuery()) == null);
                
                System.out.println("tweets.size() " + tweets.size());
            }
        } catch (TwitterException te) {
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
    public void getUserTweets(String input) {
        description = "Search tweets using user id";
        cb = tu.createOauth(cb);
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        
        int pageno = 1;
        String user = input;
        List status = new ArrayList();
        while (true) {
            try {
                int size = status.size();
                Paging page = new Paging(pageno++, 100);
                status.addAll(twitter.getUserTimeline(user, page));
                System.out.println("***********************************************");

                //System.out.println("Gathered " + twitter.getUserTimeline(user, page).size() + " tweets");
                //get status dan user
                for (Status s : twitter.getUserTimeline(user, page)) {
                    System.out.println("@" + s.getUser().getScreenName() + " . " + s.getCreatedAt() + " : " + s.getUser().getName() + "--------" + s.getText());
                    
                }
                if (status.size() == size) {
                    break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total: " + status.size());
    }

    
    public void getCurrentTrends(int id){
        
        try {
            cb = tu.createOauth(cb);
            tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
            ResponseList<Location> locations = twitter.getAvailableTrends();
            Trends trends = twitter.getPlaceTrends(id);
            for (int i = 0; i < trends.getTrends().length; i++) {
                System.out.println(trends.getTrends()[i].getName());
                
            }       } catch (TwitterException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }






     
}
