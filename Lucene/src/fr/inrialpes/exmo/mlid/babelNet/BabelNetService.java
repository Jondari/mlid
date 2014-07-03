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
import java.util.List;
import java.util.StringTokenizer;

public class BabelNetService {

	/**
	 * @deprecated
	 */
	private static String urlApi = "http://babelnet.org/search.jsp";

	/**
	 * @deprecated
	 */
	private static String identifier = "bn:";

	/**
	 * @deprecated
	 */
	private static String noResult = "No result found";

	/**
	 * @deprecated
	 */
	private static String limitRequest = "Too many requests";

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
			if (lang.equalsIgnoreCase("fr")) {
				langage = Language.FR;
			} else if (lang.equalsIgnoreCase("en")) {
				langage = Language.EN;

			} else if (lang.equalsIgnoreCase("zh")) {
				langage = Language.ZH;
			} else if (lang.equalsIgnoreCase("ru")) {
				langage = Language.RU;
			} else {
				System.out.println("Langue non reconnue");
				return null;
			}
			List<BabelSynset> synsets = bn.getSynsets(langage, word);
			for (BabelSynset synset : synsets) {
				listId.add(synset.getId());
				// System.out.println(synset.getId());
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
			// System.out.println("liste vide");
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
			String tempId = BabelNetService.getId(term, lang);
			listBabelNet.add(tempId);
		}
		return listBabelNet;
	}

	/**
	 * Méthode qui retourne la liste d'id babelnet du mot entré en paramètre à
	 * partir de l'API web
	 * 
	 * @deprecated
	 * @param word
	 *            mot dont on veut la liste d'id babelnet
	 * @param lang
	 *            langue du mot
	 * @return liste d'id babelnet
	 */
	public static List<String> getListIdWebAPI(String word, String lang) {
		String request = urlApi + "?word=" + word + "&lang=" + lang;
		// System.out.println(request);
		try {
			URL url = new URL(request);
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			List<String> listId = new ArrayList<String>();
			String ligne;
			int i = 0;
			while ((ligne = reader.readLine()) != null) {
				// affiche le fichier source
				// System.out.println(ligne);
				if (ligne.contains(identifier)) {
					// on ne prend pas le premier id car c'est un id d'exemple
					// on en prend un sur 4 car 4 occurence de l'id apparaissent
					if (ligne.contains(identifier) && i > 0 && (i % 4 == 0)) {
						// System.out
						// .println("************************************");
						int index = ligne.indexOf(identifier);
						// on récupère le mot commençant à la position "index"
						// et on l'ajoute à la liste d'id
						String temp = ligne.substring(index);
						StringTokenizer st = new StringTokenizer(temp);
						// System.out.println("Le token vaut : " +
						// filterId(st.nextToken()));
						listId.add(filterId(st.nextToken()));
					}
					i++;
				}
				if (ligne.contains(noResult)) {
					listId.add(noResult);
					break;
				}
				if (ligne.contains(limitRequest)) {
					listId.add(noResult);
					System.out
							.println("Nombre limite de requête atteiente. API non utilisable jusqu'à demain!");
				}
			}
			return listId;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode qui filtre les id obtenu afin de les rendre utilisables
	 * 
	 * @deprecated
	 * @param idToFilter
	 *            id à filtrer
	 * @return id filtrer
	 */
	private static String filterId(String idToFilter) {
		String temp1 = idToFilter.replace("\">[explore]</a>", "");
		String temp2 = temp1.replace("bn:", "");
		return temp2;
	}

}
