package fr.inrialpes.exmo.mlid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.inrialpes.exmo.mlid.babelNet.BabelNetService;
import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class MainComparateur {

	/**
	 * Variable indiquant que le terme ne dispose pas d'id babelnet
	 */
	private static String noResult = "No result found";

	public static void main(String[] args) {

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// anglaise
		String pathDirectoryLang1;

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// française
		String pathDirectoryLang2;

		// Chemin ver le fichier de rapport
		String pathDirectoryReport;

		// Filtre à appliquer aux texte
		String cmd;

		// la taille des grammes
		String n;

		// dossier temporaire qui acceuillera les fichiers prétraités de
		// langue anglaise
		String pathTemp1;

		// dossier temporaire qui acceuillera les fichiers prétraités de
		// langue française
		String pathTemp2;

		// si on a pas précisé d'argument au lancement de l'application
		// on effectue les modification manuellement
		if (args.length == 0) {
			System.out
					.println("Pas d'argument! modifié les paramètres manuellement!");
			pathDirectoryLang1 = "C:/Users/Giovanni/Desktop/File/En";
			pathDirectoryLang2 = "C:/Users/Giovanni/Desktop/File/Fr";
			pathDirectoryReport = "C:/Users/Giovanni/Desktop/File/Report/report.txt";
			cmd = "Low+Stop";
			n = "3";
			pathTemp1 = pathDirectoryLang1 + "/mod";
			pathTemp2 = pathDirectoryLang2 + "/mod";

			/* si les répertoires mod n'existe pas on les crée */
			File mod1 = new File(pathTemp1);
			if (!mod1.exists()) {
				mod1.mkdir();
			}
			File mod2 = new File(pathTemp2);
			if (!mod2.exists()) {
				mod2.mkdir();
			}

			if (cmd.contains("NGr")) {
				// on applique les filtres sur les fichiers des deux dossiers
				PreprocessAppli.main(new String[] { pathDirectoryLang1,
						pathTemp1, cmd, "en", n });
				PreprocessAppli.main(new String[] { pathDirectoryLang2,
						pathTemp2, cmd, "fr", n });
			} else {
				PreprocessAppli.main(new String[] { pathDirectoryLang1,
						pathTemp1, cmd, "en" });
				PreprocessAppli.main(new String[] { pathDirectoryLang2,
						pathTemp2, cmd, "fr" });
			}

		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au dossier source 1
		// args[1] correspond au dossier source 2
		// args[2] correspond au dossier rapport
		// args[3] correspond au filtre à appliquer sur les fichiers
		// args[4] correspond à la taille du gramme
		else {
			pathDirectoryLang1 = args[0];
			pathDirectoryLang2 = args[1];
			pathDirectoryReport = args[2];
			cmd = args[3];

			pathTemp1 = pathDirectoryLang1 + "/mod";
			pathTemp2 = pathDirectoryLang2 + "/mod";

			/* si les répertoires mod n'existe pas on les crée */
			File mod1 = new File(pathTemp1);
			if (!mod1.exists()) {
				mod1.mkdir();
			}
			File mod2 = new File(pathTemp2);
			if (!mod2.exists()) {
				mod2.mkdir();
			}

			if (args.length == 5) {
				n = args[4];
				// on applique les filtres sur les fichiers des deux dossiers
				PreprocessAppli.main(new String[] { pathDirectoryLang1,
						pathTemp1, cmd, "en", n });
				PreprocessAppli.main(new String[] { pathDirectoryLang2,
						pathTemp2, cmd, "fr", n });
			} else {
				PreprocessAppli.main(new String[] { pathDirectoryLang1,
						pathTemp1, cmd, "en" });
				PreprocessAppli.main(new String[] { pathDirectoryLang2,
						pathTemp2, cmd, "fr" });
			}
		}

		List<String> testComp1 = FileUtil.getListOfText(pathTemp1);
		List<String> testComp2 = FileUtil.getListOfText(pathTemp2);
		/* liste qui contiendra les listes de termes */
		List<List<String>> listOfList = new ArrayList<List<String>>();

		/*
		 * on récupère pour chaque document de langue anglaise leur liste de
		 * terme
		 */
		/*
		 * à partir de cette liste de terme on récupère la liste d'id babelenet
		 * correspondante
		 */
		int i = 1;
		for (String text : testComp1) {
			/* récupération de la liste de terme */
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal = token.getList();

			/* récupération des id correspondant */
			List<String> listIdBabel1 = BabelNetService.getListBabelNetId(
					listOriginal, "en");
			// System.out.println(listIdBabel1.toString());

			/* écriture dans un fichier des termes n'ayant pas d'id babelnet */
			Comparateur.reportElementNotFound(listOriginal, listIdBabel1,
					pathDirectoryReport + "log_" + i + "en.txt");

			/* suppression de ses termes de la liste */
			Comparateur.filterTerm(listIdBabel1, noResult);

			/* ajout du nom de fichier en tête de liste */
			listIdBabel1.add(0, i + "en");

			/* ajout de l'élément à la liste de liste */
			listOfList.add(listIdBabel1);

			i++;
		}

		i = 1;
		/*
		 * on récupère pour chaque document de langue française leur liste de
		 * terme /* à partir de cette liste de terme on récupère la liste d'id
		 * babelenet correspondante
		 */
		for (String text : testComp2) {
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal2 = token.getList();

			List<String> listIdBabel2 = BabelNetService.getListBabelNetId(
					listOriginal2, "fr");
			Comparateur.reportElementNotFound(listOriginal2, listIdBabel2,
					pathDirectoryReport + "log_" + i + "fr.txt");

			Comparateur.filterTerm(listIdBabel2, noResult);

			listIdBabel2.add(0, i + "fr");

			listOfList.add(listIdBabel2);

			i++;
		}
		Comparateur testComp = new Comparateur(listOfList);
		testComp.compare();
	}

}
