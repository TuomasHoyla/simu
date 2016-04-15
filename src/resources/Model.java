package resources;
import java.util.ArrayList;
import java.util.Properties;


public class Model {
	

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
//	public String allocationScheme;
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
//	public String publishingModel;
	public double publishingScale; //computed
//	public double publishingOffset; 
	public double productivityCoefficient;
	public String paperQualityModel;
//	public String citationModel; // for Paper
	public double paperQualityParameter;
	

// researcher related parameters

	public double neededToBeMotivated= 1; //pidet‰‰n vakiona (k‰ytet‰‰n vain konstruktorissa nyt)
	public double applyingIntensity;
	public double defaultMonetaryFrustration;
//	public String monetaryFrustrationModel;
	public double[] frustrationRate = new double[8];
	public double secondaryFrustrationRate;
	public double defaultPromotionalFrustration;
	public String promotionalFrustrationModel;
	public int[] promotionalFrustrationAge = new int[8];
	public double[] promotionalFrustrationrate = new double[8];
	public double sackingResistance;
	public double monetaryFrustrationWeight;
	public double promotionalFrustrationWeight;
//	public String productivityModel;
	public double frustrationProductivityWeight;
	public double monetaryProductivityWeight;
//	public String applicationQualityModel;
	public double resSkillWeight;
	
	
//	public String skillModel; // for random generators
	public double skillParameter;
//	public String researchSkillModel;
	public double fitnessVariance;
	public double fitnessSkillFactor;
	public double researchSkillParameter;


	public String caseheader; //depends on design
	public String instanssi; //computed by experiment
	public String narrative; //computed 
	
