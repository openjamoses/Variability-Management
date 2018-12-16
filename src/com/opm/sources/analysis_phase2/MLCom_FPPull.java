/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.core.SplitString;
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
public class MLCom_FPPull {

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
        Object[] datas2 = null;
        String file1 = "pr_merged_finall_333_111111111111-OPM.xlsx";
        String prfile = "gp_prmerged_shaas.xlsx";

        //String merged1 = "merged_com_merged_final_cd.xlsx";
        //String path = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        // String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/";
        //String path_pr = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        //String path_merged = "";
        String path = "";
        String path_pr = "";
        String path_new = "";

        List<String> projList = ReadExcelFile_1Column.readColumnAsString(path + file1, 4, 0, 1);
        List<String> names = ReadExcelFile_1Column.readColumnAsString(path + file1, 4, 1, 1);
        List<Double> MergedCom = ReadExcelFile_1Column.readColumnAsNumeric(path + file1, 4, 2, 1);
        List<String> created_at = new ArrayList<>();

        List<String> projectL = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 0, 1);
        List<String> forks = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 1, 1);
        List<String> shaas = ReadExcelFile_1Column.readColumnAsString(path_pr + prfile, 0, 4, 1);

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

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "","MLP_Com", "FP_Com", "MLP_Shaa","", "FP_Shaa"
        };// end of assigning the header to the object..

        for (int a = 0; a < pList.size(); a++) {
            System.out.println(a + " : " + pList.get(a));
            if (a == 0) {
                allobj.add(datas);
            }
            List<List<String>> allList_1 = Commits.count_2(pList.get(a), "mlp", "", Constants.cons.TODAY_DATE, Constants.getToken(), ct);
            List<String> shaList_1 = allList_1.get(0);
            List<String> loginList_1 = allList_1.get(2);
            
            //List<String> detailsList_1 = allList_1.get(3);
            List<List<List<String>>> lists = new ArrayList<>();
            List<String> nameL = new ArrayList<>();
            List<Double> mL = new ArrayList<>();
            for (int b = 0; b < names.size(); b++) {
                if (pList.get(a).equals(projList.get(b))) {
                    nameL.add(names.get(b));
                    mL.add(MergedCom.get(b));
                    System.out.println("       " + b + " \t" + names.get(b));
                    List<List<String>> allList = Commits.count_2(names.get(b), "fp", created_at.get(b), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                    List<String> shaList_3 = allList.get(0);
                    List<String> loginList_3 = allList.get(2);
                    lists.add(allList);
                }
            }

           
            for (int b = 0; b < nameL.size(); b++) {
                List<List<String>> allList_3 = lists.get(b);
                List<String> shaList_3 = allList_3.get(0);
                List<String> loginList_3 = allList_3.get(2);
                List<String> uniqueL = new ArrayList<>();
                for (int i = 0; i < shaList_3.size(); i++) {
                    if (!shaList_1.contains(shaList_3.get(i))) {
                        uniqueL.add(shaList_3.get(i));
                    }
                }
                List<String> prShaaL = new ArrayList<>();
                if (projectL.contains(pList.get(a))) {
                    int index = projectL.indexOf(pList.get(a));
                    String[] shaa = shaas.get(index).split("/");
                    for (int i = 0; i < shaa.length; i++) {
                        if (!shaa[i].equals("-")) {
                            prShaaL.add(shaa[i]);
                            uniqueL.add(shaa[i]);
                        }
                    }
                }

                List<String> list = new ArrayList<>();
                list.add(pList.get(a));
                list.add(nameL.get(b));
                list.add("");

                
                List<String> MLPS = shaList_1;
                MLPS.removeAll(prShaaL);
                list.add(MLPS.size() + "");
                list.add(uniqueL.size() + "");
                
                String Mshaa = "", FShaa = "";
                for (int i = 0; i < MLPS.size(); i++) {
                    Mshaa = Mshaa.concat(MLPS.get(i) + "/");
                }

                if (Mshaa.length() > 30000) {
                    String[] splits = SplitString.split(Mshaa, 30000);
                    for (int i = 0; i < splits.length; i++) {
                        list.add(splits[i]);
                    }
                } else {
                    list.add(Mshaa);
                }

                for (int i = 0; i < uniqueL.size(); i++) {
                    FShaa = FShaa.concat(uniqueL.get(i) + "/");
                }
                list.add(Mshaa);
                list.add("");
                if (FShaa.length() > 30000) {
                    String[] splits = SplitString.split(FShaa, 30000);
                    for (int i = 0; i < splits.length; i++) {
                        list.add(splits[i]);
                    }
                } else {
                    list.add(FShaa);
                }

                datas = new Object[list.size()];
                datas = list.toArray(datas);
                allobj.add(datas);
                String f_name = prfile.replaceAll("gp_prmerged_shaas", "gp_mlp_fp_shaas2");
                Create_Excel.createExcelSheet(allobj, path_new + f_name, "mlp_fp_shaa2");

            }

        }
    }
}
