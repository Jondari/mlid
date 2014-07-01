package fr.inrialpes.exmo.mlid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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

		Options option = new Options();

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// anglaise
		String pathDirectoryLang1 = null;
		Option d1 = new Option("d1", true,
				"dossier contenant les fichiers à traiter de langue anglaise");
		// Oblige le paramètre d'être présent lors de l'exécution
		d1.setRequired(true);

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// française
		String pathDirectoryLang2 = null;
		Option d2 = new Option("d2", true,
				"dossier contenant les fichiers à traiter de langue française");
		// Oblige le paramètre d'être présent lors de l'exécution
		d2.setRequired(true);

		// Chemin vers le fichier de rapport
		String pathDirectoryReport = null;
		Option f = new Option("f", true, "Chemin ver le fichier de rapport");
		// Oblige le paramètre d'être présent lors de l'exécution
		f.setRequired(true);

		// Filtre à appliquer aux textes
		String cmd = null;
		Option filtre = new Option("filtre", true,
				"Filtre à appliquer aux textes");
		// Oblige le paramètre d'être présent lors de l'exécution
		filtre.setRequired(true);

		Option nofilter = new Option("nf", false,
				"Pas d'application de filtres");
		// Oblige le paramètre d'être présent lors de l'exécution
		filtre.setRequired(true);

		// Creation du groupe d'option
		OptionGroup group = new OptionGroup();
		// Ajout des options exclusives
		group.addOption(filtre);
		group.addOption(nofilter);

		// Possibilite de rendre un groupe obligatoire
		group.setRequired(true);

		// la taille des grammes
		int n = 0;
		Option taille = new Option("taille", true, "Taille des grammes");

		option.addOption(d1);
		option.addOption(d2);
		option.addOption(f);
		// option.addOption(filtre);
		option.addOption(taille);
		// Ajout du groupe dans le conteneur Options
		option.addOptionGroup(group);

		// si on a pas précisé d'argument au lancement de l'application
		// on effectue les modification manuellement
		if (args.length == 0) {
			System.out
					.println("Vous n'avez pas entré d'argument! Ceci est un exemple d'exécution.");
			System.out
					.println("Vous pouvez modifier les paramètres manuellement!");

			// on récupère le non du système d'information
			String Os = System.getProperty("os.name");

			// on établis le chemin vers les dossiers d'exemple en fonction
			// des séparateurs du système d'exploitation
			if (Os.contains("Windows") || Os.contains("windows")) {
				pathDirectoryLang1 = System.getProperty("user.dir")
						+ "\\src\\example\\En";
				pathDirectoryLang2 = System.getProperty("user.dir")
						+ "\\src\\example\\Fr";
				pathDirectoryReport = System.getProperty("user.dir")
						+ "\\src\\example\\Report\\Report.txt";
			} else {
				pathDirectoryLang1 = System.getProperty("user.dir")
						+ "/src/example/En";
				pathDirectoryLang2 = System.getProperty("user.dir")
						+ "/src/example/Fr";
				pathDirectoryReport = System.getProperty("user.dir")
						+ "/src/example/Report/report.txt";
			}
			cmd = "Low+Stop";
			n = 3;
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {
				CommandLine cmds = parser.parse(option, args);
				pathDirectoryLang1 = cmds.getOptionValue("d1");
				pathDirectoryLang2 = cmds.getOptionValue("d2");
				pathDirectoryReport = cmds.getOptionValue("f");

				if (cmds.hasOption("nf")) {
					comparateur(pathDirectoryLang1, pathDirectoryLang2,
							pathDirectoryReport);
					System.exit(0);
				}

				if (cmds.hasOption("filtre")) {
					cmd = cmds.getOptionValue("filtre");
				}

				if (cmds.hasOption("taille")) {
					n = Integer.parseInt(cmds.getOptionValue("taille"));
					// on applique les filtres sur les fichiers des deux
					// dossiers
					// System.out.println(pathDirectoryLang1);
					// System.out.println(pathTemp1);
					// System.out.println(cmd);
				}
			} catch (ParseException e) {
				System.err.println("Parsing failed. Exception is = "
						+ e.toString());
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("MainComparateur", option);
			}
		}
		comparateur(pathDirectoryLang1, pathDirectoryLang2,
				pathDirectoryReport, cmd, n);
	}

	/**
	 * méthode qui compare l'ensemble des fichiers presents dans les dossiers d1
	 * et d2 après leur avoir appliqué un filtre
	 * 
	 * @param d1
	 *            Chemin vers le dossier contenant les fichiers à traiter de
	 *            langue anglaise
	 * @param d2
	 *            Chemin vers le dossier contenant les fichiers à traiter de
	 *            langue française
	 * @param f
	 *            Chemin vers le fichier de rapport
	 * @param filtre
	 *            Filtre à appliquer aux textes
	 * @param taille
	 *            la taille des grammes
	 */
	public static void comparateur(String d1, String d2, String f,
			String filtre, int taille) {

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// anglaise
		String pathDirectoryLang1 = d1;

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// française
		String pathDirectoryLang2 = d2;

		// Chemin ver le fichier de rapport
		String pathReport = f;

		// Filtre à appliquer aux texte
		String cmd = filtre;

		// la taille des grammes
		int n = taille;

		// dossier temporaire qui acceuillera les fichiers prétraités de
		// langue anglaise
		String pathTemp1 = null;

		// dossier temporaire qui acceuillera les fichiers prétraités de
		// langue française
		String pathTemp2 = null;

		// si on a pas précisé d'argument au lancement de l'application
		// on effectue les modification manuellement

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
			PreprocessAppli.preprocess(pathDirectoryLang1, pathTemp1, cmd,
					"en", n);
			PreprocessAppli.preprocess(pathDirectoryLang2, pathTemp2, cmd,
					"fr", n);
		} else {
			PreprocessAppli.preprocess(pathDirectoryLang1, pathTemp1, cmd,
					"en", 0);
			PreprocessAppli.preprocess(pathDirectoryLang2, pathTemp2, cmd,
					"fr", 0);
		}

		List<String> testComp1 = FileUtil.getListOfText(pathTemp1);
		List<String> testComp2 = FileUtil.getListOfText(pathTemp2);
		/* liste qui contiendra les listes de termes */
		List<List<String>> listOfList = new ArrayList<List<String>>();

		// on récupère pour chaque document de langue anglaise leur liste de
		// terme, à partir de cette liste de terme on récupère la liste d'id
		// babelenet correspondante
		int i = 1;
		for (String text : testComp1) {
			// récupération de la liste de terme
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal = token.getList();

			// récupération des id correspondant
			List<String> listIdBabel1 = BabelNetService.getListBabelNetId(
					listOriginal, "en");
			// System.out.println(listIdBabel1.toString());

			// écriture dans un fichier des termes n'ayant pas d'id babelnet
			Comparateur.reportElementNotFound(listOriginal, listIdBabel1,
					pathReport.substring(0, pathReport.length()-4) + "_log_" + i + "en.txt");

			// suppression de ses termes de la liste
			Comparateur.filterTerm(listIdBabel1, noResult);

			// ajout du nom de fichier en tête de liste
			listIdBabel1.add(0, i + "en");

			// ajout de l'élément à la liste de liste
			listOfList.add(listIdBabel1);

			i++;
		}

		i = 1;

		// on récupère pour chaque document de langue française leur liste de
		// terme à partir de cette liste de terme on récupère la liste d'id
		// babelenet correspondante
		for (String text : testComp2) {
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal2 = token.getList();

			List<String> listIdBabel2 = BabelNetService.getListBabelNetId(
					listOriginal2, "fr");
			Comparateur.reportElementNotFound(listOriginal2, listIdBabel2,
					pathReport.substring(0, pathReport.length()-4) + "_log_" + i + "fr.txt");

			Comparateur.filterTerm(listIdBabel2, noResult);

			listIdBabel2.add(0, i + "fr");

			listOfList.add(listIdBabel2);

			i++;
		}
		Comparateur testComp = new Comparateur(listOfList);
		testComp.setPathReport(pathReport);
		testComp.compare();
	}

	/**
	 * méthode qui compare l'ensemble des fichiers presents dans les dossiers d1
	 * et d2 après leur avoir appliqué un filtre
	 * 
	 * @param d1
	 *            Chemin vers le dossier contenant les fichiers à traiter de
	 *            langue anglaise
	 * @param d2
	 *            Chemin vers le dossier contenant les fichiers à traiter de
	 *            langue française
	 * @param f
	 *            Chemin vers le fichier de rapport
	 */
	public static void comparateur(String d1, String d2, String f) {

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// anglaise
		String pathDirectoryLang1 = d1;

		// Chemin vers le dossier contenant les fichiers à traiter de langue
		// française
		String pathDirectoryLang2 = d2;

		// Chemin ver le fichier de rapport
		String pathReport = f;

		List<String> testComp1 = FileUtil.getListOfText(pathDirectoryLang1);
		List<String> testComp2 = FileUtil.getListOfText(pathDirectoryLang2);
		/* liste qui contiendra les listes de termes */
		List<List<String>> listOfList = new ArrayList<List<String>>();

		// on récupère pour chaque document de langue anglaise leur liste de
		// terme, à partir de cette liste de terme on récupère la liste d'id
		// babelenet correspondante
		int i = 1;
		for (String text : testComp1) {
			// récupération de la liste de terme
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal = token.getList();

			// récupération des id correspondant
			List<String> listIdBabel1 = BabelNetService.getListBabelNetId(
					listOriginal, "en");
			// System.out.println(listIdBabel1.toString());

			// écriture dans un fichier des termes n'ayant pas d'id babelnet
			Comparateur.reportElementNotFound(listOriginal, listIdBabel1,
					pathReport.substring(0, pathReport.length()-4) + "_log_" + i + "en.txt");

			// suppression de ses termes de la liste
			Comparateur.filterTerm(listIdBabel1, noResult);

			// ajout du nom de fichier en tête de liste
			listIdBabel1.add(0, i + "en");

			// ajout de l'élément à la liste de liste
			listOfList.add(listIdBabel1);

			i++;
		}

		i = 1;

		// on récupère pour chaque document de langue française leur liste de
		// terme à partir de cette liste de terme on récupère la liste d'id
		// babelenet correspondante
		for (String text : testComp2) {
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal2 = token.getList();

			List<String> listIdBabel2 = BabelNetService.getListBabelNetId(
					listOriginal2, "fr");
			Comparateur.reportElementNotFound(listOriginal2, listIdBabel2,
					pathReport.substring(0, pathReport.length()-4) + "_log_" + i + "fr.txt");

			Comparateur.filterTerm(listIdBabel2, noResult);

			listIdBabel2.add(0, i + "fr");

			listOfList.add(listIdBabel2);

			i++;
		}
		Comparateur testComp = new Comparateur(listOfList);
		testComp.setPathReport(pathReport);
		testComp.compare();
	}

}
