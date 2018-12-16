/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;


import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;;

/**
 *
 * @author john
 */
public class Del_MJ_Com {
    
    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        Object[] datas = null;
        int ct = 0;

        String file1 = "gp_mlp_fp_prdetails.xlsx";
        String variant = "Variant-Statistics.xlsx";
        String path_variants = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Repos/statistics/";

        String path = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";

        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variants + variant, 0, 0, 2);

        List<String> project = ReadExcelFile_1Column.readColumnAsString(path + file1, 3, 0, 1);
        List<String> forks = ReadExcelFile_1Column.readColumnAsString(path + file1, 3, 1, 1);
        
        List<Double> PR_Commits = ReadExcelFile_1Column.readColumnAsNumeric(path + file1, 3, 2, 1);
        List<Double> PR_Changes = ReadExcelFile_1Column.readColumnAsNumeric(path + file1, 3, 3, 1);
        
        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "PR_Commits", "PR_Changes"};// end of assigning the header to the object..
        allobj.add(datas);

        for (int i = 0; i < project.size(); i++) {
            if (proj_list.contains(project.get(i))) {
                datas = new Object[]{project.get(i), forks.get(i), PR_Commits.get(i), PR_Changes.get(i)};// end of assigning the header to the object..
                allobj.add(datas);
            }
        }
        String file_name = variant.replaceAll("Variant-Statistics", "gp_mlp_fp_prdetails_final");
        Create_Excel.createExcelSheet(allobj,path_new + file_name, "pr_commits");

    }
}
