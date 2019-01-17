/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;


import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author john
 */
public class Clone_Repos {
    public static void main(String[] args) throws Exception {
        cloning();
    }
    private static void cloning() throws Exception {
        Object[] datas = null;
        String file_google = "googleplay_-1_500.xlsx";
        String path_google = "/Users/john/Desktop/DESKTOP/Files/00deleted_commits/00Cleaned_final/";

        String path_downloads = "/Users/john/Desktop/DESKTOP/Files/00deleted_commits/downloads/";
        String path_excel = "/Users/john/Desktop/DESKTOP/Files/00deleted_commits/downloads/";
        //todo:      
        List<String> reposList = pick(path_google + file_google, 0, 0, 1);
        ArrayList< Object[]> allobj = new ArrayList<Object[]>();
        datas = new Object[]{"Project", "Zip", "Size"};
        allobj.add(datas);

        for (int i = 0; i < 1; i++) {
            String message = downloads(reposList.get(i), path_downloads);
            String zip_name = reposList.get(i).split("/")[0] + "_" + reposList.get(i).split("/")[1] + ".zip";
            System.out.println(i + " : " + message);
            if (!message.equals("Downloads Failed")) {
                datas = new Object[]{reposList.get(i), zip_name, ""};
                allobj.add(datas);
            }
        }
        String f_name = file_google.replaceAll("googleplay_", "zip_files_");
        Create_Excel.createExcel(allobj, 0, path_excel + f_name, "zip_files");
    }
    /**
     * 
     * @param project - the reponame e.g., bitcoin-wallet/bitcoin-wallet
     * @param downloads_path where the clone will be saved on your PC
     * @return message to showing either download success or failure
     */
    public static String downloads(String project, String downloads_path) {
        String file_name = downloads_path + project.split("/")[0] + "_" + project.split("/")[1] + ".zip";
        String message = project + " \t downloads success!...";
        try {
            Path path = Paths.get(file_name);
            URI u = URI.create("https://api.github.com/repos/" + project + "/zipball/master");
            try (InputStream in = u.toURL().openStream()) {
                Files.copy(in, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Downloads Failed";
        }
        return message;
    }
    
    public static ArrayList<String> pick(String file, int loop, int position, int next) throws Exception {

        int j, x;
        // array list to store the forks
        ArrayList<String> lists = new ArrayList<String>();
        //calling the file name.....
        XSSFWorkbook workbook = File_Details.readFileName(file);
        x = loop;// setting the sheet number...
        XSSFSheet spreadsheet = workbook.getSheetAt(x);
        String sname = workbook.getSheetName(x);

        Row row;
        Cell cell = null;
        for (j = next; j < spreadsheet.getLastRowNum() + 1; ++j) {//To loop thru the rows in a sheet
            row = spreadsheet.getRow(j);
            cell = row.getCell(position); //forks are in the eighth column...
            if (cell != null) {
                switch (cell.getCellType()) {
                    //Checking for strings values inthe cells..
                    case Cell.CELL_TYPE_STRING:
                        //if (!cell.getStringCellValue().equals("") && !cell.getStringCellValue().equals(type)) {
                            // adding the call value to the arraylist called forksList 
                            lists.add(cell.getStringCellValue());
                            //System.out.println(cell.getStringCellValue());
                        ///}//end of if statement...
                        break;
                    //Checking for numeric values inthe cells..
                    case Cell.CELL_TYPE_NUMERIC:
                        //lists.add( String.valueOf(cell.getNumericCellValue()) );
                        break;
                    //Checking for bank in the cells..
                    case Cell.CELL_TYPE_BLANK:
                        break;
                }//end of switch statement
            }

        }// end of  for loop for the rows..

        //returns the arraylist to the main class....
        return lists;
    }
}
