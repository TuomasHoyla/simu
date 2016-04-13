package resources;
import java.util.Properties;
import java.util.*;
import configreader.getPropertyValues;
import java.io.IOException;

public class Modelv2 {
	
//	static getPropertyValues modelConfig = new getPropertyValues();
//	static getPropertyValues experimentConfig = new getPropertyValues();

		public String dir;
		public String totalfile;
		public String datafile;
		public String logfile;
		public String modelfile;
	// simulation execution
		public long researcherSeed;
		public long paperSeed;
		public int warmUp;
		public int repetitionCount;
		public int runLength;
		public int safetyDistance;

	// organisation related
		public int PopulationSize;
		public int levelCount=4;
		public int[] PositionLevels = new int[8];
		public String promotionModel;
		public double promotionTreshold;
		public int[] sackingAge = new int[8];

	// funding related
		public String allocationScheme;
		public String evaluationErrorModel;
		public double evaluationError;
		public double overhead; // 0 - 1
		public double evenlyDistributedPart;
		public double maximumResearchEffort=1.;//fix
		public double defaultResearchShare;
		public double desiredResearchEffort=1.; //fix 
		public double resourceLevel;// strictly more than 0
		public double AllocatableResource; //computed
		public double averageResource; //computed
		public double defaultResearchTime; //computed

	//publishing related parameters	
		public String publishingModel;
		public double publishingScale; //computed
		public double publishingOffset; //is it needed?
		public double productivityCoefficient;
		public String paperQualityModel;
		public String citationModel; // for Paper
		public double paperQualityParameter;
		

	// researcher related parameters

		public double neededToBeMotivated= 1; //pidet‰‰n vakiona (k‰ytet‰‰n vain konstruktorissa nyt)
		public double applyingIntensity;
		public double defaultMonetaryFrustration;
		public String monetaryFrustrationModel;
		public double frustrationGrowthRate;
		public double[] frustrationRate = new double[8];
		public double secondaryFrustrationRate;
		public double defaultPromotionalFrustration;
		public String promotionalFrustrationModel;
		public int[] promotionalFrustrationAge = new int[8];
		public double[] promotionalFrustrationrate = new double[8];
		public double sackingResistance;
		public double monetaryFrustrationWeight;
		public double promotionalFrustrationWeight;
		public String productivityModel;
		public double frustrationProductivityWeight;
		public double monetaryProductivityWeight;
		public String applicationQualityModel;
		public double resSkillWeight;
		
		
		public String skillModel; // for random generators
		public double skillParameter;
		public String researchSkillModel;
		public double fitnessVariance;
		public double fitnessSkillFactor;
		public double researchSkillParameter;


		public String caseheader ="Case"; //depends on design
		public String instanssi; //computed by experiment
		public String narrative; //computed 
		
		public ArrayList<String> factorName = new ArrayList();
		public ArrayList<String> factorValues = new ArrayList();


