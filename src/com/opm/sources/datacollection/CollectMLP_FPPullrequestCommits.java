/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.PullrequestCommits;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class CollectMLP_FPPullrequestCommits {

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
        int ct = 0;
        //String file_stats = "Repos.xlsx";
        String file_collect1 = "file_pr_closed-shaa.xlsx";
        String variant = "Variant-Statistics.xlsx";
        String path_collect = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00pr_files/";
        String path_variant = "/Users/john/Desktop/Dev_Commits/00New_Repos/statistics/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00pr_files/00details/";
        //todo:
        //String path_collect = "";
        //String path_variant = "";
        //String path_new = "";
        String[] FILES = {variant};
        List<String> pVList = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 0, 0, 2);

        String[] files_collect = {file_collect1};
        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "PR-Commits", "PR-Changes"};
        int tot_s = 0;
        for (int a = 0; a < files_collect.length; a++) {
            int sheet_index = 0;
            int total_sheets = File_Details.getWorksheets(path_collect + files_collect[a]);
            ///System.out.println("Reading Collection Excel....!");
            while (sheet_index < total_sheets) {
                if (sheet_index == 0) {
                    allobj.add(datas);
                }

                String project = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "B2");
                String p_PR = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "B2");
                List<String> nList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 2, 2);
                List<String> prList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 4, 1);
                List<Double> numList = ReadExcelFile_1Column.readColumnAsNumeric(path_collect + files_collect[a], sheet_index, 6, 1);
                List<String> dprList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 3, 1);
                List<String> nameList = new ArrayList<>();
                nameList.add(project);
                for (int i = 0; i < nList.size(); i++) {
                    nameList.add(nList.get(i));
                }
                if (pVList.contains(project)) {
                    int index = pVList.indexOf(project);
                    JSONParser parser = new JSONParser();
                    List<String> mjList = new ArrayList<>();
                    for (int b = 0; b < 1; b++) {
                        String pPR = dprList.get(b);
                        String[] splits_ml = pPR.split(" \\| ");
                        double f_ml_sheet_index = 0;
                        for (int i = 0; i < splits_ml.length; i++) {
                            String devString = splits_ml[i].substring(0, splits_ml[i].lastIndexOf("(") - 1);
                            //System.out.println(i + "\t" + devString);
                            double mlp_commits = 0;
                            double mlp_changes = 0;
                            String pr_num = splits_ml[i].substring(splits_ml[i].indexOf("(") + 1, splits_ml[i].indexOf(")"));
                            String[] splits_pr = pr_num.split("-");
                           // System.out.println("                 "+project+" \t"+splits_ml[i]);
                            for (int j = 0; j < splits_pr.length; j++) {
                                if (ct == Constants.getToken().length) {
                                    ct = 0;
                                }
                                String tot_string = PullrequestCommits.count(nameList.get(b), splits_pr[j], Constants.getToken(), ct);
                                mlp_commits += Double.parseDouble(tot_string.split("/")[0]);
                                mlp_changes += Double.parseDouble(tot_string.split("/")[1]);
                            }
                                datas = new Object[]{project, devString, mlp_commits, mlp_changes};
                                allobj.add(datas);
                                tot_s++;
                                System.out.println(tot_s + ": " + project + "\t" + devString);
                        }
                    }
                    String file_name = variant.replaceAll("Variant-Statistics", "pr_closed_final3");
                    Create_Excel.createExcelSheet(allobj,path_new + file_name, "pr_commits");
                }

                sheet_index++;
            }
        }
    }
}
