/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;


import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.Commits;
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
public class Collection_MergedCom {

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
        //String merged1 = "merged_com_merged_final_cd.xlsx";

        //String path = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        // String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/";
        //String path_merged = "";
        String path = "";
        String path_new = "";

        List<String> projList = ReadExcelFile_1Column.readColumnAsString(path + file1, 0, 0, 1);
        List<String> names = ReadExcelFile_1Column.readColumnAsString(path + file1, 0, 1, 1);
        List<Double> MergedCom = ReadExcelFile_1Column.readColumnAsNumeric(path + file1, 0, 2, 1);
        List<String> created_at = new ArrayList<>();

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

        for (int a = 0; a < pList.size(); a++) {
            System.out.println(a + " : " + pList.get(a));
            ArrayList< Object[]> allobj = new ArrayList<Object[]>();
            datas = new Object[]{"Project", "Forks", "MergedCom", "", "Merged_Shaas", "Merged_Com", "Merged_Dev", "Tot_Dev"
            };// end of assigning the header to the object..
            allobj.add(datas);

            List<List<String>> allList_1 = Commits.count_2(pList.get(a), "mlp", "", Constants.cons.TODAY_DATE, Constants.getToken(), ct);
            List<String> shaList_1 = allList_1.get(0);
            List<String> loginList_1 = allList_1.get(2);
            //List<String> detailsList_1 = allList_1.get(3);
            List<List<List<String>>> lists = new ArrayList<>();
            List<String> nameL = new ArrayList<>();
            for (int b = 0; b < names.size(); b++) {
                if (pList.get(a).equals(projList.get(b))) {
                    nameL.add(names.get(b));
                    System.out.println("       " + b + " \t" + names.get(b));
                    List<List<String>> allList = Commits.count_2(names.get(b), "fp", created_at.get(b), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                    lists.add(allList);
                }
            }

            for (int b = 0; b < nameL.size(); b++) {
                List<List<String>> allList_3 = lists.get(b);
                List<String> shaList_3 = allList_3.get(0);
                List<String> loginList_3 = allList_3.get(2);
                //List<String> detailList_3 = allList_3.get(3);

                List<String> mergedShaa = new ArrayList<>();
                Set<String> devSet = new LinkedHashSet<>();
                //double changes = 0;
                for (int c = 0; c < shaList_3.size(); c++) {
                    int c_shas = 0;

                    for (int i = 0; i < lists.size(); i++) {
                        if (i != b) {
                            List<List<String>> allList2 = lists.get(i);
                            List<String> shaList = allList2.get(0);
                            //List<String> dateList = allList2.get(1);
                            //List<String> messageList = allList2.get(2);
                            if (shaList.contains(shaList_3.get(c))) {
                                c_shas++;
                            }
                        }
                    }
                    if (shaList_1.contains(shaList_3.get(c))) {
                        c_shas++;
                    }
                    String cat_ = "";
                    if (c_shas > 0) {
                        mergedShaa.add(shaList_3.get(c));
                        devSet.add(loginList_3.get(c));
                        //changes += Double.parseDouble(detailList_3.get(c).split("/")[4]);
                    }
                }
                String merged_shaa = "", devString = "";
                Iterator iterator2 = devSet.iterator();
                while (iterator2.hasNext()) {
                    devString = devString.concat((String) iterator2.next());
                }
                for (int i = 0; i < mergedShaa.size(); i++) {
                    merged_shaa = merged_shaa.concat(mergedShaa.get(i) + "/");
                }

                System.out.println("   " + nameL.size() + "\t" + MergedCom.size());
                datas = new Object[]{pList.get(a), nameL.get(b), MergedCom.get(b), "", merged_shaa, Double.parseDouble(mergedShaa.size() + ""), devString, Double.parseDouble(devSet.size() + "")
                };// end of assigning the header to the object..
                allobj.add(datas);

                String f_name = file1.replaceAll("MergedCommits", "gp_merged_shaass");
                Create_Excel.createExcelSheet(allobj,path_new + f_name, "shaa_details");
            }
        }
    }
}
