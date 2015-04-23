package edu.gatech.seclass.gradescalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Students {

	XSSFWorkbook workbook;
	XSSFSheet[] sheets;
	FileInputStream file;
	
	public Students(String db) {
		
		try {
			//Get XLSX file
            file = new FileInputStream(new File(db));
             
            //Get the workbook instance for XLSX file 
            workbook = new XSSFWorkbook(file);
            sheets = new XSSFSheet[workbook.getNumberOfSheets()];
            //Get the sheets in workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            	sheets[i] = workbook.getSheetAt(i);
            }  
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}
}
