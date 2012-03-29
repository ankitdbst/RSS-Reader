/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rssreader;

/**
 *
 * @author victor
 */
import java.net.URL;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * @author Hanumant Shikhare
 */
public class Reader {
  URL url;
  XmlReader reader = null;
  SyndFeed feed;
  String proxy = "10.1.1.16",
           port = "80",
           username = "313672",
           password = "nkumar";

  public void read(URL channelUrl) {
   Authenticator.setDefault(new SimpleAuthenticator(username,password));
   Properties systemProperties = System.getProperties();
   systemProperties.setProperty("http.proxyHost",proxy);
   systemProperties.setProperty("http.proxyPort",port);
   url  = channelUrl;

    try {
      reader = new XmlReader(url);
      feed = new SyndFeedInput().build(reader);
      if (reader != null)
        reader.close();
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
  }

  public class SimpleAuthenticator extends Authenticator{
       private String username,password;
       public SimpleAuthenticator(String username,String password){
          this.username = username;
          this.password = password;
       }

        @Override
       protected PasswordAuthentication getPasswordAuthentication(){
          return new PasswordAuthentication(
                 username,password.toCharArray());
       }
    }
}
