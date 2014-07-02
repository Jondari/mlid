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
import fr.inrialpes.exmo.mlid.preprocess.LowerCase;
import fr.inrialpes.exmo.mlid.preprocess.StopWord;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.mlid.util.ListUtil;

public class BabelNetAppli {

	public static void main(String[] args) {

		/** Classe qui regroupe toute les options définies */
		Options option = new Options();

		/** fichier à traiter */
		Option pathFile = new Option("f1", true, "fichier source");
		// Oblige le paramètre d'être présent lors de l'exécution
		pathFile.setRequired(true);

		/** fichier résultat */
		Option pathDest = new Option("f2", true, "fichier résultat");
		// Oblige le paramètre d'être présent lors de l'exécution
		pathDest.setRequired(true);

		/** langue du fichier à traiter */
		Option lang = new Option("lang", true, "langue du fichier à traiter");

		// ajout des options définies
		option.addOption(pathFile);
		option.addOption(pathDest);
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

				String pathFileS = cmd.getOptionValue("f1");
				String pathDestS = cmd.getOptionValue("f2");
				String langS = cmd.getOptionValue("lang");

				getBabelID(pathFileS, pathDestS, langS);

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
	 * Méthode qui retourne un fichier f2 contenant les id babelnet des mots du
	 * fichier f1
	 * 
	 * @param f1
	 *            fichier à traiter
	 * @param f2
	 *            fichier résultat
	 * @param langu
	 *            langue du fichier à traiter
	 */
	public static void getBabelID(String f1, String f2, String langu) {
		String noResult = "No result found";

		/** fichier contenant les terme n'ayant pas obtenu d'id babelnet */
		String pathDirectoryReport = null;

		/** fichier à traiter */
		String pathFileS = f1;
		/** fichier résultat */
		String pathDestS = f2;
		/** langue du fichier à traiter */
		String langS = langu;
		if (pathDestS.contains("/")) {
			pathDirectoryReport = pathDestS.substring(0,
					pathDestS.lastIndexOf("/"))
					+ "/report.txt";
		} else {
			pathDirectoryReport = pathDestS.substring(0,
					pathDestS.lastIndexOf("\\"))
					+ "\\report.txt";
		}

		// on récupère la chaine de caractère */
		String text = FileUtil.getText(pathFileS);
		System.out.println(text);

		// on traite la chaine de charactère
		List<String> listTerm = new StopWord(new LowerCase(text), langS)
				.getList();
		System.out.println(listTerm.toString());
		// System.out.println(listTerm.toString());
		listTerm = ListUtil.removeDuplicate(listTerm);
		System.out.println(listTerm.toString());
		// System.out.println("*** Après suppression doublon ***");
		// System.out.println(listTerm.toString());
		// récupère la liste d'id babelnet
		List<String> idText = BabelNetService
				.getListBabelNetId(listTerm, langS);
		System.out.println(idText.toString());

		// on rapporte dans un autre fichier les termes n'ayant pas eu
		// d'id
		ListUtil.reportElementNotFound(listTerm, idText, pathDirectoryReport);

		// suppression de ses termes de la liste
		ListUtil.filterTerm(idText, noResult);

		// on écrit la liste dans un autre fichier text
		FileUtil.writeText(pathDestS, idText, true);
	}

}
