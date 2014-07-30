package fr.inrialpes.exmo.mlid;

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

import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.mlid.util.ListUtil;

public class ComputeMax {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Classe qui regroupe toute les options définies
		Options option = new Options();

		// dossier contenant les fichiers babelnet d'une langue
		String pathDirectoryLang1 = null;
		Option d1 = new Option("d1", true,
				"dossier contenant les fichiers babelnet d'une langue");
		// Oblige le paramètre d'être présent lors de l'exécution
		d1.setRequired(true);
		String d1S = null;

		// dossier contenant les fichiers babelnet d'une autre langue
		String pathDirectoryLang2 = null;
		Option d2 = new Option("d2", true,
				"dossier contenant les fichiers babelnet d'une autre langue");
		// Oblige le paramètre d'être présent lors de l'exécution
		d2.setRequired(true);
		String d2S = null;

		// Langue du dossier d1
		Option langD1 = new Option("langD1", true, "langue du dossier d1");
		// Oblige le paramètre d'être présent lors de l'exécution
		langD1.setRequired(true);
		String langD1S = null;

		// Langue du dossier d2
		Option langD2 = new Option("langD2", true, "langue du dossier d2");
		// Oblige le paramètre d'être présent lors de l'exécution
		langD2.setRequired(true);
		String langD2S = null;

		// Chemin vers le fichier de rapport
		String pathDirectoryReport = null;
		Option f = new Option("f", true, "Chemin ver le fichier de rapport");
		// Oblige le paramètre d'être présent lors de l'exécution
		f.setRequired(true);

		// Utilisation de listes d'id
		Boolean list = null;
		Option L = new Option("L", false,
				"Utilisation d'une liste d'ID pour un terme");
		// Oblige le paramètre d'être présent lors de l'exécution

		Option noL = new Option("noL", false,
				"Utilisation d'un seul ID pour un terme");
		// Oblige le paramètre d'être présent lors de l'exécution

		OptionGroup group = new OptionGroup();
		group.setRequired(true);

		group.addOption(L);
		group.addOption(noL);

		// On ajoute nos Option
		option.addOption(d1);
		option.addOption(langD1);
		option.addOption(d2);
		option.addOption(langD2);
		option.addOption(f);
		option.addOptionGroup(group);

