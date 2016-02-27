package resources;

public class Funding {

	private double originalResources;
	private double resourcesThatAreLeftFromOriginalResources;
	private double skillRequiredForResources;
	private int levelRequiredForProposal;

	public Funding(double totalResourcesAvailable, double skillRequiredForResources, int levelRequiredForProposal) {
		this.originalResources = totalResourcesAvailable; //A:ta on tarjolla 0.5, B:ta 1.0
		this.skillRequiredForResources = skillRequiredForResources; //ApplyingSkill tulee kehiin vasta kun rahotuksia jaetaan
		this.levelRequiredForProposal = levelRequiredForProposal;
		resourcesThatAreLeftFromOriginalResources = originalResources;
	}

	public double getOriginalResources() {
		return originalResources;
	}
	
	public void addToOriginalResources(double resources) {
		this.originalResources += resources;
		this.resourcesThatAreLeftFromOriginalResources += resources;
	}
	
	public void setOriginalResources(double resources) {
		this.originalResources = resources;
	}

	public double giveResources(double skills) {
		double resourceTogiven = resourcesThatAreLeftFromOriginalResources*skills;
		return resourceTogiven;
	}
/*
	public void addResources(double money){
		this.resourcesThatAreLeftFromOriginalResources += money;
	}
	*/

	public void resetFundingCall() {
		resourcesThatAreLeftFromOriginalResources = originalResources;
	}

	public void setMoney(double resourcesThatAreLeftForApplying) {
		this.resourcesThatAreLeftFromOriginalResources = resourcesThatAreLeftForApplying;
	}

	public double getSkillRequired() {
		return skillRequiredForResources;
	}
/**
	public void setSkillRequired(int skillRequired) {
		this.skillRequiredForResources = skillRequired;
	}
	*/

	public int getLevelRequiredForProposal() {
		return levelRequiredForProposal;
	}
/*
	public void setLevelRequiredForProposal(int levelRequired) {
		this.levelRequiredForProposal = levelRequired;
	}
	*/
}
