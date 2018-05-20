/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
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
public class Tweet {

    Scanner sc = new Scanner(System.in);
    TwitterUser tu = new TwitterUser();
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Twitter twitter ;
    List<Status> tweets = new ArrayList<>();
    TwitterFactory tf ;
    

    public void getUserTweets(String input,JTextArea j,JTextArea total) {
        twitter = tu.createOauth(cb);
        int pageno = 1;
        String user = input;
        List status = new ArrayList();
        j.append("Gatered Tweets From @"+input+"\n");
        j.append("***********************************************\n");
        while (true) {
            try {
                int size = status.size();
                Paging page = new Paging(pageno++, 100);
                status.addAll(twitter.getUserTimeline(user, page));
                
                
                for (Status s : twitter.getUserTimeline(user, page)) {
                    j.append("@" + s.getUser().getScreenName()  + " : " + s.getUser().getName() + "\t\t" + s.getText()+"\n\n");
                    
                }
                if (status.size() == size) {
                    break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        total.append( status.size()+"tweet(s)\n");
    }

    
    public void getCurrentTrends(int id,JTextArea j,JTextArea total){
        
        try {
            twitter = tu.createOauth(cb);
            Trends trends = twitter.getPlaceTrends(id);
            total.append(+trends.getTrends().length+ "trends");
            j.append("Current "+trends.getTrends().length+" world trends.\n");
            for (int i = 0; i < trends.getTrends().length; i++) {
                j.append(i+"."+trends.getTrends()[i].getName()+"\n");
            }       } catch (TwitterException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void newGetTweets(String input,JTextArea j,JTextArea total){
                String search_term = "#"+input ;
		int totalTweets = 0;
		long maxID = -1;
                final int MAX_QUERIES= 30;
                final int TWEETS_PER_QUERY= 100;
		twitter = tu.createOauth(cb);
		try
		{
			Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
			RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
			j.append("You have "+searchTweetsRateLimit.getRemaining()+" calls remaining out of "+
                                searchTweetsRateLimit.getLimit()+", Limit resets in "+searchTweetsRateLimit.getSecondsUntilReset()+" seconds\n");
			for (int queryNumber=0;queryNumber < MAX_QUERIES; queryNumber++)
			{
				if (searchTweetsRateLimit.getRemaining() == 0)
				{
					j.append("!!! Sleeping for"+ searchTweetsRateLimit.getSecondsUntilReset() +" seconds due to rate limits\n");
					Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset()+2) * 1000l);
				}

				Query q = new Query(search_term);			
				q.setCount(TWEETS_PER_QUERY);				
				q.resultType(Query.RECENT);											
				if (maxID != -1)
				{
					q.setMaxId(maxID - 1);
				}
				QueryResult r = twitter.search(q);
				if (r.getTweets().isEmpty())
				{
					break;			
				}
				for (Status s: r.getTweets())				
				{
					totalTweets++;

					if (maxID == -1 || s.getId() < maxID)
					{
						maxID = s.getId();
					}
					j.append("@"+ s.getUser().getScreenName() +"  : "+s.getUser().getName()+" \t\t "+s.getText()+"\n\n");
				}
				searchTweetsRateLimit = r.getRateLimitStatus();
			}
		}
		catch (Exception e)
		{	
			j.append("Something wrong. ");

			e.printStackTrace();
		}
		total.append(totalTweets+" tweets" );
                
    }     
}
