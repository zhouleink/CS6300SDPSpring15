package edu.gatech.seclass.gradescalc;

public class Student {
	
	private String name;
	private String gtid;

	public Student(String name, String gtid) {
		this.name = name;
		this.gtid = gtid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGtid(String gtid) {
		this.gtid = gtid;
	}

	public String getGtid() {
		return this.gtid;
	}

}
