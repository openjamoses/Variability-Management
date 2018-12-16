/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.pullrequests;


import com.opm.variability.core.Call_URL;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class PR_Details_111 {

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
        String[] tokens = Constants.getToken();
        int ct = 0;
        //String file_stats = "Repos.xlsx";
        String file_collect1 = "file_pr_closed-shaa.xlsx";
        String merged1 = "merged_com_merged_final_cd.xlsx";
        String path_collect = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00pr_files/";
        String path_merged = "/Users/john/Desktop/00commits/fmerged/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00pr_files/00details/";
        //todo:
        //String path_collect = "";
        //String path_merged = "";
        //String path_new = "";

        String[] FILES = {merged1};

        List<String> proj_list = new ArrayList<>();
        List<String> chPlist = new ArrayList<>();
        List<List<String>> chFlist = new ArrayList<>();

        for (int a = 0; a < FILES.length; a++) {
            int count = 0;
            int numbers = File_Details.getWorksheets(path_merged + FILES[a]);
            //System.out.println("Reading Collection Excel....!");
            while (count < numbers) {

                String project = File_Details.setProjectName(path_merged + FILES[a], count, "B2");
                String pChange = File_Details.setProjectName(path_merged + FILES[a], count, "G2");
                List<String> nList = ReadExcelFile_1Column.readColumnAsString(path_merged + FILES[a], count, 6, 2);

                proj_list.add(project);
                chPlist.add(pChange);
                chFlist.add(nList);

                System.out.println(count + " : " + project);

                count++;
            }
        }
        String[] files_collect = {file_collect1};
        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Tot_Dev", "MLP_Dev_MJ", "MLP_Dev_MN", "FP_Dev_MJ", "FP_Dev_MN"};
        for (int a = 0; a < files_collect.length; a++) {
            int count = 0;
            int numbers = File_Details.getWorksheets(path_collect + files_collect[a]);
            //System.out.println("Reading Collection Excel....!");
            while (count < numbers) {
                if (count == 0) {
                    allobj.add(datas);
                }
                String project = File_Details.setProjectName(path_collect + files_collect[a], count, "B2");
                String prList = File_Details.setProjectName(path_collect + files_collect[a], count, "D2");
                String dprList = File_Details.setProjectName(path_collect + files_collect[a], count, "E2");
                List<String> nList = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 2, 2);
                // List<String> prList = Pick_GeneralNext.pick(path_collect + files_collect[a], count, 4, 1);
                List<Double> numList = ReadExcelFile_1Column.readColumnAsNumeric(path_collect + files_collect[a], count, 6, 1);
                double total_pr = 0;
                List<String> devlist = new ArrayList<>();
                List<String> catPlist = new ArrayList<>();

                List<String> devFlist = new ArrayList<>();
                List<String> catFlist = new ArrayList<>();

                String pChange = "";
                if (proj_list.contains(project)) {
                    int index = proj_list.indexOf(project);
                    pChange = chPlist.get(index);
                    List<String> clist = chFlist.get(index);
                    String[] splits = pChange.split("/");
                    for (int i = 0; i < splits.length; i++) {
                        String lastL = splits[i].split("\\|")[splits[i].split("\\|").length - 1];
                        if (lastL.contains(":")) {
                            devlist.add(lastL.split(":")[0]);
                            catPlist.add(lastL.split(":")[1]);

                        }
                    }
                    for (int i = 0; i < clist.size(); i++) {
                        String[] splitsF = clist.get(i).split("/");
                        for (int j = 0; j < splitsF.length; j++) {
                            String lastL = splitsF[j].split("\\|")[splitsF[j].split("\\|").length - 1];
                            if (lastL.contains(":")) {
                                devFlist.add(lastL.split(":")[0]);
                                catFlist.add(lastL.split(":")[1]);

                                //System.out.println("     "+lastL.substring(0,lastL.lastIndexOf(":")));
                                //devlist.add(lastL.substring(0, lastL.lastIndexOf(":")));
                            }
                        }
                    }
                }
                //System.out.println(count + " : " + project);
                double mlp_mj = 0;
                double mlp_mn = 0;
                double fks_mj = 0;
                double fks_mn = 0;
                Set<String> devSet3 = new LinkedHashSet<>();
                JSONParser parser = new JSONParser();
                String[] splits_ml = prList.split(" \\| ");

                System.out.println(count + "  :  " + project + "\t" + numList.get(0));
                double f_ml_count = 0;
                for (int i = 0; i < splits_ml.length; i++) {
                    
                    System.out.println("               "+i+" : "+splits_ml[i]);
                    String devString = splits_ml[i].split("-")[0];
                    String pr_num = splits_ml[i].substring(splits_ml[i].indexOf("(") + 1, splits_ml[i].indexOf(")"));
                    String[] splits_pr = pr_num.split("-");
                    for (int j = 0; j < splits_pr.length; j++) {
                        if (ct == tokens.length) {
                            ct = 0;
                        }
                        String jsonString = Call_URL.callURL("https://api.github.com/repos/" + project + "/pulls/" + splits_pr[j] + "?access_token=" + tokens[ct++]);
                        //System.out.println("https://api.github.com/repos/" + project + "/pulls/" + splits_pr[j]);

                        if (JSONUtils.isValidJSONObject(jsonString) == true) {
                            JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
                            JSONObject userOBJ = (JSONObject) jSONObject.get("user");

                            if ((String) userOBJ.get("login") != null) {
                                String login = (String) userOBJ.get("login");
                                //System.out.println(devlist+"\t"+login);
                                //System.out.println(devFlist+"\t"+login);

                                if (devlist.contains(login) || devFlist.contains(login)) {
                                    total_pr = numList.get(0);
                                }
                                if (devlist.contains(login)) {
                                    //String tot_string = Commits2.count(nameList.get(b), splits_pr[j], tokens, ct);
                                    int index2 = devlist.indexOf(login);
                                    String cat = catPlist.get(index2);
                                    if (cat.equals("Major")) {
                                        mlp_mj++;
                                        //mlp_mj_Set.add(login);
                                    } else {
                                        mlp_mn++;
                                        //mlp_mn_Set.add(login);
                                    }
                                    // devSet1.add(login);
                                    devSet3.add(login);

                                    // mlp_commits += Double.parseDouble(tot_string.split("/")[0]);
                                    // mlp_changes += Double.parseDouble(tot_string.split("/")[1]);
                                }

                                if (devFlist.contains(login)) {
                                    //String tot_string = Commits2.count(nameList.get(b), splits_pr[j], tokens, ct);
                                    int index2 = devFlist.indexOf(login);
                                    String cat = catFlist.get(index2);
                                    if (cat.equals("Major")) {
                                        fks_mj++;
                                        //fks_mj_Set.add(login);
                                    } else {
                                        fks_mn++;
                                        //fks_mn_Set.add(login);
                                    }

                                    devSet3.add(login);
                                    // mlp_commits += Double.parseDouble(tot_string.split("/")[0]);
                                    // mlp_changes += Double.parseDouble(tot_string.split("/")[1]);

                                }

                            }
                        } else {
                            System.out.println("Invalid fork::: \t" + jsonString);
                        }
                    }
                    //System.out.println(i + " \t" + pr_num);
                }
                //}

                datas = new Object[]{project, total_pr, mlp_mj, mlp_mn, fks_mj, fks_mn};
                allobj.add(datas);
                String file_name = files_collect[a].replaceAll("file_pr_closed-shaa", "pr_closed_final3");
                Create_Excel.createExcelSheet(allobj,path_new + file_name, "variants_pr");
                count++;
            }
        }
    }
}
