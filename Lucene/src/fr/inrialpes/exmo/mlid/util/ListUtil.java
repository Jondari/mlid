package fr.inrialpes.exmo.mlid.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {

	/**
	 * Méthode qui supprime les doublon d'une liste
	 * 
	 * @param listWithDuplicate
	 *            liste dont on souhaite éliminer les doublons
	 * @return liste sans doublons
	 */
	public static List<String> removeDuplicate(List<String> listWithDuplicate) {
		// Créer une liste de contenu unique basée sur les éléments de ArrayList
		Set<String> mySet = new HashSet<String>(listWithDuplicate);

		// Créer une Nouvelle ArrayList à partir de Set
		List<String> newList = new ArrayList<String>(mySet);
		return newList;
	}
	
	/**
	 * Méthode qui supprime tout les termes correspondant à l'argument term
	 * présent dans la liste entrée en paramètre
	 * 
	 * @param listToFilter
	 *            liste à traiter
	 * @param term
	 *            terme à supprimer de la liste
	 */
	public static void filterTerm(List<String> listToFilter, String term) {
		for (int i = 0; i < listToFilter.size(); i++) {
			if (listToFilter.get(i).equals(term)) {
				listToFilter.remove(i);
				if (i > 0)
					i--;
			}
		}
		/*if (listToFilter.get(0).equals(term)) {
			listToFilter.remove(0);
		}*/
	}
	
	/**
	 * Méthode qui écrit dans un fichier les termes n'ayant pas obtenu
	 * d'identifiant babelnet
	 */
	public static void reportElementNotFound(List<String> listOriginal,
			List<String> listBabelnet, String reportPath) {
		String noResult = "No result found";
		/* on récupère l'indice de tous les termes noResult */
		ArrayList<Integer> listIndice = new ArrayList<Integer>();
		for (int i = 0; i < listBabelnet.size(); i++) {
			if (listBabelnet.get(i).equals(noResult)) {
				listIndice.add(i);
			}
		}
		// System.out.println("*****************************************");
		// System.out.println(listIndice.toString());
		// System.out.println("*****************************************");
		/*
		 * on écrit les mots qui correspoondent aux indices récupéré dans le
		 * fichier report.txt
		 */
		for (int indice : listIndice) {
			FileUtil.writeText(reportPath, listOriginal.get(indice) + " ", true);
			// System.out.println(listOriginal.get(indice));
			FileUtil.writeText(reportPath, "\n\r", true);
		}
	}

}
