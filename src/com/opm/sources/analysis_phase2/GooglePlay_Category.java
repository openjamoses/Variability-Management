/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class GooglePlay_Category {

    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        Object[] datas = null;
        int ct = 0;
        String file_repos = "MLP_and_Forks.xlsx";
        String file_shaa1 = "file_googleplay_com-shaa.xlsx";
        //String file_com2 = "Commits_Cleared_500-1000.xlsx";

        String path_repos = "/Users/john/Documents/";
        String path_com = "/Users/john/Documents/";

        String path_new = "/Users/john/Documents/project_s/";
        //todo:

        String[] files = {file_shaa1};

        //List<String> projList = new ArrayList<>();
        //List<String> catList = new ArrayList<>();
        List<String> namesList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();

        for (int a = 0; a < files.length; a++) {
            int numbers = File_Details.getWorksheets(path_com + files[a]);
            int count = 0;
            while (count < numbers) {
                String project = File_Details.setProjectName(path_com + files[a], count, "B2");
                //String cat = File_Details.setProjectName(path_com + files[a], count, "Q2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path_com + files[a], count, 2, 1);
                List<String> category = ReadExcelFile_1Column.readColumnAsString(path_com + files[a], count, 16, 1);

                //projList.add(project);
                //catList.add(cat);
                for (int i = 0; i < names.size(); i++) {
                    namesList.add(names.get(i));
                    categoryList.add(category.get(i));
                }

                System.out.println(count + " : " + project);
                count++;
            }

        }

        List<String> repos = ReadExcelFile_1Column.readColumnAsString(path_repos + file_repos, 0, 0, 1);
        List<String> namesL = ReadExcelFile_1Column.readColumnAsString(path_repos + file_repos, 0, 1, 1);

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "Category"};// end of assigning the header to the object..

        for (int j = 0; j < namesL.size(); j++) {
            if (j == 0) {
                allobj.add(datas);
            }
            if (namesList.contains(namesL.get(j))) {
                int index = namesList.indexOf(namesL.get(j));
                datas = new Object[]{repos.get(j), namesL.get(j), categoryList.get(index)};// end of assigning the header to the object..
                allobj.add(datas);
            }

            String file_name = file_shaa1.replaceAll("file_googleplay_com-shaa", "MLP_and_Forks_category");
            Create_Excel.createExcelSheet(allobj, path_new + file_name, "mlp_forks_category");
        }

    }
}
