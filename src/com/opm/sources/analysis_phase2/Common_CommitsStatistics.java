/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.Commits;
import com.opm.variability.util.Constants;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

/**
 *
 * @author john
 */
public class Common_CommitsStatistics {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        appDetails();
    }

    private static void appDetails() throws Exception {

        DecimalFormat newFormat = new DecimalFormat("#.##");

        Object[] datas = null;
        int ct = 0;

        String file_google1 = "merged_com_merged_final_cd.xlsx";

        String[] fork_package = {file_google1};

        //todo:
        //String path_package = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests/";
        //String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests2/";
        String path_package = "";
        String path_new = "";

        //todo:
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;

        int s_count = 88;
        for (int aa = 0; aa < fork_package.length; aa++) {
            try {   //first connection with GET request

                int numbers = File_Details.getWorksheets(path_package + fork_package[aa]);
                int count = 99;

                while (count < numbers) {
                    ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                    datas = new Object[]{"N.S", "Project", "CommitsB", "CommitsF", "Unique", "ChangeB", "ChangeF", "ChangeU", "", "ComF_MLP", "ChangeF_MLP"};
                    allobj.add(datas);
                    List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 1, 2);
                    List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 2, 2);
                    List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 3, 2);
                    String project = File_Details.setProjectName(path_package + fork_package[aa], count, "B2");

                    List<String> conList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 4, 2);

                    List<String> createdList = new ArrayList<>();
                    int flag = 0;
                    for (int i = 0; i < nameList.size(); i++) {
                        flag += unique.get(i);
                        JSONParser parser = new JSONParser();
                        if (ct == (Constants.getToken().length)) {/// the the index for the tokens array...
                            ct = 0; //// go back to the first index......
                        }
                        String forks_url = Call_URL.callURL("https://api.github.com/repos/" + nameList.get(i) + "?access_token=" + Constants.getToken()[ct++]);

                        if (JSONUtils.isValidJSONObject(forks_url) == false) {///

                            System.out.println(" :Invalid fork found!");
                            break;
                        }
                        JSONObject jSONObject = (JSONObject) parser.parse(forks_url);
                        String created_at = (String) jSONObject.get("created_at");
                        createdList.add(created_at);
                    }

                    String maxCreated = DateOperations.sorts(createdList, createdList).split("/")[1];
                    if (flag > 0) {
                        s_count++;
                        List<List<List<String>>> lists = new ArrayList<>();
                        // System.out.println(count+" : "+min_date);

                        List<List<String>> allList_1 = Commits.countDetails2(project, "mlp", "", Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                        List<String> shaList_1 = allList_1.get(0);
                        List<String> emailList_1 = allList_1.get(1);
                        List<String> loginList_1 = allList_1.get(2);
                        List<String> detailList_1 = allList_1.get(3);
                        List<String> dateList_1 = allList_1.get(4);
                        //System.out.println("MLP:  " + project + "\t" + shaList_1.size());
                        double changes1 = 0;
                        for (int i = 0; i < shaList_1.size(); i++) {
                            if (!detailList_1.get(i).equals("")) {

                                try {
                                    changes1 += Double.parseDouble(detailList_1.get(i).split("/")[detailList_1.get(i).split("/").length - 4]);

                                } catch (Exception e) {
                                    changes1 += 1;
                                }
                            }
                        }
                        datas = new Object[]{"", project, "", "", "", "", "", ""};
                        allobj.add(datas);
                        //System.out.println(createdList.get(0) + "\t" + dateList_1.get(dateList_1.size() - 1));
                        for (int i = 0; i < nameList.size(); i++) {
                            List<List<String>> allList_2 = Commits.countDetails2(nameList.get(i), "fp", createdList.get(i), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                            List<String> shaList_2 = allList_2.get(0);
                            List<String> emailList_2 = allList_2.get(1);
                            List<String> loginList_2 = allList_2.get(2);
                            List<String> detailList_2 = allList_2.get(3);
                            List<String> dateList_2 = allList_2.get(4);

                            double com = 0;
                            double changesB = 0;
                            double changesF = 0;
                            double changesU = 0;
                            for (int j = 0; j < shaList_1.size(); j++) {
                                if (DateOperations.compareDates(dateList_1.get(j), createdList.get(i)) == true) {
                                    com++;
                                    if (!detailList_1.get(j).equals("")) {
                                        try {
                                            changesB += Double.parseDouble(detailList_1.get(j).split("/")[detailList_1.get(i).split("/").length - 4]);
                                        } catch (Exception e) {
                                            changesB += 1;
                                        }
                                    }
                                }
                            }

                            for (int j = 0; j < shaList_2.size(); j++) {
                                if (!detailList_2.get(j).equals("")) {
                                    try {
                                        changesF += Double.parseDouble(detailList_2.get(j).split("/")[detailList_2.get(j).split("/").length - 4]);
                                    } catch (Exception e) {
                                        //e.printStackTrace();
                                        System.out.println("Inside the catch ::::" + detailList_2.get(j).split("/")[4]);
                                        changesF += 1;
                                    }
                                }
                            }

                            //for (int j = 0; j < conList.size(); j++) {
                            String[] splits = conList.get(i).split("/");
                            for (int k = 0; k < splits.length; k++) {
                                if (splits[k].contains(":")) {
                                    changesU += Double.parseDouble(splits[k].substring(splits[k].lastIndexOf(":") + 1, splits[k].length()));
                                }
                            }
                            //}

                            double comF = Double.parseDouble(shaList_1.size() + "") - com;
                            double chagF = changes1 - changesB;
                            System.out.println(nameList.get(i) + " :  " + com + "\t" + commits.get(i) + "\t" + unique.get(i) + "\t" + changesB + "\t" + changesF + "\t" + changesU);
                            datas = new Object[]{Double.parseDouble((i + 1) + ""), nameList.get(i), com, commits.get(i) - unique.get(i), unique.get(i), changesB, changesF - changesU, changesU, "", comF, chagF};
                            allobj.add(datas);
                        }

                        String f_name = fork_package[aa].replaceAll("merged_com_merged_final", "fp_com_chang");
                        Create_Excel.createExcelSheet(allobj, path_new + f_name, project.split("/")[0] + "_" + s_count);
                    }
                    //Create_Excel.createExcel2(allobj, 0, path_new + f_name, "google_play");
                    count++;
                }
            } catch (Exception ex) {
                // some exception handling here
                ex.printStackTrace();
            }
        }
    }
}
