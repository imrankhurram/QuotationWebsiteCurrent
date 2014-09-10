package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.RemortMonPRecordingService;

public interface IRemortMonPRecordingServiceDAO {
	public List<RemortMonPRecordingService> getRemortMonPRecordingsList(String pCountry) ;
	public void addRemortMonPRecording(int pnumberofsensors,int pperyearOneYear,int ppermonthOneYear,int pperyearThreeYear,int ppermonthThreeYear,int pperyearFiveYear,int ppermonthFiveYear);
	public void modifyRemortMonPRecording(int pnumberofsensors,int pperyearOneYear,int ppermonthOneYear,int pperyearThreeYear,int ppermonthThreeYear,int pperyearFiveYear,int ppermonthFiveYear, int pID);
	public void deleteRemortMonPRecording(int pID);
}
