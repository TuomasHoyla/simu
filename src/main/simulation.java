package main;

/** TODO
 * 
 * - simulointi-luokkaa pit�nee faktoroida niin, ett� irrotetaan organisaatioon liittyv�t osat (organisaatiokohtainen tutkijapooli, rekrytointi ja promootiomekanismit jne) omaksi luokakseen, mik� mahdollistaisi jatkossa useamman organisaation simuloinnin ja j�tt�isi simulointiluokan vastaamaan kokonaisuuden rakenteesta ja simulointikokeen ajamisesta
- Model luokan sis�ll�ist� irrotetaan osa experiment-luokaksi (koekaavion hallinta ja mallin muokkaus faktori faktorilta, toistojen m��r�t yms, sy�tt� ja tulostiedostot
- mallin sis�ll�lliset luokat (kuten Researcher, paper uusi organisation luokka) kirjoitetaan takaisin k�ytt�m��n sis�isi� muuttujia (Model-luokan muuttujien sijaan) ja lis�t��n config tms metodi, joka alustaa sis�iset vakiot/parametrit Model-luokan pohjalta. T�ll�in Model-luokkaan teht�v�t viittaukset voivat olla tarvittaessa ep�suorempia. mahdollisesti sis�lt�luokat vastaisivat omasta tiedon monitoroinnistaan.

Kokonaiskuva olisi jotain seuraavan kaltaista:
- luetaan "experiment_config" -tiedosto, joka m��rittelee simuloinnin keston, toistot, mittauspisteet sek� koekaavion (faktorit, tasot) ja mallin oletuskonfiguraation tiedoston ja tulostiedostot kuvausteksteineen
- luetaan mallin oletuskonfi (parametrien nimi + arvoparit)
- parseroidaan koekaavio ja jokaiselle tunnistetulle kokeelle
-- alustetaan malli oletusarvoon
-- konfataan malli a.o. kokeen faktorien mukaan
-- alustetaan mallin sis�lt�luokat konfatun mallin mukaiseksi ja tulosmonitorit koesuunnitelman mukaisiksi
-- simuloidaan koepisteen malliversio m��ritellyin toistoin
-- tallennetaan tulokset

Ainakin itsest�ni tuo yll�kuvattu vaikuttaa p��osin suoraviivaiselta. Omat Java-taitoni t�ll� hetkell� ylitt�vi� osuuksia ovat
- robusti tietojen luku

- mallin virkarakenteen yleist�minen (nyt hard-koodattu nelj� tasoa mutta n-tasoa olisi joustavampi). 
En aikanaan onnistunut tekem��n tutkijalistoista taulukkoa, jolloin saisi luupattua yli tasokohtaisten 
listojen ja voisi pit�� eri tasojen kohortit erillisin�

- 2-k kokeen konfaus (tiedostosta luettavalle faktorilistalle ja k:n arvolle) 
(edellytt�nee jonkin tyyppist� rekursiota, jos k ei ole kiinte�) 
[k�yt�nn�ss� experiment konfi voisi sis�lt�� koekaavion tyyliin
faktori; tasojen m��r�; arvo1; arvo2; jne]
 * 
 */


import java.text.DecimalFormat;

import configreader.getPropertyValues;

import java.util.ArrayList;
import java.util.Properties;

import resources.*;

public class simulation {
	
	public static Model M = new Model();
	public static Modelv2 M2 = new Modelv2();
	public static Monitor mon = new Monitor();
	public static Organisation O = new Organisation();
	FileRead fileread = new FileRead();
	DecimalFormat df = new DecimalFormat("#.##");
	SharedFunding funder = new SharedFunding();
	public static RandomGenerator randomGenerator = new RandomGenerator();
	public static Properties configs ;
	public static Properties experiment;
	public static boolean headingSet=false;

	int year;

	//Default constructor
	public simulation() {
		// TODO use default simulation configuration
		getPropertyValues configfile = new getPropertyValues("resources/config.properties"); //lacks other file as a parameter
		getPropertyValues experimentfile = new  getPropertyValues("resources/experiment.properties");
		try {
		configs = configfile.getPropValues();
		experiment = experimentfile.getPropValues();
		}
		catch (Exception e) {}
	}

	//Constructor if file exists
	public simulation(String referenceStateFile, String expFile) {
		// TODO read files
		getPropertyValues configfile = new getPropertyValues(referenceStateFile); //lacks other file as a parameter
		getPropertyValues experimentfile = new  getPropertyValues(expFile);
		try {
		configs = configfile.getPropValues();
		experiment = experimentfile.getPropValues();
		}
		catch (Exception e) {}
	}
	
	
	public void iterate (int count){
		for (int loop=0; loop< count; loop++) 
		{
			O.RemoveResearchers();
			O.promote();
			O.AddResearchers();
			funder.allocate();
			O.compareReceivedMoney(); //Compare what got and what not, sets frustration and productivity
			O.publish(year);
			O.CountByLevels();
			mon.updateCounters();
			year++;
		}
		
	}
	
	public void runExperiment(String instance) {
		year = 0;
		O.initialize ();
		mon.resetCounters();
		iterate(M.warmUp);
		for (int sample=0; sample < M.repetitionCount; sample++){
			mon.resetCounters();
			iterate(M.runLength); // single sample
			mon.logReport2(M.runLength, instance);
			iterate(M.safetyDistance); //separation
		} //end sampling
		O.researcherArray.clear();
		O.oldPapers.clear();
	}
	public void simulate()  {
		

M2.resetModel(configs);
M2.configureExperiments(experiment);
mon.setHeadings();	
runFactors(M2.factorName, M2.factorValues,"");
mon.logNarrative();
}

		



	public void runFactors(ArrayList<String> fName, ArrayList<String> fValues, String instance) {
		
		int size = fName.size();
		String name = fName.remove(size-1);
		String values = fValues.remove(size-1);
		String parsedValues[] = values.split(",");
		for(int i=0; i< parsedValues.length; i++) {
			M2.setFactor(name, parsedValues[i]);
			String newInstance = instance+";"+ parsedValues[i];
			if(size >1) {
			runFactors(fName, fValues, newInstance);}
			else {
				runExperiment(newInstance);
			}
		}
	}	
	

	/** This is the main method for simulation 
	 * @param args referenceStateFile, experimentFile
	 */
	public static void main(String[] args) {
		
		simulation test = null;
		
		/**
		 * Tries parameters. Runs as .jar as follows: "java -jar simulation.jar file1.conf file2.conf"
		 */
	    try {
	    	
	    	test = new simulation(args[0], args[1]);
	    	
	    }
	    catch (ArrayIndexOutOfBoundsException e){
	    	
	        System.out.println("ArrayIndexOutOfBoundsException caught: " + e + " no simulation setting file parameters given, using defaults");
	        
	        test = new simulation();
	    }
	    finally {
	    	
	    	test.simulate();
	    }
	    

		
	}

}