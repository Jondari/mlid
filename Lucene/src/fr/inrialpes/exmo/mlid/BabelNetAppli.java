package fr.inrialpes.exmo.mlid;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.inrialpes.exmo.mlid.babelNet.BabelNetService;
import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.mlid.util.ListUtil;

public class BabelNetAppli {

	public static void main(String[] args) {

		// Classe qui regroupe toute les options définies
		Options option = new Options();

		// Chemin vers le dossier contenant les fichiers à traiter
		Option d1 = new Option("d1", true, "dossier des fichiers sources");
		// Oblige le paramètre d'être présent lors de l'exécution
		d1.setRequired(true);

		// Chemin vers le dossier contenant les fichiers résultat
		Option d2 = new Option("d2", true, "dossier des fichiers résultats");
		// Oblige le paramètre d'être présent lors de l'exécution
		d2.setRequired(true);

		// Chemin vers le dossier contenant les fichiers résultat
		Option dr = new Option("dr", true, "dossier des fichiers rapports");
		// Oblige le paramètre d'être présent lors de l'exécution
		dr.setRequired(true);

		// langue des fichier à traiter
		Option lang = new Option("lang", true, "langue du fichier à traiter");

		// ajout des options définies
		option.addOption(d1);
		option.addOption(d2);
		option.addOption(dr);
		option.addOption(lang);

		if (args.length == 0) {
			throw new RuntimeException(
					"Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {

				CommandLine cmd = parser.parse(option, args);

				String pathDirFiles = cmd.getOptionValue("d1");
				String pathDirDest = cmd.getOptionValue("d2");
				String pathDirReport = cmd.getOptionValue("dr");
				String langS = cmd.getOptionValue("lang");

				getBabelID(pathDirFiles, pathDirDest, pathDirReport, langS);

			} catch (ParseException e) {
				System.err.println("Parsing failed. Exception is = "
						+ e.toString());
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("BalbelnetAppli", option);
			}
		}
	}

	/**
	 * Méthode qui écrit dans le dossier spécifié les fichiers avec les ID
	 * balbelNet correspondant au fichier du dossier source. Les mots n'ayant
	 * pas obtenu d'identifiant babelNet sont rapportés dans le dossier rapport
	 * donné en paramètre.
	 * 
	 * @param dirSource
	 *            dossier contenant les fichier à traiter
	 * @param dirDest
	 *            dossier contenant les fichiers avec ID babelnet
	 * @param dirReport
	 *            dossier contenant les fichiers rapportant les mots n'ayant pas
	 *            obtenu d'ID babelNet
	 * @param lang
	 *            langue des fichier à traiter
	 */
	public static void getBabelID(String dirSource, String dirDest,
			String dirReport, String lang) {
		String noResult = "No result found";

		String separator = System.getProperty("file.separator");
		System.out.println(separator);

		List<String> testComp1 = FileUtil.getListOfText(dirSource);

		List<String> NameComp1 = FileUtil.getListNameFile(dirSource);

		// on récupère pour chaque document leur liste de terme, à partir de
		// cette liste de terme
		// on récupère la liste d'id babelenet correspondante
		int i = 0;
		for (String text : testComp1) {
			// récupération de la liste de terme
			PreprocessFilter token = new Tokenization(text);
			List<String> listOriginal = token.getList();

			// récupération des id correspondant
			List<String> listIdBabel = BabelNetService.getListBabelNetId(
					listOriginal, lang);
			// System.out.println(listIdBabel1.toString());

			// écriture dans un fichier des termes n'ayant pas d'id babelnet
			ListUtil.reportElementNotFound(listOriginal, listIdBabel, dirReport
					+ separator + "reportNotFound_" + NameComp1.get(i));

			// suppression de ses termes de la liste
			ListUtil.filterTerm(listIdBabel, noResult);

			// écriture de la liste d'ID dans un fichier
			FileUtil.writeText(
					dirDest
							+ separator
							+ NameComp1.get(i).substring(0,
									NameComp1.get(i).length() - 4) + "ID.txt",
					listIdBabel, true);

			i++;
		}
	}

}
