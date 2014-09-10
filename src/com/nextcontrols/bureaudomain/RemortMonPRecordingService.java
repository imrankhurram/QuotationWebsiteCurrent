package com.nextcontrols.bureaudomain;



public class RemortMonPRecordingService {
	
	private int id;
	private int numberofsensors;
	private int peryearOneYear;
	private int permonthOneYear;
	private int peryearThreeYear;
	private int permonthThreeYear;
	private int peryearFiveYear;
	private int permonthFiveYear;
	public RemortMonPRecordingService() {

	}
	
	public RemortMonPRecordingService(int pid,int pnumberofsensors,int pperyearOneYear,
			int ppermonthOneYear,int pperyearThreeYear,int ppermonthThreeYear,int pperyearFiveYear,int ppermonthFiveYear) {
		super();
		this.id = pid;
		this.numberofsensors= pnumberofsensors;
		this.peryearOneYear=pperyearOneYear;
		this.permonthOneYear=ppermonthOneYear;
		this.peryearThreeYear=pperyearThreeYear;
		this.permonthThreeYear=ppermonthThreeYear;
		this.peryearFiveYear=pperyearFiveYear;
		this.permonthFiveYear=ppermonthFiveYear;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getNumberofsensors() {
		return numberofsensors;
	}

	public void setNumberofsensors(int numberofsensors) {
		this.numberofsensors = numberofsensors;
	}

	public int getPeryearOneYear() {
		return peryearOneYear;
	}

	public void setPeryearOneYear(int peryearOneYear) {
		this.peryearOneYear = peryearOneYear;
	}

	public int getPermonthOneYear() {
		return permonthOneYear;
	}

	public void setPermonthOneYear(int permonthOneYear) {
		this.permonthOneYear = permonthOneYear;
	}

	public int getPeryearThreeYear() {
		return peryearThreeYear;
	}

	public void setPeryearThreeYear(int peryearThreeYear) {
		this.peryearThreeYear = peryearThreeYear;
	}

	public int getPermonthThreeYear() {
		return permonthThreeYear;
	}

	public void setPermonthThreeYear(int permonthThreeYear) {
		this.permonthThreeYear = permonthThreeYear;
	}

	public int getPeryearFiveYear() {
		return peryearFiveYear;
	}

	public void setPeryearFiveYear(int peryearFiveYear) {
		this.peryearFiveYear = peryearFiveYear;
	}

	public int getPermonthFiveYear() {
		return permonthFiveYear;
	}

	public void setPermonthFiveYear(int permonthFiveYear) {
		this.permonthFiveYear = permonthFiveYear;
	}
	
}
