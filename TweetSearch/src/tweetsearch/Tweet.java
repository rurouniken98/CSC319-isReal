/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import twitter4j.Paging;
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
    
    Scanner sc = new Scanner(System.in);
    TwitterUser tu = new TwitterUser();
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Twitter twitter;
    List<Status> tweets = new ArrayList<>();
    
    public void getTweets(String input) {
        cb = tu.createOauth(cb);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        int pageno = 1;
        String user = input;
        List statuses = new ArrayList();
        while (true) {
            try {
                int size = statuses.size();
                Paging page = new Paging(pageno++, 100);
                statuses.addAll(twitter.getUserTimeline(user, page));
                System.out.println("***********************************************");

                //System.out.println("Gathered " + twitter.getUserTimeline(user, page).size() + " tweets");
                //get status dan user
                for (Status status : twitter.getUserTimeline(user, page)) {
                    //System.out.println("*********Place Tweets :**************\npalce country :"+status.getPlace().getCountry()+"\nplace full name :"+status.getPlace().getFullName()+"\nplace name :"+status.getPlace().getName()+"\nplace id :"+status.getPlace().getId()+"\nplace tipe :"+status.getPlace().getPlaceType()+"\nplace addres :"+status.getPlace().getStreetAddress());
                    //System.out.println("Status id : "+status.getId());
                    //System.out.println("id user : "+status.getUser().getId());
                    //System.out.println("Length status :  "+status.getText().length());
                    System.out.println("@" + status.getUser().getScreenName() + " . " + status.getCreatedAt() + " : " + status.getUser().getName() + "--------" + status.getText());
                    //System.out.println("url :"+status.getUser().getURL());
                    //System.out.println("Lang :"+status.getLang());
                }
                if (statuses.size() == size) {
                    break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total: " + statuses.size());
    }

    public void searchTweets(String input) {
        cb = tu.createOauth(cb);
        int wantedTweets = 112;
        long firstQueryID = Long.MAX_VALUE;
        int remainingTweets = wantedTweets;
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
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
                        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
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
    
    public void textUserInterface() {
        System.out.println("Twitter Search");
        System.out.println("********************************************");
        /*System.out.println("Search  : 1");
        System.out.println("Get user timeline  : 2");
        System.out.println("Please input command");
        int command = sc.nextInt();
        switch (command) {
            case 1:
                {
                    System.out.println("Please input keword");
                    String input = sc.next();
                    searchTweets(input);
                    break;
                }
                case 2:
                {
                    System.out.println("Please input user");
                    String input = sc.next();
                    getTweets(input);
                    break;
                }
                case 3:
                {
                    System.out.println("Please input hashtag");
                    String input = sc.next();
                    hashtag(input);
                    break;
                }
        }*/
        System.out.println("Please input keyword:");
        String input=sc.nextLine();
        hashtag(input);
        searchAgain();
        
    }
    
    public void searchAgain() {
        System.out.println("Do you want to search again?[y/n]");
        String input = sc.next();
        if (input.equalsIgnoreCase("y")) {
            textUserInterface();
        } else if (input.equalsIgnoreCase("n")) {
            System.out.println("EXIT");
            System.exit(0);
        }
    }
    
   public void hashtag(String input) {
        String hashTag = "#" + input;
        int count = 100;
        long sinceId = 0;
        long numberOfTweets = 0;
        cb = tu.createOauth(cb);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance(); 
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

    public  void getTweets(Query query, Twitter twitter, String mode, long sinceId, long numberOfTweets) {
       long maxId = 0;
        long whileCount = 0;
          boolean getTweets = true;
        while (getTweets) {
            try {
                QueryResult result = twitter.search(query);
                if (result.getTweets() == null || result.getTweets().isEmpty()) {
                   getTweets = false;
                } else {
                    System.out.println("***********************************************");
                    System.out.println("Gathered " + result.getTweets().size() + " tweets");
                    int forCount = 0;
                    for (Status status : result.getTweets()) {
                        if (whileCount == 0 && forCount == 0) {
                            sinceId = status.getId();
                            //System.out.println("sinceId= " + sinceId);
                        }
                        System.out.println("@" + status.getUser().getScreenName() + " : " + status.getUser().getName() + "--------" + status.getText());
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
        
        System.out.println("Total tweets count=======" + numberOfTweets);
      System.exit(0);
        
    }    
    







     
}
