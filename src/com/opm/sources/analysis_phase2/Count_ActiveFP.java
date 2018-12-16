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
public class Count_ActiveFP {

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
        Object[] datas2 = null;
        //String[] tokens = Constants.getToken();
        // int ct = 0;

        //String variant = "Variant-Statistics.xlsx";
        String file1 = "repos_gp_combshaa1.xlsx";
        String file2 = "repos_gp_combshaa2.xlsx";
        String file3 = "repos_gp_combshaa3.xlsx";
        String file4 = "repos_gp_combshaa4.xlsx";
        String file5 = "repos_gp_combshaa5.xlsx";
        String path_com = "";
        String path_new = "";
        //todo:

        String[] files = {file1, file2, file3, file4, file5};

        List<String> projList = new ArrayList<>();
        //List<String> projList2 = new ArrayList<>();
        List<Double> uniqueList = new ArrayList<>();
        //List<Double> comL = new ArrayList<>();
        //List<String> catList = new ArrayList<>();
        // List<List<String>> namesList = new ArrayList<>();
        //List<List<String>> categoryList = new ArrayList<>();

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        ArrayList< Object[]> allobj2 = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Unique_Forks"};// end of assigning the header to the object..
        datas2 = new Object[]{"Project", "Fork", "Unique_Fork"};// end of assigning the header to the object..
        double total = 0;

        for (int a = 0; a < files.length; a++) {
            int numbers = File_Details.getWorksheets(path_com + files[a]);
            int count = 0;
            if (a == 0) {
                allobj.add(datas);
                allobj2.add(datas2);
            }
            while (count < numbers) {
                String project = File_Details.setProjectName(path_com + files[a], count, "B2");
                //String cat = File_Details.setProjectName(path_com + files[a], count, "Q2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path_com + files[a], count, 2, 1);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_com + files[a], count, 7, 2);

                double flag = 0;
                //projList2.add(project);
                for (int i = 0; i < names.size(); i++) {
                    datas2 = new Object[]{project, names.get(i), unique.get(i)};// end of assigning the header to the object..

                    allobj2.add(datas2);
                    // comL.add(unique.get(i));
                }
                for (int i = 0; i < unique.size(); i++) {
                    if (unique.get(i) > 0) {
                        flag++;
                    }
                }
                if (flag > 0) {
                    projList.add(project);
                    //uniqueList.add(flag);
                    total += flag;
                    datas = new Object[]{project, flag};// end of assigning the header to the object..
                    allobj.add(datas);

                    String file_name = file1.replaceAll("repos_gp_combshaa1", "repos_gp_activefp");
                    Create_Excel.createExcelSheet(allobj, path_new + file_name, "active_fp1");
                }

                String file_name = file1.replaceAll("repos_gp_combshaa1", "repos_gp_activefp");
                Create_Excel.createExcelSheet(allobj2,path_new + file_name, "active_fp2");
                System.out.println(count + " : " + project);
                count++;
            }

        }
        datas = new Object[]{Double.parseDouble(projList.size() + ""), total};// end of assigning the header to the object..
        allobj.add(datas);

        String file_name = file1.replaceAll("repos_gp_combshaa1", "repos_gp_activefp");
        Create_Excel.createExcelSheet(allobj, path_new + file_name, "active_fp1");

    }
}
