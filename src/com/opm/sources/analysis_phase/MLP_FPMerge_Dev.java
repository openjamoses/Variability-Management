/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase;

import com.opm.variability.core.Coversions;
import com.opm.variability.core.File_Details;
import com.opm.variability.core.MathsFunctions;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class MLP_FPMerge_Dev {

    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        DecimalFormat newFormat = new DecimalFormat("#.##");
        Object[] datas = null;
        //String file_stats = "Repos.xlsx";
        String file1 = "gp_mlp_fp_dev_final.xlsx";

        String path_google = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        //String path_stats = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        //todo:
        String[] files_google = {file1};

        for (int a = 0; a < files_google.length; a++) {
            int sheet_index = 0;
            int total_sheets = File_Details.getWorksheets(path_google + files_google[a]);
            //System.out.println("Reading Collection Excel....!");
            while (sheet_index < total_sheets) {
                ArrayList< Object[]> DataSets = new ArrayList<Object[]>();
                datas = new Object[]{"Project", "forks", "Dev_Com", "Dev_Perc", "Dev_Contrib", "Major", "Minor", "Tot_Dev", "Tot_Com"};
                DataSets.add(datas);

                String project = File_Details.setProjectName(path_google + files_google[a], sheet_index, "A2");
                List<String> name = ReadExcelFile_1Column.readColumnAsString(path_google + files_google[a], sheet_index, 1, 2);
                List<String> developerList = ReadExcelFile_1Column.readColumnAsString(path_google + files_google[a], sheet_index, 2, 1);
                List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_google + files_google[a], sheet_index, 3, 1);
                List<String> nL = new ArrayList<>();
                List<String> fork_nameList = new ArrayList<>();
                nL.add(project);
                fork_nameList.add(project);
                for (int i = 0; i < name.size(); i++) {
                    nL.add(name.get(i));
                    fork_nameList.add(name.get(i));
                }
                List<List<String>> lists_all = new ArrayList<>();
                for (int i = 0; i < developerList.size(); i++) {
                    String[] splits = developerList.get(i).split("/");
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < splits.length; j++) {
                        list.add(splits[j]);

                    }
                    lists_all.add(list);
                }
                String name_i = "", name_j = "", email_prefix_i = "", email_prefix_j = "", login_i = "", login_j = "", location_i = "", location_j = "", created_at_i = "", created_at_j = "", date_i = "", date_j = "";
                String contrib_i = "", contrib_j = "";

                List<String> contList = new ArrayList<>();

                for (int i = 0; i < lists_all.size(); i++) {
                    //System.out.println(i+"\t"+contribution.get(i));
                    // contrib_i = contribution.get(i);
                    //String[] split_i = contribution.get(i).split("/");
                    date_i = developerList.get(i);
                    List<String> lists_i = lists_all.get(i);
                    //List<String> lists2_i = lists_all2.get(i);
                    // name_i = 
                    for (int x = 0; x < lists_i.size(); x++) {
                        name_i = lists_i.get(x).split("\\|")[0];
                        //System.out.println(split_i[x]+"\t"+split_i.length);
                        if (lists_i.get(x).split("\\|").length > 1) {

                            if (lists_i.get(x).split("\\|")[1].contains("@")) {
                                email_prefix_i = lists_i.get(x).split("\\|")[1].substring(0, lists_i.get(x).split("\\|")[1].indexOf("@"));
                            }

                            login_i = lists_i.get(x).split("\\|")[2].split(":")[0];
                        } else {
                            email_prefix_i = "em##";
                            login_i = "###";
                        }
                        for (int j = 0; j < lists_all.size(); j++) {
                            //contrib_j = contribution.get(j);
                            List<String> lists_j = lists_all.get(j);
                            //List<String> lists2_j = lists_all2.get(j);
                            //String[] split_j = contribution.get(j).split("/");
                            date_j = developerList.get(j);
                            for (int y = 0; y < lists_j.size(); y++) {
                                name_j = lists_j.get(y).split("\\|")[0];
                                if (lists_j.get(y).split("\\|").length > 1) {

                                    //System.out.println(lists_j.size()+"\t"+y);
                                    //System.out.println(name_i+"\t"+email_prefix_i+"\t"+login_i);
                                    ///System.out.println(name_j+"\t"+email_prefix_j+"\t"+login_j);
                                    if (lists_j.get(y).split("\\|")[1].contains("@")) {
                                        email_prefix_j = lists_j.get(y).split("\\|")[1].substring(0, lists_j.get(y).split("\\|")[1].indexOf("@"));
                                    }
                                    login_j = lists_j.get(y).split("\\|")[2].split(":")[0];

                                } else {
                                    email_prefix_j = "em2###";
                                    login_j = "log2###";
                                }
                                //todo::::: .........
                                String str1 = "", commits1 = "";
                                String str2 = "", commits2 = "";

                                String str_1 = "", commits_1 = "";
                                //String str_2 = "", commits_2 = "";

                                String str3 = "", commits3 = "";

                                if (email_prefix_i.equals(email_prefix_j) && !login_i.equals("login######") && login_j.equals("login######")) {// || name_i.equals(name_j) || email_prefix_i.equals(email_prefix_j)){
                                    if (x < lists_i.size() && y < lists_j.size()) {
                                        str1 = lists_i.get(x).substring(0, lists_i.get(x).lastIndexOf(":"));
                                        str2 = lists_j.get(y).substring(0, lists_j.get(y).lastIndexOf(":"));

                                        if (str1.length() > 5 && str2.length() > 5) {
                                            String com_i = lists_i.get(x).substring(lists_i.get(x).lastIndexOf(":") + 1, lists_i.get(x).length());
                                            String com_j = lists_j.get(y).substring(lists_j.get(y).lastIndexOf(":") + 1, lists_j.get(y).length());

                                            if (date_i.equals(date_j)) {
                                                double com = 0;
                                                double com2 = 0;
                                                if (Coversions.isDouble(com_i) && Coversions.isDouble(com_j)) {
                                                    com = Double.parseDouble(com_i) + Double.parseDouble(com_j);
                                                }

                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str1 + ":" + com);
                                                    lists_i.remove(lists_i.indexOf(lists_j.get(y)));
                                                    lists_all.set(i, lists_i);
                                                }
                                                //System.out.println("1 : " + contribution.get(i) + "\t" + split_i[x] + "\t" + str1 + ":" + com);
                                                //System.out.println("1... : " + contribution.get(i) + "\t" + split_j[y]);

                                            } else {
                                                //contrib_j = contrib_j.replaceAll(split_j[y], str1 + com_j);
                                                //System.out.println(lists_j+"\t"+split_j[y]);

                                                int index = lists_j.indexOf(lists_j.get(y));
                                                if (index != -1) {
                                                    lists_j.set(index, str1 + ":" + com_j);
                                                    lists_all.set(j, lists_j);
                                                }

                                                //System.err.println("11:  " + contribution.get(i) + "\t" + split_i[x]);
                                            }
                                        }
                                    }
                                    //System.out.println("1: "+login_i+" , "+login_j);
                                } else if (email_prefix_i.equals(email_prefix_j) && !login_j.equals("login######") && login_i.equals("login######")) {
                                    if (x < lists_i.size() && y < lists_j.size()) {
                                        str1 = lists_i.get(x).substring(0, lists_i.get(x).lastIndexOf(":"));
                                        str2 = lists_j.get(y).substring(0, lists_j.get(y).lastIndexOf(":"));
                                        if (str1.length() > 5 && str2.length() > 5) {
                                            String com_i = lists_i.get(x).substring(lists_i.get(x).lastIndexOf(":") + 1, lists_i.get(x).length());
                                            String com_j = lists_j.get(y).substring(lists_j.get(y).lastIndexOf(":") + 1, lists_j.get(y).length());

                                            if (date_i.equals(date_j)) {
                                                double com = 0;
                                                double com2 = 0;
                                                if (Coversions.isDouble(com_i) && Coversions.isDouble(com_j)) {
                                                    com = Double.parseDouble(com_i) + Double.parseDouble(com_j);
                                                }

                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str2 + ":" + com);
                                                    lists_i.remove(lists_i.indexOf(lists_j.get(y)));
                                                    lists_all.set(i, lists_i);
                                                }

                                                //System.err.println("2 : " + contribution.get(i) + "\t" + split_i[x] + "\t" + com);
                                            } else {
                                                //contrib_i = contrib_i.replaceAll(split_i[x], str2 + ":" + com_i);

                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str2 + ":" + com_i);
                                                    lists_all.set(j, lists_i);
                                                }
                                                //System.err.println("21 : " + contribution.get(i) + "\t" + split_i[x]);
                                            }
                                        }
                                    }
                                    //System.out.println("4: "+login_i+" , "+login_j);

                                } else if (name_i.equals(name_j) && !login_i.equals("login######") && login_j.equals("login######")) {
                                    if (x < lists_i.size() && y < lists_j.size()) {
                                        str1 = lists_i.get(x).substring(0, lists_i.get(x).lastIndexOf(":"));
                                        str2 = lists_j.get(y).substring(0, lists_j.get(y).lastIndexOf(":"));

                                        String com_i = lists_i.get(x).substring(lists_i.get(x).lastIndexOf(":") + 1, lists_i.get(x).length());
                                        String com_j = lists_j.get(y).substring(lists_j.get(y).lastIndexOf(":") + 1, lists_j.get(y).length());

                                        if (str1.length() > 5 && str2.length() > 5) {
                                            if (date_i.equals(date_j)) {
                                                double com = 0;
                                                double com2 = 0;
                                                if (Coversions.isDouble(com_i) && Coversions.isDouble(com_j)) {
                                                    com = Double.parseDouble(com_i) + Double.parseDouble(com_j);
                                                }

                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str1 + ":" + com);
                                                    lists_i.remove(lists_i.indexOf(lists_j.get(y)));
                                                    lists_all.set(i, lists_i);
                                                }
                                            } else {

                                                //contrib_j = contrib_j.replaceAll(split_j[y], str1 + ":" + com_j);
                                                int index = lists_j.indexOf(lists_j.get(y));
                                                if (index != -1) {
                                                    lists_j.set(index, str1 + ":" + com_j);
                                                    lists_all.set(j, lists_j);
                                                }
                                                /// System.err.println("31 : " + contribution.get(j) + "\t" + split_j[y]);
                                            }
                                        }
                                    }
                                } else if (name_i.equals(name_j) && !login_j.equals("login######") && login_i.equals("login######")) {
                                    if (x < lists_i.size() && y < lists_j.size()) {
                                        str1 = lists_i.get(x).substring(0, lists_i.get(x).lastIndexOf(":"));
                                        str2 = lists_j.get(y).substring(0, lists_j.get(y).lastIndexOf(":"));

                                        if (str1.length() > 5 && str2.length() > 5) {
                                            String com_i = lists_i.get(x).substring(lists_i.get(x).lastIndexOf(":") + 1, lists_i.get(x).length());
                                            String com_j = lists_j.get(y).substring(lists_j.get(y).lastIndexOf(":") + 1, lists_j.get(y).length());
                                            if (date_i.equals(date_j)) {
                                                double com = 0;
                                                double com2 = 0;
                                                if (Coversions.isDouble(com_i) && Coversions.isDouble(com_j)) {
                                                    com = Double.parseDouble(com_i) + Double.parseDouble(com_j);
                                                }

                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str2 + ":" + com);
                                                    lists_i.remove(lists_i.indexOf(lists_j.get(y)));
                                                    lists_all.set(i, lists_i);
                                                }
                                                //System.err.println("4 : " + contribution.get(i) + "\t" + split_i[x] + "\t" + com);
                                            } else {
                                                int index = lists_i.indexOf(lists_i.get(x));
                                                if (index != -1) {
                                                    lists_i.set(index, str2 + ":" + com_i);
                                                    lists_all.set(j, lists_i);
                                                }
                                            }
                                        }
                                    }
                                    //		
                                } else if (email_prefix_i.equals(email_prefix_j) && login_i.equals(login_j) && x != y) {
                                    if (x < lists_i.size() && y < lists_j.size()) {
//System.out.println(lists_i.size() + " \t" + x);
                                        if (lists_i.size() < x && lists_j.size() < y) {
                                            str1 = lists_i.get(x).substring(0, lists_i.get(x).lastIndexOf(":"));
                                            str2 = lists_j.get(y).substring(0, lists_j.get(y).lastIndexOf(":"));
                                            if (str1.length() > 5 && str2.length() > 5) {
                                                String com_i = lists_i.get(x).substring(lists_i.get(x).lastIndexOf(":") + 1, lists_i.get(x).length());
                                                String com_j = lists_j.get(y).substring(lists_j.get(y).lastIndexOf(":") + 1, lists_j.get(y).length());

                                                if (date_i.equals(date_j)) {
                                                    double com = 0;
                                                    double com2 = 0;
                                                    if (Coversions.isDouble(com_i) && Coversions.isDouble(com_j)) {
                                                        com = Double.parseDouble(com_i) + Double.parseDouble(com_j);
                                                    }

                                                    int index = lists_i.indexOf(lists_i.get(x));
                                                    if (index != -1) {
                                                        lists_i.set(index, str1 + ":" + com);
                                                        lists_i.remove(lists_i.indexOf(lists_j.get(y)));
                                                        lists_all.set(i, lists_i);

                                                    }

                                                } else {
                                                    int index = lists_j.indexOf(lists_j.get(y));
                                                    if (index != -1) {
                                                        lists_j.set(index, str1 + ":" + com_j);
                                                        lists_all.set(j, lists_j);
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }

                            }
                        }
                    }
                }

                List<String> rList1 = new ArrayList<>();
                List<String> lists1 = lists_all.get(0);
                String combined1 = "";
                List<Double> percL = new ArrayList<>();
                double tot = 0;
                List<Double> tot_list = new ArrayList<>();
                for (int j = 0; j < lists1.size(); j++) {
                    combined1 = combined1.concat(lists1.get(j) + "/");
                    if (lists1.get(j).contains(":")) {
                        tot_list.add(Double.parseDouble(lists1.get(j).substring(lists1.get(j).lastIndexOf(":") + 1, lists1.get(j).length())));
                        tot += Double.parseDouble(lists1.get(j).substring(lists1.get(j).lastIndexOf(":") + 1, lists1.get(j).length()));

                    } else {
                        rList1.add(lists1.get(j));
                    }
                }
                lists1.removeAll(rList1);

                for (int i = 0; i < tot_list.size(); i++) {
                    double perc0 = (tot_list.get(i) / tot) * 100;
                    percL.add(Double.valueOf(newFormat.format(perc0)));
                }
                String login_mlp = "", log_percentage_1 = "", log_category_1 = "";
                MathsFunctions.InsertionSort(lists1, percL, tot_list);
                int max1 = 0;
                double c_mj1 = 0, c_mn1 = 0;
                if (tot_list.size() > 0) {
                    String cat;
                    for (int i = 0; i < tot_list.size(); i++) {
                        if (max1 <= 80) {
                            cat = "Major";
                            c_mj1++;
                        } else {
                            c_mn1++;
                            cat = "Minor";

                        }

                        if (lists1.get(i).contains(":")) {
                            // System.out.println(i+":"+log_list_1.get(i)+"\t"+max1+"\t"+tot_list_1.get(i)+"\t"+cat);
                            login_mlp = login_mlp.concat(lists1.get(i).substring(0, lists1.get(i).lastIndexOf(":")) + ":" + tot_list.get(i) + "/");
                            log_percentage_1 = log_percentage_1.concat(lists1.get(i).substring(0, lists1.get(i).lastIndexOf(":")) + ":" + percL.get(i) + "/");
                            log_category_1 = log_category_1.concat(lists1.get(i).substring(0, lists1.get(i).lastIndexOf(":")) + ":" + cat + "/");

                        }
                        max1 += percL.get(i);
                    }
                }

                datas = new Object[]{project, "", login_mlp, log_percentage_1, log_category_1, c_mj1, c_mn1, c_mj1 + c_mn1, commits.get(0)};
                DataSets.add(datas);
                //lists_all.remove(0);

                for (int i1 = 1; i1 < lists_all.size(); i1++) {
                    List<String> lists = lists_all.get(i1);
                    String combined = "";
                    List<Double> percL_2 = new ArrayList<>();
                    List<Double> tot_list_2 = new ArrayList<>();
                    List<String> rList2 = new ArrayList<>();
                    double tot2 = 0;

                    for (int j = 0; j < lists.size(); j++) {
                        combined = combined.concat(lists.get(j) + "/");
                        //System.err.println(lists.get(j).substring(lists.get(j).lastIndexOf(":") + 1, lists.get(j).length()));
                        if (lists.get(j).contains(":")) {
                            tot_list_2.add(Double.parseDouble(lists.get(j).substring(lists.get(j).lastIndexOf(":") + 1, lists.get(j).length())));
                            tot2 += Double.parseDouble(lists.get(j).substring(lists.get(j).lastIndexOf(":") + 1, lists.get(j).length()));

                        } else {
                            rList2.add(lists.get(j));
                        }
                    }
                    lists.removeAll(rList2);

                    for (int i = 0; i < tot_list_2.size(); i++) {
                        double perc0 = (tot_list_2.get(i) / tot2) * 100;
                        percL_2.add(Double.valueOf(newFormat.format(perc0)));
                    }
                    MathsFunctions.InsertionSort(lists, percL_2, tot_list_2);
                    int max2 = 0;
                    double c_mj2 = 0, c_mn2 = 0;
                    String email_collections = "", log_percentage_2 = "", log_category_2 = "";
                    if (lists.size() > 0) {
                        String cat;
                        for (int i = 0; i < tot_list_2.size(); i++) {
                            if (max2 <= 80) {
                                cat = "Major";
                                c_mj2++;
                            } else {
                                c_mn2++;
                                cat = "Minor";
                            }
                            if (lists.get(i).contains(":")) {
                                email_collections = email_collections.concat(lists.get(i).substring(0, lists.get(i).lastIndexOf(":")) + ":" + tot_list_2.get(i) + "/");
                                log_percentage_2 = log_percentage_2.concat(lists.get(i).substring(0, lists.get(i).lastIndexOf(":")) + ":" + percL_2.get(i) + "/");
                                log_category_2 = log_category_2.concat(lists.get(i).substring(0, lists.get(i).lastIndexOf(":")) + ":" + cat + "/");

                            }

                            max2 += percL_2.get(i);
                        }

                    }

                    double comm = 0;
                    if (nL.contains(fork_nameList.get(i1))) {
                        int index = nL.indexOf(fork_nameList.get(i1));
                        comm = commits.get(index);
                    }
                    datas = new Object[]{"", fork_nameList.get(i1), email_collections, log_percentage_2, log_category_2, c_mj2, c_mn2, c_mj2 + c_mn2, comm};
                    DataSets.add(datas);

                }

                String file_name = files_google[a].replaceAll("dev_final", "merged_final");
                Create_Excel.createExcelSheet(DataSets,path_new + file_name, project.split("/")[0] + "_" + sheet_index);

                sheet_index++;
            }
        }

    }
}
