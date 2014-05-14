package fr.inrialpes.exmo.mlid;


import java.util.ArrayList;
import java.util.List;

import fr.inrialpes.exmo.mlid.babelNet.BabelNetService;
import fr.inrialpes.exmo.mlid.preprocess.LowerCase;
import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.StopWord;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class MainComparateur {

	/**
	 * Chemin vers le dossier contenant les fichiers à traiter de langue anglaise
	 */
	private static String pathDirectoryLang1 = "C:/Users/Giovanni/Desktop/File/En";
	
	/**
	 * Chemin vers le dossier contenant les fichiers à traiter de langue française
	 */
	private static String pathDirectoryLang2 = "C:/Users/Giovanni/Desktop/File/Fr";
	
	/**
	 * Chemin ver le fichier de rapport
	 */
	private static String pathDirectoryReport = "C:/Users/Giovanni/Desktop/File/Report/report.txt";
	
	/**
	 * Variable indiquant que le terme ne dispose pas d'id babelnet
	 */
	private static String noResult = "No result found";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> testComp1 = FileUtil.getListOfText(pathDirectoryLang1);
		List<String> testComp2 = FileUtil.getListOfText(pathDirectoryLang2);
		/* liste qui contiendra les listes de termes */
		List<List<String>>listOfList = new ArrayList<List<String>>();
		
		/* on récupère pour chaque document de langue anglaise leur liste de terme */
		/* à partir de cette liste de terme on récupère la liste d'id babelenet correspondante */
		int i =1;
		for(String text : testComp1){
			/* récupération de la liste de terme */
			PreprocessFilter lowNStop = new StopWord(new LowerCase(text), "en");
			List<String> listOriginal = lowNStop.getList();
			
			/* récupération des id correspondant */
			List<String>listIdBabel1 = BabelNetService.getListBabelNetId(listOriginal, "en");
			//System.out.println(listIdBabel1.toString());
			
			/* écriture dans un fichier des termes n'ayant pas d'id babelnet */
			Comparateur.reportElementNotFound(listOriginal, listIdBabel1, pathDirectoryReport);
			
			/* suppression de ses termes de la liste */
			Comparateur.filterTerm(listIdBabel1, noResult);
			
			/*ajout du nom de fichier en tête de liste */
			listIdBabel1.add(0, i+"en");
			
			/* ajout de l'élément à la liste de liste */
			listOfList.add(listIdBabel1);
			
			i++;
		}
		
		i=1;
		/* on récupère pour chaque document de langue française leur liste de terme 
		/* à partir de cette liste de terme on récupère la liste d'id babelenet correspondante */
		for(String text : testComp2){
			PreprocessFilter lowNStop = new StopWord(new LowerCase(text), "fr");
			List<String> listOriginal2 = lowNStop.getList();
			
			List<String>listIdBabel2 = BabelNetService.getListBabelNetId(listOriginal2, "fr");
			Comparateur.reportElementNotFound(listOriginal2, listIdBabel2, pathDirectoryReport);
			
			Comparateur.filterTerm(listIdBabel2, noResult);
			
			listIdBabel2.add(0, i+"fr");
			
			listOfList.add(listIdBabel2);
			
			i++;
		}
		Comparateur testComp = new Comparateur(listOfList);
		testComp.compare();
	}

	
}
