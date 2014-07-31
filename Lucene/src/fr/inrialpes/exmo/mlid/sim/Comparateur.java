package fr.inrialpes.exmo.mlid.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.inrialpes.exmo.mlid.util.Couple;
import fr.inrialpes.exmo.mlid.util.Couples;
import fr.inrialpes.exmo.mlid.util.FileUtil;
import fr.inrialpes.exmo.mlid.util.ListUtil;
import fr.inrialpes.exmo.mlid.util.MapUtil;

public class Comparateur {

	/**
	 * Chemin vers le fichier rapport
	 */
	private String pathReport = "./src/rapport/rapport.txt";

	/**
	 * Liste contenant les vecteurs correspondant au texte du dossier 1
	 */
	public List<List<String>> listOfListD1 = null;

	/**
	 * Liste contenant les vecteurs correspondant au texte du dossier 
	 */
	public List<List<String>> listOfListD2 = null;
	
	/**
	 * Liste contenant les vecteurs correspondant au texte du dossier 1
	 */
	public List<List<List<String>>> listOfListD1_2 = null;
	
	/**
	 * Liste contenant les vecteurs correspondant au texte du dossier 2
	 */
	public List<List<List<String>>> listOfListD2_2 = null;

	public Comparateur() {
	}

	Map<String, Integer> nbTermText = new HashMap<String, Integer>();

	/**
	 * 
	 * @param list
	 *            liste des listes à comparer
	 */
	public Comparateur(List<List<String>> listD1, List<List<String>> listD2) {
		listOfListD1 = ListUtil.removeDuplicate(listD1);
		listOfListD2 = ListUtil.removeDuplicate(listD2);
		getNbTermText();
	}

	/**
	 * 
	 * @param listOfListD2 
	 * @param list
	 *            liste des listes à comparer
	 */
	public Comparateur(ArrayList<List<List<String>>> listOfListD1, ArrayList<List<List<String>>> listOfListD2) {

		listOfListD1_2 = ListUtil.removeDuplicateList(listOfListD1);
		listOfListD2_2 = ListUtil.removeDuplicateList(listOfListD2);
		getNbTermTextL();
	}

	/**
	 * Méthode qui retourne le nombre de terme en commun entre 2 listes
	 * 
	 * @param list1
	 * @param list2
	 * @return nombre de terme commun au deux listes
	 */
	public int getNbCommonTerm(List<String> list1, List<String> list2) {

		HashSet<String> s1 = new HashSet<String>();
		s1.addAll(list1);
		s1.retainAll(list2);
		return s1.size();
	}

	/**
	 * Méthode qui retourne le nombre de terme en commun entre 2 listes
	 * 
	 * @param list1
	 * @param list2
	 * @return nombre de terme commun au deux listes
	 */
	public List<String> getCommonTerm(List<String> list1, List<String> list2) {

		HashSet<String> s1 = new HashSet<String>();
		s1.addAll(list1);
		s1.retainAll(list2);
		return new ArrayList<String>(s1);
	}

