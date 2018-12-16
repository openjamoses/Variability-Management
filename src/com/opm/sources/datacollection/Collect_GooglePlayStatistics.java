/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.datacollection;


import com.opm.variability.core.DateOperations;
import com.opm.variability.core.File_Details;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.CountCommits;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author john
 */
public class Collect_GooglePlayStatistics {

    public static void main(String[] args) {
        appDetails();
    }
    private static void appDetails() {
        Object[] datas = null;
        int ct = 0;
        String fork_package = "repos_selected_fpackages_.xlsx";
        String fork1 = "repos6_forks1_500.xlsx";
        String fork2 = "repos6_forks500_1000.xlsx";
        String path_fork = "/Users/john/Desktop/Dev_Commits/00atleast_6/";

        String[] files_forks = {fork1, fork2};
        String file1 = "repos_forks_packages_1_500.xlsx";
        String file2 = "repos_forks_packages_500_1000.xlsx";

        String path_summery = "/Users/john/Desktop/Dev_Commits/oopackages/";

        //todo:
        String[] files = {file1, file2};

        String path_package = "/Users/john/Desktop/Dev_Commits/";
        String path_new = "/Users/john/Desktop/Dev_Commits/00google/";
        //todo:
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;

        try {   //first connection with GET request

            List<List<String>> name_List = new ArrayList<>();
            List<List<String>> createdList = new ArrayList<>();
            List<List<String>> firstList = new ArrayList<>();
            List<List<String>> lastList = new ArrayList<>();
            List<List<Double>> daysList = new ArrayList<>();
            List<String> projList = new ArrayList<>();
            List<String> pcreated = new ArrayList<>();

            System.out.println("******************** Forks Details.... **********************");
            for (int a = 0; a < files.length; a++) {
                int numbers_1 = File_Details.getWorksheets(path_summery + files[a]);
                List<String> sheetList = ReadExcelFile_1Column.readColumnAsString(path_summery + files[a], numbers_1 - 1, 0, 0);
                for (int i = 0; i < sheetList.size(); i++) {
                    int count = File_Details.getIndex(path_fork + files_forks[a], sheetList.get(i));
                    List<String> name = ReadExcelFile_1Column.readColumnAsString(path_fork + files_forks[a], count, 2, 2);
                    List<String> created = ReadExcelFile_1Column.readColumnAsString(path_fork + files_forks[a], count, 4, 2);
                    List<String> first = ReadExcelFile_1Column.readColumnAsString(path_fork + files_forks[a], count, 5, 2);
                    List<String> last = ReadExcelFile_1Column.readColumnAsString(path_fork + files_forks[a], count, 6, 2);
                    List<Double> days = ReadExcelFile_1Column.readColumnAsNumeric(path_fork + files_forks[a], count, 7, 2);
                    String project = File_Details.setProjectName(path_fork + files_forks[a], count, "B2");
                    String create = File_Details.setProjectName(path_fork + files_forks[a], count, "E2");

                    projList.add(project);
                    pcreated.add(create);
                    name_List.add(name);
                    createdList.add(created);
                    firstList.add(first);
                    lastList.add(last);
                    daysList.add(days);
                    System.out.println("  " + count + " : " + sheetList.get(i) + " \t " + project);
                }
            }

            int numbers = File_Details.getWorksheets(path_package + fork_package);
            int count = 1;
            while (count < numbers) {
                ArrayList< Object[]> allobj = new ArrayList<Object[]>();
                datas = new Object[]{"N.S", "MLP", "FP", "Packages", "Com", "Unique", "GH-Date-F", "GH-Date-L", "Days", "GP-upd-Date", "Downloads", "Star1", "Star2", "Star3", "Star4", "Star5", "AvgStars", "Category", "Description"};
                allobj.add(datas);

                List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package, count, 2, 2);
                List<String> packageList = ReadExcelFile_1Column.readColumnAsString(path_package + fork_package, count, 3, 2);
                List<Double> commits = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package, count, 5, 2);
                List<Double> unique = ReadExcelFile_1Column.readColumnAsNumeric(path_package + fork_package, count, 6, 2);
                String project = File_Details.setProjectName(path_package + fork_package, count, "B2");
                String package_ = File_Details.setProjectName(path_package + fork_package, count, "D2");

                String sheet = File_Details.getWorksheetName(path_package + fork_package, count);

                System.out.println(count + " : " + project);
                if (projList.contains(project)) {
                    int index = projList.indexOf(project);

                    List<String> c_return = CountCommits.countCommits2(project, pcreated.get(index), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                    double counts = Double.parseDouble(c_return.get(0));
                    String first_date = c_return.get(2);
                    String last_date = c_return.get(1);
                    ct = Integer.parseInt(c_return.get(3));

                    /// Master Project .. Goes here....!!
                    datas = addMaster(project, package_, pcreated.get(index), counts, first_date, last_date, datas);
                    allobj.add(datas);
                    for (int a = 0; a < nameList.size(); a++) {
                        res = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageList.get(a) + "&hl=en).get()")
                                .method(Connection.Method.GET)
                                .execute();
                        doc = res.parse();

                        Elements link = doc.select("h1.document-title");
                        Elements app_info = doc.select("div.details-info");
                        String appIcon = app_info.select("img.cover-image").attr("src"); // App Icon url
                        String category = Jsoup.parse(app_info.select("a.category").attr("href").toString()).text();

                        Elements metaElement = doc.select("div.meta-info");

                        String updated = doc.getElementsByAttributeValue("itemprop", "datePublished").text();
                        String installs = doc.getElementsByAttributeValue("itemprop", "numDownloads").text();
                        String versions = doc.getElementsByAttributeValue("itemprop", "softwareVersion").text();
                        String genre = doc.getElementsByAttributeValue("itemprop", "genre").text();
                        String op_system = doc.getElementsByAttributeValue("itemprop", "operatingSystems").text();
                        String avgstars = doc.getElementsByAttributeValue("class", "score").text();
                        String descriptions = "";
                        if (doc.getElementsByAttributeValue("jsname", "C4s9Ed") != null) {
                            descriptions = doc.getElementsByAttributeValue("jsname", "C4s9Ed").text();
                        }
                        avgstars = avgstars.replaceAll(",", "");

                        Elements stars_element = doc.getElementsByAttributeValue("class", "bar-number");
                        List<String> stars = new ArrayList<>();
                        double[] str = new double[5];
                        for (Element element : stars_element) {
                            stars.add(element.text());
                        }
                        for (int i = 0; i < 5; i++) {
                            if (i < stars.size()) {
                                String strss = stars.get(i).replace(",", "");
                                str[i] = Double.parseDouble(strss);
                            } else {
                                str[i] = 0;
                            }
                        }
                        double AvgStars = convert(avgstars);
                        if (name_List.get(index).contains(nameList.get(a))) {
                            int index2 = name_List.get(index).indexOf(nameList.get(a));
                            datas = new Object[]{Double.parseDouble(a + ""), "", nameList.get(a), packageList.get(a), commits.get(a), unique.get(a), firstList.get(index).get(index2), lastList.get(index).get(index2), daysList.get(index).get(index2), updated, installs, str[4], str[3], str[2], str[1], str[0], AvgStars, genre, descriptions};
                            allobj.add(datas);
                        }

                        /**
                         * System.out.println("Downloads: " + installs);
                         * System.out.println("datePublished: " + updated);
                         * System.out.println("softwareVersion: " + versions);
                         * System.out.println("genre: " + genre);
                         * System.out.println("operatingSystems: " + op_system);
                         * System.out.println("************************************");
                         *
                         * **
                         */
                    }
                }
                String f_name = fork_package.replaceAll("repos_selected_fpackages_", "repos_fpackages_googleplay");
                Create_Excel.createExcelSheet(allobj,path_new + f_name, sheet);
                count++;
            }

        } catch (Exception ex) {
            //Do some exception handling here
            ex.printStackTrace();
        }
    }

    private static Object[] addMaster(String project, String package_, String created_at, double commits, String first, String last, Object[] datas) {
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;
        try {
            res = Jsoup.connect("https://play.google.com/store/apps/details?id=" + package_ + "&hl=en).get()")
                    .method(Connection.Method.GET)
                    .execute();
            doc = res.parse();

            Elements link = doc.select("h1.document-title");
            Elements app_info = doc.select("div.details-info");
            String appIcon = app_info.select("img.cover-image").attr("src"); // App Icon url
            String category = Jsoup.parse(app_info.select("a.category").attr("href").toString()).text();

            Elements metaElement = doc.select("div.meta-info");
            //System.out.println("************************************");

            String updated = doc.getElementsByAttributeValue("itemprop", "datePublished").text();
            String installs = doc.getElementsByAttributeValue("itemprop", "numDownloads").text();
            String versions = doc.getElementsByAttributeValue("itemprop", "softwareVersion").text();
            String genre = doc.getElementsByAttributeValue("itemprop", "genre").text();
            String op_system = doc.getElementsByAttributeValue("itemprop", "operatingSystems").text();
            String avgstars = doc.getElementsByAttributeValue("class", "score").text();
            avgstars = avgstars.replace(",", "");
            String descriptions = "";
            if (doc.getElementsByAttributeValue("jsname", "C4s9Ed") != null) {
                descriptions = doc.getElementsByAttributeValue("jsname", "C4s9Ed").text();
            }

            String format1 = "MMM d, yyyy";
            String format2 = "yyyy-MM-dd";
            //updated = DateOperations.format(updated, format1, format2);

            Elements stars_element = doc.getElementsByAttributeValue("class", "bar-number");
            List<String> stars = new ArrayList<>();
            double[] str = new double[5];
            for (Element element : stars_element) {
                String st = element.text();
                st = st.replace(",", "");
                stars.add(st);
            }
            for (int i = 0; i < 5; i++) {
                if (i < stars.size()) {
                    str[i] = Double.parseDouble(stars.get(i));
                } else {
                    str[i] = 0;
                }
            }
            double AvgStars = convert(avgstars);
            String date_diff = "";
            if (!first.equals("date") && !last.equals("date")) {
                date_diff = DateOperations.diff(first, last);
                datas = new Object[]{"", project, "", package_, commits, created_at, first, last, Double.parseDouble(date_diff.split("/")[0]), updated, installs, str[4], str[3], str[2], str[1], str[0], AvgStars, genre, descriptions};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;

    }

    private static double convert(String value) {
        double val = 0;
        try {
            val = Double.parseDouble(value);
        } catch (Exception e) {
            //e.printStackTrace();
            val = 0;
        }
        return val;
    }
}
