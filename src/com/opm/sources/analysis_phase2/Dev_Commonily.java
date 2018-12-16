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
import java.util.List;

/**
 *
 * @author john
 */
public class Dev_Commonily {

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
        DecimalFormat newFormat = new DecimalFormat("#.##");
        Object[] datas = null;
        int ct = 0;
        //String file_stats = "Repos.xlsx";
        String file_collect1 = "gp_mlp_fp_merged_final.xlsx";
        String variant = "Variant-Statistics.xlsx";
        String path_variants = "/Users/john/Documents/Destope_data_2018-05_18/Dev_Commits/00New_Repos/statistics/";

        String path = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";
        String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00commits/00output_latest/";

        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variants + variant, 0, 0, 2);

        String[] FILES = {file_collect1};

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Forks", "Common", "Total", "Percetage"};
        int tot_s = 0;
        for (int a = 0; a < FILES.length; a++) {

            int count = 0;
            int numbers = File_Details.getWorksheets(path + FILES[a]);
            ///System.out.println("Reading Collection Excel....!");
            count = 0;
            List<String> nL = new ArrayList<>();
            while (count < numbers) {
                if (count == 0) {
                    allobj.add(datas);
                }

                String project = File_Details.setProjectName(path + FILES[a], count, "A2");
                List<String> names = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 1, 2);
                List<String> devComL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 2, 1);
                List<String> devCatL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 4, 1);

                List<String> nameL = new ArrayList<>();
                nameL.add(project);
                for (int i = 0; i < names.size(); i++) {
                    nameL.add(names.get(i));
                }
                List<String> MdevList = new ArrayList<>();

                String[] splitss = devCatL.get(0).split("/");
                for (int i = 0; i < splitss.length; i++) {
                    if (splitss[i].contains(":")) {
                        MdevList.add(splitss[i].substring(0, splitss[i].lastIndexOf(":")));
                    }
                }

                if (proj_list.contains(project)) {

                    for (int b = 1; b < devCatL.size(); b++) {
                        List<String> devList = new ArrayList<>();
                        String[] splits = devCatL.get(b).split("/");
                        List<String> list = new ArrayList<>();
                        List<String> list2 = new ArrayList<>();
                        List<String> list3 = new ArrayList<>();
                        for (int j = 0; j < splits.length; j++) {
                            if (splits[j].contains(":")) {
                                devList.add(splits[j].substring(0, splits[j].lastIndexOf(":")));
                            }
                        }
                        int tot_com = 0;
                        for (int i = 0; i < devList.size(); i++) {
                            if (MdevList.contains(devList.get(i))) {
                                tot_com++;
                            }
                        }
                        double tot2 = tot_com * 2;
                        double div = (MdevList.size() + devList.size());
                        double perc = tot_com * 100 / div;
                        double percentage = perc;
                        try {
                            percentage = Double.parseDouble(newFormat.format(perc));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!nL.contains(nameL.get(b))) {
                            datas = new Object[]{project, nameL.get(b), tot2, div, percentage};
                            //if (!allobj.contains(datas)) {
                            allobj.add(datas);
                            nL.add(nameL.get(b));
                        }

                        //}
                    }
                    String f_name = file_collect1.replaceAll("gp_mlp_fp_merged_final", "gp_mlp_fp_commonality");
                    Create_Excel.createExcelSheet(allobj, path_new + f_name, "commonity");
                }
                count++;
            }
        }
    }
}
