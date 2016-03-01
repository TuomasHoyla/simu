package resources;

/** TODO
 * 
 * 

 * 
 */


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resources.*;
import configreader.getPropertyValues;
import main.simulation;

public class Organisation {
	
	public int PositionLevels[] = new int[4];
	public int sackingAge[] = new int[4];
	public double promotionTreshold;
	public ArrayList<Researcher> researcherArray = new ArrayList<>();
	ArrayList<Researcher> TempResearchersToBeAdded = new ArrayList<>();
	ArrayList<Researcher> TempResearchersToBeRemoved = new ArrayList<>();
	public ArrayList<Integer> cumuPaperCounter = new ArrayList<Integer>();
	public ArrayList<Integer> cumuCitationCounter = new ArrayList<Integer>();
	public ArrayList<Paper> oldPapers = new ArrayList<Paper>();
	public ArrayList<Paper> removedPapers = new ArrayList<Paper>();
	int PopulationSize;
	double sackingResistance;
	int addableCitations;
	int addablePapers;
	int totalCitations;
	int totalPapers;
	int year;
	String promotionModel;
//	private int papersFromRemovedResearchers;
	public int citationsFromRemovedResearchers;
	public int papersFromRemovedResearchers;
	public int[] ResearchersByLevels= new int[4];
	public int[] PapersByLevels= new int[4];
	public int[] CurrentPapers = new int[4];
	public int[] CurrentCitations = new int[4];	
	public int[] CitationsByLevels= new int[4];
	public int[] ResignByLevels= new int[4];
	public int[] PromoteByLevels =new int[4];
	public int[] RetirementAge = new int[4];
	public int[] PromotionAge = new int[4];
	public double[] SkillByLevels = new double[4];
	public double[] FrustrationByLevels = new double[4];
	
	Comparator<Researcher> vertaaja = new Comparator<Researcher>(){
		public int compare(Researcher p1, Researcher p2) {
			if (p1.getQualityOfApplication() < p2.getQualityOfApplication()) return -1;
			if (p1.getQualityOfApplication() > p2.getQualityOfApplication()) return 1;
			return 0;
		}
	};
	Comparator<Researcher> vertaaja2 = new Comparator<Researcher>(){
		public int compare(Researcher p1, Researcher p2) {
			if (p1.getPositionInOrganization() < p2.getPositionInOrganization()) return -1;
			if (p1.getPositionInOrganization() > p2.getPositionInOrganization()) return 1;
			if (p1.getCitations() < p2.getCitations()) return -1;
			if (p1.getCitations() > p2.getCitations()) return 1;
			return 0;
		}
	};
	
	public void initialize()
	{
	for(int i=0; i<4;i++){
 	PositionLevels[i] = simulation.M.PositionLevels[i];
	sackingAge[i] = simulation.M.sackingAge[i];
	}
	promotionTreshold= simulation.M.promotionTreshold;
	PopulationSize = simulation.M.PopulationSize;
	sackingResistance = simulation.M.sackingResistance;
	promotionModel = simulation.M.promotionModel;

	
	
// Initialize a researcher array (with random research skills and right amount of  instances to each level
	for (int i=0; i<4;i++)
	{
		
		for (int j=0; j<main.simulation.M.PositionLevels[i];j++)
		{
		researcherArray.add(new Researcher(RandomGenerator.nextSessionId() , 0, main.simulation.randomGenerator.createResSkill(), main.simulation.randomGenerator.createSkill(), 0, 0, i+1));	
		}
	}
	}
	

	public void RemoveResearchers() 
	{
		for (int i=0;i<4;i++) {ResignByLevels[i]=0;
		RetirementAge[i]=0;}
		for (Researcher ukkeli : researcherArray) 
		{
			ukkeli.consumeMoney();	
			ukkeli.addYear();
			if (ukkeli.getYearsInAcademia() >= sackingAge[3] && main.simulation.randomGenerator.createRandomDouble() >= sackingResistance) 
			{
				ukkeli.setSackingProbability(1.);
			}
			if (ukkeli.getLeavingOrganization()) 
				{ 
				TempResearchersToBeRemoved.add(ukkeli); 
				ResignByLevels[ukkeli.getPositionInOrganization()-1]++;
				RetirementAge[ukkeli.getPositionInOrganization()-1]+=ukkeli.getYearsInAcademia();
				}		
		}
		for (Researcher ukkeli: TempResearchersToBeRemoved)
		{
			for(Paper lappu: ukkeli.papers)
			{
				oldPapers.add(lappu); //ker‰t‰‰n talteen
			}
			researcherArray.remove(ukkeli);
		}
		TempResearchersToBeRemoved.clear();
	}
	
