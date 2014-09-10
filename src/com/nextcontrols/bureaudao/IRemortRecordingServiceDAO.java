package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.RemortRecordingService;

public interface IRemortRecordingServiceDAO {
	public List<RemortRecordingService> getRemortRecordingsList(String country) ;
	public void addRemortRecording(int pnumberofsensors,int pperyearOneYear,int ppermonthOneYear,int pperyearThreeYear,int ppermonthThreeYear,int pperyearFiveYear,int ppermonthFiveYear,String country);
	public void modifyRemortRecording(int pnumberofsensors,int pperyearOneYear,int ppermonthOneYear,int pperyearThreeYear,int ppermonthThreeYear,int pperyearFiveYear,int ppermonthFiveYear, int pID,String country);
	public void deleteRemortRecording(int pID,String country);
}