		if (args.length == 0) {
			throw new RuntimeException(
					"Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {
				CommandLine cmds = parser.parse(option, args);

				// on récupère les valeurs associé aux options
				pathDirectoryLang1 = cmds.getOptionValue("d1");
				pathDirectoryLang2 = cmds.getOptionValue("d2");
				pathDirectoryReport = cmds.getOptionValue("f");
				langD1S = cmds.getOptionValue("langD1");
				langD2S = cmds.getOptionValue("langD2");
				list = cmds.hasOption("L");

				if (!list) {
					// on lance la comparaison
					// Pour utiliser avec le première id babelnet
					System.out.println("utilisation de compute");
					compute(pathDirectoryLang1, pathDirectoryLang2, langD1S,
							langD2S, pathDirectoryReport);
				} else {

					// Pour utiliser avec la liste d'id babelnet
					System.out.println("utilisation de computeL");
					computeL(pathDirectoryLang1, pathDirectoryLang2, langD1S,
							langD2S, pathDirectoryReport);
				}

			} catch (ParseException e) {
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ComparateurAppli", option);
			}
		}
	}

	/**
	 * Méthode qui compare les fichiers presents dans les dossiers d1 avec ceux
	 * du dossier d2. Les dossier doivent être de langues différentes
	 * 
	 * @param d1
	 *            Chemin vers le premier dossier contenant les fichiers à
	 *            comparer
	 * @param d2
	 *            Chemin vers le second dossier contenant les fichiers à
	 *            comparer
	 * @param langD1
	 *            Langue du dossier 1
	 * @param langD2
	 *            Langue du dossier 2
	 * @param pathReport
	 *            Chemin vers le fichier de rapport
	 */
	public static void compute(String d1, String d2, String langD1,
			String langD2, String pathReport) {
		// on crée une liste qui contient tout les textes des fichiers du
		// dossier 1
		List<String> testComp1 = FileUtil.getListOfText(d1);

		// on fait de même pour le dossier 2
		List<String> testComp2 = FileUtil.getListOfText(d2);

		// on vérifie que chaque dossier contient au moins un fichier
		if (testComp1.size() < 1 || testComp2.size() < 1) {
			throw new RuntimeException(
					"Vos dossiers doivent contenir au moins un fichier! La comparaison n'aura pas lieu.");
		} else {
			List<List<String>> listOfList = new ArrayList<List<String>>();

			// On récupère les noms de fichier du dossier 1
			List<String> nameFile1 = FileUtil.getListNameFile(d1);
			// idem pour le dossier 2
			List<String> nameFile2 = FileUtil.getListNameFile(d2);

			computeProcess(testComp1, nameFile1, listOfList, langD1);

			computeProcess(testComp2, nameFile2, listOfList, langD2);

			// on compare les éléments contenus dans la liste de liste
			Comparateur testComp = new Comparateur(listOfList);
			testComp.setPathReport(pathReport);
			testComp.compareOrderedDescU();
			System.out
					.println("*****************************************************************");
			testComp.setPathReport(pathReport.substring(0,
					pathReport.length() - 4) + "2.txt");
			testComp.compareDiffLangU(langD1, langD2);

		}
	}

	/**
	 * Méthode qui récupère la liste de mots associée à chacun des textes
	 * contenus dans la liste. Elle ajoute en tête de liste le nom du fichier du
	 * fichier correspondant et ajoute les listes de mot à la liste de listes
	 * entrée en paramètre.
	 * 
	 * @param list
	 *            liste de texte dont on souhaite récupèrer les listes de mot
	 * @param nameList
	 *            lste contenant le nom des fichiers dont son issu les textes
	 * @param listOfList
	 *            liste qui contiendra les listes de mots
	 */
	private static void computeProcess(List<String> list,
			List<String> nameList, List<List<String>> listOfList, String lang) {
		int i = 0;
		for (String text : list) {
			// récupération de la liste de terme
			PreprocessFilter token = new Tokenization(text, lang);
			List<String> listBabelId = token.getList();

			// ajout du nom de fichier en tête de liste
			listBabelId.set(0,
					nameList.get(i).substring(0, nameList.get(i).length() - 4)
							+ "(" + lang + ")");

			// ajout de l'élément à la liste de liste
			listOfList.add(listBabelId);

			i++;
		}
	}

	/**
	 * Méthode qui compare les fichiers presents dans les dossiers d1 avec ceux
	 * du dossier d2. Les dossier doivent être de langues différentes
	 * 
	 * @param d1
	 *            Chemin vers le premier dossier contenant les fichiers à
	 *            comparer
	 * @param d2
	 *            Chemin vers le second dossier contenant les fichiers à
	 *            comparer
	 * @param langD1
	 *            Langue du dossier 1
	 * @param langD2
	 *            Langue du dossier 2
	 * @param pathReport
	 *            Chemin vers le fichier de rapport
	 */
	public static void computeL(String d1, String d2, String langD1,
			String langD2, String pathReport) {
		// on crée une liste qui contient tout les textes des fichiers du
		// dossier 1
		List<String> testComp1 = FileUtil.getListOfText(d1);

		// on fait de même pour le dossier 2
		List<String> testComp2 = FileUtil.getListOfText(d2);

		// on vérifie que chaque dossier contient au moins un fichier
		if (testComp1.size() < 1 || testComp2.size() < 1) {
			throw new RuntimeException(
					"Vos dossiers doivent contenir au moins un fichier! La comparaison n'aura pas lieu.");
		} else {
			ArrayList<List<List<String>>> listOfList = new ArrayList<List<List<String>>>();

			// On récupère les noms de fichier du dossier 1
			List<String> nameFile1 = FileUtil.getListNameFile(d1);
			// idem pour le dossier 2
			List<String> nameFile2 = FileUtil.getListNameFile(d2);

			computeProcessL(testComp1, nameFile1, listOfList, langD1);

			computeProcessL(testComp2, nameFile2, listOfList, langD2);

			// System.out.println("ici " + listOfList.get(0).get(1).get(1));

			// on compare les éléments contenus dans la liste de liste
			Comparateur testComp = new Comparateur(listOfList);
			testComp.setPathReport(pathReport);
			testComp.compareDiffLangU(langD1, langD2, true);
		}
	}

	/**
	 * Méthode qui récupère la liste de mots associée à chacun des textes
	 * contenus dans la liste. Elle ajoute en tête de liste le nom du fichier du
	 * fichier correspondant et ajoute les listes de mot à la liste de listes
	 * entrée en paramètre.
	 * 
	 * @param list
	 *            liste de texte dont on souhaite récupèrer les listes de mot
	 * @param nameList
	 *            lste contenant le nom des fichiers dont son issu les textes
	 * @param listOfList
	 *            liste qui contiendra les listes de mots
	 */
	private static void computeProcessL(List<String> list,
			List<String> nameList, List<List<List<String>>> listOfList,
			String lang) {
		int i = 0;
		for (String text : list) {
			// récupération de la liste de terme
			List<List<String>> listBabelId = ListUtil.separateTextInLists(text);

			List<String> nameFile = new ArrayList<String>();
			nameFile.add(nameList.get(i).substring(0,
					nameList.get(i).length() - 4)
					+ "(" + lang + ")");
			// ajout du nom de fichier en tête de liste
			listBabelId.set(0, nameFile);

			// ajout de l'élément à la liste de liste
			listOfList.add(listBabelId);

			i++;
		}
	}

}
