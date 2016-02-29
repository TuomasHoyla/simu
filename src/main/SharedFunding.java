package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resources.Researcher;

public class SharedFunding {
    double varmaFunding;
    double funding;
    
    public SharedFunding(){
        
    }
    
    public double jaaRahaa(double haluttu) {
        
        double palautettavaa = 0;
        
        if (funding >= haluttu) {
        funding -= haluttu;
        palautettavaa = haluttu;
        }
        else {
            palautettavaa = funding;
            funding = 0;
        }
   //     System.out.println(palautettavaa);
        return palautettavaa;   
    }

    public void setFunds(Researcher researcher, double amount) {
    	researcher.consumeMoney();
    	amount = researcher.getResourcesNeededToBeMotivated()-researcher.ResourcesForResearch;
    	researcher.setMoney(jaaRahaa(amount)+varmaFunding);        
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
		double nettoTutkimusResurssit = (1-simulation.M.overhead)*simulation.M.maksimiTutkimusResurssi*simulation.M.AllocatableResource; 		
		double varmatTutkimusResurssit = nettoTutkimusResurssit*simulation.M.kuinkaPaljonJaetaanTasan; 
		double varmatTutkimusResurssitPerTutkija = varmatTutkimusResurssit/simulation.O.researcherArray.size();
		varmaFunding = varmatTutkimusResurssitPerTutkija;
		for(int i=0; i<4; i++) { headcount[i]=0;}
		
		for (Researcher researcher: simulation.O.researcherArray) {
			researcher.setQualityOfApplication(simulation.M.arviointiVirhe);
			headcount[researcher.getPositionInOrganization()-1]++;
		}

		Collections.sort(simulation.O.researcherArray, vertaaja3);
		Collections.reverse(simulation.O.researcherArray);
		int ii=0;
		for(int i=4; i>0; i--) {
			funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*headcount[i-1]/simulation.M.PopulationSize; 
			int iii=ii+headcount[i];	
			for (Researcher researcher: simulation.O.researcherArray.subList(ii, iii)) {
				researcher.setResourcesNeededToBeMotivated(simulation.M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
				researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
				setFunds(researcher, simulation.M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
				researcher.setTimeForResearch();
			}
			ii=iii;
		}
   } 
}

