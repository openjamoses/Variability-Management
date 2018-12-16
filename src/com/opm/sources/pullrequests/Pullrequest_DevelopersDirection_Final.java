/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.pullrequests;


import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class Pullrequest_DevelopersDirection_Final {
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

        String file_name = "gp_mlp_fp_devvv.xlsx";
        String variant = "Variant-Statistics.xlsx";
        String path_variants = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Repos/statistics/";

        String path = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";

        int sheet_index = 0;
        int start_row_index = 1;
        
        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variants + variant, sheet_index, 0, 2);

        List<String> project = ReadExcelFile_1Column.readColumnAsString(path + file_name, sheet_index, 0, start_row_index);
        List<Double> Tot_Dev = ReadExcelFile_1Column.readColumnAsNumeric(path + file_name, sheet_index, 1, start_row_index);
        List<Double> MLP_Dev_MJ = ReadExcelFile_1Column.readColumnAsNumeric(path + file_name, sheet_index, 2, start_row_index);
        List<Double> MLP_Dev_MN = ReadExcelFile_1Column.readColumnAsNumeric(path + file_name, sheet_index, 3, start_row_index);
        List<Double> FP_Dev_MJ = ReadExcelFile_1Column.readColumnAsNumeric(path + file_name, sheet_index, 4, start_row_index);
        List<Double> FP_Dev_MN = ReadExcelFile_1Column.readColumnAsNumeric(path + file_name, sheet_index, 5, start_row_index);
        ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Tot_Dev", "MLP_Dev_MJ", "MLP_Dev_MN", "FP_Dev_MJ", "FP_Dev_MN"};// end of assigning the header to the object..
        DataSets.add(datas);
        for (int i = 0; i < project.size(); i++) {
            if (proj_list.contains(project.get(i))) {
                datas = new Object[]{project.get(i), Tot_Dev.get(i), MLP_Dev_MJ.get(i), MLP_Dev_MN.get(i), FP_Dev_MJ.get(i), FP_Dev_MN.get(i)};// end of assigning the header to the object..
                DataSets.add(datas);
            }
        }
        String file_name_output = variant.replaceAll("Variant-Statistics", "gp_mlp_fp_prdetails_final");
        Create_Excel.createExcelSheet(DataSets, path_new + file_name_output, "pr_developers");
    }
}
