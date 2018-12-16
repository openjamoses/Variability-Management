/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.core.Call_URL;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import com.opm.variability.util.Constants;

/**
 *
 * @author john
 */
public class EliminateRepos_Less6Commits {

    public static void main(String[] args) throws Exception {
        check();
    }
    private static void check() throws Exception {
        Object[] data_row = null;
        String repos = "google_play_.xlsx";
        //String repos_created = "google_play_.xlsx";

        String path = "/Users/john/Desktop/Dev_Commits/";
        //String path_created = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00atleast_6/";
        //todo:
        String[] files = {repos};
        for (int a = 0; a < files.length; a++) {

            int numbers = File_Details.getWorksheets(path + files[a]);
            int count = 0;
            int ct = 0;
            while (count < numbers) {
                ArrayList< Object[]> DataSet1 = new ArrayList<Object[]>();
                ArrayList< Object[]> DataSet2 = new ArrayList<Object[]>();
                data_row = new Object[]{"Repos", "Package","Created_at", "Forks", "Language"};
                DataSet1.add(data_row);
                
                //List<String> reposList = Pick_GeneralNext.pick(path_created + repos_created, 0, 0, 1);
                
               
                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + files[a], count, 0, 1);
                 
                List<String> packList = ReadExcelFile_1Column.readColumnAsString(path + files[a], count, 1, 1);
                List<Double> forks = ReadExcelFile_1Column.readColumnAsNumeric(path + files[a], count, 2,1);
                List<String> createdList = ReadExcelFile_1Column.readColumnAsString(path + files[a], count, 3, 1);
                List<String> langList = ReadExcelFile_1Column.readColumnAsString(path + files[a], count, 4, 1);
                //List<Double> tags = Pick_GeneralNumeric.pick(path + files[a], count, 3);
                for (int i = 0; i < nameList.size(); i++) {

                    String[] tokens = Constants.getToken();
                    int flag = 0;
                    if (ct == (tokens.length)) {/// the the index for the tokens array...
                        ct = 0; //// go back to the first index......
                    }
                    String jsonString = Call_URL.callURL("https://api.github.com/repos/" + nameList.get(i) + "/commits?until=" + Constants.cons.TODAY_DATE + "&page=1&per_page=100&access_token=" + tokens[ct++]);
                    JSONParser parser = new JSONParser();
                    if (JSONUtils.isValidJSON(jsonString) == true) {
                        JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
                        if (jsonArray.size() >= 6) {
                            flag = 1;
                        }
                    }
                    System.out.println(i + " : " + nameList.get(i) + " \t " + flag);                  
                    if (flag == 1) {
                        data_row = new Object[]{nameList.get(i), packList.get(i),createdList.get(i), forks.get(i), langList.get(i)};
                        DataSet1.add(data_row);
                    } else {
                        data_row = new Object[]{nameList.get(i), packList.get(i),createdList.get(i), forks.get(i), langList.get(i)};
                        DataSet2.add(data_row);
                    }
                }
                String file_outputName = files[a].replaceAll("google_play_", "repos_atleast_6");
                
                String sheetName_atleast_6 = "atleast_6";//
                String sheetName_lessthan_6 = "lessthan_6";//
                Create_Excel.createExcelSheet(DataSet1,path_new + file_outputName, sheetName_atleast_6);
                Create_Excel.createExcelSheet(DataSet2,path_new + file_outputName, sheetName_lessthan_6);
                count++;
            }
        }
    }
}
