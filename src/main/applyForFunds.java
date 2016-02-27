package main;

import resources.Researcher;


public class applyForFunds  {
    
    SharedFunding funds = new SharedFunding();
    
    public void receiveResearcher(Researcher researcher, double amount) {
    
        funds.setFunds(researcher, amount);
        
    }


}
