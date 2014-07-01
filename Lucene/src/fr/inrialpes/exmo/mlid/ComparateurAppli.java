package fr.inrialpes.exmo.mlid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class ComparateurAppli {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/** Classe qui regroupe toute les options définies */
		Options option = new Options();

		// dossier contenant les fichier babelnet à comparer
		Option pathBabelDir = new Option("d", true,
				"dossier contenant les fichier babelnet à comparer");
		// Oblige le paramètre d'être présent lors de l'exécution
		pathBabelDir.setRequired(true);
		option.addOption(pathBabelDir);

		if (args.length == 0) {
			throw new RuntimeException("Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {
				CommandLine cmd = parser.parse(option, args);
				String pathBabelDirS = cmd.getOptionValue("d");

				comparateur(pathBabelDirS);

			} catch (ParseException e) {
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ComparateurAppli", option);
			}
		}
	}

	/**
	 * méthode qui compare les fichiers présents dans le dossier d
	 * @param d
	 *            dossier contenant les fichier (babelnet) à comparer
	 */
	public static void comparateur(String d) {
		String pathBabelDirS = d;

		List<String> testComp1 = FileUtil.getListOfText(pathBabelDirS);
		// on vérifie que le dossier contient au moins 2 fichiers
		if (testComp1.size() < 2) {
			System.out
					.println("Votre dossier doit contenir au moins 2 fichiers! La comparaison n'aura pas lieu.");
			System.exit(0);
		} else {
			List<List<String>> listOfList = new ArrayList<List<String>>();

			List<String> nameFile = FileUtil
					.getListNameFile(pathBabelDirS);
			// System.out.println(nameFile.toString());

			int i = 0;
			for (String text : testComp1) {
				/* récupération de la liste de terme */
				PreprocessFilter token = new Tokenization(text);
				List<String> listBabelId = token.getList();

				/* ajout du nom de fichier en tête de liste */
				listBabelId.set(0, nameFile.get(i));

				/* ajout de l'élément à la liste de liste */
				listOfList.add(listBabelId);

				i++;
			}
			Comparateur testComp = new Comparateur(listOfList);
			testComp.compare();

		}
	}
}
