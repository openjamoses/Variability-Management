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
import com.opm.variability.reads.Shaa_Details;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class Identify_MergedCom {

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
        String file1 = "MergedCommits.xlsx";
        String merged1 = "merged_com_merged_final_cd.xlsx";

        //String path = "/Users/john/Desktop/Dev_Commits/00New_Repos/collections/";
        // String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/";
        String path_merged = "";
        String path = "";
        String path_new = "";

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
                //List<String> dList = Pick_GeneralNext.pick(path_merged + FILES[a], count, 6, 2);

                proj_list.add(project);
                chPlist.add(pChange);
                chFlist.add(nList);

                System.out.println(count + " : " + project);

                count++;
            }
        }

        List<String> projList = ReadExcelFile_1Column.readColumnAsString(path + file1, 0, 0, 1);
        List<String> names = ReadExcelFile_1Column.readColumnAsString(path + file1, 0, 1, 1);
        List<Double> MergedCom = ReadExcelFile_1Column.readColumnAsNumeric(path + file1, 0, 2, 1);

        List<String> created_at = new ArrayList<>();

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "MergedCom", "", "Merged_Shaas", "Merged", "Merged_Dev", "MLP_Com", "Fp_Com", "MLP_Dev", "FP_Dev", "Tot_Dev"
        };// end of assigning the header to the object..
        JSONParser parser = new JSONParser();
        int ct = 0;
        for (int i = 0; i < names.size(); i++) {

            if (ct == (Constants.getToken().length)) {/// the the index for the tokens array...
                ct = 0; //// go back to the first index......
            }
            String url = "https://api.github.com/repos/" + names.get(i) + "?access_token=" + Constants.getToken()[ct++];
            String forks_url = Call_URL.callURL(url);
            if (JSONUtils.isValidJSONObject(forks_url) == false) {///                             
                System.out.println(":Invalid fork found!   - " + url);
                break;
            }

            //System.out.println(https://api.github.com/repos/" + splits_j[j]);
            JSONObject jSONObject = (JSONObject) parser.parse(forks_url);
            String created = (String) jSONObject.get("created_at");
            created_at.add(created);
        }
        Set<String> pSet = new LinkedHashSet<>();
        List<String> pList = new ArrayList<>();
        for (int i = 0; i < projList.size(); i++) {
            pSet.add(projList.get(i));
        }
        Iterator iterator = pSet.iterator();
        while (iterator.hasNext()) {
            pList.add((String) iterator.next());
        }

        for (int a = 32; a < pList.size(); a++) {

            List<String> devlist = new ArrayList<>();
            List<String> catPlist = new ArrayList<>();

            List<String> devFlist = new ArrayList<>();
            List<String> catFlist = new ArrayList<>();

            String pChange = "";
            if (proj_list.contains(pList.get(a))) {
                int index = proj_list.indexOf(pList.get(a));
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

                //}
                System.out.println(a + " : " + pList.get(a));
                if (a == 0) {
                    allobj.add(datas);
                }
                List<String> nameL = new ArrayList<>();
                List<String> createdL = new ArrayList<>();
                List<Double> mList = new ArrayList<>();

                for (int j = 0; j < names.size(); j++) {
                    if (pList.get(a).equals(projList.get(j))) {
                        nameL.add(names.get(j));
                        createdL.add(created_at.get(j));
                        mList.add(MergedCom.get(j));
                    }
                }
                String min_date = DateOperations.sorts(createdL, createdL).split("/")[0];
                String max_date = DateOperations.sorts(createdL, createdL).split("/")[1];

                List<List<String>> allList_1 = Commits.count(pList.get(a), "mlp", min_date, Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                List<String> shaList_1 = allList_1.get(0);
                List<String> dateList_1 = allList_1.get(1);
                List<String> messageList_1 = allList_1.get(2);

                List<List<List<String>>> lists = new ArrayList<>();
                for (int j = 0; j < nameL.size(); j++) {
                    System.out.println("       " + j + " \t" + nameL.get(j));
                    List<List<String>> allList = Commits.count(nameL.get(j), "fp", createdL.get(j), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                    lists.add(allList);
                }

                for (int b = 0; b < nameL.size(); b++) {
                    List<List<String>> allList_3 = lists.get(b);
                    List<String> shaList_3 = allList_3.get(0);
                    List<String> dateList_3 = allList_3.get(1);
                    List<String> messageList_3 = allList_3.get(2);

                    List<String> shaaUnique = new ArrayList<>();
                    double mlp = 0;
                    double fkp = 0;
                    double mlp_com = 0;
                    double mlp_chang = 0;

                    Set<String> mlp_dev = new LinkedHashSet<>();
                    Set<String> fp_dev = new LinkedHashSet<>();

                    //double mlp_mj = 0;
                    //double mlp_mn = 0;
                    //double fks_mj = 0;
                    //double fks_mn = 0;
                    for (int c = 0; c < shaList_3.size(); c++) {
                        int c_shas = 0;

                        for (int i = 0; i < lists.size(); i++) {
                            if (i != b) {
                                List<List<String>> allList2 = lists.get(i);
                                List<String> shaList = allList2.get(0);
                                List<String> dateList = allList2.get(1);
                                List<String> messageList = allList2.get(2);
                                if (shaList.contains(shaList_3.get(c))) {
                                    c_shas++;
                                }
                                /**
                                 * for (int j = 0; j < shaList.size(); j++) { if
                                 * (shaList.get(j).equals(shaList_3.get(c))) {
                                 * c_shas++; fp ++; } } **
                                 */
                            }
                        }
                        if (shaList_1.contains(shaList_3.get(c))) {
                            c_shas++;
                        }
                        String cat_ = "";
                        if (c_shas == 0) {
                            shaaUnique.add(shaList_3.get(c));
                        }

                    }
                    List<String> mergedList = shaList_3;
                    mergedList.removeAll(shaaUnique);
                    String merged_shaa = "";

                    Set<String> devSet = new LinkedHashSet<>();
                    List<String> dev_list = new ArrayList<>();
                    List<String> details_list = new ArrayList<>();
                    for (int i = 0; i < mergedList.size(); i++) {
                        merged_shaa = merged_shaa.concat(mergedList.get(i) + "/");
                        String commits_details = Shaa_Details.details1(nameL.get(b), mergedList.get(i), Constants.getToken(), ct);
                        //System.out.println(shaa+"\t"+commits_details);
                        if (commits_details != null) {
                            ct = Integer.parseInt(commits_details.split("/")[commits_details.split("/").length - 1]);
                            //System.out.println("        "+tot_changes);
                            String details = commits_details.split("/")[0] + "|" + commits_details.split("/")[1] + "|" + commits_details.split("/")[3];
                            String login = commits_details.split("/")[3];
                            dev_list.add(details);

                            if (devlist.contains(login)) {
                                mlp++;
                                mlp_dev.add(login);
                                mlp_com += 1;
                            }

                            if (devFlist.contains(login)) {
                                fp_dev.add(login);
                                fkp++;
                            }

                            devSet.add(details);
                            //developers = developers.concat(details + "/");
                        }
                    }
                    List<String> devList2 = new ArrayList<>();
                    Iterator it = devSet.iterator();
                    while (it.hasNext()) {
                        devList2.add((String) it.next());
                        //developers = developers.concat((String) it.next() + "/");
                    }
                    String developers = "";
                    for (int i = 0; i < devList2.size(); i++) {
                        int tot = 0;
                        for (int j = 0; j < dev_list.size(); j++) {
                            if (devList2.get(i).equals(dev_list.get(j))) {
                                tot++;
                            }
                        }
                        developers = developers.concat(devList2.get(i) + ":" + tot + "/");
                    }

                    datas = new Object[]{pList.get(a), nameL.get(b), mList.get(b), "", merged_shaa, Double.parseDouble(mergedList.size() + ""), developers, mlp, fkp, Double.parseDouble(mlp_dev.size() + ""), Double.parseDouble(fp_dev.size() + ""), Double.parseDouble(devSet.size() + "")
                    };
                    allobj.add(datas);
                    String f_name = file1.replaceAll("MergedCommits", "gp_merged_final_shaass");
                    Create_Excel.createExcelSheet(allobj, path_new + f_name, "merged_shaa2");
                }
            }
        }
    }
}
