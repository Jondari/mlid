package fr.inrialpes.exmo.mlid.preprocess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

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

		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_46,
				new StringReader(text));

		List<String> stopWords = null;
		/* Sélection de l'anti-dictionnaire à utiliser */
		if (lang.equals("en")) {
			stopWords = ConstantStopWord.english_StopWord; // your english list
															// of stop
															// words.....
		} else if (lang.equals("fr")) {
			stopWords = ConstantStopWord.french_StopWord; // your french list of
															// stop words.....
		} else if (lang.equals("ch")) {
			stopWords = ConstantStopWord.chinese_StopWord; // your chinese list
															// of stop
															// words.....
		}
		tokenStream = new StopFilter(Version.LUCENE_46, tokenStream,
				StopFilter.makeStopSet(Version.LUCENE_46, stopWords));
		// ArrayList<String> wordList = new ArrayList<String>();
		String wordList = "";
		final CharTermAttribute charTermAttribute = tokenStream
				.addAttribute(CharTermAttribute.class);
		/* suppression des mots vide */
		try {
			tokenStream.reset();
			int i = 0;
			while (tokenStream.incrementToken()) {
				final String token = charTermAttribute.toString().toString();
				// System.out.println("token: " + token);
				// wordList.add(token);
				if (i == 0) {
					wordList = wordList + token;
					i++;
				} else {
					wordList = wordList + " " + token;
					// System.out.println("wordList: " + wordList);
				}
			}
			// this.crtString = wordList.toString();
			// return wordList.toString();
			if (lang.equals("fr")) {
				wordList = frenchStopWordFilter(wordList);
			}
			this.crtString = wordList;
			return wordList;
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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

}
