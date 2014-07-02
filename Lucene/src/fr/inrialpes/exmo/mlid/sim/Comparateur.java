package fr.inrialpes.exmo.mlid.sim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.mlid.util.ListUtil;
import fr.inrialpes.exmo.mlid.util.MapUtil;

public class Comparateur {

	/**
	 * Chemin vers le fichier rapport
	 */
	private String pathReport = "./src/rapport/rapport.txt";

	/**
	 * Liste contenant les vecteurs correspondant au texte
	 */
	public List<List<String>> listOfList = null;

	/**
	 * 
	 * @param list
	 *            liste des listes à comparer
	 */
	public Comparateur(List<List<String>> list) {
		listOfList = list;
		/* on supprime les doublons de la liste */
		for (List<String> tempList : list) {
			tempList = ListUtil.removeDuplicate(tempList);
		}
	}

	/**
	 * Méthode qui retourne le nombre de terme en commun entre 2 listes
	 * 
	 * @param list1
	 * @param list2
	 * @return nombre de terme commun au deux listes
	 */
	public int getNbCommonTerm0(List<String> list1, List<String> list2) {
		int i = 0;
		for (String crtTerm1 : list1) {
			for (String crtTerm2 : list2) {
				if (crtTerm1.equals(crtTerm2)) {
					i++;
				}
			}
		}
		return i;
	}

	/**
	 * Méthode qui retourne le nombre de terme en commun entre 2 listes
	 * 
	 * @param list1
	 * @param list2
	 * @return nombre de terme commun au deux listes
	 */
	public int getNbCommonTerm(List<String> list1, List<String> list2) {
		int sizeList1 = list1.size();
		int sizeList2 = list2.size();
		int nbTerm = 0;
		// si la liste 1 est plus grande que la liste 2 on enlève les éléments
		// de la liste 2 à la liste 1
		if (sizeList1 >= sizeList2) {
			List<String> list3 = new ArrayList<>(list1);
			// System.out.println("Liste 3 initial");
			// System.out.println(list3.toString());
			boolean change = list3.removeAll(list2);
			int sizeList3 = list3.size();
			/*
			 * if (change) { System.out.println("il y a eu du changement");
			 * System.out.println(list3.toString()); }
			 */
			nbTerm = sizeList1 - sizeList3;
		}
		// si la liste 2 est plus grande que la liste 1 on enlève les éléments
		// de la liste 1 à la liste 2
		else if (sizeList1 <= sizeList2) {
			List<String> list3 = new ArrayList<>(list2);
			// System.out.println("Liste 3 initial");
			// System.out.println(list3.toString());
			boolean change = list3.removeAll(list1);
			int sizeList3 = list3.size();
			/*
			 * if (change) { System.out.println("il y a eu du changement");
			 * System.out.println(list3.toString()); }
			 */
			nbTerm = sizeList2 - sizeList3;
		}
		return nbTerm;
	}

	/**
	 * Méthode qui écrit dans le fichier src/rapport/rapport.txt le nombre de
	 * terme que 2 fichier ont en commun
	 * 
	 * @param nameList1
	 *            nom du fichier 1
	 * @param nameList2
	 *            nom du fichier 2
	 * @param nbTerm
	 *            terme communs au deux fichiers
	 */
	public void reportNbCommonTerm(String nameList1, String nameList2,
			int nbTerm) {
		String phrase = "Les fichiers " + nameList1 + " et " + nameList2
				+ " ont " + nbTerm + " termes en commun.";
		System.out.println(phrase);
		File rapport = new File("./src/rapport/");
		if (!rapport.exists()) {
			rapport.mkdir();
		}
		FileUtil.writeText("./src/rapport/rapport.txt", phrase, true);
		FileUtil.writeText("./src/rapport/rapport.txt", "\r", true);
	}

	/**
	 * Méthode qui écrit dans un fichier entré en paramètre le nombre de terme
	 * que 2 fichier ont en commun
	 * 
	 * @param nameList1
	 *            nom du fichier 1
	 * @param nameList2
	 *            nom du fichier 2
	 * @param nbTerm
	 *            terme communs au deux fichiers
	 * @param reportPath
	 *            chemin vers le ficher rapport
	 */
	public void reportNbCommonTerm(String nameList1, String nameList2,
			int nbTerm, String reportPath) {
		String phrase = "Les fichiers " + nameList1 + " et " + nameList2
				+ " ont " + nbTerm + " termes en commun.";
		System.out.println(phrase);
		FileUtil.writeText(reportPath, phrase, true);
		FileUtil.writeText(reportPath, "\r", true);
	}

	/**
	 * Méthode qui compare les listes d'id présent dans listOfList. Elle écrit
	 * dans un fichier le nombre de terme que chaque couple de liste ont en
	 * commun.
	 */
	public void compare() {
		String name1Max = "none";
		String name2Max = "none";
		int max = -1;
		for (List<String> list1 : listOfList) {
			for (List<String> list2 : listOfList) {
				if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					int nbTerm = getNbCommonTerm(list1, list2);
					reportNbCommonTerm(name1, name2, nbTerm, pathReport);
					if (nbTerm > max) {
						max = nbTerm;
						name1Max = name1;
						name2Max = name2;
					}
				}
			}
		}
		System.out.println("Les fichiers " + name1Max + " et " + name2Max
				+ " ont le plus de terme en commun avec " + max + " termes.");
	}

	/**
	 * Méthode qui compare les listes d'id présent dans listOfList. Elle écrit
	 * dans un fichier dans l'ordre décroissant le nombre de terme que chaque
	 * couple de liste ont en commun.
	 */
	public void compareOrderedDesc() {
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (List<String> list1 : listOfList) {
			for (List<String> list2 : listOfList) {
				if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					map.put(key, nbTerm);
				}
			}
		}

		map = MapUtil.sortByValueDesc(map);
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			String[] names = key.split("/");
			String name1 = names[0];
			String name2 = names[1];

			reportNbCommonTerm(name1, name2, value, pathReport);
		}
	}

	/**
	 * Méthode qui compare les listes d'id présent dans listOfList. Elle écrit
	 * dans un fichier dans l'ordre décroissant le nombre de terme que chaque
	 * couple de liste ont en commun.
	 */
	public void compareDiffLang() {
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (List<String> list1 : listOfList) {
			for (List<String> list2 : listOfList) {
				if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					map.put(key, nbTerm);
				}
			}
		}

		map = MapUtil.sortByValueDesc(map);
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			String[] names = key.split("/");
			String name1 = names[0];
			String name2 = names[1];

			if ((name1.substring(name1.length() - 3, name1.length() - 1)
					.equalsIgnoreCase("en") && !name2.substring(
					name2.length() - 3, name2.length() - 1).equalsIgnoreCase("en"))
					|| (name1.substring(name1.length() - 3, name1.length() - 1)
							.equalsIgnoreCase("fr") && !name2.substring(
							name2.length() - 3, name2.length() - 1)
							.equalsIgnoreCase("fr"))) {
				reportNbCommonTerm(name1, name2, value, pathReport);
			}

		}
	}

	/**
	 * @param pathReport
	 *            the pathReport to set
	 */
	public void setPathReport(String pathReport) {
		this.pathReport = pathReport;
	}

}
