package fr.inrialpes.exmo.mlid;
import java.util.ArrayList;
import java.util.List;

import fr.inrialpes.exmo.mlid.preprocess.Tokenization;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.ontosim.vector.CosineVM;
import fr.inrialpes.exmo.ontosim.vector.JaccardVM;
import fr.inrialpes.exmo.ontosim.vector.VectorMeasure;
import fr.inrialpes.exmo.ontosim.vector.model.Document;
import fr.inrialpes.exmo.ontosim.vector.model.DocumentCollection;

public class ComputeSim {

	public static void main(String[] args) {
		// dossier contenant les fichiers babelnet d'une langue
		String d1;

		// dossier contenant les fichiers babelnet d'une autre langue
		String d2;

		// Type de vecteur à utiliser
		// TF ou TFIDF
		String vectorType = "TFIDF";

		// Mesure à utiliser
		// Cos pour Cosine
		// Jacc pour Jaccard
		String mesure = "Cos";

		if (args.length == 0) {
			System.out
					.println("Pas d'argument! Modifié les paramètres manuellement.");
			d1 = "C:/Users/Giovanni/Desktop/File/Fr/mod/";
			d2 = "C:/Users/Giovanni/Desktop/File/En/mod/";
		}
		// sinon on effecture directement le traitement des données.
		// args[0] correspond au dossier source 1
		// args[1] correspond au dossier source 2
		// args[2] correspond au type de vecteur à utiliser
		// args[3] correspond à la mesure à utiliser
		else {
			d1 = args[0];
			d2 = args[1];
			if (args.length == 3) {
				vectorType = args[2];
			}
			if (args.length == 4) {
				mesure = args[3];
			}
		}

		// Collection qui contiendra l'ensemble des documents
		DocumentCollection docCollection = new DocumentCollection();
		System.out.println("les termes sont " + docCollection.getTerms());

		// liste des documents du dossier 1
		List<Document> docList1 = fillDocCollection(d1, docCollection);

		// liste des documents du dossier 2
		List<Document> docList2 = fillDocCollection(d2, docCollection);
		System.out.println("les termes sont " + docCollection.getTerms());

		// liste des vecteurs des documents du dossier 1
		List<double[]> listVect1 = getListVector(vectorType, docList1,
				docCollection);

		// liste des vecteurs des documents du dossier 2
		List<double[]> listVect2 = getListVector(vectorType, docList2,
				docCollection);

		// création de la matrice et écriture dans un fichier
		String toWrite = getName(d1);
		List<String> nameDoc2 = FileUtil.getListNameFile(d2);
		int i = 0;
		// on récupère les noms des doc1 du dossier 1 et on les écrit dans le
		// fichier csv
		FileUtil.writeText(d1 + "matrice.csv", toWrite, true);
		for (double[] vector1 : listVect1) {
			toWrite = "";
			for (double[] vector2 : listVect2) {
				VectorMeasure sim = null;
				if (mesure.equalsIgnoreCase("Cos")) {
					sim = new CosineVM();
				} else if (mesure.equalsIgnoreCase("Jacc")) {
					sim = new JaccardVM();
				} else {
					System.out.println("La mesure utilisé est incorrect");
					System.exit(-1);
				}
				toWrite = toWrite + sim.getSim(vector1, vector2) + ";\t";
			}
			// on écrit le nom du document courant du dossier
			FileUtil.writeText(d1 + "matrice.csv", "\n" + nameDoc2.get(i)
					+ "; ", true);
			// on écrit la ligne de matrice
			FileUtil.writeText(d1 + "matrice.csv", toWrite + "\n", true);
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
