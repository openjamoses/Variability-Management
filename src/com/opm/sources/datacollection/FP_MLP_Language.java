/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author openja moses...
 */
public class FP_MLP_Language {

    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        indentify();
    }

    /**
     *
     * @throws Exception
     */
    private static void indentify() throws Exception {
        Object[] datas = null;
        String fork1 = "repos_first_lastcom_final.xlsx";
        String path = "";
        String path_new = "";
        String[] FILES = {fork1};
        int token_index = 0;
        for (int a = 0; a < FILES.length; a++) {
            int total_sheets = File_Details.getWorksheets(path + FILES[a]);
            int sheet_index = 0;
            int sheet_counts = 0;

            /**
             * Need to look through all the sheets..
             */
            while (sheet_index < total_sheets) {
                ArrayList< Object[]> DataSet = new ArrayList<Object[]>();
                datas = new Object[]{"Project", "Forks", "Languange"};// end of assigning the header to the object..
                DataSet.add(datas);

                //To pick value in cell numebr A2 in excel file cell va
                String project = File_Details.setProjectName(path + FILES[a], sheet_index, "A2");
                List<String> name = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, 1, 2);
                if (token_index == Constants.getToken().length) {
                    token_index = 0;
                }
                String jsonString = Call_URL.callURL("https://api.github.com/repos/" + project + "?access_token=" + Constants.getToken()[token_index++]);
                int flag = 0;
                JSONParser parser = new JSONParser();
                if (JSONUtils.isValidJSONObject(jsonString) == true) {
                    JSONObject jSONObject = (JSONObject) parser.parse(jsonString);

                    String language = (String) jSONObject.get("language");
                    if (language.equals("Java")) {
                        datas = new Object[]{project, "", language};// end of assigning the header to the object..
                        DataSet.add(datas);
                        flag++;

                        sheet_counts++;
                    }

                }
                /**
                 * found main line a java project
                 * else no need to continue checking the forks...
                 */
                if (flag > 0) {
                    for (int i = 0; i < name.size(); i++) {

                        if (token_index == Constants.getToken().length) {
                            token_index = 0;
                        }
                        jsonString = Call_URL.callURL("https://api.github.com/repos/" + name.get(i) + "?access_token=" + Constants.getToken()[token_index++]);

                        if (JSONUtils.isValidJSONObject(jsonString) == true) {
                            JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
                            String language = (String) jSONObject.get("language");
                            datas = new Object[]{"", name.get(i), language};// end of assigning the header to the object..
                            DataSet.add(datas);
                        }

                    }
                }
                //We can reuse the same sheet name when creating new sheet..!
                String sheet = File_Details.getWorksheetName(path + FILES[a], sheet_index);

                /**
                 * We ignore the apps written in other programming language
                 * apart from java..!
                 */
                if (flag > 0) {
                    
                    String f_name = FILES[a].replaceAll("repos_first_lastcom_final", "repos_first_lastcom_language_final");
                    Create_Excel.createExcelSheet(DataSet, path_new + f_name, project.split("/")[0] + "_" + sheet_counts);
                }
                sheet_index++;
            }
        }
    }
}
