/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.more;


import com.opm.variability.core.Call_URL;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class ReadFilesMain {

    public static void main(String[] args) throws Exception {
        collect();
    }

    private static void collect() throws Exception {
        ///String toDay = "2017-07-06T00:00:00Z";
        Object[] datas = null;
        String file1 = "file_github_file_latest.xlsx";
        String file2 = "PR_Projects_File_Final2_101_200.xlsx";
        String file3 = "PR_Projects_File_Final2_201_300.xlsx";
        String file4 = "PR_Projects_File_Final2_301_362.xlsx";

        //String path = "/Users/john/Documents/Destope_data_2018-05_18/00NewDatasets/";
        //String path_new = "/Users/john/Documents/Destope_data_2018-05_18/00NewDatasets/variability-extension/";
        String path = "";
        String path_new = "";

        int ct = 0;

        String[] FILES = {file1};

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"MLP", "FP", "Created_at", "COM", "Unique", "Total_file", "Unique-files", "", "Java", "Activities", "Services", "Boadcast", "Utils", "Fragments", "Content-Provider", "Others", "", "total", "files", "", "Act_Added", "Act_Removed", "Act_Modified", "Act_Renamed", "Act_unknown","", "Frag_Added", "Frag_Removed", "Frag_Modified", "Frag_Renamed", "Frag_unknown","", "Serv_Added","Serv_Removed", "Serv_Modified", "Serv_Renamed", "Serv_unknown", "", "Brod_Added", "Brod_Removed", "Brod_Modified", "Brod_Renamed", "Brod_unknown", "", "Cont_Added", "Cont_Removed", "Cont_Modified", "Cont_Renamed", "Cont_unknown"
        };// end of assigning the header to the object..
        allobj.add(datas);
        for (int a = 0; a < FILES.length; a++) {
            List<String> projectL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], 0, 0, 1);
            List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], 0, 1, 1);
            List<String> createL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], 0, 2, 1);
            List<Double> commitsL = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], 0, 3, 1);
            List<Double> uniqueL = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], 0, 4, 1);
            List<String> shaaL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], 0, 5, 1);
            List<Double> totalL = ReadExcelFile_1Column.readColumnAsNumeric(path + FILES[a], 0, 7, 1);
            List<String> filesL = ReadExcelFile_1Column.readColumnAsString(path + FILES[a], 0, 8, 1);
            //List<String> filesL = Pick_GeneralNext.pick(path + FILES[a], 0, 8, 1);

            for (int b = 0; b < nameList.size(); b++) {

                System.out.println(b + " : " + nameList.get(b));
                int Activity = 0, Fragment = 0, Service = 0, Broadcast = 0, content_provder = 0, Others = 0;
                int Act_Added = 0, Act_Removed = 0, Act_Modified = 0, Act_Renamed = 0, Act_unknown = 0;
                int Frag_Added = 0, Frag_Removed = 0, Frag_Modified = 0, Frag_Renamed = 0, Frag_unknown = 0;
                int Serv_Added = 0, Serv_Removed = 0, Serv_Modified = 0, Serv_Renamed = 0, Serv_unknown = 0;
              
                int Brod_Added = 0, Brod_Removed = 0, Brod_Modified = 0, Brod_Renamed = 0, Brod_unknown = 0;
                int Cont_Added = 0, Cont_Removed = 0, Cont_Modified = 0, Cont_Renamed = 0, Cont_unknown = 0;
                int Other_Added = 0, Other_Removed = 0, Other_Modified = 0, Other_Renamed = 0, Other_unknown = 0;
                List<String> fileNames = new ArrayList<>();
                String filee = "";
                if (filesL.get(b).contains(" , ")) {

                    String[] splits_ = filesL.get(b).split(" , ");
                    String[] splits2_ = shaaL.get(b).split(" , ");
                    for (int x = 0; x < splits_.length; x++) {

                        String[] splits = splits_[x].split(" ! ");
                        String[] splits2 = splits2_[x].split(" ! ");

                        int flag = 0;
                        System.out.println(splits.length + " :::: " + splits2.length);
                        for (int j = 0; j < splits.length; j++) {
                            if (splits[j].contains(".java")) {
                                flag++;
                            }
                        }

                        if (flag > 0) {
                            if (ct == (Constants.getToken().length)) {/// the the index for the tokens array...
                                ct = 0; //// go back to the first index......
                            }
                            String jsonString = Call_URL.callURL("https://api.github.com/repos/" + nameList.get(b) + "/commits/" + splits2_[x] + "?access_token=" + Constants.getToken()[ct++]);
                            JSONParser parser = new JSONParser();
                            if (JSONUtils.isValidJSONObject(jsonString) == true) {
                                JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
                                if ((JSONArray) jSONObject.get("files") != null) {
                                    JSONArray fileObj = (JSONArray) jSONObject.get("files");
                                    for (int i = 0; i < fileObj.size(); i++) {
                                        JSONObject fileOBJ = (JSONObject) fileObj.get(i);
                                        String fileName = (String) fileOBJ.get("filename");
                                        if (fileName.contains(".java")) {
                                            if (!fileNames.contains(fileName)) {
                                                fileNames.add(fileName);
                                                filee = filee.concat(fileName + " , ");
                                                String contents_url = (String) fileOBJ.get("contents_url");
                                                long additions = (long) fileOBJ.get("additions");
                                                long deletions = (long) fileOBJ.get("deletions");
                                                String status = (String) fileOBJ.get("status");

                                                if (ct == Constants.getToken().length) {/// the the index for the tokens array...
                                                    ct = 0; //// go back to the first index......
                                                }
                                                String url_string = Call_URL.callURL(contents_url + "&access_token=" + Constants.getToken()[ct++]);
                                                //System.out.println(contents_url);
                                                if (JSONUtils.isValidJSONObject(url_string)) {
                                                    JSONObject url_obj = (JSONObject) parser.parse(url_string);
                                                    if (ct == (Constants.getToken().length)) {/// the the index for the tokens array...
                                                        ct = 0; //// go back to the first index......
                                                    }
                                                    String download_url = (String) url_obj.get("download_url");
                                                    String file_java = Call_URL.callURL(download_url + "?access_token=" + Constants.getToken()[ct++]);
                                                    //System.out.println((String) url_obj.get("download_url"));
                                                    String file = Read.read(file_java);

                                                    //p_list2.add(package_name);
                                                    if (file.contains("/")) {
                                                        Activity += Integer.parseInt(file.split("/")[0]);
                                                        if (Integer.parseInt(file.split("/")[0]) > 0) {
                                                            if (status.toLowerCase().equals("added")) {
                                                                Act_Added++;
                                                            } else if (status.toLowerCase().equals("removed")) {
                                                                Act_Removed++;
                                                            } else if (status.toLowerCase().equals("modified")) {
                                                                Act_Modified++;
                                                            } else if (status.toLowerCase().equals("renamed")) {
                                                                Act_Renamed++;
                                                            } else {
                                                                Act_unknown++;
                                                            }
                                                        }
                                                        Fragment += Integer.parseInt(file.split("/")[1]);
                                                        if (Integer.parseInt(file.split("/")[1]) > 0) {
                                                            if (status.toLowerCase().equals("added")) {
                                                                Frag_Added++;
                                                            } else if (status.toLowerCase().equals("removed")) {
                                                                Frag_Removed++;
                                                            } else if (status.toLowerCase().equals("modified")) {
                                                                Frag_Modified++;
                                                            } else if (status.toLowerCase().equals("renamed")) {
                                                                Frag_Renamed++;
                                                            } else {
                                                                Frag_unknown++;
                                                            }
                                                        }
                                                        Service += Integer.parseInt(file.split("/")[2]);
                                                        if (Integer.parseInt(file.split("/")[2]) > 0) {
                                                            if (status.toLowerCase().equals("added")) {
                                                                Serv_Added++;
                                                            } else if (status.toLowerCase().equals("removed")) {
                                                                Serv_Removed++;
                                                            } else if (status.toLowerCase().equals("modified")) {
                                                                Serv_Modified++;
                                                            } else if (status.toLowerCase().equals("renamed")) {
                                                                Serv_Renamed++;
                                                            } else {
                                                                Serv_unknown++;
                                                            }
                                                        }
                                                        Broadcast += Integer.parseInt(file.split("/")[3]);
                                                        if (Integer.parseInt(file.split("/")[3]) > 0) {
                                                            if (status.toLowerCase().equals("added")) {
                                                                Brod_Added++;
                                                            } else if (status.toLowerCase().equals("removed")) {
                                                                Brod_Removed++;
                                                            } else if (status.toLowerCase().equals("modified")) {
                                                                Brod_Modified++;
                                                            } else if (status.toLowerCase().equals("renamed")) {
                                                                Brod_Renamed++;
                                                            } else {
                                                                Brod_unknown++;
                                                            }
                                                        }
                                                        content_provder += Integer.parseInt(file.split("/")[4]);
                                                        if (Integer.parseInt(file.split("/")[4]) > 0) {
                                                            if (status.toLowerCase().equals("added")) {
                                                                Cont_Added++;
                                                            } else if (status.toLowerCase().equals("removed")) {
                                                                Cont_Removed++;
                                                            } else if (status.toLowerCase().equals("modified")) {
                                                                Cont_Modified++;
                                                            } else if (status.toLowerCase().equals("renamed")) {
                                                                Cont_Renamed++;
                                                            } else {
                                                                Cont_unknown++;
                                                            }
                                                        }
                                                        Others += Integer.parseInt(file.split("/")[5]);

                                                    }

                                                }

                                            }
                                        }

                                    }
                                }
                            }

                        }
                    }

                }

                datas = new Object[]{projectL.get(b), nameList.get(b), createL.get(b), commitsL.get(b), uniqueL.get(b), totalL.get(b), "", "", "", Activity, Service, Broadcast, "", Fragment, content_provder, Others, "", fileNames.size(), filee, "", Act_Added, Act_Removed, Act_Modified, Act_Renamed, Act_unknown,"", Frag_Added, Frag_Removed, Frag_Modified, Frag_Renamed, Frag_unknown,"", Serv_Added,Serv_Removed, Serv_Modified, Serv_Renamed, Serv_unknown, "", Brod_Added, Brod_Removed, Brod_Modified, Brod_Renamed, Brod_unknown, "", Cont_Added, Cont_Removed, Cont_Modified, Cont_Renamed, Cont_unknown
                };// end of assigning the header to the object..
                allobj.add(datas);
                String f_name = "File_Github_Details_Latest_final.xlsx";
                Create_Excel.createExcelSheet(allobj, path_new + f_name, "File_Details");

            }

        }
    }
}
