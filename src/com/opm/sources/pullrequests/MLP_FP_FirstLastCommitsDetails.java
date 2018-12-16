/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.pullrequests;


import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.Shaa_Details;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author john
 */
public class MLP_FP_FirstLastCommitsDetails {

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
        String file1 = "repos_gp_combshaa1.xlsx";
        String file2 = "repos_gp_combshaa2.xlsx";
        String file3 = "repos_gp_combshaa3.xlsx";
        String file4 = "repos_gp_combshaa4.xlsx";
        String file5 = "repos_gp_combshaa5.xlsx";
        String variant = "repos_gp_statistics3.xlsx";
        //String path_collect = "/Users/john/Desktop/00commits/Shas-Combined/";
        //String path_variant = "/Users/john/Desktop/00commits/fmerged/cleans/merged/percentages2/statistics/";
        //String path_new = "/Users/john/Desktop/00commits/fmerged/cleans/merged/percentages2/statistics/final/";
        //todo:
        String path_collect = "";
        String path_variant = "";
        String path_new = "";

        // String[] FILES = {variant};
        //List<String> proj_list = new ArrayList<>();
        //List<String> chPlist = new ArrayList<>();
        //List<List<String>> chFlist = new ArrayList<>();
        //String project = File_Details.setProjectName(path_variant + variant, 0, "B2");
        List<String> proj_list = ReadExcelFile_1Column.readColumnAsString(path_variant + variant, 0, 0, 1);
        List<Double> tot_list = ReadExcelFile_1Column.readColumnAsNumeric(path_variant + variant, 0, 8, 1);
        List<String> projList2 = new ArrayList<>();
        List<String> subList = new ArrayList<>();
        for (int i = 0; i < proj_list.size(); i++) {
            projList2.add(proj_list.get(i).split("\\|")[0]);
            subList.add(proj_list.get(i).split("\\|")[0].split("/")[0]);
        }

        int s_count = 0;
        String[] files_collect = {file1, file2, file3, file4, file5};

        ArrayList< Object[]> allobj2 = new ArrayList<Object[]>();
        datas = new Object[]{"MLP", "Forks"};
        allobj2.add(datas);
        for (int a = 0; a < files_collect.length; a++) {
            int count = 0;
           
            int numbers = File_Details.getWorksheets(path_collect + files_collect[a]);
            ///System.out.println("Reading Collection Excel....!");
            while (count < numbers) {
                String project = File_Details.setProjectName(path_collect + files_collect[a], count, "B2");
                String pCreated = File_Details.setProjectName(path_collect + files_collect[a], count, "D2");
                String pLast = File_Details.setProjectName(path_collect + files_collect[a], count, "F2");
                String sheet = File_Details.getWorksheetName(path_collect + files_collect[a], count);

                if (projList2.contains(project) || subList.contains(project.split("/")[0])) {
                    s_count++;
                    ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                    datas = new Object[]{"Project", "Forks", "Created_at", "First_Commits", "Last_Commits", "Shaas", "Com", "Unique", "Unique_CLOC", "FP_Dev"};
                    allobj.add(datas);

                    List<String> names = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 2, 2);
                    List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_collect + files_collect[a], count, 6, 2);
                    List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_collect + files_collect[a], count, 7, 2);
                    List<String> shaas = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 9, 2);

                    List<String> create_at = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 3, 2);
                    List<String> first = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 4, 2);
                    List<String> last = ReadExcelFile_1Column.readColumnAsString(path_collect + files_collect[a], count, 5, 2);

                    System.out.println(count+"\t"+a+"\t"+s_count + "\t" + sheet);

                    double tot_forks = 0;
                    String fkk_names = "";
                    for (int i = 0; i < names.size(); i++) {
                        // System.out.println(i + "  : " + names.get(i) + "\t" + shaas.get(i));
                        if (!shaas.get(i).equals("")) {
                            tot_forks++;
                            fkk_names = fkk_names.concat(names.get(i) + "|");

                        }
                    }
                    datas = new Object[]{project, fkk_names};
                    allobj2.add(datas);

                    datas = new Object[]{project, tot_forks, pCreated, "", pLast, "", "", "", "", ""};
                    allobj.add(datas);

                    for (int i = 0; i < names.size(); i++) {

                        //System.out.println(i + " \t  " + names.get(i) + "\t" + shaas.get(i));
                        ///System.out.println("      " + shaas.get(i));
                        double Tchanges = 0;
                        Set<String> devSet = new LinkedHashSet<>();
                        if (!shaas.get(i).equals("")) {
                            String[] splits = shaas.get(i).split("/");
                            for (int j = 0; j < splits.length; j++) {
                                String u_shaa = splits[j].split(":")[0];
                                String commits_details = Shaa_Details.details1(project, splits[j].split(":")[0], Constants.getToken(), ct);
                                try {
                                    if (commits_details != null) {
                                        if (commits_details.contains("/")) {
                                            Tchanges += Double.parseDouble(commits_details.split("/")[commits_details.split("/").length - 4]);
                                            devSet.add(commits_details.split("/")[3]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        datas = new Object[]{"", names.get(i), create_at.get(i), first.get(i), last.get(i), shaas.get(i), commits.get(i), unique.get(i), Tchanges, Double.parseDouble(devSet.size() + "")};
                        allobj.add(datas);
                    }
                    String file_name = variant.replaceAll("repos_gp_statistics3", "repos_gp_final_3");
                    Create_Excel.createExcelSheet(allobj, path_new + file_name, project.split("/")[0] + "_" + s_count);
                }
                count++;
            }
        }

        String file_name = variant.replaceAll("repos_gp_statistics3", "repos_gp_final_3");
        Create_Excel.createExcelSheet(allobj2,path_new + file_name, "summery");

    }
}
