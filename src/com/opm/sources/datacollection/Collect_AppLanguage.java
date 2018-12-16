/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;


import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author openja moses
 */
public class Collect_AppLanguage {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        stats();
    }

    /**
     *
     * @throws Exception
     */
    private static void stats() throws Exception {
        Object[] datas = null;
        int token_index = 0;
        
        
        String variant = "Variant-Statistics.xlsx";
        String path_variant = "/Users/john/Documents/";
        String path_new = "/Users/john/Documents/project_s";
        //Project are listed in the first column in the excel file..!
        int column_index = 0;
        int sheet_index = 0;
        int start_row_index = 3;
        /// Stores all the project names in a list proj_list ....
        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, sheet_index, column_index, start_row_index);
        //Stores the dataset by row.. 
        ArrayList< Object[]> DataSet = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Languange"};// end of assigning the header to the object..
        DataSet.add(datas);
        JSONParser parser = new JSONParser();
        for (int i = 0; i < proj_list.size(); i++) {
            if (token_index == Constants.getToken().length) {
                token_index = 0;
            }
            String jsonString = Call_URL.callURL("https://api.github.com/repos/" + proj_list.get(i) + "?access_token=" + Constants.getToken()[token_index++]);
            if (JSONUtils.isValidJSONObject(jsonString) == true) {
                JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
                String language = (String) jSONObject.get("language");
                datas = new Object[]{proj_list.get(i), language};// end of assigning the header to the object..
                DataSet.add(datas);
            }
            String file_name = variant.replaceAll("Variant-Statistics", "Variant_languages");
            //Creating the excel file with the sheet name vlanguage...;
            Create_Excel.createExcelSheet(DataSet,path_new + file_name, "vlanguage");
        }
    }
}
