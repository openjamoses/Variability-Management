/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.variability.reads;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author john
 */
public class CountTags {

    public static String counts(String tags_url, int ct, String[] tokens) throws ParseException {
        //System.out.println("                  "+tags_url);
        int p = 1; // Page number parameter
        int i = 0; // Commit Counter

        double count = 0;
        while (true) {////loop thru the pagess....
            if (ct == (tokens.length)) {/// the the index for the tokens array...
                ct = 0; //// go back to the first index......
            }
            String jsonString = new Call_URL().callURL(tags_url + "&page=" + p + "&per_page=100&access_token=" + tokens[ct++]);
            if (jsonString.equals("Error")) {
                break;
            }
            JSONParser parser = new JSONParser();
            if (JSONUtils.isValidJSON(jsonString) == true) {

                JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
                if (jsonArray.toString().equals("[]")) {
                    /// Break out of the loop, when empty array is found!
                    break;
                }
                count += jsonArray.size();

            }

            p++;//// Goto the next Page.......
        } /// ******** End of while loop ......

        return count+"/"+ct;
    }
}
