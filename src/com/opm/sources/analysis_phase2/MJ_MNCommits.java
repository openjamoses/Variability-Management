/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;


import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.PR_IS_Details;
import com.opm.variability.reads.ReadCommits;
import com.opm.variability.reads.Shaa_Details;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author john
 */
public class MJ_MNCommits {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        collect();
    }
    
    private static void collect() throws Exception {
        Object[] datas = null;
        int date_interval = 14;
        double interval = 14.0;
        String fork1 = "repos_gp_combshaa11.xlsx";
        String fork2 = "";
        String fork3 = "";
        String fork4 = "";
        String fork5 = "";
        
        String path = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/00commits/";
        String[] FILES = {fork1};
        int ct = 0;
        for (int a = 0; a < FILES.length; a++) {
            int numbers = File_Details.getWorksheets(path + FILES[a]);
            int count = 0;
            while (count < numbers) {
                long stopTime = 0;
                long elapsedTime = 0;
                long startTime1 = System.currentTimeMillis();
                
                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 2, 2);
                List<String> createList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 3, 2);
                List<String> firstList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 4, 2);
                List<String> lastList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 5, 2);
                List<String> shaaList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 9, 2);
                List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], count, 6, 1);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], count, 7, 1);
                
                String project = File_Details.setProjectName(path + FILES[a], count, "B2");
                String pCreate = File_Details.setProjectName(path + FILES[a], count, "D2");
                String sheet = File_Details.getWorksheetName(path + FILES[a], count);
                
                List<String> sList = new ArrayList<>();
                for (int i = 0; i < shaaList.size(); i++) {
                    String[] splits = shaaList.get(i).split("/");
                    for (int j = 0; j < splits.length; j++) {
                        sList.add(splits[j].split(":")[0]);
                    }
                }
                
                System.out.println("ML: " + count + " \t" + project + "");
                
                XSSFSheet[] workSheet = new XSSFSheet[numbers];
                String projSheet = project.split("/")[0] + "_" + count;

                ct = new ReadCommits().readCommitsNow(project, pCreate, Constants.getToken(), projSheet, ct, workSheet, Constants.cons.TODAY_DATE, FILES[a], path_new, "");
                stopTime = System.currentTimeMillis();
                elapsedTime = stopTime - startTime1;
                System.out.println("      ElapsedTime in minutes = " + elapsedTime / (1000 * 60));
                
                for (int i = 0; i < nameList.size(); i++) {
                    System.out.println("    " + i + " \t" + nameList.get(i));
                    stopTime = 0;
                    elapsedTime = 0;
                    startTime1 = System.currentTimeMillis();
                    
                    ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                    JSONParser parser = new JSONParser();
                    datas = new Object[]{"Tag Date", "PR Open",
                        "PR Closed", "Is_Open", "Is_Closed", "Forks", "", "Project",
                        "Name/email/login/Location/Created_at/Updated_at/Public_repos/Public_gists/Followers/Following/Commits_Changed_Added_Deleted"
                
                    };
                    allobj.add(datas);
                    
                    List<String> detailsList = new ArrayList<>();
                    if (unique.get(i) > 0) {
                        String subSheet = projSheet + "_FP_" + i;
                        String[] split_shaa = shaaList.get(i).split("/");
                        for (int j = 0; j < split_shaa.length; j++) {
                            String shaa_details = Shaa_Details.details(nameList.get(i), split_shaa[j].split(":")[0], Constants.getToken(), ct);
                            ct = Integer.parseInt(shaa_details.split("/")[shaa_details.split("/").length-1]);
                            if (!shaa_details.equals("")) {
                                detailsList.add(shaa_details);
                            }
                        }
                        String fDate = "", lDate = "";
                        //System.out.println(" SIZE: "+detailsList.size());
                        if (detailsList.size() > 0) {
                            fDate = detailsList.get(detailsList.size() - 1).split("/")[2];
                            lDate = detailsList.get(0).split("/")[2];
                            
                            int diff = Integer.parseInt(DateOperations.diff(fDate, lDate).split("/")[0]);
                            List<String> nextDate = new ArrayList<>();
                            
                            if (diff <= 14) {
                                lDate = DateOperations.addDates(fDate, date_interval);
                                nextDate.add(lDate);
                            } else {
                                int new_diff = (int) Math.ceil(diff / interval);
                                for (int j = 0; j < new_diff; j++) {
                                    nextDate.add(DateOperations.addDates(fDate, date_interval * (j + 1)));
                                }
                            }
                            
                            int i2 = 1, i1 = 0, next = 0;
                            
                            List<List<String>> all_list = new ArrayList<>();
                            String pr_details = PR_IS_Details.pr_details(nameList.get(i), Constants.getToken(), ct, Constants.cons.TODAY_DATE);
                            ct = Integer.parseInt(pr_details.split("/")[pr_details.split("/").length-1]);
                            int cc = 0;
                            fDate = nextDate.get(cc);
                            List< List<String>> list_all = new ArrayList<>();
                            List<String> ll = new ArrayList<>();
                            List<String> ddList = new ArrayList<>();
                            for (int j = detailsList.size() - 1; j >= 0; j--) {
                                //System.out.println("    "+j+" \t"+detailsList.get(j) );
                                String ok = "No";
                                String nextDate_ = detailsList.get(j).split("/")[2];
                                
                                if (DateOperations.compareDates(nextDate_, fDate) == true) {
                                    ll.add(detailsList.get(j));
                                    if (list_all.size() > 0) {
                                        list_all.remove(list_all.size() - 1);
                                        ddList.remove(ddList.size() - 1);
                                    }
                                    list_all.add(ll);
                                    ddList.add(nextDate_ + " - " + fDate);
                                    ok = "Yes";
                                } else {
                                    ok = "No";
                                    cc++;
                                    int c = 0;
                                    
                                    for (int k = 0; k < nextDate.size(); k++) {
                                        if (DateOperations.compareDates(nextDate_, nextDate.get(k))) {
                                            c++;
                                            if (c == 1) {
                                                fDate = nextDate.get(k);
                                                ll = new ArrayList<>();
                                                ll.add(detailsList.get(j));
                                                list_all.add(ll);
                                                ddList.add(nextDate_ + " - " + fDate);
                                            }
                                        }
                                    }
                                    
                                }
                                //System.out.println(j + " " + detailsList.get(j) + "\t" + ok);
                            }
                            //System.out.println("TOTAL SIZE: "+list_all.size());
                            for (int j = 0; j < list_all.size(); j++) {
                                List<String> lists = list_all.get(j);
                                Set<String> emailSet = new LinkedHashSet<String>();
                                for (int k = 0; k < lists.size(); k++) {
                                    emailSet.add(lists.get(k).split("/")[1]);
                                }
                                Iterator iterator = emailSet.iterator();
                                List<String> email_List = new ArrayList<>();
                                List<String> final_lists = new ArrayList<>();
                                while (iterator.hasNext()) {
                                    email_List.add((String) iterator.next());
                                    final_lists.add("Name/email/login/Location/Created_at/Updated_at/0/0/0/0/0_0_0_0");
                                }
                                List<String> objList = new ArrayList<>();
                                objList.add(ddList.get(j));
                                if (j == 0) {
                                    objList.add(pr_details.split("/")[0]);
                                    objList.add(pr_details.split("/")[1]);
                                    objList.add(pr_details.split("/")[2]);
                                    objList.add(pr_details.split("/")[3]);
                                    objList.add(pr_details.split("/")[4]);
                                    objList.add(createList.get(i));
                                    objList.add(nameList.get(i));
                                } else {
                                    objList.add("-");
                                    objList.add("-");
                                    objList.add("-");
                                    objList.add("-");
                                    objList.add("-");
                                    objList.add("-");
                                    objList.add("-");
                                }
                                for (int k = 0; k < lists.size(); k++) {
                                    
                                    for (int l = 0; l < final_lists.size(); l++) {
                                        if (lists.get(k).split("/")[1].equals(email_List.get(l))) {
                                            long up = Long.parseLong(final_lists.get(l).split("/")[6]) + Long.parseLong(lists.get(k).split("/")[7]);
                                            long gis = Long.parseLong(final_lists.get(l).split("/")[7]) + Long.parseLong(lists.get(k).split("/")[8]);
                                            long fol = Long.parseLong(final_lists.get(l).split("/")[8]) + Long.parseLong(lists.get(k).split("/")[9]);
                                            long fow = Long.parseLong(final_lists.get(l).split("/")[9]) + Long.parseLong(lists.get(k).split("/")[10]);
                                            
                                            String comm_string = final_lists.get(l).split("/")[10];
                                            //System.out.println("COMMITS: "+comm_string);
                                            long com = Long.parseLong(comm_string.split("_")[0]) + 1;
                                            long ch = Long.parseLong(comm_string.split("_")[1]) + Long.parseLong(lists.get(k).split("/")[11]);
                                            long add = Long.parseLong(comm_string.split("_")[2]) + Long.parseLong(lists.get(k).split("/")[12]);
                                            long del = Long.parseLong(comm_string.split("_")[3]) + Long.parseLong(lists.get(k).split("/")[13]);
                                            final_lists.set(l, lists.get(k).split("/")[0] + "/" + lists.get(k).split("/")[1] + "/" + lists.get(k).split("/")[6] + "/" + lists.get(k).split("/")[5] + "/" + lists.get(k).split("/")[3] + "/" + lists.get(k).split("/")[4] + "/" + up + "/" + gis + "/" + fol + "/" + fow + "/" + com + "_" + ch + "_" + add + "_" + del);
                                        }
                                    }
                                }
                                for (int k = 0; k < final_lists.size(); k++) {
                                    objList.add(final_lists.get(k));
                                    //System.out.println(k+" : "+final_lists.get(k));
                                }
                                
                                datas = new Object[objList.size()];
                                datas = objList.toArray(datas);
                                allobj.add(datas);
                                
                            }
                            
                            String file_name = FILES[a].replaceAll("repos_gp_combshaa", "repos_gp_commits");
                            Create_Excel.createExcelSheet(allobj, path_new + file_name, subSheet);
                            /// DOTO::::::::: ###################################
                        }
                        
                        stopTime = System.currentTimeMillis();
                        elapsedTime = stopTime - startTime1;
                        System.out.println("              Time_Elapsed: " + elapsedTime / (1000 * 60));
                        
                    }
                    
                }
                
                count++;
            }
        }
        
    }
    
}
