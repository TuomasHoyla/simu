package main;

import resources.Researcher;

public class SharedFunding {
    double varmaFunding;
    double funding;
    
    public SharedFunding(){
        
    }
 /*   
    public double getFunding(){
        return funding;
    }
    
    public double takeFunding(double amount){
        if((funding - amount) >= 0){
            funding -= amount;
        }
//        System.out.prdoubleln("Funding taken: " + amount);
//        System.out.prdoubleln("Funding current: " + funding);
        return funding;
    }
 */   
    
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
}

