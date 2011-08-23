/**
 * 
 */
package com.idega.block.caldav.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author martynas
 * Last changed: 2011.08.22
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class TestCASRest {

    public static boolean validateFromCAS(String username, String password) throws Exception
    {
        
        String url = "http://cas.sidan.is:80/cas/v1/tickets";
        try 
        {
            HttpURLConnection hsu = (HttpURLConnection)openConn(url);
            String s =   URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode("admin","UTF-8");
            s+="&" +URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode("bedework","UTF-8");
                        
            System.out.println(s);
            OutputStreamWriter out = new OutputStreamWriter(hsu.getOutputStream());
            BufferedWriter bwr = new BufferedWriter(out); 
            bwr.write(s);
            bwr.flush();
            bwr.close();
            out.close();
            
            
            System.out.println(hsu);

            String tgt = hsu.getHeaderField("location");
            System.out.println( hsu.getResponseCode());
            if(tgt != null && hsu.getResponseCode() == 201)
            {
                System.out.println(tgt);
                
                System.out.println("Tgt is : " + tgt.substring( tgt.lastIndexOf("/") +1));
                tgt = tgt.substring( tgt.lastIndexOf("/") +1);
                bwr.close();
                closeConn(hsu);
                
                
                String serviceURL = "http://bedework.sidan.is:80/caladmin/";
                String encodedServiceURL = URLEncoder.encode("service","utf-8") +"=" + URLEncoder.encode(serviceURL,"utf-8");
                System.out.println("Service url is : " + encodedServiceURL);
                
                
                
                String myURL = url+ "/"+ tgt ;
                System.out.println(myURL);
                hsu = (HttpURLConnection)openConn(myURL);
                out = new OutputStreamWriter(hsu.getOutputStream());
                bwr = new BufferedWriter(out); 
                bwr.write(encodedServiceURL);
                bwr.flush();
                bwr.close();
                out.close();
                
                System.out.println("Response code is:  " + hsu.getResponseCode());
                
                BufferedReader isr = new BufferedReader(   new InputStreamReader(hsu.getInputStream()));
                String line;
                System.out.println( hsu.getResponseCode());
                while ((line = isr.readLine()) != null) {
                    System.out.println( line);
                }
                isr.close();
                hsu.disconnect();
                return true;
            }
            else
            {
                return false;
            }  
        }
        catch(MalformedURLException mue)
        {
            mue.printStackTrace();
            throw mue;    
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            throw ioe;
        }
    }
    
    static URLConnection openConn(String urlk)  throws MalformedURLException, IOException
    {
        
        URL url = new URL(urlk);
        HttpURLConnection hsu = (HttpURLConnection) url.openConnection();
        hsu.setDoInput(true);
        hsu.setDoOutput(true);
        hsu.setRequestMethod("POST");
        return hsu;
    }
        
    static void closeConn(HttpURLConnection c)
    {
        c.disconnect();
    }
}

