package resources;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import main.*;

import org.apache.commons.math3.distribution.NormalDistribution;

public class RandomGenerator {
	
	private Random randgenerator;
	private static SecureRandom random;
	private NormalDistribution Ndf;
	static double r;
	
	public RandomGenerator() {
		
		randgenerator = new Random();
		random = new SecureRandom();
		Ndf = new NormalDistribution();
		
	}
	
	public RandomGenerator(long seed) {
		
		randgenerator = new Random(seed);
		random = new SecureRandom();
		Ndf = new NormalDistribution();
		
	}
	


	public double createRandomDouble() {
		return randgenerator.nextDouble();
	}

	public double createNormalDistributedValue(double mean, double sDeviation) {
		double distributedValue =randgenerator.nextGaussian()*sDeviation+mean;
		return distributedValue;
	}

	public double createNormalDistributedValue() {
		return randgenerator.nextGaussian();
	}

	/**
	 * For creating random names for researchers
	 * @return
	 */
	public static String nextSessionId(){
		return new BigInteger(40, random).toString(32);
	}
	/**
	 * The method will split the papers citations for more than 1 year
	 * @param thisYear
	 * @param creationYear
	 * @return
	 */

	/**
	 * 
	 * @param beta, Global parameter
	 * @param skill research skill, fitness
	 * @param t year
	 * @param immediacy How long it takes to reach a peak
	 * @param longevity Decay rate
	 * @return
	 */
	public double Mic(double m, double beta, double skill, int t, double immediacy, double longevity) {
		//No "A"
		double ni = skill;
		double b = beta;
		if(t==0) return 0.;
		return m*(Math.pow(Math.E, b*ni*Ndf.density((Math.log(t)-immediacy)/longevity))-1);
	}		

	/**
	 * 
	 * @param number Divide double n to random parts and add those parts to array 
	 * @param part array of randomly divided numbers
	 * @return
	 */

	public double createLogDistributedRandomValue(double location, double scale) {
		double temp= randgenerator.nextGaussian()*scale+location;
		double value = Math.exp(temp);
		return value;
	}
	public int createPoisson2(double s)
	{
		double temp= randgenerator.nextDouble();
		double test0= Math.exp(-s);
		if( temp< test0) return 0;
		double test= test0;
		for (int k=1;k<40; k++)
		{
			test0= test0*s/k;
			test+=test0;
			if(temp < test) {
				return k;
			}
		}
		return 40;
	}
	/**
	 * Creates a random value between given border values
	 * @param start first number
	 * @param end last number
	 * @return
	 */
	public int nextInt(int start, int end) {
		return (int)(start+Math.random()*end);
	}

	public double nextDouble(double start, double end) {
		return (start+Math.random()*end);
	}


	/**Returns logNormal distributed value between 0-inf, usually around  1. -0.0637 is because otherwise the average would be that much over one.
	 * 
	 * @return Skill (Applying or research, can be used for both)
	 */
	/*
	 * Corrected to a controllable skill parameter!!
	 */
	public double createSkill() {

		return createLogDistributedRandomValue(0.0, simulation.M.skillParameter);

	}
	


	public double createResSkill() {
		
		
			return createLogDistributedRandomValue(0, simulation.M.researchSkillParameter);

	
	}


}
