package edu.gatech.seclass.gradescalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Grades {

	XSSFWorkbook workbook;
	XSSFSheet[] sheets;
	FileInputStream fileIn;
	FileOutputStream fileOut;
	Grades grades;
	static final String GRADES_DB = "DB" + File.separator + "GradesDatabase6300-grades.xlsx";
    
	
	public Grades(String db) {
		
		try {
			//Get XLSX file
            fileIn = new FileInputStream(new File(db));
             
            //Get the workbook instance for XLSX file 
            workbook = new XSSFWorkbook(fileIn);
            sheets = new XSSFSheet[workbook.getNumberOfSheets()];
            
            //Get the sheets in workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            	sheets[i] = workbook.getSheetAt(i);
            }  
            fileIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
	}
	
	
	
}
