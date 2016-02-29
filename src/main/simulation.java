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




	public void grantAllocation(double arviointiVirhe) {
		ArrayList<Researcher> temp2 = new ArrayList<>();
		ArrayList<Researcher> temp3 = new ArrayList<>();
		ArrayList<Researcher> temp4 = new ArrayList<>();
		double nettoTutkimusResurssit = (1-M.overhead)*M.maksimiTutkimusResurssi*M.AllocatableResource; 		
		double varmatTutkimusResurssit = nettoTutkimusResurssit*M.kuinkaPaljonJaetaanTasan; 
		double varmatTutkimusResurssitPerTutkija = varmatTutkimusResurssit/researcherArray.size();
		funder.varmaFunding = varmatTutkimusResurssitPerTutkija;

		for (Researcher researcher: researcherArray) {
			researcher.setQualityOfApplication(arviointiVirhe);
			int taso =researcher.getPositionInOrganization();
			if (taso==2) {
				temp2.add(researcher);
			}
			if (taso==3) {
				temp3.add(researcher);
			}
			if (taso==4) {
				temp4.add(researcher);
			}
		}
		for (Researcher researcher: temp2)
		{
			researcherArray.remove(researcher);
		}
		for (Researcher researcher: temp3)
		{
			researcherArray.remove(researcher);
		}
		for (Researcher researcher: temp4)
		{
			researcherArray.remove(researcher);
		}
		
		Collections.sort(researcherArray, vertaaja);

		Collections.reverse(researcherArray);
		funder.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*researcherArray.size()/M.PopulationSize; 
		for (Researcher researcher: researcherArray) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.setFunds(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
		}
		Collections.sort(temp2, vertaaja);

		Collections.reverse(temp2);
		funder.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp2.size()/M.PopulationSize; 
		for (Researcher researcher: temp2) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.setFunds(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		Collections.sort(temp3, vertaaja);

		Collections.reverse(temp3);
		funder.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp3.size()/M.PopulationSize; 
		for (Researcher researcher: temp3) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.setFunds(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		Collections.sort(temp4, vertaaja);

		Collections.reverse(temp4);
		funder.funding =(nettoTutkimusResurssit-varmatTutkimusResurssit)*temp4.size()/M.PopulationSize; 
		for (Researcher researcher: temp4) {
			researcher.setResourcesNeededToBeMotivated(M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan);
			researcher.setMoney(varmatTutkimusResurssitPerTutkija); 
			funder.setFunds(researcher, M.kuinkaPaljonMaksimiTutkimisResurssistaHalutaan-varmatTutkimusResurssitPerTutkija);
			researcher.setTimeForResearch();
			researcherArray.add(researcher);
		}
		temp2.clear();
		temp3.clear();
		temp4.clear();
	}
	

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
	public void simulate(String configurationfile)  {
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
		M.configureFulltest64b(0);; //to initialize the headings
		mon.setHeadings();
		M.setNarrative();
		for(int koe=0; koe<64 ; koe++) 
		{
			M.configureFulltest64b(koe);
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
		
		/**
		 * Simulation itself added to comments for now, fileread example above
		 */
		//	simulation test = new simulation();
		//	test.simulate();
		
	}

}