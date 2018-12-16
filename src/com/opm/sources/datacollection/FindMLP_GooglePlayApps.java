/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.opm.variability.reads.Download_Manifest;
import com.opm.variability.util.Constants;

/**
 *
 * @author Openja Moses
 * Here we only need the repos name 
 * in excel file.. and should be in the first column..!
 */
public class FindMLP_GooglePlayApps {
/**
 * 
 * @param args
 * @throws Exception 
 */
    public static void main(String[] args) throws Exception {
        extractManifest_();
    }
/**
 * 
 * @throws Exception 
 */
    private static void extractManifest_() throws Exception {
        Object[] datas = null;
        String repos = "repos_new2.xlsx";

        String path = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00google/";
        //todo:
        String[] files = {repos};

        for (int a = 0; a < files.length; a++) {

            int numbers = File_Details.getWorksheets(path + files[a]);
            int sheet_index = 0;
            int ct = 0;
            int column_num0 = 0;
            int column_num3 = 3;
            int column_num4 = 4;
            int column_num5 = 5;
            int start_row_index = 1;
            
            while (sheet_index < numbers) {
                ArrayList< Object[]> DataSet = new ArrayList<Object[]>();
                
                datas = new Object[]{"Repos", "Package", "Forks", "Size", "Tags"};

                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + files[a], sheet_index, column_num0, start_row_index);
                List<Double> forks = ReadExcelFile_1Column.readColumnAsNumeric(path + files[a], sheet_index, column_num3,start_row_index);
                List<Double> size = ReadExcelFile_1Column.readColumnAsNumeric(path + files[a], sheet_index, column_num4,start_row_index);
                List<Double> tags = ReadExcelFile_1Column.readColumnAsNumeric(path + files[a], sheet_index, column_num5,start_row_index);
                for (int i = 0; i < nameList.size(); i++) {
                    if (i == 0) {
                        DataSet.add(datas);
                    }
                    System.out.println(i + " : " + nameList.get(i));
                    Set<String> pSet = Download_Manifest.downloads(nameList.get(i), Constants.getToken(), ct);

                    Iterator iterator = pSet.iterator();
                    List<String> p_list = new ArrayList<>();
                    while (iterator.hasNext()) {
                        p_list.add((String) iterator.next());
                    }
                    ct = Integer.parseInt(p_list.get(p_list.size() - 1));
                    p_list.remove(p_list.get(p_list.size() - 1));
                    String packages = "";
                    if (p_list.size() == 1) {
                        packages = p_list.get(0);
                    } else if (p_list.size() > 1) {
                        for (int j = 0; j < p_list.size(); j++) {
                            if (j < p_list.size() - 1) {
                                packages = packages.concat(p_list.get(j) + " , ");
                            }
                            if (j == p_list.size() - 1) {
                                packages = packages.concat(p_list.get(j) + "");
                            }
                        }
                    }

                    if (p_list.size() > 0) {
                        datas = new Object[]{nameList.get(i), packages};
                        DataSet.add(datas);
                        String f_name = files[a].replaceAll("repos_new", "google_play_");
                        
                        Create_Excel.createExcelSheet(DataSet, path_new + f_name, "repos_" + sheet_index);
                    }
                }
                sheet_index++;
            }
        }
    }
}
