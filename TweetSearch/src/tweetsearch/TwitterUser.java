/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Acer
 */
public class TwitterUser {
    
    public Twitter createOauth(ConfigurationBuilder cb){
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("J3ZTUyp25HWo6TR3pRpVJ0VaV")
        .setOAuthConsumerSecret("7N1UbjpjClfjijmVW0rJ0sPW0zWBXSl3Qy0zrsit7wxNHwLJiP")
        .setOAuthAccessToken("890151296342171648-RIAxB4VWyQBR53o0i4OHVYIpZZOHevx")
        .setOAuthAccessTokenSecret("nYwMNYMCfxFPok035LK7NaY9sjHD7UaLsOdYW6EEGsWqT");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter; 
        twitter = tf.getInstance();
        return twitter;
    }
}
