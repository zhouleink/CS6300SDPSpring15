package edu.gatech.seclass.gradescalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class Course {

	private String formula;
	static final String DEFAULT_FORMULA = "ATT * 0.2 + AAG * 0.4 + APG * 0.4";
	static final String GRADES_DB = "DB" + File.separator + "GradesDatabase6300-grades.xlsx";

	private XSSFWorkbook workbook;
	private XSSFSheet studentsInfoSheet;
	private XSSFSheet teamsSheet;
	private XSSFSheet attendanceSheet;
	private XSSFSheet individualGradesSheet;
	private XSSFSheet individualContribsSheet;
	private XSSFSheet teamGradesSheet;

	public Course(Students students, Grades grades) {
		super();
		formula = DEFAULT_FORMULA;
		
		workbook = grades.workbook;
		studentsInfoSheet = students.sheets[0];
		teamsSheet = students.sheets[1];
		attendanceSheet = grades.sheets[0];
		individualGradesSheet = grades.sheets[1];
		individualContribsSheet = grades.sheets[2];
		teamGradesSheet = grades.sheets[3];
	}
	
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	// Count number of students
	public int getNumStudents() {
		int numStudents = 0;
		Iterator<Row> rows = studentsInfoSheet.iterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if (row.getCell(0) != null)
				numStudents++;
		}
		return numStudents;
	}

	// Count number of assignments
	public int getNumAssignments() {
		int numAssignments = 0;
		XSSFRow row = individualGradesSheet.getRow(0); 
		Iterator<Cell> cells = row.iterator();
		while (cells.hasNext()) {
			XSSFCell cell = (XSSFCell) cells.next();
			if (cell.getColumnIndex() == 0) continue;
			if (cell.toString().contains("Assignment"))
				numAssignments++;
		}
		return numAssignments;
	}

	// Count number of projects
	public int getNumProjects() {
		int numProjects = 0;
		XSSFRow row = individualContribsSheet.getRow(0); 
		Iterator<Cell> cells = row.iterator();
		while (cells.hasNext()) {
			XSSFCell cell = (XSSFCell) cells.next();
			if (cell.getColumnIndex() == 0) continue;
			if (cell.toString().contains("Project"))
				numProjects++;
		}
		return numProjects;
	}

	// Create a set of students
	public HashSet<Student> getStudents() {
		HashSet<Student> studentsRoster = new HashSet<Student>();
		Iterator<Row> rows = studentsInfoSheet.iterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if (row.getCell(0) != null) {
				Student student = new Student(row.getCell(0).toString(),
						row.getCell(1).getRawValue().toString());
				studentsRoster.add(student);
			}
		}
		return studentsRoster;
	}

	// Get a student by name
	public Student getStudentByName(String name) {
		Student student = null;
		HashSet<Student> studentsRoster = getStudents();
		for (Student s : studentsRoster) {
			if (s.getName().compareTo(name) == 0) {
				student = s;
				break;
			}
		}
		return student;
	}

	// Get a student by id
	public Student getStudentByID(String id) {
		Student student = null;
		HashSet<Student> studentsRoster = getStudents();
		for (Student s : studentsRoster) {
			if (s.getGtid().compareTo(id) == 0) {
				student = s;
				break;
			}
		}
		return student;
	}

	// Create a set of students' attendance info
	public HashMap<String, Integer> getStudentsAttendance() {

		HashMap<String, Integer> studentsAttendance = new HashMap<String, Integer>();
		Iterator<Row> rows = attendanceSheet.iterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if (row.getCell(0) != null) {
				studentsAttendance.put(row.getCell(0).getRawValue().toString(),
						Integer.parseInt(row.getCell(1).getRawValue().toString()));
			}
		}		
		return studentsAttendance;
	}

	// Create a set of students' team info
	public HashMap<String, String> getStudentsTeam() {
		HashMap<String, String> studentsTeam = new HashMap<String, String>();
		Iterator<Row> rows = teamsSheet.iterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if (row.getCell(0) != null) {
				String team = row.getCell(0).toString();
				Iterator<Cell> cells = row.iterator();
				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					if (cell.getColumnIndex() == 0) continue;
					if (cell != null)
						studentsTeam.put(cell.toString(), team);
				}

			}
		}		
		return studentsTeam;
	}

	// Get a student's attendance information
	public int getAttendance(Student student) {
		HashMap<String, Integer> studentsAttendance = getStudentsAttendance();
		return studentsAttendance.get(student.getGtid());
	}

	// Get a student's team information
	public String getTeam(Student student) {
		HashMap<String, String> studentsTeam = getStudentsTeam();
		return studentsTeam.get(student.getName());
	}
	
	// Add assignment
	public void addAssignment(String assignmentName) {
		XSSFRow assignmentRow = individualGradesSheet.getRow(0);
		int numCells = assignmentRow.getPhysicalNumberOfCells();

		if (assignmentRow.getCell(numCells-1) != null)
			assignmentRow.createCell(numCells).setCellValue(assignmentName);
		else
			assignmentRow.getCell(numCells-1).setCellValue(assignmentName);

		writeExcel();
	}

	// Update the Grades file
	public void updateGrades(Grades updatedGrades) {
	}

	// Update individual assignment grades
	public void addGradesForAssignment(String assignmentName,
			HashMap<Student, Integer> individualGradesMap) {

		XSSFRow assignmentRow = individualGradesSheet.getRow(0);
		int numCells = assignmentRow.getPhysicalNumberOfCells();
		int targetCol = 0;
		// Determine the column that will be updated
		for (int i = 0; i < numCells; i++) {
			if (assignmentRow.getCell(i).toString().matches(assignmentName)) {
				targetCol = i;
				break;
			}				
		}

		Iterator<Student> studentIterator = individualGradesMap.keySet().iterator();
		while (studentIterator.hasNext()) {
			Student student = studentIterator.next();
			int rowNum = findRow(individualGradesSheet, student.getGtid());
			if (rowNum != -1) {
				if (individualGradesSheet.getRow(rowNum).getPhysicalNumberOfCells() <= targetCol)
					individualGradesSheet.getRow(rowNum).createCell(targetCol).setCellValue((int)individualGradesMap.get(student));
				else 
					individualGradesSheet.getRow(rowNum).getCell(targetCol).setCellValue((int)individualGradesMap.get(student));
			}
		}
		writeExcel();
	}

	// Calculate the average assignment grades for a student
	public int getAverageAssignmentsGrade(Student student) {
		double cumGrades = 0;
		int numAssignments = getNumAssignments();
		double averageGrade = 0;

		Iterator<Row> rows = individualGradesSheet.rowIterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if(row.getCell(0).getRawValue().toString().matches(student.getGtid())) {
				Iterator<Cell> cells = row.cellIterator();
				while(cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					if (cell.getColumnIndex() == 0) continue;
					cumGrades += Double.parseDouble(cell.getRawValue());
				}
				averageGrade = cumGrades/numAssignments;
			}
		}
		return (int) Math.round(averageGrade);
	}

	// Update individual project contributions
	public void addIndividualContributions(String projectName,
			HashMap<Student, Integer> individualContribsMap) {
		XSSFRow contributionRow = individualContribsSheet.getRow(0);
		int numCells = contributionRow.getPhysicalNumberOfCells();
		int targetCol = 0;

		for (int i = 0; i < numCells; i++) {
			if (contributionRow.getCell(i).toString().contains(projectName)) {
				targetCol = i;
				break;
			}				
		}

		Iterator<Student> studentIterator = individualContribsMap.keySet().iterator();
		while(studentIterator.hasNext()) {
			Student student = studentIterator.next();
			int rowNum = findRow(individualContribsSheet, student.getGtid());
			if (rowNum != -1) {
				if (individualContribsSheet.getRow(rowNum).getPhysicalNumberOfCells() <= targetCol)
					individualContribsSheet.getRow(rowNum).createCell(targetCol).setCellValue((double)individualContribsMap.get(student));
				else 
					individualContribsSheet.getRow(rowNum).getCell(targetCol).setCellValue((double)individualContribsMap.get(student));
			}
		}

		writeExcel();
	}

	// Calculate average project grades for a student
	public int getAverageProjectsGrade(Student student) {
		double cumGrades = 0;
		int numProjects = getNumProjects();
		double averageGrade = 0;

		int individualContribsRowNum = findRow(individualContribsSheet, student.getGtid());
		int teamGradesRowNum = findRow(teamGradesSheet, getStudentsTeam().get(student.getName()));

		double[] individualContribs = new double[numProjects];
		int[] teamGrades = new int[numProjects];

		if (individualContribsRowNum != -1 && teamGradesRowNum != -1) {

			for (int i = 0; i < numProjects; i++) {
				individualContribs[i] = Double.parseDouble(individualContribsSheet.getRow(individualContribsRowNum).getCell(i+1).getRawValue().toString());
				teamGrades[i] = Integer.parseInt(teamGradesSheet.getRow(teamGradesRowNum).getCell(i+1).getRawValue().toString());

				cumGrades += (individualContribs[i] * 1.0 * teamGrades[i] / 100.0);
			}		
			averageGrade = cumGrades/numProjects;
		}
		return ((int) Math.round(averageGrade));
	}

	// Find the row number of a target
	public int findRow(XSSFSheet sheet, String key) {
		int rowNum = -1;
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			XSSFCell firstCell = row.getCell(0);
			switch (firstCell.getCellType()) {
			case 0:
				if (firstCell.getRawValue().toString().matches(key))
					rowNum = row.getRowNum();
				break;
			case 1:
				if (firstCell.toString().matches(key))
					rowNum = row.getRowNum();
				break;
			default:
				break;
			}
		}
		return rowNum;
	}

	// Write the updated info to Excel file
	public void writeExcel() {
		try {
			FileOutputStream fileOut = new FileOutputStream(GRADES_DB);
			workbook.write(fileOut);
			fileOut.close();
			workbook = new XSSFWorkbook(new FileInputStream(new File(GRADES_DB)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	// Create a set of students' Email information
	public HashMap<String, String> getStudentsEmail() {
		HashMap<String, String> studentsEmail = new HashMap<String, String>();
		Iterator<Row> rows = studentsInfoSheet.iterator();
		while (rows.hasNext()) {
			XSSFRow row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) continue;
			if (row.getCell(1) != null) {
				studentsEmail.put(row.getCell(1).getRawValue().toString(),
						  row.getCell(2).toString());
			}
		}
		return studentsEmail;
	}
	
	// Get a student's Email
	public String getEmail(Student student) {
		HashMap<String, String> studentsEmail = getStudentsEmail();
		return studentsEmail.get(student.getGtid());
	}

	// Get a student's overall grades
	public int getOverallGrade(Student student) {
		int overallGrades = 0;
		ScriptEngine jse;
		jse = new ScriptEngineManager().getEngineByName("JavaScript");
		String newFormula = getFormula();
		try {
			if (!newFormula.contains("ATT") && !newFormula.contains("AAG") && !newFormula.contains("APG"))
				throw new GradeFormulaException("Invalid Formula!");
			if (newFormula.contains("ATT"))
				newFormula = newFormula.replace("ATT", ""+getAttendance(student));
			if (newFormula.contains("AAG"))
				newFormula = newFormula.replace("AAG", ""+getAverageAssignmentsGrade(student));
			if (newFormula.contains("APG"))
				newFormula = newFormula.replace("APG", ""+getAverageProjectsGrade(student));
				
			overallGrades = (int) Math.round((double)jse.eval(newFormula));
			
		} catch (ScriptException e) {
			e.printStackTrace();
		} 
		return overallGrades;
	}




}
