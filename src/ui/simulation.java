package ui;



import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resources.*;

public class simulation {
	
	public static Model M = new Model();
	public static Monitor mon = new Monitor();
	FileRead fileread = new FileRead();
	DecimalFormat df = new DecimalFormat("#.##");
	applyForFunds funder = new applyForFunds();
	public static RandomGenerator randomGenerator = new RandomGenerator();
	public ArrayList<Researcher> researcherArray = new ArrayList<>();
	ArrayList<Researcher> TempResearchersToBeAdded = new ArrayList<>();
	ArrayList<Researcher> TempResearchersToBeRemoved = new ArrayList<>();
	public ArrayList<Integer> cumuPaperCounter = new ArrayList<Integer>();
	public ArrayList<Integer> cumuCitationCounter = new ArrayList<Integer>();
	public static ArrayList<Paper> oldPapers = new ArrayList<Paper>();
	public static ArrayList<Paper> removedPapers = new ArrayList<Paper>();
	int addableCitations;
	int addablePapers;
	int totalCitations;
	int totalPapers;
	int year;
//	private int papersFromRemovedResearchers;
	public static int citationsFromRemovedResearchers;
	public static int papersFromRemovedResearchers;
	public static int[] ResearchersByLevels= new int[4];
	public static int[] PapersByLevels= new int[4];
	public static int[] CurrentPapers = new int[4];
	public static int[] CurrentCitations = new int[4];	
	public static int[] CitationsByLevels= new int[4];
	public static int[] ResignByLevels= new int[4];
	public static int[] PromoteByLevels =new int[4];
	public static int[] RetirementAge = new int[4];
	public static int[] PromotionAge = new int[4];
	public static double[] SkillByLevels = new double[4];
	public static double[] FrustrationByLevels = new double[4];
	
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
	
// Initialize a researcher array (with random research skills and right amount of  instances to each level
	for (int i=0; i<4;i++)
	{
		
		for (int j=0; j<M.PositionLevels[i];j++)
		{
		researcherArray.add(new Researcher(RandomGenerator.nextSessionId() , 0, randomGenerator.createResSkill(), randomGenerator.createSkill(), 0, 0, i+1));	
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
			if (ukkeli.getYearsInAcademia() >= M.sackingAge[3] && randomGenerator.createRandomDouble() >= M.sackingResistance) 
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
		for (int i=0;i< M.PopulationSize-temp;i++)
		{
			researcherArray.add(new Researcher(RandomGenerator.nextSessionId() , 0, randomGenerator.createResSkill(), randomGenerator.createSkill(), 0, 0, 1));
		}
	}
	
	public void TTpromote() {

		double[] citationaverage = new double[4]; 
		CountByLevels();
		if(M.promotionModel=="Citation_based"){
			for (int i=0;i<4;i++)
			{
				citationaverage[i]= CitationsByLevels[i]/((double) ResearchersByLevels[i]);
				PromoteByLevels[i]=0;
				PromotionAge[i]=0;
			}
			
			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i &&  dude.getYearsInAcademia() >= M.sackingAge[i-1] && (dude.getCitations() >= M.promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}				
			}			
		}
		if(M.promotionModel=="Position_based"){
			for (int i=0;i<4;i++)
			{
				citationaverage[i]= CitationsByLevels[i]/((double) ResearchersByLevels[i]);
				PromoteByLevels[i]=0;
				PromotionAge[i]=0;
			}
			
			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i &&  dude.getYearsInPosition() >= M.sackingAge[i-1] && (dude.getCitations() >= M.promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}				
			}			
		}

		if(M.promotionModel=="Citation_only"){
			for (int i=0;i<4;i++)
			{
				citationaverage[i]= CitationsByLevels[i]/((double) ResearchersByLevels[i]);
				PromoteByLevels[i]=0;
				PromotionAge[i]=0;
			}
			for (int i=3;i>0; i--)
			{
					for(Researcher dude : researcherArray) {
					if (dude.getPositionInOrganization() == i && (dude.getCitations() >= M.promotionTreshold*citationaverage[i-1])) 
					{
						dude.setPositionInOrganization(i+1);
						PromoteByLevels[i-1]++;
						PromotionAge[i-1]+=dude.getYearsInAcademia();
					}
				}			
			}
		}
		if(M.promotionModel=="Fixed_HC"){
			for (int i=0;i<4;i++)
			{
//				citationaverage[i]= CitationsByLevels[i]/((double) ResearchersByLevels[i]);
				PromoteByLevels[i]=M.PositionLevels[i]-ResearchersByLevels[i];// n‰in monta tarvitaan lis‰‰ alemmilta tasoilta
				PromotionAge[i]=0;
			}
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
		

	public void evenAllocation() {
		double nettoTutkimusResurssit = M.maksimiTutkimusResurssi*M.AllocatableResource; 
		double varmatTutkimusResurssit = nettoTutkimusResurssit*M.kuinkaPaljonJaetaanTasan; //Jaetaan tasaisesti populaation kesken
		double varmatTutkimusResurssitPerTutkija = varmatTutkimusResurssit/researcherArray.size();
		for (Researcher researcher: researcherArray) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			researcher.setTimeForResearch();
		}		
	}
	
