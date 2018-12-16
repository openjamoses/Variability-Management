/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;


import com.opm.variability.core.MathsFunctions;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/**
 *
 * @author john
 */
public class MJ_Mean {

    public static void main(String[] args) throws Exception {
        stats();
    }

    private static void stats() throws Exception {
        Object[] datas = null;
        int ct = 0;

        String category1 = "stats_durations2_cd.xlsx";

        String path = "/Users/john/Desktop/Dev_Commits/00New_Repos/statistics/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/statistics/";
        //todo:
        String[] FILES = {category1};

        for (int a = 0; a < FILES.length; a++) {
            ArrayList< Object[]> allobj = new ArrayList<Object[]>();
            datas = new Object[]{"Project", "Mean", "Median"};// end of assigning the header to the object..
            int numbers = 1;
            int count = 0;
            while (count < numbers) {
                if (count == 0) {
                    allobj.add(datas);
                }
                List<String> projectL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 0, 1);
                List<String> nameL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], count, 1, 1);

                List<Double> durationL = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], count, 2, 1);
                List<String> projects = new ArrayList<>();
                Set<String> pSet = new LinkedHashSet<>();
                for (int i = 0; i < projectL.size(); i++) {
                    pSet.add(projectL.get(i));
                }
                Iterator iterator = pSet.iterator();
                List<String> pList = new ArrayList<>();
                while (iterator.hasNext()) {
                    pList.add((String) iterator.next());
                }
                for (int i = 0; i < pList.size(); i++) {
                    List<Double> list = new ArrayList<>();
                    for (int j = 0; j < projectL.size(); j++) {
                        if (pList.get(i).equals(projectL.get(j))) {
                            list.add(durationL.get(j));
                        }
                    }
                    double mean = MathsFunctions.getMean(list);
                    double median = MathsFunctions.getMedian(list);
                    datas = new Object[]{pList.get(i), mean, median};
                    allobj.add(datas);

                }

                String f_name = FILES[a].replaceAll("stats_durations2", "stats_mean");
                Create_Excel.createExcelSheet(allobj, path_new + f_name, "mean_");
                count++;
            }
        }
    }
}
