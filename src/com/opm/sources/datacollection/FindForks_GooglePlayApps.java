/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;

import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.opm.variability.reads.Download_FileName;
import com.opm.variability.util.Constants;

/**
 *
 * @author openja Moses
 * 
 */
public class FindForks_GooglePlayApps {

    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        googleplayApps();
    }
/**
 * 
 * @throws Exception 
 */
    private static void googleplayApps() throws Exception {
        Object[] datas = null;
        //Excel file containing the forks details and the correspond Shaa..
        String fork_shaa1 = "repos_gp_combshaa3.xlsx";
        String fork_shaa2 = "repos_gp_forksha_11.xlsx";
        //Excel file containing the Google play statistics..
        String file_google = "repos_gp_6_3.xlsx";
        //File paths
        String path_shaas = "/Users/john/Desktop/Dev_Commits/00New_Repos/00Combined/";
        String path_google = "/Users/john/Desktop/Dev_Commits/00New_Repos/atlest6/";
        //New file 
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/00forks_packages/";
        //todo:
        String[] FILESARRAY = {fork_shaa1};

        int sheet_sheet_index = 0;
        for (int a = 0; a < FILESARRAY.length; a++) {

            int total_sheets = File_Details.getWorksheets(path_shaas + FILESARRAY[a]);
            int sheet_index = 0;
            int token_index = 0;

            int sheet_index_0 = 0;
            int column_index_0 = 0;
            int column_index_1 = 1;
            
            int column_index_2 = 2;
            int column_index_3 = 3;
            int column_index_4 = 4;
            int column_index_5 = 5;
            int column_index_6 = 6;
            int column_index_7 = 7;
            int column_index_9 = 9;
            
            int start_row_index_1 = 1;
            int start_row_index_2 = 1;
            
            
            
            List<String> reposList = ReadExcelFile_1Column.readColumnAsString(path_google + file_google, sheet_index_0, column_index_0, start_row_index_1);
            List<String> packageList = ReadExcelFile_1Column.readColumnAsString(path_google + file_google, sheet_index_0, column_index_1, start_row_index_1);

            List<String> projList = new ArrayList<>();
            List<String> sheetList = new ArrayList<>();
            List<String> packList = new ArrayList<>();
            ArrayList< Object[]> DataSet2 = new ArrayList<Object[]>();
            while (sheet_index < total_sheets) {
                ArrayList< Object[]> DataSet1 = new ArrayList<Object[]>();
                datas = new Object[]{"N.S", "MLP", "FP", "Package", "T_Package", "Created_at", "First_date", "Last_date", "Com", "Unique"};
                DataSet1.add(datas);
                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_shaas + FILESARRAY[a], sheet_index, column_index_2, start_row_index_2);
                List<String> createList = ReadExcelFile_1Column.readColumnAsString(path_shaas + FILESARRAY[a], sheet_index, column_index_3, start_row_index_2);
                List<String> firstList = ReadExcelFile_1Column.readColumnAsString(path_shaas + FILESARRAY[a], sheet_index, column_index_4, start_row_index_2);
                List<String> lastList = ReadExcelFile_1Column.readColumnAsString(path_shaas + FILESARRAY[a], sheet_index, column_index_5, start_row_index_2);
                List<String> shaaList = ReadExcelFile_1Column.readColumnAsString(path_shaas + FILESARRAY[a], sheet_index, column_index_9, start_row_index_2);
                List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + FILESARRAY[a], sheet_index, column_index_6, start_row_index_2);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_shaas + FILESARRAY[a], sheet_index, column_index_7, start_row_index_2);
                //List<Double> vip = Pick_GeneralNumeric.pick(path_shaas + FILESARRAY[a], sheet_index, 5);
                String project = File_Details.setProjectName(path_shaas + FILESARRAY[a], sheet_index, "B2");
                String create = File_Details.setProjectName(path_shaas + FILESARRAY[a], sheet_index, "D2");
                String last = File_Details.setProjectName(path_shaas + FILESARRAY[a], sheet_index, "F2");
                String sheet = File_Details.getWorksheetName(path_shaas + FILESARRAY[a], sheet_index);
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
                int flags = 0;
                datas = new Object[]{"", project, Double.parseDouble(nameList.size() + ""), mlp_package, "", create, "", last, "", ""};
                DataSet1.add(datas);
                System.out.println(sheet_index + " : " + project + " \t " + nameList.size() + "\t" + shaaList.size());
                for (int i = 0; i < nameList.size(); i++) {
                    if (unique.get(i) > 0) {
                        String[] splits_1 = shaaList.get(i).split("/");
                        //System.out.println(" ************************************************************************* ");

                        //System.out.println("  " + i + " \t " + nameList.get(i) + " \t " + splits_1.length);
                        Set<String> p_set = new LinkedHashSet<String>();
                        List<String> pList = new ArrayList<>();
                        for (int j = 0; j < splits_1.length; j++) {
                            if (splits_1[j].split(":").length > 1) {
                                Set<String> pSet = Download_FileName.downloads(nameList.get(i), splits_1[j].split(":")[0], mlp_package, Constants.getToken(), token_index);
                                Iterator iterator = pSet.iterator();
                                List<String> p_list = new ArrayList<>();
                                while (iterator.hasNext()) {
                                    p_list.add((String) iterator.next());
                                }
                                token_index = Integer.parseInt(p_list.get(p_list.size() - 1));
                                p_list.remove(p_list.size() - 1);
                                if (p_list.size() > 0) {
                                    for (int k = 0; k < p_list.size(); k++) {
                                        p_set.add(p_list.get(k));
                                    }
                                }

                            }
                        }
                        Iterator iterator2 = p_set.iterator();
                        while (iterator2.hasNext()) {
                            pList.add((String) iterator2.next());
                        }
                        String packages = "";
                        if (pList.size() == 1) {
                            packages = pList.get(0);
                        } else if (pList.size() > 1) {
                            for (int j = 0; j < pList.size(); j++) {
                                if (j < pList.size() - 1) {
                                    packages = packages.concat(pList.get(j) + " , ");
                                }
                                if (j == pList.size() - 1) {
                                    packages = packages.concat(pList.get(j) + "");
                                }
                            }
                        }

                        if (pList.size() > 0) {
                            projList.add(nameList.get(i));
                            sheetList.add(sheet);
                            packList.add(packages);
                            datas = new Object[]{Double.parseDouble(i + ""), "", nameList.get(i), packages, Double.parseDouble(pList.size() + ""), createList.get(i), firstList.get(i), lastList.get(i), commits.get(i), unique.get(i)};
                            DataSet1.add(datas);
                            flags++;
                        }
                    }
                }
                if (flags > 0) {
                    sheet_sheet_index++;
                    String f_name = FILESARRAY[a].replaceAll("repos_gp_combshaa", "repos_gp_packages_");
                    Create_Excel.createExcelSheet(DataSet1, path_new + f_name, project.split("/")[0] + "_" + sheet_sheet_index);
                }
                sheet_index++;
            }
            datas = new Object[]{"N.S", "FP", "Package", "", "Sheet"};
            DataSet2.add(datas);
            for (int i = 0; i < projList.size(); i++) {
                datas = new Object[]{Double.parseDouble(i + ""), projList.get(i), packList.get(i), "", sheetList.get(i)};
                DataSet2.add(datas);
            }
            String f_name = FILESARRAY[a].replaceAll("repos_gp_combshaa", "repos_gp_packages_");
            String Summery_sheetName = "Summery";
            Create_Excel.createExcelSheet(DataSet2, path_new + f_name, Summery_sheetName);
        }
    }
}
