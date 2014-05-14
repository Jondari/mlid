package fr.inrialpes.exmo.mlid;


import java.util.List;

import fr.inrialpes.exmo.mlid.babelNet.BabelNetService;
import fr.inrialpes.exmo.mlid.preprocess.LowerCase;
import fr.inrialpes.exmo.mlid.preprocess.StopWord;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class BabelNetAppli {

	public static void main(String[] args) {

		// fichier à traiter
		String pathFile;

		// fichier résultat
		String pathDest;

		String pathDirectoryReport = null;

		// langue du fichier
		String lang;

		String noResult = "No result found";

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au fichier source
		// args[1] correspond au fichier destination
		// args[2] correspond à la langue du fichier
		else {
			pathFile = args[0];
			pathDest = args[1];
			lang = args[2];
			pathDirectoryReport = pathDest.substring(0,
					pathDest.lastIndexOf("/"))
					+ "/report.txt";

			// on récupère la chaine de caractère */
			String text = FileUtil.getText(pathFile);
			System.out.println(text);

			// on traite la chaine de charactère
			List<String> listTerm = new StopWord(new LowerCase(text), lang)
					.getList();
			System.out.println(listTerm.toString());
			// System.out.println(listTerm.toString());
			listTerm = Comparateur.removeDuplicate(listTerm);
			System.out.println(listTerm.toString());
			// System.out.println("*** Après suppression doublon ***");
			// System.out.println(listTerm.toString());
			// récupère la liste d'id babelnet
			List<String> idText = BabelNetService.getListBabelNetId(listTerm,
					lang);
			System.out.println(idText.toString());

			// on rapporte dans un autre fichier les termes n'ayant pas eu d'id
			Comparateur.reportElementNotFound(listTerm, idText,
					pathDirectoryReport);

			// suppression de ses termes de la liste
			Comparateur.filterTerm(idText, noResult);

			// on écrit la liste dans un autre fichier text
			FileUtil.writeText(pathDest, idText, true);
		}
	}

}
