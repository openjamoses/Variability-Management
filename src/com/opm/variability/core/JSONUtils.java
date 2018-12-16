/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.variability.core;

/**
 *
 * @author john
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {
    
  JSONUtils(){}
 
  /**
   * 
   * @param json
   * @return true if the string supplied is a json array format...
   */
  public static boolean isValidJSON(final String json) {
        boolean valid = false;
        JSONParser parser = new JSONParser();
        try {
           JSONArray jsonArray = (JSONArray) parser.parse(json);
           valid = true;
        } catch (ParseException ex) {
           // Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
           valid = false;
        }
       
   
   return valid;
  }
  /**
   * 
   * @param json
   * @return true if the string suplied is a json object
   */
  
  public static boolean isValidJSONObject(final String json) {
        boolean valid = false;
        JSONParser parser = new JSONParser();
        try {
           
           JSONObject allObj = (JSONObject)parser.parse(json);
           valid = true;
        } catch (ParseException ex) {
           // Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
           valid = false;
        }
   return valid;
  }
}
