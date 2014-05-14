package fr.inrialpes.exmo.mlid;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import fr.inrialpes.exmo.mlid.preprocess.LowerCase;
import fr.inrialpes.exmo.mlid.preprocess.NGrams;
import fr.inrialpes.exmo.mlid.preprocess.PreprocessFilter;
import fr.inrialpes.exmo.mlid.preprocess.Stemming;
import fr.inrialpes.exmo.mlid.preprocess.StopWord;
import fr.inrialpes.exmo.mlid.preprocess.Tokenization;

/**
 * Classe permettant d'appliquer un ou plusieurs filtres à un document entré en
 * paramètre et d'obtenir le fichier résultat en spécifiant son chemin d'accès.
 * Il est nécessaire de spécifier la langue du document. 
 * Les langues reconnus sont le français, l'anglais le russe et le chinois. 
 * Pour spécifier la langue entré fr, en, ru ou ch. 
 * Pour spécifier le filtre entré : 
 * - Stop pour le StopWord 
 * - Stem pour le Stemming 
 * - Token pour la Tokenization 
 * - Low pour le LowerCase 
 * - NGr pour le NGrams 
 * - Low+Stop 
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

		// fichier à traiter
		String filePath;

		// fichier résultat
		String filePathDest;

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
			filePath = "src/english_stopword.txt";
			filePathDest = "src/test.txt";
			cmd = "Low+Stop+Stem+NGr";
			lang = "en";
		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au fichier source
		// args[1] correspond au fichier destination
		// args[2] correspond au filtre à appliquer sur le fichier
		// args[3] correspond à la langue du fichier
		// args[4] correspond à la taille du gramme
		else {
			System.out.println("traitement en mode console");
			filePath = args[0];
			filePathDest = args[1];
			cmd = args[2];
			lang = args[3];
			if(args.length == 5){
				n = Integer.parseInt(args[4]);
			}
		}

		// On récupère le texte du fichier
		String tmpText = getText(filePath);

		if (cmd.equalsIgnoreCase("Low")) {
			filter = new LowerCase(tmpText);
		} else if (cmd.equalsIgnoreCase("Stop")) {
			filter = new StopWord(tmpText, lang);
		} else if (cmd.equalsIgnoreCase("Stem")) {
			filter = new Stemming(tmpText);
		} else if (cmd.equalsIgnoreCase("Token")) {
			filter = new Tokenization(tmpText);
		} else if (cmd.equalsIgnoreCase("NGr")) {
			filter = new NGrams(tmpText, n);
		} else if (cmd.equalsIgnoreCase("Low+Stop")) {
			filter = new StopWord(new LowerCase(tmpText), lang);
		} else if (cmd.equalsIgnoreCase("Low+Stop+Stem")) {
			filter = new Stemming(new StopWord(new LowerCase(tmpText), lang),
					lang);
		} else if (cmd.equalsIgnoreCase("Low+Stop+Stem+Token")) {
			filter = new Tokenization(new Stemming(new StopWord(new LowerCase(
					tmpText), lang), lang));
		} else if (cmd.equalsIgnoreCase("Low+Stop+Stem+NGr")) {
			filter = new NGrams(new Stemming(new StopWord(
					new LowerCase(tmpText), lang), lang), n);
		}

		// On écrit la nouvelle chaîne de caractère dans le fichier destination
		if (cmd.contains("Token") || cmd.contains("NGr")) {
			if (cmd.contains("Token")) {
				writeText(filePathDest, filter.getList(), true);
			}
			if (cmd.contains("NGr")) {
				writeText(filePathDest, ((NGrams) filter).getCrtList(), true);
			}
		} else {
			System.out.println(filter.getCrtString());
			writeText(filePathDest, filter.getCrtString());
			
		}
	}

	/**
	 * Méthode qui permet de charger de le contenu d'un fichier dans une chaine
	 * de caractère.
	 * 
	 * @param filePath
	 *            chemin vers le fichier
	 * @return
	 */
	public static String getText(String filePath) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(filePath));
			String text = " ";

			// On boucle sur chaque champ detecté
			int i = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				// System.out.println(line);
				// faites ici votre traitement
				if (i == 0) {
					text = text + line;
					i++;
				} else {
					text = text + " " + line;
				}
			}
			scanner.close();
			return text;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une chaîne de charactère dans un
	 * fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param text
	 *            chaine de caractère qui sera écrite dans le fichier
	 */
	public static void writeText(String filePath, String text) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath, false);
			writer.write(text, 0, text.length());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une liste dans un fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param listText
	 *            liste des chaines de caractères qui sera écrite dans le
	 *            fichier
	 * @param token
	 *            booléen valant vrai s'il l'on souhaite afficher un mot par
	 *            ligne
	 */
	public static void writeText(String filePath, List<String> listText,
			boolean token) {

		FileWriter writer = null;
		if (token) {
			try {
				writer = new FileWriter(filePath, false);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write("\r\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				writer = new FileWriter(filePath, false);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write(" ");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
