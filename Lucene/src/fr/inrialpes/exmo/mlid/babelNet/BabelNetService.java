package fr.inrialpes.exmo.mlid.babelNet;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class BabelNetService {

	/**
	 * 
	 */
	private static String noResult = "No result found";

	public static Map<String, String> mapId = new HashMap<String, String>();
	
	public static Map<String, List<String>> mapIds = new HashMap<String, List<String>>();

	/**
	 * Méthode qui retourne la liste d'id babelnet du mot entré en paramètre
	 * 
	 * @param word
	 *            mot dont on veut la liste d'id babelnet
	 * @param lang
	 *            langue du mot
	 * @return liste d'id babelnet
	 */
	public static List<String> getListId(String word, String lang) {

		BabelNet bn = BabelNet.getInstance();

		try {
			List<String> listId = new ArrayList<String>();
			Language langage = null;
			try {
				langage = Language.valueOf(lang.toUpperCase());
			} catch (IllegalArgumentException e) {
				System.err.println("Langue non reconnue");
				e.printStackTrace();
			}

			List<BabelSynset> synsets = bn.getSynsets(langage, word);
			for (BabelSynset synset : synsets) {
				listId.add(synset.getId());
			}
			return listId;
		} catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode qui retourne le premier id babelnet du mot entré en paramètre
	 * 
	 * @param word
	 *            mot dont on veut la l'id babelnet
	 * @param lang
	 *            langue du mot
	 * @return
	 */
	public static String getId(String word, String lang) {
		List<String> tempList = getListId(word, lang);
		// return tempList.get(0);
		if (tempList.isEmpty()) {
			return noResult;
		} else
			return tempList.get(0);
	}

	/**
	 * Méthode qui pour une liste de terme donnée, retourne la liste d'ID
	 * babelnet correspondant
	 * 
	 * @param listTerm
	 *            liste des termes dont on veut l'id babelnet
	 * @param lang
	 *            langue de la liste de terme
	 * @return
	 */
	public static List<String> getListBabelNetId(List<String> listTerm,
			String lang) {
		List<String> listBabelNet = new ArrayList<>();
		for (String term : listTerm) {
			if (!mapId.containsKey(term)) {
				String tempId = BabelNetService.getId(term, lang);
				mapId.put(term, tempId);
			}
			listBabelNet.add(mapId.get(term));
		}
		return listBabelNet;
	}
	
	/**
	 * Méthode qui pour une liste de terme donnée, retourne la liste d'ID
	 * babelnet correspondant
	 * 
	 * @param listTerm
	 *            liste des termes dont on veut l'id babelnet
	 * @param lang
	 *            langue de la liste de terme
	 * @return
	 */
	public static List<List<String>> getListBabelNetIds(List<String> listTerm,
			String lang) {
		List<List<String>> listBabelNet = new ArrayList<>();
		for (String term : listTerm) {
			if (!mapIds.containsKey(term)) {
				List<String> tempId = BabelNetService.getListId(term, lang);
				mapIds.put(term, tempId);
			}
			listBabelNet.add(mapIds.get(term));
		}
		return listBabelNet;
	}

}
