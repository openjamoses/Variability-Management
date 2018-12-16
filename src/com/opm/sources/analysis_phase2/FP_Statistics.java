/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.excel_.ReadExcelFile_1Row;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class FP_Statistics {

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
        //String[] tokens = Constants.getToken();
        int ct = 0;
        //String file_stats = "Repos.xlsx";
        String file1 = "repos_gp_shaas_3.xlsx";
        String file2 = "repos_gp_statistics3.xlsx";

        //String path_shaas = "/Users/john/Desktop/00commits/server/";
        //String path_stats = "/Users/john/Desktop/00commits/fmerged/cleans/merged/percentages2/statistics/";
        //String path_new = "/Users/john/Desktop/00commits/server/OUTPUT/";
        
        String path_shaas = "";
        String path_stats = "";
        String path_new = "";

        List<String> plist = ReadExcelFile_1Column.readColumnAsString(path_stats + file2, 1, 0, 1);
        List<Double> Tot_Variants = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 1, 1);
        List<Double> Tot_Weeks = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 2, 1);
        List<Double> Tot_Commits = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 3, 1);
        List<Double> Tot_Changes = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 4, 1);
        List<Double> MVA = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 5, 1);
        List<Double> Major = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 6, 1);
        List<Double> Minor = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 7, 1);
        List<Double> Tot_Dev = ReadExcelFile_1Column.readColumnAsNumeric(path_stats + file2, 1, 8, 1);

        List<String> projList = new ArrayList<>();
        for (int i = 0; i < plist.size(); i++) {
            projList.add(plist.get(i).split("\\|")[0]);
        }
        String[] files_collect = {file1};

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Tot_Variants", "Tot_Commits", "Tot_Changes", "MVA", "Major", "Minor", "Tot_Dev", "Weeks", "Tot_Com_ML", "Tot_ChdLOC_ML", "Tot_Com_FPs", "Tot_Unq_Com_FPs", "Tot_ChdLOC_FPs"};

        int s_count = 0;
        for (int a = 0; a < files_collect.length; a++) {
            int count = 0;
            int numbers = File_Details.getWorksheets(path_shaas + files_collect[a]);
            ///System.out.println("Reading Collection Excel....!");

            while (count < numbers) {
                if (count == 0) {
                    allobj.add(datas);
                }
                String project = File_Details.setProjectName(path_shaas + files_collect[a], count, "A2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path_shaas + files_collect[a], count, 1, 2);
                List<Double> Com = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + files_collect[a], count, 6, 2);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + files_collect[a], count, 7, 2);
                List<Double> Unique_CLOC = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + files_collect[a], count, 8, 2);

                System.out.println(count+"\t"+project);
                if (projList.contains(project)) {
                    s_count ++;
                    System.out.println("     Total: "+s_count);
                    double Tot_Com_FPs = 0;
                    double Tot_Unq_Com_FPs = 0;
                    double Tot_ChdLOC_FPs = 0;

                    for (int i = 0; i < names.size(); i++) {
                        Tot_Com_FPs += Com.get(i);
                        Tot_Unq_Com_FPs += unique.get(i);
                        Tot_ChdLOC_FPs += Unique_CLOC.get(i);
                    }
                    int index = projList.indexOf(project);
                    datas = new Object[]{projList.get(index), Tot_Variants.get(index), Tot_Commits.get(index), Tot_Changes.get(index), MVA.get(index), Major.get(index), Minor.get(index), Tot_Dev.get(index), Tot_Weeks.get(index), "", "", Tot_Com_FPs, Tot_Unq_Com_FPs, Tot_ChdLOC_FPs};
                    allobj.add(datas);
                    
                    String file_name = file1.replaceAll("repos_gp_shaas_3", "repos_variants_statistic_final");
                    Create_Excel.createExcelSheet(allobj, path_new + file_name, "variants_statistics_11");
                }

                count++;
            }
        }

    }
}
