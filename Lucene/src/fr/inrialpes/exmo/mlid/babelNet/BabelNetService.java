package fr.inrialpes.exmo.mlid.babelNet;

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

	private static String urlApi = "http://babelnet.org/search.jsp";

	private static String identifier = "bn:";

	private static String noResult = "No result found";
	
	private static String limitRequest = "Too many requests";

	public static List<String> getListId(String word, String lang) {
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
				if(ligne.contains(limitRequest)){
					listId.add(noResult);
					System.out.println("Nombre limite de requête atteiente. API non utilisable jusqu'à demain!");
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
	 * Méthode qui retourne le premier id babelnet du mot entré en paramètre
	 * 
	 * @param word
	 * @param lang
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
	 * Méthode qui filtre les id obtenu afin de les rendre utilisables
	 * 
	 * @param idToFilter
	 * @return
	 */
	private static String filterId(String idToFilter) {
		String temp1 = idToFilter.replace("\">[explore]</a>", "");
		String temp2 = temp1.replace("bn:", "");
		return temp2;
	}

	/**
	 * Méthode qui pour une liste de terme donnée, retourne la liste d'ID
	 * babelnet correspondant
	 * 
	 * @param listTerm
	 * @param lang
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
}
