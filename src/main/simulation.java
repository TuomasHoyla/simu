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
import resources.*;

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