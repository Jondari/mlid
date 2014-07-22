package fr.inrialpes.exmo.mlid.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {

	/**
	 * Méthode qui supprime les doublon d'une liste
	 * @param <T>
	 * 
	 * @param listWithDuplicate
	 *            liste dont on souhaite éliminer les doublons
	 * @return liste sans doublons
	 */
	public static <T> List<T> removeDuplicate(List<T> listWithDuplicate) {
		// Créer une liste de contenu unique basée sur les éléments de ArrayList
		Set<T> mySet = new HashSet<T>(listWithDuplicate);

		// Créer une Nouvelle ArrayList à partir de Set
		List<T> newList = new ArrayList<T>(mySet);
		return newList;
	}
	
	public static List<List<List<String>>> removeDuplicateList (List<List<List<String>>> listWithDuplicate) {
		List<List<List<String>>> newList = new ArrayList<List<List<String>>>();
		// pour chaque texte
		for(List<List<String>> crtText : listWithDuplicate){
			List<List<String>> listTermTemp = new ArrayList<List<String>>();
			// pour chaque terme
			for(List<String> crtTerm : crtText){
				//si la liste de terme temporaire ne contient pas le terme courant on lui ajoute
				if(!listTermTemp.contains(crtTerm)){
					listTermTemp.add(crtTerm);
				}
			}
			//on ajoute la nouvelle liste de terme sans doublon a notre liste
			newList.add(listTermTemp);
		}
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
		/*
		 * if (listToFilter.get(0).equals(term)) { listToFilter.remove(0); }
		 */
	}

	/**
	 * Méthode qui supprime toutes les listes vides présentes dans la liste
	 * entrée en paramètre
	 * 
	 * @param listToFilter
	 *            liste à traiter
	 * @param term
	 *            terme à supprimer de la liste
	 */
	public static void filterEmptyList(List<List<String>> listToFilter,
			String term) {
		for (int i = 0; i < listToFilter.size(); i++) {
			if (listToFilter.get(i).isEmpty()) {
				listToFilter.remove(i);
				if (i > 0)
					i--;
			}
		}
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
			FileUtil.writeText(reportPath, "\r", true);
		}
	}

	/**
	 * Méthode qui écrit dans un fichier les termes n'ayant pas obtenu
	 * d'identifiant babelnet
	 */
	public static void reportElementNotFoundL(List<String> listOriginal,
			List<List<String>> listBabelnet, String reportPath) {
		String noResult = "No result found";
		/* on récupère l'indice de tous les termes noResult */
		ArrayList<Integer> listIndice = new ArrayList<Integer>();
		for (int i = 0; i < listBabelnet.size(); i++) {

			if (listBabelnet.get(i).isEmpty()) {
				listIndice.add(i);
			}
		}
		/*
		 * on écrit les mots qui correspondent aux indices récupéré dans le
		 * fichier report.txt
		 */
		for (int indice : listIndice) {
			FileUtil.writeText(reportPath, listOriginal.get(indice) + " ", true);
			// System.out.println(listOriginal.get(indice));
			FileUtil.writeText(reportPath, "\r", true);
		}
	}

	/**
	 * Méthode qui pour un texte retourne la liste de liste associé. Dans cette
	 * liste chaque liste correspond à un bloque de mots séparé par un saut de
	 * ligne.
	 * 
	 * @param text
	 *            texte dont on souhaite la liste
	 * @return
	 */
	public static List<List<String>> separateTextInLists(String text) {
		String tabText[] = text.split(" ");
		List<List<String>> container = new ArrayList<List<String>>();
		int i = 0;
		while (i < tabText.length) {
			List<String> crtList = new ArrayList<String>();
			while (i < tabText.length && !tabText[i].equals("")) {
				crtList.add(tabText[i]);
				// System.out.println(i + " : " + test[i]);
				i++;
			}
			i++;
			container.add(crtList);
		}
		return container;
	}

}
