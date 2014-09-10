package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.MonitoringService;
import com.nextcontrols.bureaudomain.MonitoringServiceCosts;

public interface IMonitoringServiceDAO {
	public List<MonitoringService> getMonitoringServicesList() ;
	public void addMonitoringService(double pthreeyeardisc,double pfiveyeardisc,double pfactor2,String pcountry,String pcurrency, String pDescription, int pStepValue1, int pStepValue2, int pStepValue3, int pStepValue4, int pStepValue5);
	public void modifyMonitoringService(double pthreeyeardisc,double pfiveyeardisc,double pfactor2,String pcountry,String pcurrency, int pID, String pDescription, int pStepValue1, int pStepValue2, int pStepValue3, int pStepValue4, int pStepValue5);
	public void deleteMonitoringService(int pID);
	public List<MonitoringServiceCosts>getMonitoringServiceCosts(int pID,int pStepValue1, int pStepValue2, int pStepValue3, int pStepValue4, int pStepValue5);
	public List<String>getCountryList();
	public List<String>getCurrencyList();
	public void updateMonitoringServiceCosts(MonitoringService pmonitoringService);
	public List<Integer> getMonitoringServicesList(String pCountry, int pNumberOfSensors) ;

}
