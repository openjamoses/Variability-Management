/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase;


import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.MathsFunctions;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class MLP_FPActiveDuration {

    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        Object[] datas = null;
        String category1 = "file_googleplay_com-shaa.xlsx";
        String category2 = "forks_category_400-800.xlsx";
        String category3 = "forks_category_800-1200.xlsx";
        String category4 = "forks_category_1200-1600.xlsx";
        String category5 = "forks_category_1600-1805.xlsx";

        String path = "/Users/john/Desktop/Dev_Commits/00New_Repos/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/statistics/";
        //todo:
        String[] FILES = {category1};

        for (int a = 0; a < FILES.length; a++) {
            ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
            datas = new Object[]{"N/S", "MLP", "Variants", "Duration", "Mean", "Median"};// end of assigning the header to the object..
            int sheets_total = File_Details.getWorksheets(path + FILES[a]);
            int sheet_index = 0;
            while (sheet_index < sheets_total) {
                if (sheet_index == 0) {
                    DataSets.add(datas);
                }
                String project = File_Details.setProjectName(path + FILES[a], sheet_index, "B2");
                //String pCreated = File_Details.setProjectName(path + FILES[a], sheet_index, "B2");
                //String pFirst = File_Details.setProjectName(path + FILES[a], sheet_index, "B2");
                String pLast = File_Details.setProjectName(path + FILES[a], sheet_index, "H2");

                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, 2, 2);
                List<String> createList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, 6, 2);
                List<String> firstList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, 6, 2);
                List<String> LasList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, 7, 2);

                String sheet = File_Details.getWorksheetName(path + FILES[a], sheet_index);

                //System.out.println(firstList.size()+"\t"+LasList.size());
                //System.out.println("" + DateOperations.sorts(firstList, LasList).split("/"));
                String min_date = DateOperations.sorts(firstList, LasList).split("/")[0];
                String max_date = DateOperations.sorts(firstList, LasList).split("/")[1];
                String durations = DateOperations.diff(min_date, max_date);

                List<Double> dList = new ArrayList<>();
                for (int i = 0; i < nameList.size(); i++) {
                    dList.add(Double.parseDouble(DateOperations.diff(LasList.get(i), pLast).split("/")[0])/7);
                }
                double mean = MathsFunctions.getMean(dList);
                double median = MathsFunctions.getMedian(dList);

                datas = new Object[]{Double.parseDouble((sheet_index + 1) + ""), project, Double.parseDouble(nameList.size() + ""), Double.parseDouble(durations.split("/")[0])/7, mean, median};// end of assigning the header to the object..
                DataSets.add(datas);

                String f_name = FILES[a].replaceAll("file_googleplay_com-", "mlp_statistics2_");
                Create_Excel.createExcelSheet(DataSets,path_new + f_name, "ml_statistic");
                sheet_index++;
            }
        }
    }
}