	public void randomAllocation() {
		double nettoTutkimusResurssit = M.maksimiTutkimusResurssi*M.AllocatableResource; 
		double varmatTutkimusResurssit = nettoTutkimusResurssit*M.kuinkaPaljonJaetaanTasan; //Jaetaan tasaisesti populaation kesken
		double varmatTutkimusResurssitPerTutkija = varmatTutkimusResurssit/researcherArray.size();
		funder.funds.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit); 
		funder.funds.varmaFunding = varmatTutkimusResurssitPerTutkija;

		Collections.shuffle(researcherArray);//sekoitetaan

		for (Researcher researcher: researcherArray) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			funder.receiveResearcher(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
		}		
	}

	public void grantAllocation(double arviointiVirhe) {
		ArrayList<Researcher> temp2 = new ArrayList<>();
		ArrayList<Researcher> temp3 = new ArrayList<>();
		ArrayList<Researcher> temp4 = new ArrayList<>();
		double nettoTutkimusResurssit = (1-M.overhead)*M.maksimiTutkimusResurssi*M.AllocatableResource; 		
		double varmatTutkimusResurssit = nettoTutkimusResurssit*M.kuinkaPaljonJaetaanTasan; 
		double varmatTutkimusResurssitPerTutkija = varmatTutkimusResurssit/researcherArray.size();
		funder.funds.varmaFunding = varmatTutkimusResurssitPerTutkija;

		for (Researcher researcher: researcherArray) {
			researcher.setQualityOfApplication(arviointiVirhe);
			int taso =researcher.getPositionInOrganization();
			if (taso==2) {
				temp2.add(researcher);
			}
			if (taso==3) {
				temp3.add(researcher);
			}
			if (taso==4) {
				temp4.add(researcher);
			}
		}
		for (Researcher researcher: temp2)
		{
			researcherArray.remove(researcher);
		}
		for (Researcher researcher: temp3)
		{
			researcherArray.remove(researcher);
		}
		for (Researcher researcher: temp4)
		{
			researcherArray.remove(researcher);
		}
		
		Collections.sort(researcherArray, vertaaja);

		Collections.reverse(researcherArray);
		funder.funds.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*researcherArray.size()/M.PopulationSize; 
		for (Researcher researcher: researcherArray) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.receiveResearcher(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
		}
		Collections.sort(temp2, vertaaja);

		Collections.reverse(temp2);
		funder.funds.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp2.size()/M.PopulationSize; 
		for (Researcher researcher: temp2) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.receiveResearcher(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		Collections.sort(temp3, vertaaja);

		Collections.reverse(temp3);
		funder.funds.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp3.size()/M.PopulationSize; 
		for (Researcher researcher: temp3) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.receiveResearcher(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		Collections.sort(temp4, vertaaja);

		Collections.reverse(temp4);
		funder.funds.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp4.size()/M.PopulationSize; 
		for (Researcher researcher: temp4) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.receiveResearcher(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		temp2.clear();
		temp3.clear();
		temp4.clear();
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

	private void compareReceivedMoney() {
		mon.res=0;
		for (Researcher researcher : researcherArray) {
			mon.res+=researcher.getResourcesForResearch();
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

	public void allocate(){
		if(M.allocationScheme=="Communism") {evenAllocation();}
		if(M.allocationScheme=="Lottery") {randomAllocation();}
		if(M.allocationScheme=="Grant") {grantAllocation(M.arviointiVirhe);} 
		}
	

	public void iterate (int count){
		for (int loop=0; loop< count; loop++) 
		{
			RemoveResearchers();
			TTpromote();
			AddResearchers();
			allocate();
			compareReceivedMoney(); //Compare what got and what not, sets frustration and productivity
			publishTT(year);
			CountByLevels();
			mon.updateCounters();
			year++;
		}
		
	}
	public void simulate()  {
		M.resetGrant();
		M.configureFulltest64b(0);; //to initialize the headings
		mon.setHeadings();
		M.setNarrative();
		for(int koe=0; koe<64 ; koe++) 
		{
			M.configureFulltest64b(koe);
			initialize();
			mon.resetCounters();
			iterate(100); //warm up
			for (int sample=0; sample < 10; sample++)
			{
				mon.resetCounters();
				iterate(200); // single sample
				mon.logReport(200);
//				System.out.println(mon.cumRes);
				iterate(50); //separation

			} //end sampling

			researcherArray.clear();
			oldPapers.clear();
			year = 0;
		} //end koe
		mon.logNarrative();
		

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		simulation test = new simulation();

		test.simulate();


	}

}