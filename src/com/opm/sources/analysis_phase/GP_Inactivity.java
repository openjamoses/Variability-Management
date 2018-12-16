/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase;

import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Openja Moses
 */
public class GP_Inactivity {

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
        Object[] datas2 = null;
        //String file_stats = "Repos.xlsx";
        String file_collect1 = "file_googleplay_com-shaa.xlsx";
        String variant = "gp_mlpfp_dev_final.xlsx";
        String path_collect = "/Users/john/Documents/";
        String path_variant = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        /**
         * TODO::: Incase we want to run it direct from the server.. inside the
         * root directory..
         *
         */
        //String path_collect = "";/// 
        //String path_variant = "";
        //String path_new = "";
        String[] FILES = {variant};
        //String project = File_Details.setProjectName(path_variant + variant, 0, "B2");
        List<String> projList = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 1, 0, 1);
        List<String> forkNameList = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 1, 1, 1);

        String[] files_collect = {file_collect1};
        ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
        //ArrayList< Object[]> DataSets2 = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "G-Commit", "GP-Update", "Variants"};
        // datas2 = new Object[]{"Project", "Forks","GP-Update"};

        for (int a = 0; a < files_collect.length; a++) {
            int sheet_index = 0;
            int total_sheet = File_Details.getWorksheets(path_collect + files_collect[a]);
            ///System.out.println("Reading Collection Excel....!");

            /**
             * Looping though all the sheet in the excel file
             */
            while (sheet_index < total_sheet) {
                if (sheet_index == 0) {
                    DataSets.add(datas);
                }

                String project = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "B2");
                String pGLast = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "H2");
                String pGupdate = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "I2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 2, 2);
                List<String> GLast = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 7, 2);
                List<String> GUpdate = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 8, 2);

                if (projList.contains(project)) {
                    String durations00 = DateOperations.diff2(pGLast, pGupdate);
                    //datas2 = new Object[]{project, project,Double.parseDouble(durations00.split("/")[0] + "") / 7};
                    //DataSets2.add(datas2);
                    double variants = 1;
                    for (int i = 0; i < names.size(); i++) {
                        if (forkNameList.contains(names.get(i))) {
                            variants++;
                        }
                    }
                    for (int i = 0; i < names.size(); i++) {
                        if (forkNameList.contains(names.get(i))) {
                            String durations1 = DateOperations.diff(GLast.get(i), pGLast);
                            String durations2 = DateOperations.diff2(GUpdate.get(i), pGupdate);
                            String durations01 = DateOperations.diff2(GLast.get(i), GUpdate.get(i));
                            datas = new Object[]{project, names.get(i), Double.parseDouble(durations1.split("/")[0] + "") / 7, Double.parseDouble(durations2.split("/")[0] + "") / 7, variants};
                            DataSets.add(datas);
                            String file_name = variant.replaceAll("gp_mlpfp_dev_final", "gp_mlpfp_duration");
                            Create_Excel.createExcelSheet(DataSets, path_new + file_name, "duration");
                            //Create_Excel.createExcel2(DataSets2, 0, path_new + file_name, "duration_2");
                        }
                    }

                }

                sheet_index++;
            }// End of while loop
        }// End of looping through the different file
    }//End of the method declaration...
}
