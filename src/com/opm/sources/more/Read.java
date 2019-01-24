/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.more;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 *
 * @author john
 */
public class Read {

    public static String read(String input) {
        int Activity = 0, Fragment = 0, Service = 0, Broadcast = 0,content_provder=0, Others = 0;
        try {
            //String line = "";
            //FileReader file = new FileReader(input);

            // lines(Path path, Charset cs)
            //System.out.println("print-results:   "+input);
            String temp = "";
            int flag = 0;
            String[] splits = input.split(System.getProperty("line.separator"));
            for (int i = 0; i < splits.length; i++) {
                String line = splits[i];
                if (line.contains("import") && (line.toLowerCase().contains("firebasestorage")
                        || line.toLowerCase().contains("storage") || line.contains("Observable")
                        || line.toLowerCase().contains("cursor") || line.toLowerCase().contains("database")
                        || line.toLowerCase().contains("contentvalues") || line.toLowerCase().contains("execsql")
                        || line.toLowerCase().contains("provider")  || line.contains("ContentObserver")
                        || line.toLowerCase().contains("preferences") || line.toLowerCase().contains("session") 
                        || line.toLowerCase().contains("sql") || line.contains("DataClip")
                        || line.contains("ContentResolver") )) {
                    
                    //System.out.println(content_provder+" : "+input);
                    content_provder = 1;
                    //flag = 1;
                    //break;
                }
                if (line.contains("extends") && line.toLowerCase().contains("activity")) {
                    Activity = 1;
                    //flag = 1;
                    break;
                }if (line.contains("extends") && line.toLowerCase().contains("fragment")) {
                    Fragment = 1;
                    //flag = 1;
                    break;
                }if (line.contains("extends") && line.toLowerCase().contains("service")) {
                    Service = 1;
                    //flag = 1;
                    break;
                }if (line.contains("extends") && line.toLowerCase().contains("receiver") || line.toLowerCase().contains("appwidgetprovider")) {
                    Broadcast = 1;
                    //flag = 1;
                    break;
                }
                

            }

            //System.out.println(scanner.nextLine());
            /**
             * *
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        int sum = Activity + Fragment + Service  + Broadcast + content_provder;
        if (sum == 0) {
            Others = 1;
        }
        return Activity + "/" + Fragment + "/" + Service + "/" + Broadcast+"/"+content_provder+"/"+Others;
    }
}
