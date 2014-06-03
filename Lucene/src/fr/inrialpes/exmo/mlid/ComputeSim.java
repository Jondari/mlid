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

import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.ontosim.vector.CosineVM;
import fr.inrialpes.exmo.ontosim.vector.JaccardVM;
import fr.inrialpes.exmo.ontosim.vector.VectorMeasure;
import fr.inrialpes.exmo.ontosim.vector.model.Document;
import fr.inrialpes.exmo.ontosim.vector.model.DocumentCollection;

public class ComputeSim {

	public static void main(String[] args) {

		Options option = new Options();

		// dossier contenant les fichiers babelnet d'une langue
		Option d1 = new Option("d1", true,
				"dossier contenant les fichiers babelnet d'une langue");
		// Oblige le paramètre d'être présent lors de l'exécution
		d1.setRequired(true);
		String d1S = null;

		// dossier contenant les fichiers babelnet d'une autre langue
		Option d2 = new Option("d2", true,
				"dossier contenant les fichiers babelnet d'une autre langue");
		// Oblige le paramètre d'être présent lors de l'exécution
		d2.setRequired(true);
		String d2S = null;

		// Type de vecteur à utiliser
		// TF ou TFIDF
		Option vectorType = new Option("vectorType", true,
				"Type de vecteur à utiliser : TF ou TFIDF");
		String vectorTypeS = "TFIDF";

		// Mesure à utiliser
		// Cos pour Cosine
		// Jacc pour Jaccard
		Option mesure = new Option("mesure", true,
				"Mesure à utiliser : Cos pour Cosine ou Jacc pour Jaccard");
		String mesureS = "Cos";
		option.addOption(d1);
		option.addOption(d2);
		option.addOption(vectorType);
		option.addOption(mesure);

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! Modifié les paramètres manuellement.");
			d1S = "C:/Users/Giovanni/Desktop/File/Fr/mod/";
			d2S = "C:/Users/Giovanni/Desktop/File/En/mod/";
		}
		// sinon on effecture directement le traitement des données.
		else {
			CommandLineParser parser = new GnuParser();
			try {
				CommandLine cmd = parser.parse(option, args);

				d1S = cmd.getOptionValue("d1");
				d2S = cmd.getOptionValue("d2");
				if (cmd.hasOption("vectorType")) {
					vectorTypeS = cmd.getOptionValue("vectorType");
				}
				if (cmd.hasOption("mesure")) {
					mesureS = cmd.getOptionValue("mesure");
				}
			} catch (ParseException e) {
				// Affichage de l'aide
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ComputeSim", option);
			}
		}
		compute(d1S, d2S, vectorTypeS, mesureS);
	}

	/**
	 * méthode qui genere la matrice des similarités des fichiers presents dans les dossiers d1 et d2
	 * @param d1
	 *            dossier contenant les fichiers babelnet d'une langue
	 * @param d2
	 *            dossier contenant les fichiers babelnet d'une autre langue
	 * @param vectorType
	 *            Type de vecteur à utiliser
	 * @param mesure
	 *            Mesure à utiliser
	 */
	private static void compute(String d1, String d2, String vectorType,
			String mesure) {
		// dossier contenant les fichiers babelnet d'une langue
		String d1S = d1;

		// dossier contenant les fichiers babelnet d'une langue
		String d2S = d2;

		// Type de vecteur à utiliser
		// TF ou TFIDF
		String vectorTypeS = vectorType;

		// Mesure à utiliser
		// Cos pour Cosine
		// Jacc pour Jaccard
		String mesureS = mesure;

		// Collection qui contiendra l'ensemble des documents
		DocumentCollection docCollection = new DocumentCollection();
		System.out.println("les termes sont " + docCollection.getTerms());

		// liste des documents du dossier 1
		List<Document> docList1 = fillDocCollection(d1S, docCollection);

		// liste des documents du dossier 2
		List<Document> docList2 = fillDocCollection(d2S, docCollection);
		System.out.println("les termes sont " + docCollection.getTerms());

		// liste des vecteurs des documents du dossier 1
		List<double[]> listVect1 = getListVector(vectorTypeS, docList1,
				docCollection);

		// liste des vecteurs des documents du dossier 2
		List<double[]> listVect2 = getListVector(vectorTypeS, docList2,
				docCollection);

		// création de la matrice et écriture dans un fichier
		String toWrite = getName(d1S);
		List<String> nameDoc2 = FileUtil.getListNameFile(d2S);
		int i = 0;
		// on récupère les noms des doc1 du dossier 1 et on les écrit dans le
		// fichier csv
		FileUtil.writeText(d1S + "/matrice.csv", toWrite, true);
		for (double[] vector1 : listVect1) {
			toWrite = "";
			for (double[] vector2 : listVect2) {
				VectorMeasure sim = null;
				if (mesureS.equalsIgnoreCase("Cos")) {
					sim = new CosineVM();
				} else if (mesureS.equalsIgnoreCase("Jacc")) {
					sim = new JaccardVM();
				} else {
					System.out.println("La mesure utilisé est incorrect");
					System.exit(-1);
				}
				toWrite = toWrite + sim.getSim(vector1, vector2) + ";\t";
			}
			// on écrit le nom du document courant du dossier
			FileUtil.writeText(d1S + "/matrice.csv", "\n" + nameDoc2.get(i)
					+ "; ", true);
			// on écrit la ligne de matrice
			FileUtil.writeText(d1S + "/matrice.csv", toWrite + "\n", true);
			i++;

		}
	}

	/**
	 * Méthode qui à partir d'un chemin vers un répertoire va remplir la
	 * collection de document en paramètre avec des document correspondant au
	 * fichier texte présent dans le répertoire. Cette méthode retourne la liste
	 * des documents ajoutés à la collection.
	 * 
	 * @param pathDirectory
	 * @param docCollection
	 */
	public static List<Document> fillDocCollection(String pathDirectory,
			DocumentCollection docCollection) {

		List<String> listOfFile = FileUtil.getListNameFile(pathDirectory);

		List<Document> listOfDoc = new ArrayList<Document>();
		for (String namefileTemp : listOfFile) {
			String tmpText;
			String fileDestTemp;
			// on vérifie la façon dont a été ecrit le chemin afin de le
			// complété de façon adéquate
			if (pathDirectory.contains("/")) {
				// On récupère le texte du fichier
				tmpText = FileUtil.getText(pathDirectory + "/" + namefileTemp);
				// On définie le fichier résultat
				fileDestTemp = pathDirectory + "/" + namefileTemp;
			} else {
				tmpText = FileUtil.getText(pathDirectory + "\\" + namefileTemp);
				// System.out.println(tmpText);
				fileDestTemp = pathDirectory + "\\" + namefileTemp;
			}
			// on récupère la liste de termes
			List<String> tempBabel = new Tokenization(
					FileUtil.getText(fileDestTemp)).getCrtList();
			// on crée un document
			Document docTemp = new Document(namefileTemp);
			// on y ajoute la liste de terme
			docTemp.addOccTerms(tempBabel);
			// on ajoute le document à la collection de document
			docCollection.add(docTemp);

			// on ajoute le document à la liste de document
			listOfDoc.add(docTemp);
		}
		return listOfDoc;
	}

	/***
	 * Méthode qui retourne la liste des vecteurs correspondant à la liste des
	 * documents entré en paramètre
	 * 
	 * @param vectorType
	 *            type du vecteur : TF ou TFIDF
	 * @param listDoc
	 *            liste de document dont on veut les vecteurs
	 * @param docCol
	 *            corpus
	 * @return
	 */
	public static List<double[]> getListVector(String vectorType,
			List<Document> listDoc, DocumentCollection docCol) {
		List<double[]> listVect = new ArrayList<double[]>();
		if (vectorType.equalsIgnoreCase("TF")) {
			for (Document crtDoc : listDoc) {
				double[] temp = docCol.getTFDocVector(crtDoc);
				listVect.add(temp);
			}
			return listVect;
		} else if (vectorType.equalsIgnoreCase("TFIDF")) {
			for (Document crtDoc : listDoc) {
				double[] temp = docCol.getTFIDFDocVector(crtDoc);
				listVect.add(temp);
			}
			return listVect;
		} else {
			System.out.println("Type non défini!");
			return null;
		}
	}

	/**
	 * Méthode qui retourne la chaine de caractère correspondant au nom de
	 * fichier texte du répertoire entré en paramètre
	 * 
	 * @param dir
	 * @return
	 */
	public static String getName(String dir) {
		String nameDoc = "\t\t\t\t\t";
		List<String> temp = FileUtil.getListNameFile(dir);
		for (String name : temp) {
			nameDoc = nameDoc + name + ";\t";
		}
		return nameDoc;
	}

}
