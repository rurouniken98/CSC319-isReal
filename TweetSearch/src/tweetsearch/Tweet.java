/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    TwitterUser tu = new TwitterUser();
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Twitter twitter;
	ArrayList<Status> tweets;

    public void pullTwitter() throws TwitterException {
        cb = tu.createOauth(cb);
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

    
    public void getTweets(String input) {
        cb = tu.createOauth(cb);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(input);
        QueryResult result;
        int count=0;
        try {
            result = twitter.search(query);
            do {
                for (Status status : result.getTweets()) {
                        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                        //count++;
                        //System.out.println(count); 
                }
            } while ((query = result.nextQuery()) == null);
            System.exit(0);
        } catch (TwitterException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print(count);

    }
    public void searchTweet(String input) {
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
            for (Status status: twitter.getUserTimeline(user, page)) {
                //System.out.println("*********Place Tweets :**************\npalce country :"+status.getPlace().getCountry()+"\nplace full name :"+status.getPlace().getFullName()+"\nplace name :"+status.getPlace().getName()+"\nplace id :"+status.getPlace().getId()+"\nplace tipe :"+status.getPlace().getPlaceType()+"\nplace addres :"+status.getPlace().getStreetAddress());
                //System.out.println("Status id : "+status.getId());
                //System.out.println("id user : "+status.getUser().getId());
                //System.out.println("Length status :  "+status.getText().length());
                System.out.println("@" + status.getUser().getScreenName() +" . "+status.getCreatedAt()+ " : "+status.getUser().getName()+"--------"+status.getText());
                //System.out.println("url :"+status.getUser().getURL());
                //System.out.println("Lang :"+status.getLang());
            }
            if (statuses.size() == size)
                break;
        }catch(TwitterException e) {
            e.printStackTrace();
        }
    }
    System.out.println("Total: "+statuses.size());
}
    
  /*  public void getTweets(String tag, int numberOfTweets, int queryCount) {
        cb = tu.createOauth(cb);
        twitter = new TwitterFactory(cb.build()).getInstance();
		tweets = new ArrayList<Status>();
		Query query = new Query(tag);
		long lastID = Long.MAX_VALUE;
 
		while (tweets.size() < numberOfTweets) {
			if (numberOfTweets - tweets.size() > 100)
				query.setCount(queryCount);
			else
				query.setCount(numberOfTweets - tweets.size());
			try {
				QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				System.out.println("Gathered " + tweets.size() + " tweets" + "\n");
				for (Status t : tweets) {
					if (t.getId() < lastID)
						lastID = t.getId();
				}
			}
 
			catch (TwitterException te) {
				System.out.println("Couldn't connect: " + te);
			}
			;
			query.setMaxId(lastID - 1);
		}
	}*/
    public void textUserInterface(){
        System.out.println("Twitter Search");
        System.out.println("********************************************");
        System.out.println("Search User : 1");
        System.out.println("Search Recently: 2");
        
    }

}
