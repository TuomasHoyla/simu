package resources;
import java.util.Vector;

import main.*;

public class Researcher  {

	static RandomGenerator randgenerator = new RandomGenerator(simulation.M.researcherSeed);
	public String name;
	double aplTimeResources = 0;
	private double qualityOfApplication;
	private double researchSkill;
	private double applyingSkill;
	public double timeAvailableForResearch = 1;
	private int yearsInAcedemia;
	private int yearsInPosition=0;
	public double monetaryFrustration = 0;
	public double promotionalFrustration = 0;
	private double totalFrustration;
	public double ResourcesForResearch = 0;
	private int positionInOrganization; 
	private double resourcesNeededToBeMotivated;
	private double productivity;
	private double baseProductivity;
	private double monetaryProductivity;
	private double sackingProbability;
	private boolean leavingOrganization;
	double timeUsedForApplying;
	public Vector<Paper> papers = new Vector<Paper>(); 
	private int paperCount;

	//Constructors TODO Citations voi ottaa huoletta pois
	Researcher() {
		this("Researcher",0,50,50,0,0,0);
	}

	public Researcher (String name) {
		this(name, 0,1,1,0,0,0); 
	}

	Researcher (String name, int yearsInAcademia) {
		this(name, yearsInAcademia,50,50,0,0,0); 
	}

	Researcher (String name, int yearsInAcademia, double researchSkill) {
		this(name, yearsInAcademia,researchSkill,50,0,0,0); 
	}

	Researcher (String name, int yearsInAcademia, double researchSkill, double applyingSkill) {
		this(name, yearsInAcademia,researchSkill,applyingSkill,0,0,0); 
	}

	Researcher (String name, int yearsInAcademia, double researchSkill, double applyingSkill, int citations) {
		this(name, yearsInAcademia,researchSkill,applyingSkill,citations,0,0); 
	}

	Researcher (String name, int yearsInAcademia, double researchSkill, double applyingSkill, int citations, int ResourcesForResearch) {
		this(name, yearsInAcademia,researchSkill,applyingSkill,citations,ResourcesForResearch,0); 
	}

	public Researcher(String name, int yearsInAcademia, double researchSkill, double applyingSkill, int citations, int ResourcesForResearch, int positionInOrganization){
		this.name = name;
		this.yearsInAcedemia = yearsInAcademia;
		this.researchSkill = researchSkill;
		this.applyingSkill = applyingSkill;
		this.ResourcesForResearch = 0;
		this.positionInOrganization = positionInOrganization;
		this.productivity = 1.0; //no need to be one...
		this.leavingOrganization = false;
		this.resourcesNeededToBeMotivated = simulation.M.neededToBeMotivated; //Average
	}
	public void setMoney(double grantAmount) {
		ResourcesForResearch += grantAmount;
	}
	
	public double getResourcesForResearch() {
		return ResourcesForResearch;
	}
		
	public void consumeMoney() {
		ResourcesForResearch=0; 
		if (ResourcesForResearch < 0) ResourcesForResearch = 0; 
	}

	public double getResearchSkill() {
		return researchSkill;
	}

	public void addResearchSkill(double skill) {
		researchSkill += skill;
	}

	public void addApplyingSkill(double skill) {
		applyingSkill += skill;
	}
	/**
	 * Gives the parameter for application quality
	 * @param normalDistributedSkill 1-efectivenessOfFundingProcess/100, is the distribution ratio
	 */

	public double getApplyingSkill() {
		return applyingSkill;}

	public int getYearsInAcademia() {
		return yearsInAcedemia;
	}
	public int getYearsInPosition() {
		return yearsInPosition;
	}

	public String getName() {
		return name;
	}

	public double getFrustration() {
		return totalFrustration;
	}
	public double getMonetaryFrustration(){
		return monetaryFrustration;
	}

	public void addYear() {
		yearsInAcedemia++;
		yearsInPosition++;
	}
	public void setMonetaryFrustration() {
		int temp = getPositionInOrganization()-1;
			monetaryFrustration += (1-ResourcesForResearch/resourcesNeededToBeMotivated)*simulation.M.frustrationRate[temp]/(1-simulation.M.averageResource/simulation.M.desiredResearchEffort);
			if(ResourcesForResearch < simulation.M.averageResource){
				monetaryFrustration +=simulation.M.secondaryFrustrationRate*(simulation.M.averageResource- ResourcesForResearch)/(1-simulation.M.averageResource);
			}
			else {
				monetaryFrustration +=simulation.M.secondaryFrustrationRate*(simulation.M.averageResource- ResourcesForResearch)/simulation.M.averageResource;
				if(monetaryFrustration <0.) {monetaryFrustration=0.;}
			}
		}


	/**
	 * sets Promotional frustration, 
	 */
	public void setPromotionalFrustration(){
		int temp = getPositionInOrganization()-1;

		switch(simulation.M.promotionalFrustrationModel) {
		case("Academic_Age"): {
			if(getYearsInAcademia()> simulation.M.promotionalFrustrationAge[temp]){
				promotionalFrustration += randgenerator.createRandomDouble()*simulation.M.promotionalFrustrationrate[temp];
			}
			break;
		}
		case("Time_in_Position"): {
			if(getYearsInPosition()> simulation.M.promotionalFrustrationAge[temp]){
				promotionalFrustration += randgenerator.createRandomDouble()*simulation.M.promotionalFrustrationrate[temp];
			}
			break;
		}
		default: {System.out.println(simulation.M.promotionalFrustrationModel);
		}
		}

		if (promotionalFrustration < 0) {promotionalFrustration = 0;} //Frustration cant get negative value
	}


