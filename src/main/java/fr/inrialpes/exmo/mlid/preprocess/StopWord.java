package fr.inrialpes.exmo.mlid.preprocess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class StopWord extends PreprocessFilter {

	/**
	 * effectue un filtrage des mots vides anglais pour l'attribut crtString
	 * */
	public StopWord() {
		if (crtString != null) {
			process(crtString);
		}
	}

	/**
	 * effectue un filtrage des mots vides anglais pour le texte entré en
	 * paramètre
	 * 
	 */
	public StopWord(String text) {
		this.crtString = text;
		this.crtString = this.process(this.crtString);
	}

	/**
	 * effectue un filtrage des mots vides de la langue spécifiée pour le texte
	 * entré en paramètre langue spécifiable anglais par en chinois par ch
	 * français par fr
	 */
	public StopWord(String text, String lang) {
		this.crtString = text;
		this.crtString = this.process(this.crtString, lang);
		/*
		 * si chaine de caractère en langue française on réapplique le filtre
		 * pour supprimer les mots vides révélés par la première application du
		 * filtre
		 */
		if (lang.equals("fr")) {
			this.crtString = this.process(this.crtString, lang);
		}
	}

	/**
	 * effectue un filtrage des mots vides de la spécifiée sur la chaine traité
	 * par un autre filtre
	 * 
	 * @param crtfilter
	 * @param lang
	 */
	public StopWord(PreprocessFilter crtfilter, String lang) {
		this.crtString = crtfilter.getCrtString();
		process(crtString, lang);
		/*
		 * si chaine de caractère en langue française on réapplique le filtre
		 * pour supprimer les mots vides révélés par la première application du
		 * filtre
		 */
		if (lang.equals("fr")) {
			process(crtString, lang);
		}
		// crtfilter.setCrtString(newString);
	}

	@Override
	/**
	 * Méthode qui supprime les mots vides anglais du texte entré en paramètre
	 */
	public String process(String text) {
		// TODO Auto-generated method stub
		process(text, "en");
		return crtString;
	}

	@SuppressWarnings("resource")
	/**
	 * Méthode qui supprime les mots vides de la langue spécifié du texte entré en paramètre
	 * @param text texte à traiter
	 * @param lang langue de l'anti dictionnaire
	 * @return
	 */
	public String process(String text, String lang) {

		String wordList = stopWordFilter(text, lang);
		if (lang.equals("fr")) {
			wordList = frenchStopWordFilter(wordList);
		}
		wordList = removePunctuation(wordList);
		this.crtString = wordList;
		return wordList;
	}

	/**
	 * méthode qui récupère une liste de mot et la met dans une liste et
	 * retourne la obtenu liste
	 */
	List<String> getStopWordList(String filePath) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(filePath));
			List<String> stopWord = new ArrayList<String>();

			// On boucle sur chaque champ detecté
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				// System.out.println(line);
				// faites ici votre traitement
				stopWord.add(line);
			}

			scanner.close();
			return stopWord;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode qui filtre les mots vide utilisant une apostrophe
	 * 
	 * @param text
	 * @return
	 */
	private String frenchStopWordFilter(String text) {
		String tmp = text;
		for (String stopWord : ConstantStopWord.french_StopWord2) {
			if (tmp.contains(stopWord)) {
				tmp = tmp.replace(stopWord, "");
			}
		}
		return tmp;
	}

	/**
	 * Méthode qui filtre les mots vides de la chaine de caratères entrée en
	 * fonction de la langue entré en paramètre
	 * 
	 * @param text
	 * @param lang
	 * @return
	 */
	private String stopWordFilter(String text, String lang) {
		String tmp = text;
		List<String> stopWords = null;
		if (lang.equals("en")) {
			// your english list of stop words.....
			stopWords = ConstantStopWord.english_StopWord;
		} else if (lang.equals("fr")) {
			// your french list of stop words.....
			stopWords = ConstantStopWord.french_StopWord;
		} else if (lang.equals("ch")) {
			// your chinese list of stop words.....
			stopWords = ConstantStopWord.chinese_StopWord;
		}
		for (String stopWord : stopWords) {
			if (tmp.contains(stopWord)) {
				tmp = stringFilter(tmp, stopWord);
			}
		}
		return tmp;
	}

	/**
	 * Méthode qui élimine de la chaine de caractère entré en paramètre le mot
	 * spécifié.
	 * 
	 * @param text
	 *            chaine de caactère à filtrer
	 * @param wordToFilter
	 *            mot à enlever
	 * @return nouvelle chaine ne possédant plus le mot spécifié
	 **/
	private String stringFilter(String text, String wordToFilter) {
		String token = "";
		String result = "";
		StringTokenizer st = new StringTokenizer(text);
		// nnombre de mot de la chaine de caractère
		//int numToken = st.countTokens();
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			result += (token.equals(wordToFilter)) ? "" : token + " ";
		}
		return result;
	}
	
	/**
	 * Méthode qui élimine la ponctuation du texte entré en paramètre
	 * @param text
	 * @return
	 */
	private String removePunctuation(String text){
		String tmp = text;
		for (String stopWord : ConstantStopWord.puntuation) {
			if (tmp.contains(stopWord)) {
				tmp = tmp.replace(stopWord, "");
			}
		}
		return tmp;
	}

}