	public void CountByLevels()
	{
	for (int i=0;i<4; i++)
	{
		ResearchersByLevels[i]=0;
		PapersByLevels[i]=0;
		CitationsByLevels[i]=0;
		SkillByLevels[i]=0.;
		FrustrationByLevels[i]=0.;
	}
	for (Researcher ukkeli : researcherArray) 
	{
		ResearchersByLevels[ukkeli.getPositionInOrganization()-1]++;	
		PapersByLevels[ukkeli.getPositionInOrganization()-1]+=ukkeli.getPapers();	
		CitationsByLevels[ukkeli.getPositionInOrganization()-1]+=ukkeli.getCitations();	
		SkillByLevels[ukkeli.getPositionInOrganization()-1]+=ukkeli.getResearchSkill();
		FrustrationByLevels[ukkeli.getPositionInOrganization()-1]+=ukkeli.getFrustration();
	}
	}

	public void AddResearchers()
	{
		CountByLevels();
		int temp= ResearchersByLevels[0]+ResearchersByLevels[1]+ResearchersByLevels[2]+ResearchersByLevels[3];
		for (int i=0;i< PopulationSize-temp;i++)
		{
			researcherArray.add(new Researcher(RandomGenerator.nextSessionId() , 0, main.simulation.randomGenerator.createResSkill(), main.simulation.randomGenerator.createSkill(), 0, 0, 1));
		}
	}
	
	public void TTpromote() {

		double[] citationaverage = new double[4]; 
		CountByLevels();
		for (int i=0;i<4;i++){
			citationaverage[i]= CitationsByLevels[i]/((double) ResearchersByLevels[i]);
			PromoteByLevels[i]=0;
			PromotionAge[i]=0;
		}
		for(Researcher dude : researcherArray) {
			int i= dude.getPositionInOrganization();
			if (promotionModel=="Citation_based"&& dude.getYearsInAcademia() >= sackingAge[i-1] && (dude.getCitations() >= promotionTreshold*citationaverage[i-1])) 
				{
				dude.setPositionInOrganization(i+1);
				PromoteByLevels[i-1]++;
				PromotionAge[i-1]+=dude.getYearsInAcademia();
				}
		}				

		
/*		
		if(promotionModel=="Citation_based"){
			
			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i &&  dude.getYearsInAcademia() >= sackingAge[i-1] && (dude.getCitations() >= promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}				
			}			
		}
		*/
		if(promotionModel=="Position_based"){
			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i &&  dude.getYearsInPosition() >= sackingAge[i-1] && (dude.getCitations() >= promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}				
			}			
		}

		if(promotionModel=="Citation_only"){

			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i && (dude.getCitations() >= promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}			
			}
		}

			if(promotionModel=="Fixed_HC"){
			Collections.sort(researcherArray, vertaaja2);
			Collections.reverse(researcherArray);
			int ii=3;
			int jj=0;
			
			for(Researcher dude : researcherArray) {
				if(dude.getPositionInOrganization() < ii) {
					if(jj< PromoteByLevels[ii]) {
						System.out.println(jj+" "+PromoteByLevels[ii]);
						PromoteByLevels[ii]=jj;
						}
					PromoteByLevels[ii-1]+=PromoteByLevels[ii];
					ii--;
					jj=0;
				}
				if(dude.getPositionInOrganization() == ii && jj < PromoteByLevels[ii])
				{
					dude.setPositionInOrganization(ii+1);
					jj++;
					PromotionAge[ii-1]+=dude.getYearsInAcademia();
				}
			}
			for (int i=0; i<3; i++){
			PromoteByLevels[i]=PromoteByLevels[i+1];
			}
			PromoteByLevels[3]=0;
		}
	}
		


public void publishTT(int currentYear) {
	
	for (int i=0;i<4;i++)
	{
		CurrentPapers[i]=0;
		CurrentCitations[i]=0;
	}
		for(Researcher researcher : researcherArray) {
			researcher.publish(currentYear);
		}
		
		citationsFromRemovedResearchers=0;	
		papersFromRemovedResearchers=0;
		for(Paper lappu:oldPapers) {
			citationsFromRemovedResearchers+=lappu.updateCitationsTT(currentYear);
			papersFromRemovedResearchers++;
			if(lappu.isDead()) {removedPapers.add(lappu);}
		}
		for(Paper lappu:removedPapers) {
			oldPapers.remove(lappu);
		}
		removedPapers.clear();
	}

	public void compareReceivedMoney() {
		main.simulation.mon.res=0;
		for (Researcher researcher : researcherArray) {
			main.simulation.mon.res+=researcher.getResourcesForResearch();
			researcher.setTotalFrustration();
			researcher.setProductivity();
		}
	}
	
	public int getTotalCitations() {
		totalCitations = 0;
		for (Researcher dude : researcherArray) {
			totalCitations += dude.getCitations();
		}
		return totalCitations;
	}

	public int getTotalPapers() {
		totalPapers = 0;
		for (Researcher dude : researcherArray) {
			totalPapers += dude.getPapers();
		}
		return totalPapers;
	}


}