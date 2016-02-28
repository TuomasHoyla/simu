package resources;
import main.*;
import java.io.*;




public class Monitor {
//	private static int[] CumPositionLevels= new int[4];
	private static int[] CumResearchersByLevels= new int[4];
	private static int[] CumPapersByLevels= new int[4];
	private static int[] CumCurrentPapers = new int[4];
	private static int[] CumCurrentCitations = new int[4];	
	private static int[] CumCitationsByLevels= new int[4];
	private static double[] CumSkillByLevels = new double[4];
	private static double[] CumFrustrationByLevels = new double[4];
	private static int[] CumResignByLevels = new int[4];
	private int cumResign;
	private static int[] CumPromoteByLevels = new int[4];
	private static int[] CumRetirementAge = new int[4];
	private static int[] CumPromotionAge = new int[4];
	private int cumCitationsFromRemoved;
	private int cumPapersFromRemoved;
	private int TotalCurrentCitations;
	private int TotalCurrentPapers;
	private double TotalSkill;
	private double TotalFrustration;
	public double res;
	public double cumRes;


public void resetCounters()
{
	for (int i=0; i<4;i++)
	{
//		CumPositionLevels[i]=0;
		CumResearchersByLevels[i]=0;
		CumCitationsByLevels[i]=0;
		CumPapersByLevels[i]=0;
		CumCurrentPapers[i]=0;
		CumCurrentCitations[i]=0;
		CumSkillByLevels[i]=0;
		CumResignByLevels[i]=0;
		CumPromoteByLevels[i]=0;
		CumFrustrationByLevels[i]=0;
		CumRetirementAge[i]=0;
		CumPromotionAge[i]=0;
	}
	cumCitationsFromRemoved=0;
	cumPapersFromRemoved=0;
	cumResign=0;
	cumRes=0;

}
public void updateCounters()
{
	for (int i=0;i<4; i++)
	{
		CumResearchersByLevels[i]+=simulation.ResearchersByLevels[i];
		CumPapersByLevels[i]+=simulation.PapersByLevels[i];
		CumCitationsByLevels[i]+=simulation.CitationsByLevels[i];
		CumSkillByLevels[i]+=simulation.SkillByLevels[i];
		CumCurrentCitations[i]+=simulation.CurrentCitations[i];
		CumCurrentPapers[i]+=simulation.CurrentPapers[i];
		CumResignByLevels[i]+=simulation.ResignByLevels[i];
		CumPromoteByLevels[i]+=simulation.PromoteByLevels[i];
		CumFrustrationByLevels[i]+=simulation.FrustrationByLevels[i];
		CumRetirementAge[i]+=simulation.RetirementAge[i];
		CumPromotionAge[i]+=simulation.PromotionAge[i];
	}
	cumCitationsFromRemoved+=simulation.citationsFromRemovedResearchers;
	cumPapersFromRemoved+=simulation.papersFromRemovedResearchers;
	cumRes+=res;
}
public void reportHeadings() {
	System.out.println(simulation.M.narrative);
}
public void setHeadings() {
	try {
		FileRead.writeLines3(simulation.M.caseheader+"; Ladder; HeadCount; Skill; YearlyPapers; YearlyCitations; Resignations; Promotions; Frustration; RetirementAge; PromotionAge",simulation.M.datafile);		
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		e.printStackTrace();
	}	
	try {
		FileRead.writeLines3(simulation.M.caseheader+"; Skill; Frustration; YearlyPapers; YearlyCitations; OldCitations; OldPapers; Recruitments",simulation.M.totalfile);		
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		e.printStackTrace();
	}	
	
}
public void logNarrative() {
	try {
		FileRead.writeLines3(simulation.M.narrative,simulation.M.logfile);
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		e.printStackTrace();
	}	
}
public void report(int y){
	double yd = y;
	for (int i=0;i<4;i++) {
		/*
		System.out.print(simulation.M.instanssi +"; "+(i+1)+"; "+CumResearchersByLevels[i]/yd+"; "+CumPapersByLevels[i]/yd+"; "+CumCitationsByLevels[i]/yd+"; "+CumSkillByLevels[i]/yd);
		System.out.print("; "+CumCurrentPapers[i]/yd+"; "+CumCurrentCitations[i]/yd);
		System.out.println("; "+CumResignByLevels[i]/yd+"; "+CumPromoteByLevels[i]/yd+"; "+CumFrustrationByLevels[i]/yd +"; "+CumRetirementAge[i]/yd+"; "+CumPromotionAge[i]/yd);
		*/
		System.out.print(simulation.M.instanssi +"; "+(i+1)+"; "+CumResearchersByLevels[i]/yd+"; "+CumSkillByLevels[i]/CumResearchersByLevels[i]);
		System.out.print("; "+CumCurrentPapers[i]/CumResearchersByLevels[i]+"; "+CumCurrentCitations[i]/CumResearchersByLevels[i]);
		System.out.println("; "+CumResignByLevels[i]/CumResearchersByLevels[i]+"; "+CumPromoteByLevels[i]/CumResearchersByLevels[i]+"; "+CumFrustrationByLevels[i]/CumResearchersByLevels[i] +"; "+CumRetirementAge[i]/CumResignByLevels[i]+"; "+CumPromotionAge[i]/(CumPromoteByLevels[i]+0.0001));
		
	}

}

public void logReport(int y){
	double yd = y;
	String line=" ";
	for (int i=0;i<4;i++) {
		/*
		line= simulation.M.instanssi +"; "+(i+1)+"; "+CumResearchersByLevels[i]/yd+"; "+CumPapersByLevels[i]/yd+"; "+CumCitationsByLevels[i]/yd+"; "+CumSkillByLevels[i]/yd;
		line+="; "+CumCurrentPapers[i]/yd+"; "+CumCurrentCitations[i]/yd;
		line+="; "+CumResignByLevels[i]/yd+"; "+CumPromoteByLevels[i]/yd+"; "+CumFrustrationByLevels[i]/yd +"; "+CumRetirementAge[i]/yd+"; "+CumPromotionAge[i]/yd;
		*/
		line=simulation.M.instanssi +"; "+(i+1)+"; "+CumResearchersByLevels[i]/yd+"; "+CumSkillByLevels[i]/CumResearchersByLevels[i];
		line+="; "+(CumCurrentPapers[i]+0.00001)/CumResearchersByLevels[i]+"; "+(CumCurrentCitations[i]+.00001)/CumResearchersByLevels[i];
		line+="; "+(CumResignByLevels[i]+.000001)/CumResearchersByLevels[i]+"; "+(CumPromoteByLevels[i]+.00001)/CumResearchersByLevels[i]+"; "+CumFrustrationByLevels[i]/CumResearchersByLevels[i] +"; "+(CumRetirementAge[i]+.00001)/CumResignByLevels[i]+"; "+CumPromotionAge[i]/(CumPromoteByLevels[i]+0.0001);
		try {
			FileRead.writeLines3(line,simulation.M.datafile);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	countTotals();
	line= simulation.M.instanssi +"; "+TotalSkill/yd+"; "+TotalFrustration/yd+"; "+TotalCurrentPapers/yd+"; "+TotalCurrentCitations/yd+"; "+cumCitationsFromRemoved/yd+"; "+cumPapersFromRemoved/yd+"; "+cumResign/yd;
		try {
			FileRead.writeLines3(line,simulation.M.totalfile);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	

	

}
public void countTotals(){
	TotalCurrentPapers=0;
	TotalCurrentCitations=0;
	TotalSkill=0;
	TotalFrustration=0;
	cumResign=0;
	
	for (int i=0;i<4; i++) {
		TotalCurrentPapers+=CumCurrentPapers[i];
		TotalCurrentCitations+=CumCurrentCitations[i];
		TotalSkill+=CumSkillByLevels[i];
		TotalFrustration+=CumFrustrationByLevels[i];
		cumResign+=CumResignByLevels[i];
	}
}
}
