/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.analysis_phase2;


import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
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
public class Googleplay_Stats {

    public static void main(String[] args) {
        appDetails();
    }

    private static void appDetails() {
        Object[] datas = null;
        int ct = 0;
        String file1 = "Repos-and-Corresponding-Google-play-packages.xlsx";
        //String[] fork_package = {file_pack3};
        //todo:
        //String path_package = "/Users/john/Documents/Destope_data_2018-05_18/00commits/server/OUTPUT/";
        //String path_new = "/Users/john/Desktop/Dev_Commits/00New_Repos/files_packages/00googleplay/";

        String path_package = "";
        String path_new = "";

        //todo:
        Connection.Response res = null;
        Document doc = null;
        Boolean OK = true;
        int start = 0;

        try {   //first connection with GET request

            int count = 0;
            List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path_package + file1, count, 0, 1);
            List<String> packageList = ReadExcelFile_1Column.readColumnAsString(path_package + file1, count, 1, 1);
            ArrayList< Object[]> allobj = new ArrayList<Object[]>();
            datas = new Object[]{"Repos", "Packages", "GP-upd-Date", "Downloads", "Star1", "Star2", "Star3", "Star4", "Star5", "AvgStars","Total" ,"Size","Developer", "Category", "Description"};
            for (int a = 0; a < nameList.size(); a++) {
                if (a == 0) {
                    allobj.add(datas);
                }
                res = readUrl("https://play.google.com/store/apps/details?id=" + packageList.get(a) + "&hl=en).get()");
                if (res != null) {

                    doc = res.parse();
                    Elements link = doc.select("h1.document-title");
                    Elements app_info = doc.select("div.details-info");
                    String appIcon = app_info.select("img.cover-image").attr("src"); // App Icon url
                    String category = Jsoup.parse(app_info.select("a.category").attr("href").toString()).text();

                    Elements metaElement = doc.select("div.meta-info");

                    String updated = doc.getElementsByAttributeValue("class", "htlgb").text();
                    String installs = doc.getElementsByAttributeValue("class", "htlgb").text();
                    String size = doc.getElementsByAttributeValue("class", "htlgb").text();
                    String versions = doc.getElementsByAttributeValue("class", "htlgb").text();
                    String genre = doc.getElementsByAttributeValue("class", "genre").text();
                    String op_system = doc.getElementsByAttributeValue("class", "htlgb").text();
                    String avgstars = doc.getElementsByAttributeValue("class", "BHMmbe").text();
                    String cat = doc.getElementsByAttributeValue("class", "hrTbp R8zArc").text();
                    String totl = doc.getElementsByAttributeValue("class", "").text();
                    String descriptions = "";
                    if (doc.getElementsByAttributeValue("name", "description").attr("content") != null) {
                        descriptions = doc.getElementsByAttributeValue("name", "description").attr("content");
                    }
                    Elements addition_element = doc.getElementsByAttributeValue("class", "htlgb");
                    List<String> addL = new ArrayList<>();
                    ////
                    for (Element element : addition_element) {
                        String text = element.text();
                        if (text.length() > 0 || !text.equals("")) {
                            addL.add(element.text());
                        }
                    }
                    avgstars = avgstars.replaceAll(",", "");
                    Elements stars_element = doc.getElementsByAttributeValue("class", "Gn2mNd");
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
                    String dev = "";
                    if (addL.size() > 18) {
                        dev = addL.get(18);
                        cat = cat.replaceAll(dev, "");
                    }
                    
                    try{
                    //System.out.println(nameList.size()+"\t"+packageList.size()+"\t"+commits.size()+"\t"+unique.size()+"\t"+firstList.size()+"\t"+lastList.size());
                    datas = new Object[]{nameList.get(a), packageList.get(a), addL.get(0), addL.get(4), str[4], str[3], str[2], str[1], str[0], AvgStars,totl, addL.get(2),dev, cat, descriptions};
                    allobj.add(datas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    String f_name = "gp_statistics_final2.xlsx";
                     Create_Excel.createExcelSheet(allobj, path_new + f_name, "gp_statistics2");
                }
            }
        } catch (Exception ex) {
            //Do some exception handling here
            ex.printStackTrace();
        }
        // }
    }

    private static Connection.Response readUrl(String url) {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(url + "\t not found on googleplay..!");
            return res;
        }
        return res;
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
