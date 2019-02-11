/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opm.sources.more;

import com.opm.variability.core.Call_URL;
import com.opm.variability.core.DateOperations;
import com.opm.variability.core.JSONUtils;
import com.opm.variability.excel_.Create_Excel;
import com.opm.variability.excel_.ReadExcelFile_1Column;
import com.opm.variability.reads.Commits;
import com.opm.variability.util.Constants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author john
 */
public class Collection_CommitsFileDetails_Total_and_Unique {

    public static void main(String[] args) throws Exception {
        collect();
    }

    private static void collect() throws Exception {
        ///String toDay = "2017-07-06T00:00:00Z";
        Object[] datas = null;
        String[] tokens = Constants.getToken();
        String file_ = "file_github_file_latest.xlsx";

        String path = "";
        String path_new = "";
        int ct = 0;

        List<String> projectL = ReadExcelFile_1Column.readColumnAsString(path + file_, 0, 0, 1);
        List<String> nameList = ReadExcelFile_1Column.readColumnAsString(path + file_, 0, 1, 1);
        List<String> createL = ReadExcelFile_1Column.readColumnAsString(path + file_, 0, 2, 1);

        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        ArrayList< Object[]> allobj2 = new ArrayList<Object[]>();
       
        datas = new Object[]{"MLP", "FP", "Created_at", "COM", "Total_file", "Unique-files", "", "Java", "Java-Unique", "", "Added_J", "Removed_J", "Modified_J", "Renamed_J", "Unknown_J", "", "Activities", "Services", "Boadcast", "Utils", "Fragments", "Content-Provider", "Others", "", "files", "", "Act_Added", "Act_Removed", "Act_Modified", "Act_Renamed", "Act_unknown", "", "Frag_Added", "Frag_Removed", "Frag_Modified", "Frag_Renamed", "Frag_unknown", "", "Serv_Added", "Serv_Removed", "Serv_Modified", "Serv_Renamed", "Serv_unknown", "", "Brod_Added", "Brod_Removed", "Brod_Modified", "Brod_Renamed", "Brod_unknown", "", "Cont_Added", "Cont_Removed", "Cont_Modified", "Cont_Renamed", "Cont_unknown", "", "Others_Added", "Others_Removed", "Others_Modified", "Others_Renamed", "Others_unknown","","Added_ALL", "Removed_ALL", "Modified_ALL","Renamed_ALL","Unknown_ALL","","Added_ALL_UN", "Removed_ALL_UN", "Modified_ALL_UN","Renamed_ALL_UN","Unknown_ALL_UN"
        };// end of assigning the header to the object..
        allobj.add(datas);

        datas = new Object[]{"MLP", "FP", "Created_at", "Unique", "Total_file", "Unique-files", "", "Java", "Java-Unique", "", "Added_J", "Removed_J", "Modified_J", "Renamed_J", "Unknown_J", "", "Activities", "Services", "Boadcast", "Utils", "Fragments", "Content-Provider", "Others", "", "files", "", "Act_Added", "Act_Removed", "Act_Modified", "Act_Renamed", "Act_unknown", "", "Frag_Added", "Frag_Removed", "Frag_Modified", "Frag_Renamed", "Frag_unknown", "", "Serv_Added", "Serv_Removed", "Serv_Modified", "Serv_Renamed", "Serv_unknown", "", "Brod_Added", "Brod_Removed", "Brod_Modified", "Brod_Renamed", "Brod_unknown", "", "Cont_Added", "Cont_Removed", "Cont_Modified", "Cont_Renamed", "Cont_unknown", "", "Others_Added", "Others_Removed", "Others_Modified", "Others_Renamed", "Others_unknown","","Added_ALL", "Removed_ALL", "Modified_ALL","Renamed_ALL","Unknown_ALL","","Added_ALL_UN", "Removed_ALL_UN", "Modified_ALL_UN","Renamed_ALL_UN","Unknown_ALL_UN"
        };// end of assigning the header to the object..
        allobj2.add(datas);
        Set<String> projSet = new HashSet<>();
        projSet.addAll(projectL);
        List<String> projList = new ArrayList<>();
        projList.addAll(projSet);
        List<String> minDAteList = new ArrayList<>();

        for (int a = 0; a < projList.size(); a++) {

            List<String> createdAtList = new ArrayList<>();
            for (int b = 0; b < projectL.size(); b++) {
                if (projList.get(a).equals(projectL.get(b))) {
                    createdAtList.add(createL.get(b));
                }
            }
            String minDate = DateOperations.sorts(createdAtList, createdAtList).split("/")[0];
            minDAteList.add(minDate);
        }

        for (int a = 46; a < projList.size(); a++) {
            System.out.println(a + "  MLP: " + projList.get(a));

            List<List<String>> allList_1 = Commits.count(projList.get(a), "mlp", minDAteList.get(a), Constants.cons.TODAY_DATE, tokens, ct);
            List<String> shaList_1 = allList_1.get(0);
            List<String> dateList_1 = allList_1.get(1);
            List<String> messageList_1 = allList_1.get(2);
            List<String> modelList_1 = allList_1.get(allList_1.size() - 1);
            ct = Integer.parseInt(modelList_1.get(modelList_1.size() - 1));

            for (int b = 0; b < projectL.size(); b++) {
                if (projList.get(a).equals(projectL.get(b))) {
                    System.out.println("    " + b + " : " + nameList.get(b));
                    List<List<String>> allList = Commits.count(nameList.get(b), "fp", createL.get(b), Constants.cons.TODAY_DATE, Constants.getToken(), ct);
                    List<String> modelL = allList.get(allList.size() - 1);
                    ct = Integer.parseInt(modelL.get(modelL.size() - 1));
                    List<String> shaList_3 = allList.get(0);
                    List<String> dateList_3 = allList.get(1);
                    List<String> messageList_3 = allList.get(2);
                    List<String> modelList_3 = allList.get(allList.size() - 1);
                    ct = Integer.parseInt(modelList_3.get(modelList_3.size() - 1));

                    double Activity_TOT = 0, Fragment_TOT = 0, Service_TOT = 0, Broadcast_TOT = 0, content_provder_TOT = 0, Others_TOT = 0;
                    double Act_Added_TOT = 0, Act_Removed_TOT = 0, Act_Modified_TOT = 0, Act_Renamed_TOT = 0, Act_unknown_TOT = 0;
                    double Frag_Added_TOT = 0, Frag_Removed_TOT = 0, Frag_Modified_TOT = 0, Frag_Renamed_TOT = 0, Frag_unknown_TOT = 0;
                    double Serv_Added_TOT = 0, Serv_Removed_TOT = 0, Serv_Modified_TOT = 0, Serv_Renamed_TOT = 0, Serv_unknown_TOT = 0;

                    double Brod_Added_TOT = 0, Brod_Removed_TOT = 0, Brod_Modified_TOT = 0, Brod_Renamed_TOT = 0, Brod_unknown_TOT = 0;
                    double Cont_Added_TOT = 0, Cont_Removed_TOT = 0, Cont_Modified_TOT = 0, Cont_Renamed_TOT = 0, Cont_unknown_TOT = 0;
                    double Other_Added_TOT = 0, Other_Removed_TOT = 0, Other_Modified_TOT = 0, Other_Renamed_TOT = 0, Other_unknown_TOT = 0;

                    double Added_J_TOT = 0, Removed_J_TOT = 0, Modified_J_TOT = 0, Renamed_J_TOT = 0, Unknown_J_TOT = 0;
                    double allFile_TOT = 0, allJavaFile_TOT = 0, allUnique_TOT = 0,allUnique_ALL_TOT = 0;
                    String Allfile_string_TOT = "";
                    
                     double Added_All_TOT = 0, Removed_All_TOT = 0, Modified_All_TOT = 0, Renamed_All_TOT = 0, Unknown_All_TOT = 0;
                        double Added_All_Un_TOT = 0, Removed_All_Un_TOT = 0, Modified_All_Un_TOT = 0, Renamed_All_Un_TOT = 0, Unknown_All_Un_TOT = 0;


                    ///UNIQUE FILE STATISTICS
                    double Activity_UN = 0, Fragment_UN = 0, Service_UN = 0, Broadcast_UN = 0, content_provder_UN = 0, Others_UN = 0;
                    double Act_Added_UN = 0, Act_Removed_UN = 0, Act_Modified_UN = 0, Act_Renamed_UN = 0, Act_unknown_UN = 0;
                    double Frag_Added_UN = 0, Frag_Removed_UN = 0, Frag_Modified_UN = 0, Frag_Renamed_UN = 0, Frag_unknown_UN = 0;
                    double Serv_Added_UN = 0, Serv_Removed_UN = 0, Serv_Modified_UN = 0, Serv_Renamed_UN = 0, Serv_unknown_UN = 0;

                    double Brod_Added_UN = 0, Brod_Removed_UN = 0, Brod_Modified_UN = 0, Brod_Renamed_UN = 0, Brod_unknown_UN = 0;
                    double Cont_Added_UN = 0, Cont_Removed_UN = 0, Cont_Modified_UN = 0, Cont_Renamed_UN = 0, Cont_unknown_UN = 0;
                    double Other_Added_UN = 0, Other_Removed_UN = 0, Other_Modified_UN = 0, Other_Renamed_UN = 0, Other_unknown_UN = 0;

                    double Added_J_UN = 0, Removed_J_UN = 0, Modified_J_UN = 0, Renamed_J_UN = 0, Unknown_J_UN = 0;
                    double allFile_UN = 0, allJavaFile_UN = 0, allUnique_UN = 0,allUnique_ALL_UN = 0;
                    
                     double Added_All_UN = 0, Removed_All_UN = 0, Modified_All_UN = 0, Renamed_All_UN = 0, Unknown_All_UN = 0;
                        double Added_All_Un_UN = 0, Removed_All_Un_UN = 0, Modified_All_Un_UN = 0, Renamed_All_Un_UN = 0, Unknown_All_Un_UN = 0;

                    String Allfile_string_UN = "";
                    double UNIQUE = 0;
                    List<String> uniqueFile_list = new ArrayList<>();
                    
                    for (int c = 0; c < shaList_3.size(); c++) {

                        if (c%1000 == 0) {
                            System.out.println("         "+c);
                        }
                        double Activity = 0, Fragment = 0, Service = 0, Broadcast = 0, content_provder = 0, Others = 0;
                        double Act_Added = 0, Act_Removed = 0, Act_Modified = 0, Act_Renamed = 0, Act_unknown = 0;
                        double Frag_Added = 0, Frag_Removed = 0, Frag_Modified = 0, Frag_Renamed = 0, Frag_unknown = 0;
                        double Serv_Added = 0, Serv_Removed = 0, Serv_Modified = 0, Serv_Renamed = 0, Serv_unknown = 0;

                        double Brod_Added = 0, Brod_Removed = 0, Brod_Modified = 0, Brod_Renamed = 0, Brod_unknown = 0;
                        double Cont_Added = 0, Cont_Removed = 0, Cont_Modified = 0, Cont_Renamed = 0, Cont_unknown = 0;
                        double Other_Added = 0, Other_Removed = 0, Other_Modified = 0, Other_Renamed = 0, Other_unknown = 0;

                        double Added_J = 0, Removed_J = 0, Modified_J = 0, Renamed_J = 0, Unknown_J = 0;
                        double allFile = 0, allJavaFile = 0, allUnique = 0,allUnique_ALL = 0;
                        
                        double Added_All = 0, Removed_All = 0, Modified_All = 0, Renamed_All = 0, Unknown_All = 0;
                        double Added_All_Un = 0, Removed_All_Un = 0, Modified_All_Un = 0, Renamed_All_Un = 0, Unknown_All_Un = 0;
                        
                        String Allfile_string = "";

                        if (ct == (tokens.length)) {/// the the index for the tokens array...
                            ct = 0; //// go back to the first index......
                        }
                        String jsonString = Call_URL.callURL("https://api.github.com/repos/" + nameList.get(b) + "/commits/" + shaList_3.get(c) + "?access_token=" + tokens[ct++]);
                        JSONParser parser = new JSONParser();

                        if (JSONUtils.isValidJSONObject(jsonString) == true) {
                            JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
                            if ((JSONArray) jSONObject.get("files") != null) {
                                JSONArray fileObj = (JSONArray) jSONObject.get("files");
                                for (int i = 0; i < fileObj.size(); i++) {
                                    JSONObject fileOBJ = (JSONObject) fileObj.get(i);
                                    String fileName = (String) fileOBJ.get("filename");
                                    String contents_url = (String) fileOBJ.get("contents_url");
                                            long additions = (long) fileOBJ.get("additions");
                                            long deletions = (long) fileOBJ.get("deletions");
                                            String status = (String) fileOBJ.get("status");
                                    allFile++;
                                     if (status.toLowerCase().equals("added")) {
                                                Added_All++;
                                            } else if (status.toLowerCase().equals("removed")) {
                                                Removed_All++;
                                            } else if (status.toLowerCase().equals("modified")) {
                                                Modified_All++;
                                            } else if (status.toLowerCase().equals("renamed")) {
                                                Renamed_All++;
                                            } else {
                                                Unknown_All++;
                                            }

                                    if (fileName.contains(".java")) {
                                        allJavaFile++;
                                    }
                                    if (!uniqueFile_list.contains(fileName)) {
                                        uniqueFile_list.add(fileName);
                                        allUnique_ALL ++;
                                        
                                        if (status.toLowerCase().equals("added")) {
                                                Added_All_Un++;
                                            } else if (status.toLowerCase().equals("removed")) {
                                                Removed_All_Un++;
                                            } else if (status.toLowerCase().equals("modified")) {
                                                Modified_All_Un++;
                                            } else if (status.toLowerCase().equals("renamed")) {
                                                Renamed_All_Un++;
                                            } else {
                                                Unknown_All_Un++;
                                            }
                                        
                                        
                                        if (fileName.contains(".java")) {
                                            allUnique++;
                                            Allfile_string = Allfile_string.concat(fileName + " , ");
                                            

                                            if (status.toLowerCase().equals("added")) {
                                                Added_J++;
                                            } else if (status.toLowerCase().equals("removed")) {
                                                Removed_J++;
                                            } else if (status.toLowerCase().equals("modified")) {
                                                Modified_J++;
                                            } else if (status.toLowerCase().equals("renamed")) {
                                                Renamed_J++;
                                            } else {
                                                Unknown_J++;
                                            }

                                            if (ct == (tokens.length)) {/// the the index for the tokens array...
                                                ct = 0; //// go back to the first index......
                                            }
                                            String url_string = Call_URL.callURL(contents_url + "&access_token=" + tokens[ct++]);
                                            //System.out.println(contents_url);
                                            if (JSONUtils.isValidJSONObject(url_string)) {
                                                JSONObject url_obj = (JSONObject) parser.parse(url_string);
                                                if (ct == (tokens.length)) {/// the the index for the tokens array...
                                                    ct = 0; //// go back to the first index......
                                                }
                                                String download_url = (String) url_obj.get("download_url");
                                                String file_java = Call_URL.callURL(download_url + "?access_token=" + tokens[ct++]);
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
                                                    if (Integer.parseInt(file.split("/")[5]) > 0) {
                                                        if (status.toLowerCase().equals("added")) {
                                                            Other_Added++;
                                                        } else if (status.toLowerCase().equals("removed")) {
                                                            Other_Removed++;
                                                        } else if (status.toLowerCase().equals("modified")) {
                                                            Other_Modified++;
                                                        } else if (status.toLowerCase().equals("renamed")) {
                                                            Other_Renamed++;
                                                        } else {
                                                            Other_unknown++;
                                                        }
                                                    }

                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        }

                        //TOTAL FILE STATISTICS
                        Allfile_string_TOT = Allfile_string_TOT.concat(Allfile_string + " , ");
                        Activity_TOT += Activity;
                        Fragment_TOT += Fragment;
                        Service_TOT += Service;
                        Broadcast_TOT += Broadcast;
                        content_provder_TOT += content_provder;
                        Others_TOT += Others;
                        Act_Added_TOT += Act_Added;
                        Act_Removed_TOT += Act_Removed;
                        Act_Modified_TOT += Act_Modified;
                        Act_Renamed_TOT += Act_Renamed;
                        Act_unknown_TOT += Act_unknown;
                        Frag_Added_TOT += Frag_Added;
                        Frag_Removed_TOT += Frag_Removed;
                        Frag_Modified_TOT += Frag_Modified;
                        Frag_Renamed_TOT += Frag_Renamed;
                        Frag_unknown_TOT += Frag_unknown;
                        Serv_Added_TOT += Serv_Added;
                        Serv_Removed_TOT += Serv_Removed;
                        Serv_Modified_TOT += Serv_Modified;
                        Serv_Renamed_TOT += Serv_Renamed;
                        Serv_unknown_TOT += Serv_unknown;
                        Brod_Added_TOT += Brod_Added;
                        Brod_Removed_TOT += Brod_Removed;
                        Brod_Modified_TOT += Brod_Modified;
                        Brod_Renamed_TOT += Brod_Renamed;
                        Brod_unknown_TOT += Brod_unknown;
                        Cont_Added_TOT += Cont_Added;
                        Cont_Removed_TOT += Cont_Removed;
                        Cont_Modified_TOT += Cont_Modified;
                        Cont_Renamed_TOT += Cont_Renamed;
                        Cont_unknown_TOT += Cont_unknown;
                        Other_Added_TOT += Other_Added;
                        Other_Removed_TOT += Other_Removed;
                        Other_Modified_TOT += Other_Modified;
                        Other_Renamed_TOT += Other_Renamed;
                        Other_unknown_TOT += Other_unknown;

                        Added_J_TOT += Added_J;
                        Removed_J_TOT += Removed_J;
                        Modified_J_TOT += Modified_J;
                        Renamed_J_TOT += Renamed_J;
                        Unknown_J_TOT += Unknown_J;
                        allFile_TOT += allFile;
                        allJavaFile_TOT += allJavaFile;
                        allUnique_TOT += allUnique;
                        allUnique_ALL_TOT += allUnique_ALL;
                        
                        Added_All_TOT += Added_All;
                        Removed_All_TOT += Removed_All;
                        Modified_All_TOT += Modified_All;
                        Renamed_All_TOT += Renamed_All;
                        Unknown_All_TOT += Unknown_All;
                        Added_All_Un_TOT += Added_All_Un;
                        Removed_All_Un_TOT += Removed_All_Un;
                        Modified_All_Un_TOT += Modified_All_Un;
                        Renamed_All_Un_TOT += Renamed_All_Un;
                        Unknown_All_Un_TOT += Unknown_All_Un;


                        ///GETTING THE UNIQUE STATISTICS FROM HERE....
                        if (!shaList_1.contains(shaList_3.get(c))) {

                            UNIQUE ++;
                            Allfile_string_UN = Allfile_string_UN.concat(Allfile_string + " , ");
                            Activity_UN += Activity;
                            Fragment_UN += Fragment;
                            Service_UN += Service;
                            Broadcast_UN += Broadcast;
                            content_provder_UN += content_provder;
                            Others_UN += Others;
                            Act_Added_UN += Act_Added;
                            Act_Removed_UN += Act_Removed;
                            Act_Modified_UN += Act_Modified;
                            Act_Renamed_UN += Act_Renamed;
                            Act_unknown_UN += Act_unknown;
                            Frag_Added_UN += Frag_Added;
                            Frag_Removed_UN += Frag_Removed;
                            Frag_Modified_UN += Frag_Modified;
                            Frag_Renamed_UN += Frag_Renamed;
                            Frag_unknown_UN += Frag_unknown;
                            Serv_Added_UN += Serv_Added;
                            Serv_Removed_UN += Serv_Removed;
                            Serv_Modified_UN += Serv_Modified;
                            Serv_Renamed_UN += Serv_Renamed;
                            Serv_unknown_UN += Serv_unknown;

                            Brod_Added_UN += Brod_Added;
                            Brod_Removed_UN += Brod_Removed;
                            Brod_Modified_UN += Brod_Modified;
                            Brod_Renamed_UN += Brod_Renamed;
                            Brod_unknown_UN += Brod_unknown;
                            Cont_Added_UN += Cont_Added;
                            Cont_Removed_UN += Cont_Removed;
                            Cont_Modified_UN += Cont_Modified;
                            Cont_Renamed_UN += Cont_Renamed;
                            Cont_unknown_UN += Cont_unknown;
                            Other_Added_UN += Other_Added;
                            Other_Removed_UN += Other_Removed;
                            Other_Modified_UN += Other_Modified;
                            Other_Renamed_UN += Other_Renamed;
                            Other_unknown_UN += Other_unknown;

                            Added_J_UN += Added_J;
                            Removed_J_UN += Removed_J;
                            Modified_J_UN += Modified_J;
                            Renamed_J_UN += Renamed_J;
                            Unknown_J_UN += Unknown_J;
                            allFile_UN += allFile;
                            allJavaFile_UN += allJavaFile;
                            allUnique_UN += allUnique;
                            allUnique_ALL_UN += allUnique_ALL;
                            
                            Added_All_UN += Added_All;
                        Removed_All_UN += Removed_All;
                        Modified_All_UN += Modified_All;
                        Renamed_All_UN += Renamed_All;
                        Unknown_All_UN += Unknown_All;
                        Added_All_Un_UN += Added_All_Un;
                        Removed_All_Un_UN += Removed_All_Un;
                        Modified_All_Un_UN += Modified_All_Un;
                        Renamed_All_Un_UN += Renamed_All_Un;
                        Unknown_All_Un_UN += Unknown_All_Un;
                        }
                    }

                    datas = new Object[]{projectL.get(b), nameList.get(b), createL.get(b), Double.parseDouble(shaList_3.size() + ""), allFile_TOT, allUnique_ALL_TOT, "", allJavaFile_TOT, allUnique_TOT, "", Added_J_TOT, Removed_J_TOT, Modified_J_TOT, Renamed_J_TOT, Unknown_J_TOT, "", Activity_TOT, Service_TOT, Broadcast_TOT, "", Fragment_TOT, content_provder_TOT, Others_TOT, "", Allfile_string_TOT, "", Act_Added_TOT, Act_Removed_TOT, Act_Modified_TOT, Act_Renamed_TOT, Act_unknown_TOT, "", Frag_Added_TOT, Frag_Removed_TOT, Frag_Modified_TOT, Frag_Renamed_TOT, Frag_unknown_TOT, "", Serv_Added_TOT, Serv_Removed_TOT, Serv_Modified_TOT, Serv_Renamed_TOT, Serv_unknown_TOT, "", Brod_Added_TOT, Brod_Removed_TOT, Brod_Modified_TOT, Brod_Renamed_TOT, Brod_unknown_TOT, "", Cont_Added_TOT, Cont_Removed_TOT, Cont_Modified_TOT, Cont_Renamed_TOT, Cont_unknown_TOT, "", Other_Added_TOT, Other_Removed_TOT, Other_Modified_TOT, Other_Renamed_TOT, Other_unknown_TOT,"",Added_All_TOT,Removed_All_TOT,Modified_All_TOT,Renamed_All_TOT,Unknown_All_TOT,"",Added_All_Un_TOT,Removed_All_Un_TOT,Modified_All_Un_TOT,Renamed_All_Un_TOT,Unknown_All_Un_TOT
                    };// end of assigning the header to the object..
                    allobj.add(datas);

                    String f_name = "File_Total_Unique_Statistics_two_sheets222.xlsx";
                    Create_Excel.createExcelSheet(allobj, path_new + f_name, "total_statistic_two");

                    datas = new Object[]{projectL.get(b), nameList.get(b), createL.get(b), UNIQUE, allFile_UN, allUnique_ALL_UN, "", allJavaFile_UN, allUnique_UN, "", Added_J_UN, Removed_J_UN, Modified_J_UN, Renamed_J_UN, Unknown_J_UN, "", Activity_UN, Service_UN, Broadcast_UN, "", Fragment_UN, content_provder_UN, Others_UN, "", Allfile_string_UN, "", Act_Added_UN, Act_Removed_UN, Act_Modified_UN, Act_Renamed_UN, Act_unknown_UN, "", Frag_Added_UN, Frag_Removed_UN, Frag_Modified_UN, Frag_Renamed_UN, Frag_unknown_UN, "", Serv_Added_UN, Serv_Removed_UN, Serv_Modified_UN, Serv_Renamed_UN, Serv_unknown_UN, "", Brod_Added_UN, Brod_Removed_UN, Brod_Modified_UN, Brod_Renamed_UN, Brod_unknown_UN, "", Cont_Added_UN, Cont_Removed_UN, Cont_Modified_UN, Cont_Renamed_UN, Cont_unknown_UN, "", Other_Added_UN, Other_Removed_UN, Other_Modified_UN, Other_Renamed_UN, Other_unknown_UN,"","",Added_All_UN,Removed_All_UN,Modified_All_UN,Renamed_All_UN,Unknown_All_UN,"",Added_All_Un_UN,Removed_All_Un_UN,Modified_All_Un_UN,Renamed_All_Un_UN,Unknown_All_Un_UN
                    };// end of assigning the header to the object..
                    allobj2.add(datas);
                    Create_Excel.createExcelSheet(allobj2, path_new + f_name, "unique_statistic_two");

                }
            }

        }

    }
}
