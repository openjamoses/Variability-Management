/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;

import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

/**
 *
 * @author john
 */
public class Comonity {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        appDetails();
    }

    private static void appDetails() throws Exception {

        DecimalFormat newFormat = new DecimalFormat("#.##");

        Object[] datas = null;
        int ct = 0;

        String file_google1 = "merged_com_merged_final_cd.xlsx";

        String[] fork_package = {file_google1};

        //todo:
        String path_package = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests2/";
        //String path_package = "";
        //String path_new = "";

        //todo:
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;

        int s_count = 0;
        for (int aa = 0; aa < fork_package.length; aa++) {

            try {   //first connection with GET request

                int numbers = File_Details.getWorksheets(path_package + fork_package[aa]);
                int count = 0;

                ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                datas = new Object[]{"N.S", "Project", "Common", "Total", "Percentage"};
                while (count < numbers) {
                    if (count == 0) {
                        allobj.add(datas);
                    }
                    List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 1, 2);
                    List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 2, 2);
                    List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 3, 2);
                    String project = File_Details.setProjectName(path_package + fork_package[aa], count, "B2");
                    List<String> conList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 4, 1);
                    List<String> catList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 6, 1);

                    List<Double> major = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 7, 1);
                    List<Double> minor = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 8, 1);
                    List<Double> total = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 9, 1);

                    Set<String> devSet = new LinkedHashSet<>();
                    List<String> devList = new ArrayList<>();
                    List<String> category = new ArrayList<>();
                    for (int i = 0; i < catList.size(); i++) {
                        String[] splits = catList.get(i).split("/");
                        for (int j = 0; j < splits.length; j++) {
                            if (splits[j].contains(":")) {
                                devSet.add(splits[j].substring(0, splits[j].lastIndexOf(":")));
                                devList.add(splits[j].substring(0, splits[j].lastIndexOf(":")));
                                category.add(splits[j].substring(splits[j].lastIndexOf(":") + 1, splits[j].length()));
                            }
                        }
                    }

                    System.out.println(devSet.size() + "\t" + devList.size());

                    Iterator iterator = devSet.iterator();
                    List<String> dev = new ArrayList<>();
                    List<String> notCommon = new ArrayList<>();
                    while (iterator.hasNext()) {
                        dev.add((String) iterator.next());

                    }
                    double tot = 0;
                    List<String> common = new ArrayList<>();
                    for (int i = 0; i < dev.size(); i++) {
                        notCommon.add(dev.get(i));
                        System.out.println(i + " \t" + dev.get(i));
                        int c = 0;
                        for (int j = 0; j < devList.size(); j++) {
                            if (dev.get(i).equals(devList.get(j))) {
                                c++;
                            }
                        }
                        if (c > 1) {
                            common.add(dev.get(i));
                        }
                        tot += c;
                    }
                    notCommon.removeAll(common);

                    double perc = (devList.size() - notCommon.size()) * 100 / Double.parseDouble(devList.size()+"");
                    
                    datas = new Object[]{Double.parseDouble((count + 1) + ""), project, devList.size() - notCommon.size(), Double.parseDouble(devList.size() + ""),Double.valueOf(newFormat.format(perc)) };
                    allobj.add(datas);
                    String f_name = fork_package[aa].replaceAll("merged_com_merged_final", "common_percentage");
                    Create_Excel.createExcelSheet(allobj,path_new + f_name, "common_general");
                    //Create_Excel.createExcel2(allobj, 0, path_new + f_name, "google_play");
                    count++;
                }
            } catch (Exception ex) {
                // some exception handling here
                ex.printStackTrace();
            }
        }
    }

    private static void appDetails1() throws Exception {

        DecimalFormat newFormat = new DecimalFormat("#.##");

        Object[] datas = null;
        int ct = 0;

        String file_google1 = "merged_com_merged_final_cd.xlsx";

        String[] fork_package = {file_google1};

        //todo:
        String path_package = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/tests2/";
        //String path_package = "";
        //String path_new = "";

        //todo:
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;

        int s_count = 0;
        for (int aa = 0; aa < fork_package.length; aa++) {

            try {   //first connection with GET request

                int numbers = File_Details.getWorksheets(path_package + fork_package[aa]);
                int count = 0;

                ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                datas = new Object[]{"N.S", "Project", "Common", "Total", "Percentage"};
                while (count < numbers) {
                    if (count == 0) {
                        allobj.add(datas);
                    }
                    List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 1, 2);
                    List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 2, 2);
                    List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 3, 2);
                    String project = File_Details.setProjectName(path_package + fork_package[aa], count, "B2");
                    List<String> conList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 4, 1);
                    List<String> catList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package[aa], count, 6, 1);

                    List<Double> major = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 7, 1);
                    List<Double> minor = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 8, 1);
                    List<Double> total = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package[aa], count, 9, 1);

                    Set<String> devSet = new LinkedHashSet<>();
                    List<String> devList = new ArrayList<>();
                    List<String> category = new ArrayList<>();
                    for (int i = 0; i < catList.size(); i++) {
                        String[] splits = catList.get(i).split("/");
                        for (int j = 0; j < splits.length; j++) {
                            if (splits[j].contains(":")) {
                                String cat_ = splits[j].substring(splits[j].lastIndexOf(":") + 1, splits[j].length());
                                if (cat_.equals("Major")) {
                                    devSet.add(splits[j].substring(0, splits[j].lastIndexOf(":")));
                                    devList.add(splits[j].substring(0, splits[j].lastIndexOf(":")));
                                    category.add(splits[j].substring(splits[j].lastIndexOf(":") + 1, splits[j].length()));
                                }
                            }
                        }
                    }

                    System.out.println(devSet.size() + "\t" + devList.size());

                    Iterator iterator = devSet.iterator();
                    List<String> dev = new ArrayList<>();
                    List<String> notCommon = new ArrayList<>();
                    while (iterator.hasNext()) {
                        dev.add((String) iterator.next());

                    }
                    double tot = 0;
                    List<String> common = new ArrayList<>();
                    for (int i = 0; i < dev.size(); i++) {
                        notCommon.add(dev.get(i));
                        System.out.println(i + " \t" + dev.get(i));
                        int c = 0;
                        for (int j = 0; j < devList.size(); j++) {
                            if (dev.get(i).equals(devList.get(j))) {
                                c++;
                            }
                        }
                        if (c > 1) {
                            common.add(dev.get(i));
                        }
                        tot += c;
                    }
                    notCommon.removeAll(common);

                    double perc = (devList.size() - notCommon.size()) * 100 / Double.parseDouble(devList.size()+"");
                    datas = new Object[]{Double.parseDouble((count + 1) + ""), project, devList.size() - notCommon.size(), Double.parseDouble(devList.size() + ""), Double.valueOf(newFormat.format(perc))};
                    allobj.add(datas);
                    String f_name = fork_package[aa].replaceAll("merged_com_merged_final", "common_percentage");
                    Create_Excel.createExcelSheet(allobj,path_new + f_name, "common_major");
                    //Create_Excel.createExcel2(allobj, 0, path_new + f_name, "google_play");
                    count++;
                }
            } catch (Exception ex) {
                // some exception handling here
                ex.printStackTrace();
            }
        }
    }
}
