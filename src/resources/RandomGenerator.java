package resources;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import main.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;



public class RandomGenerator {
	Random randgenerator = new Random();
	private static SecureRandom random = new SecureRandom();
	PoissonDistribution papergenerator = new PoissonDistribution(0.1);
	private NormalDistribution Ndf = new NormalDistribution();
	static double r;
	double powerOfNoise = 4.5; //This indicates how much given resources affect to noise in the system; ie. if 3.0, 50% of resources cause 30% noise, and if 4.5, 50% allocation creates 16.3% noise.

//	}
	public double createRandomDouble() {
		r =randgenerator.nextDouble();
		return r;
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

	public int getSkipperNumber() {
		return randgenerator.nextInt();
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
	public double[] divideUniformlyRandomly(double number, int part) {
		double uniformRandoms[] = new double[part];

		double mean = number / part;
		double sum = 0.0;

		for (int i=0; i<part / 2; i++) {
			uniformRandoms[i] = randgenerator.nextDouble() * mean;
			uniformRandoms[part - i - 1] = mean + randgenerator.nextDouble() * mean;
			sum += uniformRandoms[i] + uniformRandoms[part - i -1];
		}
		uniformRandoms[(int)Math.ceil(part/2)] = uniformRandoms[(int)Math.ceil(part/2)] + number - sum;
		return uniformRandoms;
	}


	public static double logb( double a, double b )
	{
		return Math.log(a) / Math.log(b);
	}

	public double log1_5( double a )
	{
		return logb(a,1.5);
	}
	

	/**
	 * Creates exponential array for user interface
	 * @param x
	 * @param linear
	 * @param curve
	 * @return
	 */
	public double fExp(double x, double linear, double curve) {
		return Math.exp(x*linear)*curve;


	}
	/**
	 * Creates linear array for user interface
	 * @param slope
	 * @param x
	 * @param Intercept
	 * @return
	 */
	public double fLin(double slope, double x, double Intercept) {
		return slope*x+Intercept; // mx+b
	}

	/**
	 * 
	 * @param location μ, 0
	 * @param scale σ , 0.25
	 * @return distributed value
	 */
	public double createLogDistributedRandomValue(double location, double scale) {
		double temp= randgenerator.nextGaussian()*scale+location;
		double value = Math.exp(temp);
		return value;
	}
	public int createPoisson(double time)
	{
		int temp=0;
		for (int i=0; i< time ; i++)
		{
			temp+=papergenerator.sample();
		}
		return temp;
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

	/**Returns logNormal distributed value between 0-inf, usually around  1. -0.0637 is because otherwise the average would be that much over one.
	 * 
	 * @return Skill (Applying or research, can be used for both)
	 */
	public double createSkill() {

		if(simulation.M.skillModel=="LogNormal") {
		return createLogDistributedRandomValue(0.0, 0.35);
		}
		else if(simulation.M.skillModel=="LogNormalScaled") {
			return createLogDistributedRandomValue(0.0, 0.35)/1.0637;
		}
			
		else return 1.0;
	}
	
	/**
	 * Sub-method for Mic Model to create beta value
	 * @return random beta between 0 and 4.164, mean ~ 1
	 */
	private double createBetaForMic() {
		return createLogDistributedRandomValue(0.0, 0.29)-0.043;
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



	public double createResSkill() {
		
		
		if(simulation.M.researchSkillModel=="LogNormal") {
//			return 2*random.nextDouble()*createLogDistributedRandomValue(0, simulation.M.researchSkillParameter);
			return createLogDistributedRandomValue(0, simulation.M.researchSkillParameter);
		}
		else {
			System.out.println("Res skill");
			return 1.0;
		}
	
	}


}
