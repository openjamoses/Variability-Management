/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.util.Constants;
import java.util.List;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class Determine_ML {

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
        String file1 = "repos_gp_statistics3.xlsx";
        String variant = "repos_gp_statistics3.xlsx";

        String path_collect = "/Users/john/Desktop/00commits/server/";
        String path_variant = "/Users/john/Desktop/00commits/fmerged/cleans/merged/percentages2/statistics/";
        String path_new = "/Users/john/Desktop/00commits/server/OUTPUT/";
        //todo:
        //String path_collect = "";
        //String path_variant = "";
        //String path_new = "";

        // String[] FILES = {variant};
        //List<String> proj_list = new ArrayList<>();
        //List<String> chPlist = new ArrayList<>();
        //List<List<String>> chFlist = new ArrayList<>();
        //String project = File_Details.setProjectName(path_variant + variant, 0, "B2");
        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 1, 0, 1);
        List<String> forks_list = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 1, 10, 1);
        JSONParser parser = new JSONParser();
        for (int i = 0; i < proj_list.size(); i++) {
            if (ct == Constants.getToken().length) {
                ct = 0;
            }
            String jsonString = Call_URL.callURL("https://api.github.com/repos/" + proj_list.get(i).split("\\|")[0] + "?access_token=" + Constants.getToken()[ct++]);

            if (JSONUtils.isValidJSONObject(jsonString) == true) {
                System.out.println(i + " : " + proj_list.get(i).split("\\|")[0] + "\t fine .....");
            } else {
                //System.out.println("              " + i + " : " + proj_list.get(i).split("\\|")[0] + "\t invalid .....");
                System.out.println("Invalid fork::: \t" + proj_list.get(i).split("\\|")[0]+"\t"+"https://api.github.com/repos/" + proj_list.get(i).split("\\|")[0]);
            }
        }
    }
}
