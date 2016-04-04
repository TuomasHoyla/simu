package resources;
import main.*;
public class Paper {
	static RandomGenerator randGenerator = new RandomGenerator(simulation.M.paperSeed);
	
	private int citations = 0;
	private double m, beta, fitness, immediacy, longevity;
	private int creatingYear;
	private boolean isDead = false;

	/*
	 * Constructors
	 */
	public Paper(double m, double beta, double skill, int creatingYear, double immediacy, double longevity){

		this.m = m;
		this.beta = beta;
		this.fitness = skill;
		this.creatingYear = creatingYear;
		this.immediacy = immediacy;
		this.longevity = longevity;
		
		switch(simulation.M.paperQualityModel) {
		case("Skill_based"): {
			this.fitness=skill;	
			break;
		}
		case("Random_Skill_based"):{
			this.fitness= skill*simulation.randomGenerator.createLogDistributedRandomValue(0.0,simulation.M.paperQualityParameter);		
			break;
		}
		case("Skill_based_Random_m"): {
			this.m=m*simulation.randomGenerator.createLogDistributedRandomValue(0.0, 0.5);
			break;
		}
		}
/*		
		if(simulation.M.paperQualityModel=="Skill_based"){
				this.fitness=skill;
			}				
		if(simulation.M.paperQualityModel=="Random_Skill_based"){
				this.fitness= skill*simulation.randomGenerator.createLogDistributedRandomValue(0.0,simulation.M.paperQualityParameter);
			}				
		if(simulation.M.paperQualityModel=="Skill_based_Random_m"){
				this.m=m*simulation.randomGenerator.createLogDistributedRandomValue(0.0, 0.5);
			}						
*/
	}

	Paper() {
		this(1,1,1,1,1,1);
	}


	/**
	 * Citations by MiC model
	 * @param thisYear
	 * @param researcherSkill
	 */
	public int updateCitationsTT(int thisYear) {
		double temp= randGenerator.Mic(m, beta, fitness, (thisYear-getCreatingYear()), immediacy, longevity);
		if((thisYear -getCreatingYear()) >= 1 && temp <= 0.01) {isDead=true;}
		if(simulation.M.citationModel=="Wang"){
			int citationsToAdd = (int) Math.rint(2*temp);
			if (citationsToAdd >= 0) citations += citationsToAdd;
			return citationsToAdd;
		}
		else if(simulation.M.citationModel=="WangPoisson"){
			int citationsToAdd = randGenerator.createPoisson2(temp);
			if (citationsToAdd >= 0) citations += citationsToAdd;
			return citationsToAdd;
		}
		else return 0;
	}

	public int getCitations() {
		return citations;
	}

	public void setCitations(int citations) {
		this.citations = citations;
	}

	public int getCreatingYear() {
		return creatingYear;
	}
	public boolean isDead()
	{
		return isDead;
	}

}