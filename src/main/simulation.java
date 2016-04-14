package main;



import java.text.DecimalFormat;

import configreader.getPropertyValues;

import java.util.ArrayList;
import java.util.Properties;

import resources.*;

public class simulation {
	
	public static Model M = new Model();
	public static Monitor mon = new Monitor();
	public static Organisation O = new Organisation();
	FileRead fileread = new FileRead();
	DecimalFormat df = new DecimalFormat("#.##");
	SharedFunding funder = new SharedFunding();
	public static RandomGenerator randomGenerator = new RandomGenerator();
	public static Properties configs ;
	public static Properties experiment;
	public static boolean headingSet=false;

	int year;

	//Default constructor
	public simulation() {
		getPropertyValues configfile = new getPropertyValues("config.properties"); //lacks other file as a parameter
		getPropertyValues experimentfile = new  getPropertyValues("experiment.properties");
		try {
		configs = configfile.getPropValues();
		experiment = experimentfile.getPropValues();
		}
		catch (Exception e) {}
	}

	//Constructor if file exists
	public simulation(String referenceStateFile, String expFile) {
		getPropertyValues configfile = new getPropertyValues(referenceStateFile); //lacks other file as a parameter
		getPropertyValues experimentfile = new  getPropertyValues(expFile);
		try {
		configs = configfile.getPropValues();
		experiment = experimentfile.getPropValues();
		}
		catch (Exception e) {}
	}
	
	
	public void iterate (int count){
		for (int loop=0; loop< count; loop++) 
		{
			O.RemoveResearchers();
			O.promote();
			O.AddResearchers();
			funder.allocate();
			O.compareReceivedMoney(); //Compare what got and what not, sets frustration and productivity
			O.publish(year);
			O.CountByLevels();
			mon.updateCounters();
			year++;
		}
		
	}
	
	public void runExperiment(String instance) {
		year = 0;
		O.initialize ();
		mon.resetCounters();
		iterate(M.warmUp);
		for (int sample=0; sample < M.repetitionCount; sample++){
			mon.resetCounters();
			iterate(M.runLength); // single sample
			mon.logReport2(M.runLength, instance);
			iterate(M.safetyDistance); //separation
		} //end sampling
		O.researcherArray.clear();
		O.oldPapers.clear();
	}
	public void simulate()  {
		

M.resetModel(configs);
M.configureExperiments(experiment);
mon.setHeadings();	
runFactors(M.factorName, M.factorValues,M.factorName.size()-1,"");
//mon.logNarrative();
}

		
public void runFactors(ArrayList<String> fName, ArrayList<String> fValues, int expNumber, String instance) {
		
		String name = fName.get(expNumber);
		String values = fValues.get(expNumber);
		String parsedValues[] = values.split(",");
		for(int i=0; i< parsedValues.length; i++) {
			M.setFactor(name, parsedValues[i]);
			String newInstance = instance+";"+ parsedValues[i];
			if(expNumber >0) {
				runFactors(fName, fValues, expNumber-1,newInstance);}
			else {
				runExperiment(newInstance);
			}
		}
	}	
	

	/** This is the main method for simulation 
	 * @param args referenceStateFile, experimentFile
	 */
	public static void main(String[] args) {
		
		simulation test = null;
		
		/**
		 * Tries parameters. Runs as .jar as follows: "java -jar simulation.jar file1.conf file2.conf"
		 */
	    try {
	    	
	    	test = new simulation(args[0], args[1]);
	    	
	    }
	    catch (ArrayIndexOutOfBoundsException e){
	    	
	        System.out.println("ArrayIndexOutOfBoundsException caught: " + e + " no simulation setting file parameters given, using defaults");
	        
	        test = new simulation();
	    }
	    finally {
	    	
	    	test.simulate();
	    }
	    		
	}

}