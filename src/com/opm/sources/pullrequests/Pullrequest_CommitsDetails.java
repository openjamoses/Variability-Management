/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.pullrequests;


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
public class Pullrequest_CommitsDetails {

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
        //String file_stats = "Repos.xlsx";
        String file_collect1 = "file_pr_shaa3.xlsx";
        String variant = "Variant-Statistics.xlsx";
        //String path_collect = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/";
        //String path_variant = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Repos/statistics/";
        //String path_new = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Repos/files_packages/00pr_files/00details/";
        //todo:
        String path_collect = "";
        String path_variant = "";
        String path_new = "";
        int ct = 0;

        String[] FILES = {variant};
        List<String> pVList = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 0, 0, 2);
        // List<String> nVList = Pick_GeneralNext.pick(path_variant + variant, 0, 9, 1);

        String[] files_collect = {file_collect1};
        ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "PR-Commits", "PR-Changes", "Shaa"};
        int tot_s = 0;
        for (int a = 0; a < files_collect.length; a++) {
            int sheet_index = 0;
            int total_sheet = File_Details.getWorksheets(path_collect + files_collect[a]);
            ///System.out.println("Reading Collection Excel....!");
            
            while (sheet_index < total_sheet) {
                if (sheet_index == 0) {
                    DataSets.add(datas);
                }

                String project = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "B2");

                String mlp_pullrequest = File_Details.setProjectName(path_collect + files_collect[a], sheet_index, "B2");
                List<String> fork_nameList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 2, 2);
                List<String> pullrequestList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 4, 1);
                List<Double> numList = ReadExcelFile_1Column.readColumnAsNumeric(path_collect + files_collect[a], sheet_index, 6, 1);
                List<String> dpullrequestList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], sheet_index, 3, 1);
                List<String> nameList = new ArrayList<>();
                nameList.add(project);
                for (int i = 0; i < fork_nameList.size(); i++) {
                    nameList.add(fork_nameList.get(i));
                }
                if (pVList.contains(project)) {
                    int index = pVList.indexOf(project);
                    JSONParser parser = new JSONParser();
                    List<String> mjList = new ArrayList<>();
                    for (int b = 1; b < nameList.size(); b++) {
                        String pPR = dpullrequestList.get(b);
                        String[] splits_ml = pPR.split(" \\| ");
                        double f_ml_sheet_index = 0;
                        for (int i = 0; i < 1; i++) {
                            String devString = splits_ml[i].substring(0, splits_ml[i].lastIndexOf("(") - 1);
                            //System.out.println(i + "\t" + nameList.get(b));
                            double mlp_commits = 0;
                            double mlp_changes = 0;
                            String shaaString = "";
                            String pr_num = splits_ml[i].substring(splits_ml[i].indexOf("(") + 1, splits_ml[i].indexOf(")"));
                            //System.out.println("   ||||  "+pPR);
                            String[] splits_pr = pr_num.split("-");
                            System.out.println("                 "+project+" \t"+nameList.get(b));
                            for (int j = 0; j < splits_pr.length; j++) {
                                if (ct == Constants.getToken().length) {
                                    ct = 0;
                                }
                                // System.out.println(j+" :::\t"+splits_pr[j]);
                                String tot_string = PullrequestCommits.count2(nameList.get(b), splits_pr[j], Constants.getToken(), ct);
                                mlp_commits += Double.parseDouble(tot_string.split("\\|")[0]);
                                mlp_changes += Double.parseDouble(tot_string.split("\\|")[1]);
                                System.out.println("    "+tot_string.split("\\|")[2]);
                                
                                String[] shaa = tot_string.split("\\|")[2].split("/");
                                for (int k = 0; k < shaa.length; k++) {
                                    shaaString = shaaString.concat(shaa[k] + "/");
                                }

                            }
                            datas = new Object[]{project, nameList.get(b), mlp_commits, mlp_changes, shaaString};
                            DataSets.add(datas);
                            tot_s++;
                        }
                    }

                    String file_name = variant.replaceAll("Variant-Statistics", "gp_prmerged_shaas");
                    Create_Excel.createExcelSheet(DataSets,path_new + file_name, "gp_pr_merged");
                }

                sheet_index++;
            }
        }
    }
}
