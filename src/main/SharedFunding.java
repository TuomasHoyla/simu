package main;

import java.util.Collections;
import java.util.Comparator;

import resources.Researcher;

public class SharedFunding {
    double securedFunding;
    double funding;
    
    public SharedFunding(){
        
    }
    
    public double giveGrant(double wantedAmount) {
        
        double grantSize = 0;
        
        if (funding >= wantedAmount) {
        funding -= wantedAmount;
        grantSize = wantedAmount;
        }
        else {
            grantSize = funding;
            funding = 0;
        }
        return grantSize;   
    }

    public void setFunds(Researcher researcher, double amount) {
    	researcher.consumeMoney();
    	amount = researcher.getResourcesNeededToBeMotivated()-researcher.ResourcesForResearch;
    	researcher.setMoney(giveGrant(amount)+securedFunding);        
    }

    public void setFunds(double d) {
        funding = d;
        
    }
    public void allocate(){

		Comparator<Researcher> vertaaja3 = new Comparator<Researcher>(){
			public int compare(Researcher p1, Researcher p2) {
				if (p1.getPositionInOrganization() < p2.getPositionInOrganization()) return -1;
				if (p1.getPositionInOrganization() > p2.getPositionInOrganization()) return 1;
				if (p1.getQualityOfApplication() < p2.getQualityOfApplication()) return -1;
				if (p1.getQualityOfApplication() > p2.getQualityOfApplication()) return 1;
				return 0;
			}
		};
		int[] headcount=new int[4];
		double netResources = (1-simulation.M.overhead)*simulation.M.maximumResearchEffort*simulation.M.AllocatableResource; 		
		double securedResources = netResources*simulation.M.evenlyDistributedPart; 
		double securedFundingLevel = securedResources/simulation.O.researcherArray.size();
		securedFunding = securedFundingLevel;
		for(int i=0; i<4; i++) { headcount[i]=0;}
		
		for (Researcher researcher: simulation.O.researcherArray) {
			researcher.setQualityOfApplication(simulation.M.evaluationError);
			headcount[researcher.getPositionInOrganization()-1]++;
		}

		Collections.sort(simulation.O.researcherArray, vertaaja3);
		Collections.reverse(simulation.O.researcherArray);
		int ii=0;
		for(int i=4; i>0; i--) {
			funding =(netResources-securedResources)*headcount[i-1]/simulation.M.PopulationSize; 
			int iii=ii+headcount[i-1];	
			for (Researcher researcher: simulation.O.researcherArray.subList(ii, iii)) {
				researcher.setResourcesNeededToBeMotivated(simulation.M.desiredResearchEffort);
				researcher.setMoney(securedFundingLevel); 
				setFunds(researcher, simulation.M.desiredResearchEffort-securedFundingLevel);
				researcher.setTimeForResearch();
			}
			ii=iii;
		}
   } 
}

