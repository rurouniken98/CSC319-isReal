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
    ConfigurationBuilder cb = new ConfigurationBuilder();
    public ConfigurationBuilder createOauth(){
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("HOzjKqyUq1ZdUVfyEuI0IQ8op")
        .setOAuthConsumerSecret("NtSENmTOOqVRkPoeAo74o5elX8gQjSmTbWkMgMZttrXa5eSTMQ")
        .setOAuthAccessToken("890151296342171648-FcA28BMXKSJtX5spD0xh0xWiwYJmCz1")
        .setOAuthAccessTokenSecret("nw43sCMpSps0zH1N0YTn0QzpVTl8DaJo2FEjGshi562hW");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return cb;
    }
}
