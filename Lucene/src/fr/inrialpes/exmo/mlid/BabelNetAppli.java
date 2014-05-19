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
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

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

		String noResult = "No result found";

		String pathDirectoryReport = null;

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {

				CommandLine cmd = parser.parse(option, args);

				String pathFileS = cmd.getOptionValue("f1");
				String pathDestS = cmd.getOptionValue("f2");
				String langS = cmd.getOptionValue("lang");
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
				listTerm = Comparateur.removeDuplicate(listTerm);
				System.out.println(listTerm.toString());
				// System.out.println("*** Après suppression doublon ***");
				// System.out.println(listTerm.toString());
				// récupère la liste d'id babelnet
				List<String> idText = BabelNetService.getListBabelNetId(
						listTerm, langS);
				System.out.println(idText.toString());

				// on rapporte dans un autre fichier les termes n'ayant pas eu
				// d'id
				Comparateur.reportElementNotFound(listTerm, idText,
						pathDirectoryReport);

				// suppression de ses termes de la liste
				Comparateur.filterTerm(idText, noResult);

				// on écrit la liste dans un autre fichier text
				FileUtil.writeText(pathDestS, idText, true);
			} catch (ParseException e) {
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("BalbelnetAppli", option);
			}
		}
	}

}