	public ArrayList<String> factorName = new ArrayList();
	public ArrayList<String> factorValues = new ArrayList();


public void resetModel(Properties config){
	
	Properties prop =config;
	try {
		levelCount =Integer.parseInt((prop.getProperty("levelCount")));
		String temp[] = prop.getProperty("positionLevels").split(",");
		for(int i=0; i< levelCount; i++) {PositionLevels[i]= Integer.parseInt(temp[i]);}
		temp =prop.getProperty("sackingAge").split(",");
		for(int i=0; i< levelCount; i++) {sackingAge[i]= Integer.parseInt(temp[i]);}
		temp =prop.getProperty("promotionalFrustrationAge").split(",");
		for(int i=0; i< levelCount; i++) {promotionalFrustrationAge[i]= Integer.parseInt(temp[i]);}
		temp =prop.getProperty("promotionalFrustrationRate").split(",");
		for(int i=0; i< levelCount; i++) {promotionalFrustrationrate[i]= Double.parseDouble(temp[i]);}
		temp =prop.getProperty("frustrationRate").split(",");
		for(int i=0; i< levelCount; i++) {frustrationRate[i]= Double.parseDouble(temp[i]);}

		PopulationSize = 0;
		for (int i=0; i<levelCount; i++) {PopulationSize+=PositionLevels[i];}

		promotionModel = prop.getProperty("promotionModel");
		promotionTreshold = Double.parseDouble(prop.getProperty("promotionTreshold")); 
		sackingResistance=Double.parseDouble(prop.getProperty("sackingResistance"));	
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
		publishingScale=1./resourceLevel; 
		productivityCoefficient=Double.parseDouble(prop.getProperty("productivityCoefficient"));
		paperQualityModel = prop.getProperty("paperQualityModel");
		applyingIntensity=Double.parseDouble(prop.getProperty("applyingIntensity"));
		defaultMonetaryFrustration =Double.parseDouble(prop.getProperty("defaultMonetaryFrustration"));		
		secondaryFrustrationRate=Double.parseDouble(prop.getProperty("secondaryFrustrationRate"));
		defaultPromotionalFrustration = Double.parseDouble(prop.getProperty("defaultPromotionalFrustration"));		
		promotionalFrustrationModel=prop.getProperty("promotionalFrustrationModel");
		monetaryFrustrationWeight=Double.parseDouble(prop.getProperty("monetaryFrustrationWeight"));
		promotionalFrustrationWeight=Double.parseDouble(prop.getProperty("promotionalFrustrationWeight"));
		sackingResistance=Double.parseDouble(prop.getProperty("sackingResistance"));
		frustrationProductivityWeight=Double.parseDouble(prop.getProperty("frustrationProductivityWeight"));
		monetaryProductivityWeight=Double.parseDouble(prop.getProperty("monetaryProductivityWeight"));
		resSkillWeight=Double.parseDouble(prop.getProperty("resSkillWeight"));
				
		fitnessVariance=Double.parseDouble(prop.getProperty("fitnessVariance"));
		fitnessSkillFactor=Double.parseDouble(prop.getProperty("fitnessSkillFactor"));
		researchSkillParameter=Math.sqrt(fitnessVariance*fitnessSkillFactor);
		paperQualityParameter=Math.sqrt(fitnessVariance*(1-fitnessSkillFactor));
		skillParameter=researchSkillParameter; 	
	
	} catch (Exception e) {
		System.out.println("Problems reading configuration file");
		System.out.println(prop);
		e.printStackTrace();

	}
	
}
	
public void configureExperiments(Properties experiment) {
	Properties exp = experiment;
//	System.out.println(exp);
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
	String temphead="";
	for(int i=0; i<9; i++) {
		String temp0 = exp.getProperty("factor"+i);
		if (temp0 !=null) {
			String[] temp = temp0.split(":");
			if(temp[1] != null) {
				factorName.add(temp[0]);
				factorValues.add(temp[1]);
				temphead=";"+temp[0]+temphead;
			}
		}
	}
	caseheader+=temphead;
	if(factorName.size()< 1) {
		System.out.println("No factors recognized for experiment");	
	}
}


public void setFactor(String name, String value) {

 	switch (name) {
 	
 	case "promotionModel":
 		promotionModel=value;
 		break;
 	case "evaluationErrorModel":
 		evaluationErrorModel=value;
 		break;
 	case "paperQualityModel":
 		paperQualityModel=value;
 		break;
 	case "promotionalFrustrationModel":
 		promotionalFrustrationModel=value;
 		break;	
	
 	case "allocationScheme":
// 		allocationScheme=value;
 		switch(value) {
 		case("Lottery"):
 			overhead=0.;
 			evenlyDistributedPart=0.;
 			evaluationError=1.;
 			break;
 		case("Communism"):
 			overhead=0.;
 			evenlyDistributedPart=1;
 			break;
 		case("Capitalism"):
 		case("Idealism"):
 			overhead=0;
 			evaluationError=0.;
 			evenlyDistributedPart=0.;
 			break;
 		default:
 		}
 		break;	
 		
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
	case "evaluationError":
		evaluationError=Double.parseDouble(value);
		break;
	case "overhead":
		overhead=Double.parseDouble(value);
		break;
	case "evenlyDistributedPart":
		evenlyDistributedPart=Double.parseDouble(value);
		break;
	case "promotionTreshold":
		promotionTreshold=Double.parseDouble(value);
		break;
	case "sackingResistance":
		sackingResistance=Double.parseDouble(value);
		break;
	case "productivityCoefficient":
		productivityCoefficient=Double.parseDouble(value);
		break;
	case "applyingIntensity":
		applyingIntensity=Double.parseDouble(value);
		break;
	case "defaultMonetaryFrustration":
		defaultMonetaryFrustration=Double.parseDouble(value);
		break;
	case "secondaryFrustrationRate":
		secondaryFrustrationRate=Double.parseDouble(value);
		break;
	case "defaultPromotionalFrustration":
		defaultPromotionalFrustration=Double.parseDouble(value);
		break;
	case "monetaryFrustrationWeight":
		monetaryFrustrationWeight=Double.parseDouble(value);
		break;
	case "promotionalFrustrationWeight":
		promotionalFrustrationWeight=Double.parseDouble(value);
		break;
	case "frustrationProductivityWeight":
		frustrationProductivityWeight=Double.parseDouble(value);
		break;
	case "monetaryProductivityWeight":
		monetaryProductivityWeight=Double.parseDouble(value);
		break;
	case "resSkillWeight":
		resSkillWeight=Double.parseDouble(value);
		break;
	case "fitnessVariance":
		fitnessVariance=Double.parseDouble(value);
		researchSkillParameter=Math.sqrt(fitnessVariance*fitnessSkillFactor);
		paperQualityParameter=Math.sqrt(fitnessVariance*(1-fitnessSkillFactor));
		break;
	case "fitnessSkillFactor":
		fitnessSkillFactor=Double.parseDouble(value);
		researchSkillParameter=Math.sqrt(fitnessVariance*fitnessSkillFactor);
		paperQualityParameter=Math.sqrt(fitnessVariance*(1-fitnessSkillFactor));
		break;
	case "paperQualityParameter":
		paperQualityParameter=Double.parseDouble(value);
		break;
	case "researchSkillParameter":
		researchSkillParameter=Double.parseDouble(value);
		skillParameter=researchSkillParameter; 

	default:
		System.out.println("Factor "+name+" not recognized");		
	}
	
}


public void updateNarrative(String text){
narrative+=text+"\n";
}

	

	public void setNarrative(){
		narrative="Default values \n";
		narrative+="Population size = "+ PopulationSize + " allocatable resource = "+AllocatableResource;
		narrative+="Promotion model is "+promotionModel+ " "+promotionTreshold+" Publishing scale is "+publishingScale+"\n";
		narrative+="Evaluation error model is "+evaluationErrorModel+"\n";
		narrative+="Paper quality model "+paperQualityModel+"\n";
		narrative+="Skill parameter "+skillParameter;
		narrative+=" Research skill parameter "+researchSkillParameter+"\n";
		narrative+="Frustration rates "+frustrationRate[0]+" "+secondaryFrustrationRate+"\n";
		narrative+="Frustration rates "+frustrationRate[0]+" "+frustrationRate[1]+" "+frustrationRate[2]+" "+frustrationRate[3]+"\n";
		narrative+="Promotional frustration ages "+promotionalFrustrationAge[0]+" "+promotionalFrustrationAge[1]+" "+promotionalFrustrationAge[2]+" "+promotionalFrustrationAge[3]+"\n";
		narrative+="Promotional frustration rates "+promotionalFrustrationrate[0]+" "+promotionalFrustrationrate[1]+" "+promotionalFrustrationrate[2]+" "+promotionalFrustrationrate[3]+"\n";
		narrative+="Retirement starting age "+sackingAge[3]+" resistance rate "+sackingResistance+"\n";

		}
}