	/**
	 * Méthode qui retourne true si les deux listes de listes ont au moins un
	 * terme en commun.
	 * 
	 * @param listOfList1
	 * @param listOfList2
	 * @return
	 */
	public boolean haveCommonTerm(List<List<String>> listOfList1,
			List<List<String>> listOfList2) {

		for (List<String> listCrt1 : listOfList1) {
			for (List<String> listCrt2 : listOfList2) {
				if (!listCrt1.equals(listCrt2)) {
					if (this.getNbCommonTerm(listCrt1, listCrt2) >= 1)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Méthode qui retourne les listes de termes en commun.
	 * 
	 * @param listOfList1
	 * @param listOfList2
	 * @return
	 */
	public List<List<String>> getListCommonTerms(
			List<List<String>> listOfList1, List<List<String>> listOfList2) {
		List<List<String>> commonTerms = new ArrayList<List<String>>();
		for (List<String> listCrt1 : listOfList1) {
			for (List<String> listCrt2 : listOfList2) {
				if (!listCrt1.equals(listCrt2)) {
					if (this.getNbCommonTerm(listCrt1, listCrt2) >= 1)
						commonTerms.add(getCommonTerm(listCrt1, listCrt2));
				}
			}
		}
		return commonTerms;
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
	 *            le nombre de termes communs au deux fichiers
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
	 * Méthode qui écrit dans un fichier entré en paramètre les termes que 2
	 * fichier ont en commun
	 * 
	 * @param nameList1
	 *            nom du fichier 1
	 * @param nameList2
	 *            nom du fichier 2
	 * @param commonTerms
	 *            terme communs au deux fichiers
	 * @param reportPath
	 *            chemin vers le ficher rapport
	 */
	public void reportCommonTerms(String nameList1, String nameList2,
			List<String> commonTerms, String reportPath) {
		String phrase = "Les fichiers " + nameList1 + " et " + nameList2
				+ " ont en commun les termes : ";
		// System.out.println(phrase);
		FileUtil.writeText(reportPath, phrase + "\r", true);
		for (String term : commonTerms) {
			FileUtil.writeText(reportPath, term + "\r", true);
		}
	}

	/**
	 * Méthode qui écrit dans un fichier entré en paramètre les termes que 2
	 * fichier ont en commun
	 * 
	 * @param commonTerms
	 *            terme communs au deux fichiers
	 * @param reportPath
	 *            chemin vers le ficher rapport
	 */
	public void reportCommonTerms(List<String> commonTerms, String reportPath) {
		for (String term : commonTerms) {
			FileUtil.writeText(reportPath, term + "\r", true);
		}
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
		for (List<String> list1 : listOfListD1) {
			for (List<String> list2 : listOfListD2) {
				//if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					int nbTerm = getNbCommonTerm(list1, list2);
					reportNbCommonTerm(name1, name2, nbTerm, pathReport);
					if (nbTerm > max) {
						max = nbTerm;
						name1Max = name1;
						name2Max = name2;
					}
				//}
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

		for (List<String> list1 : listOfListD1) {
			for (List<String> list2 : listOfListD2) {
				//if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					map.put(key, nbTerm);
				//}
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
	 * couple de liste ont en commun sans doublon.
	 */
	public void compareOrderedDescU() {
		Map<String, Integer> map = new HashMap<String, Integer>();

		// création d'un ensemble pouvant contenir des listes
		Couples<String> couplesName = new Couples<String>();

		for (List<String> list1 : listOfListD1) {
			for (List<String> list2 : listOfListD2) {
				//if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					// si ce couple de noms n'existe pas
					if (!couplesName.exist(name1, name2)) {
						// on le crée
						Couple<String> crtCpl = new Couple<String>(name1, name2);
						// on l'ajoute à l'ensemble et la map
						couplesName.add(crtCpl);
						map.put(key, nbTerm);
					}
				//}
			}
		}

		// on trie la map par ordre décroissant de valer
		map = MapUtil.sortByValueDesc(map);
		// on écrit dans le fichier rapport le nombre de termes que les fichiers
		// on en commun
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
	public void compareDiffLang(String lang1, String lang2) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (List<String> list1 : listOfListD1) {
			for (List<String> list2 : listOfListD2) {
				//if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					map.put(key, nbTerm);
				//}
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
					.equalsIgnoreCase(lang1) && !name2.substring(
					name2.length() - 3, name2.length() - 1).equalsIgnoreCase(
					lang1))
					|| (name1.substring(name1.length() - 3, name1.length() - 1)
							.equalsIgnoreCase(lang2) && !name2.substring(
							name2.length() - 3, name2.length() - 1)
							.equalsIgnoreCase(lang2))) {
				reportNbCommonTerm(name1, name2, value, pathReport);
			}

		}
	}

	/**
	 * Méthode qui compare les listes d'id présent dans listOfList. Elle écrit
	 * dans un fichier dans l'ordre décroissant le nombre de terme que chaque
	 * couple de liste ont en commun sans doublon.
	 */
	public void compareDiffLangU(String lang1, String lang2) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<List<String>, Integer> mapCommonTerms = new HashMap<List<String>, Integer>();

		Couples<String> couplesName = new Couples<String>();

		for (List<String> list1 : listOfListD1) {
			for (List<String> list2 : listOfListD2) {
				//if (!list1.equals(list2)) {
					String name1 = list1.get(0);
					String name2 = list2.get(0);
					String key = name1 + "/" + name2;
					int nbTerm = getNbCommonTerm(list1, list2);

					List<String> commonTerms = getCommonTerm(list1, list2);

					if (!couplesName.exist(name1, name2)) {
						Couple<String> crtCpl = new Couple<String>(name1, name2);
						couplesName.add(crtCpl);
						map.put(key, nbTerm);
						mapCommonTerms.put(commonTerms, nbTerm);
					}
				//}
			}
		}

		map = MapUtil.sortByValueDesc(map);
		mapCommonTerms = MapUtil.sortByValueDesc(mapCommonTerms);

		Iterator iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();

			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();

			String[] names = key.split("/");
			String name1 = names[0];
			String name2 = names[1];

			if ((name1.substring(name1.length() - 3, name1.length() - 1)
					.equalsIgnoreCase(lang1) && !name2.substring(
					name2.length() - 3, name2.length() - 1).equalsIgnoreCase(
					lang1))
					|| (name1.substring(name1.length() - 3, name1.length() - 1)
							.equalsIgnoreCase(lang2) && !name2.substring(
							name2.length() - 3, name2.length() - 1)
							.equalsIgnoreCase(lang2))) {
				int nbTermText1 = nbTermText.get(name1);
				int nbTermText2 = nbTermText.get(name2);
				String phrase = name1 + " " + name2 + " " + value + " "
						+ nbTermText1 + " " + nbTermText2;
				FileUtil.writeText(pathReport, phrase + "\r", true);
			}
		}

		iterator = map.entrySet().iterator();
		Iterator iterator2 = mapCommonTerms.entrySet().iterator();

		while (iterator.hasNext() && iterator2.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Map.Entry entry2 = (Map.Entry) iterator2.next();

			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();

			List<String> key2 = (List<String>) entry2.getKey();

			String[] names = key.split("/");
			String name1 = names[0];
			String name2 = names[1];

			if ((name1.substring(name1.length() - 3, name1.length() - 1)
					.equalsIgnoreCase(lang1) && !name2.substring(
					name2.length() - 3, name2.length() - 1).equalsIgnoreCase(
					lang1))
					|| (name1.substring(name1.length() - 3, name1.length() - 1)
							.equalsIgnoreCase(lang2) && !name2.substring(
							name2.length() - 3, name2.length() - 1)
							.equalsIgnoreCase(lang2))) {

				//s'il y a des termes en commun on génère le rapport correspondant
				if (key2.size() > 0) {
					reportCommonTerms(name1, name2, key2,
							pathReport.substring(0, pathReport.length() - 4)
									+ "2.txt");
					FileUtil.writeText(
							pathReport.substring(0, pathReport.length() - 4)
									+ "2.txt", "\r", true);
				}
			}
		}
	}

	/**
	 * Méthode qui compare les listes d'id présent dans listOfList. Elle écrit
	 * dans un fichier dans l'ordre décroissant le nombre de terme que chaque
	 * couple de liste ont en commun sans doublon. (avec liste d'ID babelNet)
	 */
	public void compareDiffLangU(String lang1, String lang2, boolean listID) {
		if (listID) {
			Map<String, Integer> map = new HashMap<String, Integer>();

			Map<List<List<String>>, Integer> mapCommonTerms = new HashMap<List<List<String>>, Integer>();

			Couples<String> couplesName = new Couples<String>();
			int nbTerm = 0;

			for (List<List<String>> list1 : listOfListD1_2) {
				for (List<List<String>> list2 : listOfListD2_2) {
					//if (!list1.equals(list2)) {
						String name1 = list1.get(0).get(0);
						String name2 = list2.get(0).get(0);
						String key = name1 + "/" + name2;

						// si les fichiers sont de langues différentes on
						// cherche leur terme en commun 
						if ((name1.substring(name1.length() - 3,
								name1.length() - 1).equalsIgnoreCase(lang1) && !name2
								.substring(name2.length() - 3,
										name2.length() - 1).equalsIgnoreCase(
										lang1))
								|| (name1.substring(name1.length() - 3,
										name1.length() - 1).equalsIgnoreCase(
										lang2) && !name2.substring(
										name2.length() - 3, name2.length() - 1)
										.equalsIgnoreCase(lang2))) {

							if (haveCommonTerm(list1, list2)) {

								if (!couplesName.exist(name1, name2)) {
									Couple<String> crtCpl = new Couple<String>(
											name1, name2);
									couplesName.add(crtCpl);
									List<List<String>> commonTerms = getListCommonTerms(
											list1, list2);
									nbTerm = commonTerms.size();
									map.put(key, nbTerm);
									mapCommonTerms.put(commonTerms, nbTerm);
								}
							}
							// s'ils n'ont pas de terme en communs
							else {
								if (!couplesName.exist(name1, name2)) {
									Couple<String> crtCpl = new Couple<String>(
											name1, name2);
									couplesName.add(crtCpl);
									nbTerm = 0;
									map.put(key, nbTerm);
								}
							}

						}
					//}
				}
			}

			map = MapUtil.sortByValueDesc(map);
			mapCommonTerms = MapUtil.sortByValueDesc(mapCommonTerms);

			Iterator iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();

				String key = (String) entry.getKey();

				Integer value = (Integer) entry.getValue();
				String[] names = key.split("/");
				String name1 = names[0];
				String name2 = names[1];

				int nbTermText1 = nbTermText.get(name1);
				int nbTermText2 = nbTermText.get(name2);
				String phrase = name1 + " " + name2 + " " + value + " "
						+ nbTermText1 + " " + nbTermText2;
				FileUtil.writeText(pathReport, phrase + "\r", true);
			}

			iterator = map.entrySet().iterator();
			Iterator iterator2 = mapCommonTerms.entrySet().iterator();

			while (iterator.hasNext() && iterator2.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				Map.Entry entry2 = (Map.Entry) iterator2.next();

				String key = (String) entry.getKey();
				List<List<String>> key2 = (List<List<String>>) entry2.getKey();

				Integer value = (Integer) entry.getValue();
				String[] names = key.split("/");
				String name1 = names[0];
				String name2 = names[1];

				String phrase = "Les fichiers " + name1 + " et " + name2
						+ " ont en commun les termes : ";
				FileUtil.writeText(
						pathReport.substring(0, pathReport.length() - 4)
								+ "2.txt", phrase + "\r", true);
				
				//s'il n'ont pas de terme en commun le rapport n'est pas généré
				for (List<String> listTerms : key2) {
					reportCommonTerms(listTerms,
							pathReport.substring(0, pathReport.length() - 4)
									+ "2.txt");
					FileUtil.writeText(
							pathReport.substring(0, pathReport.length() - 4)
									+ "2.txt", "\r", true);
				}
			}
		} else
			throw new RuntimeException("Utilisé la méthode sans booléen!");
	}

	/**
	 * Méthode qui remplie l'attribut nbTermText avec le nom de fichier et le
	 * nombre de terme que ce dernier possède
	 */
	private void getNbTermTextL() {
		for (List<List<String>> crtText : listOfListD1_2) {
			nbTermText.put(crtText.get(0).get(0), crtText.size());
			// System.out.println(crtText.get(0).get(0) + " " + crtText.size());
		}
		for (List<List<String>> crtText : listOfListD2_2) {
			nbTermText.put(crtText.get(0).get(0), crtText.size());
			// System.out.println(crtText.get(0).get(0) + " " + crtText.size());
		}
	}

	/**
	 * Méthode qui remplie l'attribut nbTermText avec le nom de fichier et le
	 * nombre de terme que ce dernier possède
	 */
	private void getNbTermText() {
		for (List<String> crtText : listOfListD1) {
			nbTermText.put(crtText.get(0), crtText.size());
			// System.out.println(crtText.get(0).get(0) + " " + crtText.size());
		}
		for (List<String> crtText : listOfListD2) {
			nbTermText.put(crtText.get(0), crtText.size());
			// System.out.println(crtText.get(0).get(0) + " " + crtText.size());
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
