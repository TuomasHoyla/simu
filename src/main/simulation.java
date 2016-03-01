package main;

/** TODO
 * 
 * - simulointi-luokkaa pitänee faktoroida niin, että irrotetaan organisaatioon liittyvät osat (organisaatiokohtainen tutkijapooli, rekrytointi ja promootiomekanismit jne) omaksi luokakseen, mikä mahdollistaisi jatkossa useamman organisaation simuloinnin ja jättäisi simulointiluokan vastaamaan kokonaisuuden rakenteesta ja simulointikokeen ajamisesta
- Model luokan sisällöistä irrotetaan osa experiment-luokaksi (koekaavion hallinta ja mallin muokkaus faktori faktorilta, toistojen määrät yms, syöttö ja tulostiedostot
- mallin sisällölliset luokat (kuten Researcher, paper uusi organisation luokka) kirjoitetaan takaisin käyttämään sisäisiä muuttujia (Model-luokan muuttujien sijaan) ja lisätään config tms metodi, joka alustaa sisäiset vakiot/parametrit Model-luokan pohjalta. Tällöin Model-luokkaan tehtävät viittaukset voivat olla tarvittaessa epäsuorempia. mahdollisesti sisältöluokat vastaisivat omasta tiedon monitoroinnistaan.

Kokonaiskuva olisi jotain seuraavan kaltaista:
- luetaan "experiment_config" -tiedosto, joka määrittelee simuloinnin keston, toistot, mittauspisteet sekä koekaavion (faktorit, tasot) ja mallin oletuskonfiguraation tiedoston ja tulostiedostot kuvausteksteineen
- luetaan mallin oletuskonfi (parametrien nimi + arvoparit)
- parseroidaan koekaavio ja jokaiselle tunnistetulle kokeelle
-- alustetaan malli oletusarvoon
-- konfataan malli a.o. kokeen faktorien mukaan
-- alustetaan mallin sisältöluokat konfatun mallin mukaiseksi ja tulosmonitorit koesuunnitelman mukaisiksi
-- simuloidaan koepisteen malliversio määritellyin toistoin
-- tallennetaan tulokset

Ainakin itsestäni tuo ylläkuvattu vaikuttaa pääosin suoraviivaiselta. Omat Java-taitoni tällä hetkellä ylittäviä osuuksia ovat
- robusti tietojen luku

- mallin virkarakenteen yleistäminen (nyt hard-koodattu neljä tasoa mutta n-tasoa olisi joustavampi). 
En aikanaan onnistunut tekemään tutkijalistoista taulukkoa, jolloin saisi luupattua yli tasokohtaisten 
listojen ja voisi pitää eri tasojen kohortit erillisinä

- 2-k kokeen konfaus (tiedostosta luettavalle faktorilistalle ja k:n arvolle) 
(edellyttänee jonkin tyyppistä rekursiota, jos k ei ole kiinteä) 
[käytännössä experiment konfi voisi sisältää koekaavion tyyliin
faktori; tasojen määrä; arvo1; arvo2; jne]
 * 
 */


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resources.*;
import configreader.getPropertyValues;

public class simulation {
	
	public static Model M = new Model();
	public static Monitor mon = new Monitor();
	public static Organisation O = new Organisation();
	FileRead fileread = new FileRead();
	DecimalFormat df = new DecimalFormat("#.##");
	SharedFunding funder = new SharedFunding();
	public static RandomGenerator randomGenerator = new RandomGenerator();

	int year;



	public void iterate (int count){
		for (int loop=0; loop< count; loop++) 
		{
			O.RemoveResearchers();
			O.TTpromote();
			O.AddResearchers();
			funder.allocate();
			O.compareReceivedMoney(); //Compare what got and what not, sets frustration and productivity
			O.publishTT(year);
			O.CountByLevels();
			mon.updateCounters();
			year++;
		}
		
	}
	public void simulate()  {
/*		
M.readExperiment(configurationfile);
M.readModel();
mon.setHeadings();	
for (int koe=0; koe<M.kokeet; koe++) {
M.configure(koe);
O.initialize ()
mon.resetCounters();
iterate(M.warmUp);
			for (int sample=0; sample < M.sample; sample++)
			{
				mon.resetCounters();
				iterate(M.duration); // single sample
				mon.logReport(M.duration);
				iterate(M.separation); //separation
			} //end sampling
			O.researcherArray.clear();
			O.oldPapers.clear();
			year = 0;
		} //end koe
mon.logNarrative();

*/
		M.resetGrant();
		M.configure(0);; //to initialize the headings
		mon.setHeadings();
		M.setNarrative();
		for(int koe=1; koe<13 ; koe++) 
		{
			M.configure(koe);
			O.initialize();
			mon.resetCounters();
			iterate(100); //warm up
			for (int sample=0; sample < 10; sample++)
			{
				mon.resetCounters();
				iterate(200); // single sample
				mon.logReport(200);
				iterate(50); //separation

			} //end sampling

			O.researcherArray.clear();
			O.oldPapers.clear();
			year = 0;
		} //end koe
		mon.logNarrative();
		

	}

	

	/** This is the main method for simulation 
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		//From package configreader
		getPropertyValues configfile = new getPropertyValues();
		
		try {
			//List all the properties
			System.out.println(configfile.getPropValues());
			
			//print unique property
			System.out.println(configfile.getPropValues().getProperty("populationsize"));
		} catch (IOException e) {
			System.out.println("No config found!");
			e.printStackTrace();
		}
		*/
		
		/**
		 * Simulation itself added to comments for now, fileread example above
		 */
			simulation test = new simulation();
			test.simulate();
		
	}

}