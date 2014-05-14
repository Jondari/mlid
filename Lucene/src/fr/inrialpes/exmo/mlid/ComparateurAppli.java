package fr.inrialpes.exmo.mlid;


import java.util.ArrayList;
import java.util.List;

import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.sim.Comparateur;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class ComparateurAppli {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// dossier contenant les fichier babelnet à comparer
		String pathBabelDir;

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! Veuillez entrer des arguments!");
		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au dossier source
		else {
			pathBabelDir = args[0];

			List<String> testComp1 = FileUtil.getListOfText(pathBabelDir);
			// on vérifie que le dossier contient au moins 2 fichiers
			if (testComp1.size() < 2) {
				System.out
						.println("Votre dossier doit contenir au moins 2 fichiers! La comparaison n'aura pas lieu.");
				System.exit(0);
			} else {
				List<List<String>> listOfList = new ArrayList<List<String>>();

				List<String> nameFile = FileUtil.getListNameFile(pathBabelDir);
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

}
