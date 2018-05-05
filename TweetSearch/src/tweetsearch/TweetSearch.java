/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetsearch;
import twitter4j.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Acer
 */
public class TweetSearch {

    /**
     * @param args the command line arguments
     * 
     */
    public static void main(String[] args) throws TwitterException{
       Scanner sc = new Scanner(System.in);
       Tweet t = new Tweet();
       t.pullTwitter();
       System.out.println("Please input keyword");
       String input = sc.next();
       t.searchTweet(input);
    }
    
}

