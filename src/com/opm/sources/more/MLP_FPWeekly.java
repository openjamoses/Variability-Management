/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.more;

import com.opm.variability.core.DateOperations;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class MLP_FPWeekly {

    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        int ct = 0;
        Object[] datas = null;
        
        String file_refac = "Analysis-Data.xlsx";

        //String file_collect2 = "Commits_Cleared_500-1000.xlsx";
        String path2 = "/Users/john/Documents/Destope_data_2018-05_18/00NewDatasets/variability-extension/";
        //String path_stats = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00NewDatasets/variability-extension/";

        //todo:
        int sheet_index = 0;
        List<String> MLV = ReadExcelFile_1Column.readColumnAsString(path2 + file_refac, sheet_index, 0, 1);
        List<String> FV = ReadExcelFile_1Column.readColumnAsString(path2 + file_refac, sheet_index, 1, 1);
        List<String> FV_StartDate = ReadExcelFile_1Column.readColumnAsString(path2 + file_refac, sheet_index, 16, 1);
        List<String> MLV_EndDate = ReadExcelFile_1Column.readColumnAsString(path2 + file_refac, sheet_index, 17, 1);
        //List<String> FV_EndDate = Pick_GeneralNext.pick(path2 + file_refac, sheet_index, 7, 1);

        //System.out.println("Run Started...");
        ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
        datas = new Object[]{"ML-Project", "F-Project", "Week"};// end of assigning the header to the object..      
        DataSets.add(datas);

        for (int i = 0; i < MLV.size(); i++) {
            System.out.println(i + " MLV::: " + MLV.get(i));
            
            System.out.println("       " + i + " FV::: " + FV.get(i));
            double days = 0;
            String dateDiff = DateOperations.diff(FV_StartDate.get(i), MLV_EndDate.get(i));
            days = Double.parseDouble(dateDiff.split("/")[0]);

            double week = days / 7;
            datas = new Object[]{MLV.get(i), FV.get(i), week};// end of assigning the header to the object..      
            DataSets.add(datas);
            String f_name = "MLV_FVweekly.xlsx";
            Create_Excel.createExcelSheet(DataSets,path_new + f_name, "repos_weekly");
        }
    }
}