	/**
	 * Works
	 */
	public void setTotalFrustration() {

		setMonetaryFrustration();
		setPromotionalFrustration();

		totalFrustration = simulation.M.monetaryFrustrationWeight*monetaryFrustration+simulation.M.promotionalFrustrationWeight*promotionalFrustration;
	}

	public double getResourcesNeededToBeMotivated() {
		return resourcesNeededToBeMotivated;
	}

	public int getPapers() {
		return papers.size();
	}

	public int getCitations() {
		int citations = 0;
		for (Paper paper: papers){
			citations+= paper.getCitations();
		}

		return citations;
	}
	public void setResearchSkill(double skill) {
		this.researchSkill = skill;
	}

	public int getPositionInOrganization() {
		return positionInOrganization;
	}


	public void setPositionInOrganization(int positionInOrganization) { //set level in org and at the same set the expected annually salary
		this.positionInOrganization = positionInOrganization;
		this.promotionalFrustration = simulation.M.defaultPromotionalFrustration;
		this.monetaryFrustration = simulation.M.defaultMonetaryFrustration;
		this.yearsInPosition = 0;
	}


	public double getProductivity() {
		return productivity;
	}

	/**
	 * Set base and monetary productivity and sums them up
	 */
	public void setProductivity() {

		this.baseProductivity = 1-getFrustration()*simulation.M.frustrationProductivityWeight;
		monetaryProductivity =  (ResourcesForResearch/resourcesNeededToBeMotivated);
		productivity = baseProductivity+simulation.M.monetaryProductivityWeight*(monetaryProductivity-baseProductivity);		

	}

	public double getTimeAvailableForResearch() {
		return timeAvailableForResearch;
	}

	public boolean getLeavingOrganization() {
		if (this.sackingProbability >= 1.0 || totalFrustration >= 1.0) {leavingOrganization = true; 
		}
		return leavingOrganization;
	}


	/**
	 * 
	 * Sacking probability happens just in first & last level (No tenure & retirement)
	 */
	public void setSackingProbability(double sackingProbability) {
		this.sackingProbability += sackingProbability;
	}

	public double getSackingProbability() {
		return sackingProbability;
	}


	/**
	 * Set quality of application and the time used for applying
	 * @param valintaTarkkuus
	 */
	public void setQualityOfApplication (double evaluationError) {
		double evalError=1.;
//		timeUsedForApplying = simulation.M.defaultResearchTime*simulation.M.applyingIntensity*(1-totalFrustration);

		qualityOfApplication= (simulation.M.resSkillWeight*researchSkill + (1-simulation.M.resSkillWeight)*applyingSkill*(1-totalFrustration));


		switch(simulation.M.evaluationErrorModel) {
		case("Mult"):
		{
			evalError=(1.+randgenerator.createNormalDistributedValue(0, evaluationError));
			qualityOfApplication = qualityOfApplication*evalError;
			break;

		}
		case ("Sum"):
		{
			evalError=(1.+randgenerator.createNormalDistributedValue(0, evaluationError));
			qualityOfApplication+=evalError;
			break;
		}
		case("Blended"):
		{
			qualityOfApplication=(1.-evaluationError)*qualityOfApplication+evaluationError*randgenerator.createLogDistributedRandomValue(0,simulation.M.researchSkillParameter);
			break;
		}
		default: {System.out.println("Evaluation error model");}
		
		}
		
	}

	public void setTimeForResearch() {

		timeAvailableForResearch = simulation.M.defaultResearchTime+ResourcesForResearch-timeUsedForApplying;
	}


	public double getQualityOfApplication() {
		return qualityOfApplication;
	}

	public void setResourcesNeededToBeMotivated(double d) {

		resourcesNeededToBeMotivated=d;

	}
	public void publish(int currentYear)
	{

		paperCount = simulation.randomGenerator.createPoisson2(simulation.M.publishingScale*this.getTimeAvailableForResearch()*(1-simulation.M.productivityCoefficient+this.getProductivity()*simulation.M.productivityCoefficient));							


		if (paperCount < 0) paperCount = 0; //Prevents negative counts
	
			for (int i = 1; i <= paperCount; i++) {
				this.papers.add(new Paper(1,1.0, this.getResearchSkill(), currentYear, 1, 1));
			}				
		
		simulation.O.CurrentPapers[this.getPositionInOrganization()-1]+=paperCount;
		for (Paper paper : this.papers) {
			simulation.O.CurrentCitations[this.getPositionInOrganization()-1]+= paper.updateCitationsTT(currentYear);
			if(paper.isDead()) {simulation.O.removedPapers.add(paper);}
		}
		for(Paper lappu:simulation.O.removedPapers) {
			this.papers.remove(lappu);
		}
		simulation.O.removedPapers.clear();

	}
}