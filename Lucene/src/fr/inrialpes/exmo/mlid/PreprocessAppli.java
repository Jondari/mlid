package fr.inrialpes.exmo.mlid;

import java.util.List;

import fr.inrialpes.exmo.mlid.preprocess.LowerCase;
import fr.inrialpes.exmo.mlid.preprocess.NGrams;
import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Stemming;
import fr.inrialpes.exmo.mlid.preprocess.StopWord;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.util.FileUtil;

/**
 * Classe permettant d'appliquer un ou plusieurs filtres à un document entré en
 * paramètre et d'obtenir le fichier résultat en spécifiant son chemin d'accès.
 * Il est nécessaire de spécifier la langue du document. Les langues reconnus
 * sont le français, l'anglais le russe et le chinois. 
 * Pour spécifier la langue entré fr, en, ru ou ch. 
 * Pour spécifier le filtre entré : 
 * - Stop pour le StopWord 
 * - Stem pour le Stemming
 * - Token pour la Tokenization 
 * - Low pour le LowerCase 
 * - NGr pour le NGrams 
 * - Low+Token 
 * - Low+Stop 
 * - Low+Stop+Token 
 * - Low+Stop+Stem 
 * - Low+Stop+Stem+Token 
 * - Low+Stop+Stem+NGr
 * 
 * @author Giovanni
 * 
 */
public class PreprocessAppli {

	public static void main(String[] args) {
		// si on a pas précisé d'argument au lancement de l'application
		// on effectue les modification manuellement

		// dossier des fichiers à traiter
		String dirPath;

		// dossier contenant les fichiers résultats
		String dirPathDest;

		// filtre à appliquer
		String cmd;

		// langue du fichier
		String lang;

		// la taille des grammes
		int n = 3;

		PreprocessFilter filter = null;

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! modifié les paramètres manuellement!");
			dirPath = "src/english_stopword.txt";
			dirPathDest = "src/test.txt";
			cmd = "Low+Stop+Stem+NGr";
			lang = "en";
		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au dossier source
		// args[1] correspond au dossier destination
		// args[2] correspond au filtre à appliquer sur les fichiers
		// args[3] correspond à la langue du fichier
		// args[4] correspond à la taille du gramme
		else {
			System.out.println("traitement en mode console");
			dirPath = args[0];
			dirPathDest = args[1];
			cmd = args[2];
			lang = args[3];
			if (args.length == 5) {
				n = Integer.parseInt(args[4]);
			}
		}

		// on récupère la liste des noms des fichiers textes présents dans le
		// dossier donné en paramètre
		List<String> listOfFile = FileUtil.getListNameFile(dirPath);

		// pour chaque fichier texte du dossier on applique le filtre
		for (String filePathTemp : listOfFile) {
			String tmpText;
			String fileDestTemp;
			// on vérifie la façon dont a été ecrit le chemin afin de le
			// complété de façon adéquate
			if (dirPathDest.contains("/")) {
				// On récupère le texte du fichier
				tmpText = FileUtil.getText(dirPath + "/" + filePathTemp);
				// On définie le fichier résultat
				fileDestTemp = dirPathDest + "/" + "mod_" + filePathTemp;
			} else {
				tmpText = FileUtil.getText(dirPath + "\\" + filePathTemp);
				System.out.println(tmpText);
				fileDestTemp = dirPathDest + "\\" + "mod_" + filePathTemp;
			}

			if (cmd.equalsIgnoreCase("Low")) {
				filter = new LowerCase(tmpText);
			} else if (cmd.equalsIgnoreCase("Stop")) {
				filter = new StopWord(tmpText, lang);
			} else if (cmd.equalsIgnoreCase("Stem")) {
				filter = new Stemming(tmpText, lang);
			} else if (cmd.equalsIgnoreCase("Token")) {
				filter = new Tokenization(tmpText);
			} else if (cmd.equalsIgnoreCase("NGr")) {
				filter = new NGrams(tmpText, n);
			} else if (cmd.equalsIgnoreCase("Low+Token")) {
				filter = new Tokenization(new LowerCase(tmpText));
			} else if (cmd.equalsIgnoreCase("Low+Stop")) {
				filter = new StopWord(new LowerCase(tmpText), lang);
			} else if (cmd.equalsIgnoreCase("Low+Stop+Token")) {
				filter = new Tokenization(new StopWord(new LowerCase(tmpText),
						lang));
			} else if (cmd.equalsIgnoreCase("Low+Stop+Stem")) {
				filter = new Stemming(
						new StopWord(new LowerCase(tmpText), lang), lang);
			} else if (cmd.equalsIgnoreCase("Low+Stop+Stem+Token")) {
				filter = new Tokenization(new Stemming(new StopWord(
						new LowerCase(tmpText), lang), lang));
			} else if (cmd.equalsIgnoreCase("Low+Stop+Stem+NGr")) {
				filter = new NGrams(new Stemming(new StopWord(new LowerCase(
						tmpText), lang), lang), n);
			}

			// On écrit la nouvelle chaîne de caractère dans le fichier
			// destination
			if (cmd.contains("Token") || cmd.contains("NGr")) {
				if (cmd.contains("Token")) {
					FileUtil.writeText(fileDestTemp, filter.getList(), true);
				}
				if (cmd.contains("NGr")) {
					FileUtil.writeText(dirPathDest,
							((NGrams) filter).getCrtList(), true);
				}
			} else {
				System.out.println(filter.getCrtString());
				FileUtil.writeText(fileDestTemp, filter.getCrtString());
			}
		}
	}

}
