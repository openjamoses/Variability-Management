/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;


import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.Download_Manifest;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author john
 */
public class Identify_Packages {

    public static void main(String[] args) throws Exception {
        check();
    }

    /**
     *
     * @throws Exception
     */
    private static void check() throws Exception {
        Object[] datas = null;
        String fork_shaa1 = "fork_shas_1-500.xlsx";
        String fork_shaa2 = "fork_shas_500-1000.xlsx";
        String file_google = "google_play-All.xlsx";

        String path_shaas = "/Users/john/Desktop/DESKTOP/Files/00deleted_commits/00forked_details/00shaas/";
        String path_google = "/Users/john/Desktop/DESKTOP/Files/";
        String path_new = "/Users/john/Desktop/DESKTOP/Files/00deleted_commits/00forked_details/00packages/";
        //todo:
        String[] files = {fork_shaa1};

        for (int a = 0; a < files.length; a++) {

            int numbers = File_Details.getWorksheets(path_shaas + files[a]);
            int count = 0;
            int ct = 0;

            List<String> reposList = ReadExcelFile_1Column.readColumnAsString(path_google + file_google, 0, 0, 1);
            List<String> packageList = ReadExcelFile_1Column.readColumnAsString(path_google + file_google, 0, 1, 1);

            while (count < numbers) {
                ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                datas = new Object[]{"N.S", "MLP", "FP", "Package", "T_Package", "Com", "Unique"};
                allobj.add(datas);
                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_shaas + files[a], count, 2, 2);
                List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + files[a], count, 3,2);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + files[a], count, 4,2);
                String project = File_Details.setProjectName(path_shaas + files[a], count, "B2");
                String sheet = File_Details.getWorksheetName(path_shaas + files[a], count);
                int index = reposList.indexOf(project);
                String mlp_package = "";
                if (index >= 0) {
                    mlp_package = packageList.get(index);
                }

                double t_unique = 0;
                for (int i = 0; i < unique.size(); i++) {
                    if (unique.get(i) > 0) {
                        t_unique++;
                    }
                }
                datas = new Object[]{"", project, Double.parseDouble(nameList.size() + ""), mlp_package, "", "", t_unique};
                allobj.add(datas);
                System.out.println(count + " : " + project + " \t " + nameList.size());
                for (int i = 0; i < nameList.size(); i++) {
                    if (unique.get(i) > 0) {
                        Set<String> pSet = Download_Manifest.downloads(nameList.get(i), Constants.getToken(), ct);
                        Iterator iterator = pSet.iterator();
                        List<String> p_list = new ArrayList<>();
                        while (iterator.hasNext()) {
                            p_list.add((String) iterator.next());
                        }
                        ct = Integer.parseInt(p_list.get(p_list.size() - 1));
                        p_list.remove(p_list.get(p_list.size() - 1));
                        String packages = "";
                        System.out.println("  " + i + " : " + nameList.get(i) + "\t" + p_list.size());
                        if (p_list.size() == 1) {
                            packages = p_list.get(0);
                        } else if (p_list.size() > 1) {
                            for (int j = 0; j < p_list.size(); j++) {
                                if (j < p_list.size() - 1) {
                                    packages = packages.concat(p_list.get(j) + " , ");
                                }
                                if (j == p_list.size() - 1) {
                                    packages = packages.concat(p_list.get(j) + "");
                                }
                            }
                        }

                        if (p_list.size() > 0) {
                            datas = new Object[]{Double.parseDouble(i + ""), "", nameList.get(i), packages, Double.parseDouble(p_list.size() + ""), commits.get(i), unique.get(i)};
                            allobj.add(datas);
                        }
                    }

                }
                String f_name = files[a].replaceAll("fork_shas_", "forked_google_play_");
                Create_Excel.createExcelSheet(allobj,path_new + f_name, sheet);

                count++;
            }
        }
    }
}
