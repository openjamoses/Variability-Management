/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.reads.Commits;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.opm.variability.reads.PullrequestCommits;
import com.opm.variability.util.Constants;

/**
 *
 * @author openja moses
 * 
 */
public class IdentifyForks_UniqueCommits {
    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        indentify();
    }
    /**
     *
     * @throws Exception
     */
    private static void indentify() throws Exception {
        ////String toDay = "2017-07-06T00:00:00Z";
        Object[] datas = null;
        String fork1 = "repos6_forks1_500.xlsx";
        String fork2 = "repos6_forks500_1000.xlsx";
        String fork3 = "selected_fork_800-1200.xlsx";
        String fork4 = "selected_fork_1200-1600.xlsx";
        String fork5 = "selected_fork_1600-1805.xlsx";

        String path = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Desktop/DESKTOP/Files/shaa/";
        String[] FILES = {fork1};
        int ct = 0;
        for (int a = 0; a < FILES.length; a++) {
            int numbers = File_Details.getWorksheets(path + FILES[a]);
            int sheet_index = 0;
            int column_num1 = 1;
            int column_num4 = 4;
            int column_num5 = 5;
            int column_num6 = 6;
            int start_row_index = 2;

            while (sheet_index < numbers) {
                List<String> repos_nameList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, column_num1, start_row_index);
                List<String> createDateList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, column_num4, start_row_index);
                List<String> firstCommitList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, column_num5, start_row_index);
                List<String> lastCommitList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], sheet_index, column_num6, start_row_index);
                String mainline_name = File_Details.setProjectName(path + FILES[a], sheet_index, "A2");
                String sheet = File_Details.getWorksheetName(path + FILES[a], sheet_index);

                String min_date = DateOperations.sorts(createDateList, lastCommitList).split("/")[0];
                String max_date = DateOperations.sorts(firstCommitList, lastCommitList).split("/")[1];

                List<List<List<String>>> lists = new ArrayList<>();

                // System.out.println(count+" : "+min_date);
                List<List<String>> allList_1 = Commits.count(mainline_name, "mlp", min_date, max_date, Constants.getToken(), ct);
                List<String> shaList_1 = allList_1.get(0);
                List<String> dateList_1 = allList_1.get(1);
                List<String> messageList_1 = allList_1.get(2);

                System.out.println(sheet_index + " : " + shaList_1.size());
                String shaa_mlp = "";
                for (int i = 0; i < shaList_1.size(); i++) {
                    shaa_mlp = shaa_mlp.concat(shaList_1.get(i) + "/");
                }

                for (int i = 0; i < repos_nameList.size(); i++) {
                    List<List<String>> allList = Commits.count(repos_nameList.get(i), "fp", lastCommitList.get(i), lastCommitList.get(i), Constants.getToken(), ct);
                    lists.add(allList);
                    //System.out.println(allList.get(0).size() + "\t" + allList.get(0));
                    //System.out.println(allList.get(1).size() + "\t" + allList.get(1));
                    //System.out.println(allList.get(2).size() + "\t" + allList.get(2));
                }

                ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                datas = new Object[]{"N.s", "MLP", "FP", "COM", "UNIQUE", "VIP", "SCATTERED", "PERVASIVE", "Times","Shaas"
                };// end of assigning the header to the object..
                allobj.add(datas);
                datas = new Object[]{"", mainline_name, Double.parseDouble(repos_nameList.size() + ""), "", "", "", "", "","",shaa_mlp
                };
                allobj.add(datas);

                List< ArrayList< Object[]>> obj_list = new ArrayList<>();
                for (int b = 0; b < repos_nameList.size(); b++) {
                    List<List<String>> allList_3 = lists.get(b);
                    List<String> shaList_3 = allList_3.get(0);
                    List<String> dateList_3 = allList_3.get(1);
                    List<String> messageList_3 = allList_3.get(2);

                    double total_unique = 0, total_vip = 0, total_scattered = 0, total_pervasive = 0, num_times = 0,total_main = 0;
                    //List<String> cat_list = new ArrayList<>();
                    String sha_collections = "";
                    Set<Integer> sha_unique = new LinkedHashSet<Integer>();
                    for (int c = 0; c < shaList_3.size(); c++) {
                        int c_shas = 0;
                        int fp = 0, mlp = 0;

                        for (int i = 0; i < lists.size(); i++) {
                            if (i != b) {
                                List<List<String>> allList2 = lists.get(i);
                                //for (int y = 0; y < lists.get(i).size(); y++) {
                                List<String> shaList = allList2.get(0);
                                List<String> dateList = allList2.get(1);
                                List<String> messageList = allList2.get(2);
                                if (shaList.contains(shaList_3.get(c))) {
                                    c_shas++;
                                    sha_unique.add(i);
                                    fp++;
                                }
                            }
                        }
                        if (shaList_1.contains(shaList_3.get(c))) {
                            c_shas++;
                            mlp++;
                        }
                        String cat_ = "";
                        if (c_shas == 0) {
                            total_unique++;
                            cat_ = "Unique";
                        } else if (fp > 0 && mlp > 0 || mlp > 0 && fp == 0 ) {
                            total_vip++;
                            cat_ = "vip";
                        } else if (fp > 0 && mlp == 0) {
                            total_scattered++;
                            cat_ = "scattered";
                        } else if ((sha_unique.size() + 1) == lists.size() && mlp == 0) {
                            total_pervasive++;
                            cat_ = "pervasive";
                        }
                        //num_times += sha_unique.size();
                        sha_collections = sha_collections.concat(shaList_3.get(c) + ":" + cat_ + "/");
                    }
                    datas = new Object[]{Double.parseDouble(b + ""), "", repos_nameList.get(b), Double.parseDouble(shaList_3.size() + "") ,total_unique, total_vip, total_scattered, total_pervasive,Double.parseDouble(sha_unique.size() + ""),sha_collections};
                    allobj.add(datas);
                }
                String f_name = FILES[a].replaceAll("selected_fork_", "fork_shas_2_");
                Create_Excel.createExcelSheet(allobj,path_new + f_name, sheet);
                sheet_index++;
            }
        }
    }
}
