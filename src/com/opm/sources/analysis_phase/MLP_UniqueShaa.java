/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase;


import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.ReadExcelFile_1Row;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class MLP_UniqueShaa {
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
        //String file_stats = "Rerow_index.xlsx";
        String file_collect1 = "gp_rmerged_shaass.xlsx";
        String prfile = "gp_prmerged_shaas.xlsx";
        String path_collect = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        String path_pr = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Rerow_index/files_packages/00pr_files/00details/";
        //todo:
        //String path_collect = "";
        //String path_pr = "";
        //String path_new = "";

        String[] FILES = {file_collect1};

        List<String> projectL = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 0, 1);
        List<String> forks = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 1, 1);
        List<String> shaas = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 4, 1);
        
        ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "PR-Commits", "PR-Changes", "Shaa"};
        int tot_s = 0;
        for (int a = 0; a < FILES.length; a++) {
            int sheet_index = 0;
            int total_sheets = File_Details.getWorksheets(path_collect + FILES[a]);
            ///System.out.println("Reading Collection Excel....!");
            sheet_index = 0;
            while (sheet_index < total_sheets) {
                if (sheet_index == 0) {
                    DataSets.add(datas);
                }

                String project = File_Details.setProjectName(path_collect + FILES[a], sheet_index, "A2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path_collect + FILES[a], sheet_index, 1, 2);
                List<String> Mlist = ReadExcelFile_1Row.readRowofStrings(path_collect+FILES[a], sheet_index, 1);
                
                System.out.println(sheet_index+" : "+Mlist);
                List<String> MShaa = new ArrayList<>();
                List<String> FShaa = new ArrayList<>();
                for (int i = 3; i < Mlist.size(); i++) {
                    MShaa.add(Mlist.get(i));
                }
               
                for (int i = 0; i < names.size(); i++) {
                    int row_index = i+2;
                    List<String> Flist = ReadExcelFile_1Row.readRowofStrings(path_collect+FILES[a], sheet_index,row_index);
                    List<String> l = new ArrayList<>() ;
                    for (int j = 7; j < Flist.size(); j++) {
                        l.add(Flist.get(j));
                    }
                    FShaa.add(prfile);
                }

                sheet_index++;
            }// end of while loop
        }// end of for loop 
    }// end of the method declaration..
}
