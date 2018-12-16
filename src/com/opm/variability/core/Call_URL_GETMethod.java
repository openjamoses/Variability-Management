/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.variability.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 *
 * @author openja moses
 * Read URL with special GET Content
 */
public class Call_URL_GETMethod {
    /**
     *
     * @param myURL
     * @return
     */
    public static String callURL(String myURL) {
        //System.out.println("Requested URL:" + myURL);
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "application/vnd.github.cloak-preview+json");
            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + myURL, e);
            //return "Error";
            //throw new RuntimeException("Exception while calling URL:" + myURL, e);
        }
        return sb.toString();
    }
    
    public static String callURL2(String myURL, String type) {
        //System.out.println("Requested URL:" + myURL);
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "application/vnd.github."+type+ "+json");
            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + myURL, e);
            //return "Error";
            //throw new RuntimeException("Exception while calling URL:" + myURL, e);
        }
        return sb.toString();
    }
}

