/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;
import twitter4j.*;
/**
 *
 * @author Acer
 */
public class TweetSearch {

    /**
     * @param args the command line arguments
     * @throws twitter4j.TwitterException
     * 
     */
    public static void main(String[] args) throws TwitterException{
        TwitterSearchGUI t = new TwitterSearchGUI();
        t.setVisible(true);
        //Tweet tweet = new Tweet();
        //tweet.textUserInterface();
       
    }
    
}

