package fr.inrialpes.exmo.mlid;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
 * sont le français, l'anglais le russe et le chinois. Pour spécifier la langue
 * entré fr, en, ru ou ch. Pour spécifier le filtre entré : - Stop pour le
 * StopWord - Stem pour le Stemming - Token pour la Tokenization - Low pour le
 * LowerCase - NGr pour le NGrams - Low+Token - Low+Stop - Low+Stop+Token -
 * Low+Stop+Stem - Low+Stop+Stem+Token - Low+Stop+Stem+NGr
 * 
 * @author Giovanni
 * 
 */
public class PreprocessAppli {

	public static void main(String[] args) {
		// si on a pas précisé d'argument au lancement de l'application
		// on effectue les modification manuellement

		Options option = new Options();

		// dossier des fichiers à traiter
		String dirPath = null;
		Option d1 = new Option("d1", true,
				"dossier contenant les fichiers à traiter");
		// Oblige le paramètre d'être présent lors de l'exécution
		d1.setRequired(true);

		// dossier contenant les fichiers résultats
		String dirPathDest = null;
		Option d2 = new Option("d2", true,
				"dossier contenant les fichiers une fois traitées");
		// Oblige le paramètre d'être présent lors de l'exécution
		d2.setRequired(true);

		// filtre à appliquer
		String cmd = null;
		Option filtre = new Option("filtre", true,
				"Filtre à appliquer aux textes");
		// Oblige le paramètre d'être présent lors de l'exécution
		filtre.setRequired(true);

		// langue du fichier
		String lang = null;
		Option langue = new Option("lang", true,
				"langue des fichiers du dossier");
		// Oblige le paramètre d'être présent lors de l'exécution
		filtre.setRequired(true);

		// la taille des grammes
		int n = 3;
		Option taille = new Option("taille", true, "Taille des grammes");

		option.addOption(d1);
		option.addOption(d2);
		option.addOption(langue);
		option.addOption(filtre);
		option.addOption(taille);

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
		else {
			CommandLineParser parser = new GnuParser();
			try {
				CommandLine cmds = parser.parse(option, args);
				System.out.println("traitement en mode console");
				dirPath = cmds.getOptionValue("d1");
				dirPathDest = cmds.getOptionValue("d2");
				cmd = cmds.getOptionValue("filtre");
				lang = cmds.getOptionValue("lang");
				if (cmds.hasOption("taille")) {
					n = Integer.parseInt(cmds.getOptionValue("taille"));
				}
			} catch (ParseException e) {
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("PreprocessAppli", option);
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
			} else {
				System.out
						.println("Commande non reconnue! Arret du programme!");
				System.exit(-1);
			}

			// On écrit la nouvelle chaîne de caractère dans le fichier
			// destination
			if (cmd.contains("Token") || cmd.contains("NGr")) {
				if (cmd.contains("Token")) {
					FileUtil.writeText(fileDestTemp, filter.getList(), true);
				}
				if (cmd.contains("NGr")) {
					FileUtil.writeText(fileDestTemp,
							((NGrams) filter).getCrtList(), true);
				}
			} else {
				System.out.println(filter.getCrtString());
				FileUtil.writeText(fileDestTemp, filter.getCrtString());
			}
		}
	}

}
