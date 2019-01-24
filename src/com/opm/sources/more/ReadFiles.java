/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.more;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author john
 */
public class ReadFiles {

    public static List<String> readFile(String project, String shaa, int ct, String[] tockens, List<String> fileNames, List<String> fileNames2) throws ParseException {
        if (ct >= (tockens.length)) {/// the the index for the tokens array...
            ct = 0; //// go back to the first index......
        }
        //System.err.println(project+"  : "+shaa);
        int Activity = 0, Fragment = 0, Service = 0, Broadcast = 0, content_provder = 0, Others = 0;
        String jsonString = Call_URL.callURL("https://api.github.com/repos/" + project + "/commits/" + shaa + "?access_token=" + tockens[ct++]);
        JSONParser parser = new JSONParser();
        if (JSONUtils.isValidJSONObject(jsonString) == true) {
            JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
            if ((JSONArray) jSONObject.get("files") != null) {
                JSONArray fileObj = (JSONArray) jSONObject.get("files");
                for (int i = 0; i < fileObj.size(); i++) {
                    JSONObject fileOBJ = (JSONObject) fileObj.get(i);
                    String fileName = (String) fileOBJ.get("filename");
                    if (fileName.contains(".java")) {
                        if (!fileNames2.contains(fileName)) {
                            
                        }
                        String contents_url = (String) fileOBJ.get("contents_url");
                        if (ct == (tockens.length)) {/// the the index for the tokens array...
                            ct = 0; //// go back to the first index......
                        }
                        String url_string = Call_URL.callURL(contents_url + "&access_token=" + tockens[ct++]);
                        //System.out.println(contents_url);
                        if (JSONUtils.isValidJSONObject(url_string)) {
                            JSONObject url_obj = (JSONObject) parser.parse(url_string);
                            if (ct == (tockens.length)) {/// the the index for the tokens array...
                                ct = 0; //// go back to the first index......
                            }
                            String download_url = (String) url_obj.get("download_url");
                            if (!fileNames.contains(download_url)) {
                               fileNames.add(download_url); 
                            }
                        }
                        
                    }

                }
            }
        }
        fileNames.add(ct+"");
        return fileNames;

        //return Activity + "/" + Fragment + "/" + Service + "/" + Broadcast + "/" + content_provder + "/" + ct;
    }
}