	public void resetModel(Properties config){
		
		try {
			Properties prop =config;
			levelCount =Integer.parseInt((prop.getProperty("levelCount")));
			PositionLevels[0]= Integer.parseInt((prop.getProperty("PositionLevels0")));
			PositionLevels[1]= Integer.parseInt((prop.getProperty("PositionLevels1")));
			PositionLevels[2]= Integer.parseInt((prop.getProperty("PositionLevels2")));
			PositionLevels[3]= Integer.parseInt((prop.getProperty("PositionLevels3")));
			PopulationSize = 0;
			for (int i=0; i<levelCount; i++) {PopulationSize+=PositionLevels[i];}
			promotionModel = prop.getProperty("promotionModel");
			promotionTreshold = Double.parseDouble(prop.getProperty("promotionTreshold")); 
			sackingAge[0]=Integer.parseInt((prop.getProperty("sackingAge0")));
			sackingAge[1]=Integer.parseInt((prop.getProperty("sackingAge1")));
			sackingAge[2]=Integer.parseInt((prop.getProperty("sackingAge2")));
			sackingAge[3]=Integer.parseInt((prop.getProperty("sackingAge3")));
			sackingResistance=Double.parseDouble(prop.getProperty("sackingResistance"));	
			allocationScheme=prop.getProperty("allocationScheme");
			evaluationErrorModel= prop.getProperty("evaluationErrorModel");
			evaluationError=Double.parseDouble(prop.getProperty("evaluationError"));
			overhead = Double.parseDouble(prop.getProperty("overhead"));
			evenlyDistributedPart = Double.parseDouble(prop.getProperty("evenlyDistributedPart"));
			resourceLevel= Double.parseDouble(prop.getProperty("resourceLevel"));
			defaultResearchShare=Double.parseDouble(prop.getProperty("defaultResearchShare"));
			defaultResearchTime=defaultResearchShare*resourceLevel;
			AllocatableResource = PopulationSize*resourceLevel*(1-defaultResearchShare);
			averageResource=AllocatableResource/PopulationSize;
			desiredResearchEffort = 1-defaultResearchTime;	
			maximumResearchEffort = 1; //fixed
			publishingModel=prop.getProperty("publishingModel");
			publishingScale=1./resourceLevel; 
			productivityCoefficient=Double.parseDouble(prop.getProperty("productivityCoefficient"));
			publishingOffset=1.; //needed?
			paperQualityModel = prop.getProperty("paperQualityModel");
			citationModel=prop.getProperty("citationModel");
			applyingIntensity=Double.parseDouble(prop.getProperty("applyingIntensity"));
			defaultMonetaryFrustration =Double.parseDouble(prop.getProperty("defaultMonetaryFrustration"));		
			monetaryFrustrationModel=prop.getProperty("monetaryFrustrationModel");
			frustrationGrowthRate =Double.parseDouble(prop.getProperty("frustrationGrowthRate"))/(1-averageResource/desiredResearchEffort);
			frustrationRate[0]=Double.parseDouble(prop.getProperty("frustrationRate0"))/(1-averageResource/desiredResearchEffort);
			frustrationRate[1]=Double.parseDouble(prop.getProperty("frustrationRate1"))/(1-averageResource/desiredResearchEffort);
			frustrationRate[2]=Double.parseDouble(prop.getProperty("frustrationRate2"))/(1-averageResource/desiredResearchEffort);
			frustrationRate[3]=Double.parseDouble(prop.getProperty("frustrationRate3"))/(1-averageResource/desiredResearchEffort);
			secondaryFrustrationRate=Double.parseDouble(prop.getProperty("productivityCoefficient"));
			defaultPromotionalFrustration = Double.parseDouble(prop.getProperty("productivityCoefficient"));		
			promotionalFrustrationModel=prop.getProperty("promotionalFrustrationModel");
			promotionalFrustrationAge[0] = Integer.parseInt(prop.getProperty("promotionalFrustrationAge0"));
			promotionalFrustrationAge[1] = Integer.parseInt(prop.getProperty("promotionalFrustrationAge1"));
			promotionalFrustrationAge[2] = Integer.parseInt(prop.getProperty("promotionalFrustrationAge2"));
			promotionalFrustrationAge[3] = Integer.parseInt(prop.getProperty("promotionalFrustrationAge3"));
			promotionalFrustrationrate[0] = Double.parseDouble(prop.getProperty("promotionalFrustrationRate0")); 
			promotionalFrustrationrate[1] = Double.parseDouble(prop.getProperty("promotionalFrustrationRate1"));
			promotionalFrustrationrate[2] = Double.parseDouble(prop.getProperty("promotionalFrustrationRate2"));
			promotionalFrustrationrate[3] = Double.parseDouble(prop.getProperty("promotionalFrustrationRate3"));
			monetaryFrustrationWeight=Double.parseDouble(prop.getProperty("monetaryFrustrationWeight"));
			promotionalFrustrationWeight=Double.parseDouble(prop.getProperty("promotionalFrustrationWeight"));
			sackingResistance=Double.parseDouble(prop.getProperty("sackingResistance"));
			productivityModel=prop.getProperty("productivityModel");
			frustrationProductivityWeight=Double.parseDouble(prop.getProperty("frustrationProductivityWeight"));
			monetaryProductivityWeight=Double.parseDouble(prop.getProperty("monetaryProductivityWeight"));
			applicationQualityModel=prop.getProperty("applicationQualityModel");
			resSkillWeight=Double.parseDouble(prop.getProperty("resSkillWeight"));
				
				
			researchSkillModel=prop.getProperty("researchSkillModel");
			fitnessVariance=Double.parseDouble(prop.getProperty("fitnessVariance"));
			fitnessSkillFactor=Double.parseDouble(prop.getProperty("fitnessSkillFactor"));
			researchSkillParameter=Math.sqrt(fitnessVariance*fitnessSkillFactor);
			paperQualityParameter=Math.sqrt(fitnessVariance*(1-fitnessSkillFactor));
			skillModel=prop.getProperty("skillModel"); 
			skillParameter=researchSkillParameter; 
		
		} catch (Exception e) {
			System.out.println("No config found!");
			e.printStackTrace();
		}
		
	}
		
	public void configureExperiments(Properties experiment) {
		Properties exp = experiment;

		dir=exp.getProperty("workingdirectory");
		totalfile=dir+"totalfile.txt";
		datafile = dir+"datafile.txt";
		logfile=dir+"logfile.txt";
	// simulation execution
		researcherSeed= Long.parseLong(exp.getProperty("researcherSeed"));
		paperSeed= Long.parseLong(exp.getProperty("paperSeed"));
		warmUp = Integer.parseInt(exp.getProperty("warmUp"));
		repetitionCount = Integer.parseInt(exp.getProperty("repetitionCount"));
		runLength = Integer.parseInt(exp.getProperty("runLength"));
		safetyDistance = Integer.parseInt(exp.getProperty("safetyDistance"));
		caseheader = exp.getProperty("caseheader");
		for(int i=0; i<9; i++) {
			String temp0 = exp.getProperty("factor"+i);
			if (temp0 !=null) {
				String[] temp = temp0.split(":");
				if(temp[1] != null) {
					factorName.add(temp[0]);
					factorValues.add(temp[1]);
					caseheader+=";"+temp[0];
				}
			}
		}
	}
	
	
	public void setFactor(String name, String value) {
	
	 	switch (name) {
			
			case "resourceLevel": 
			resourceLevel = Double.parseDouble(value);
			defaultResearchTime=defaultResearchShare*resourceLevel;
			AllocatableResource = PopulationSize*resourceLevel*(1-defaultResearchShare);
			averageResource=AllocatableResource/PopulationSize;
			desiredResearchEffort = 1-defaultResearchTime;	
			publishingScale=1./resourceLevel; 
			break;
			
			case "defaultResearchShare":
			defaultResearchShare = Double.parseDouble(value);
			defaultResearchTime=defaultResearchShare*resourceLevel;
			AllocatableResource = PopulationSize*resourceLevel*(1-defaultResearchShare);
			desiredResearchEffort = 1-defaultResearchTime;	
			break;
			
		}
		
	}


	public void setNarrative(){
		narrative="Default values \n";


		}
	public void updateNarrative(String text){
	narrative+=text+"\n";
	}
}
