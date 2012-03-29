/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rssreader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author victor
 */
   public class Channel{
      URL url;
      String channelName = "";
      public Channel(String name, String url){
        try{
          this.url = new URL(url);
        }catch(MalformedURLException e){
            System.out.println(e.getMessage());
        }
        this.channelName = name;
      }
   }
