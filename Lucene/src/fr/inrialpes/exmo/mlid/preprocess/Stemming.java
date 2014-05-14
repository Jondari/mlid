package fr.inrialpes.exmo.mlid.preprocess;

import java.util.List;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.FrenchStemmer;
import org.tartarus.snowball.ext.PorterStemmer;
import org.tartarus.snowball.ext.RussianStemmer;

public class Stemming extends PreprocessFilter {

	/**
	 * Tronque les mots de la chaine de caractère traité par un autre filtre par
	 * une méthode spécialisé pour la langué précisée
	 * 
	 * @param crtfilter
	 * @param lang
	 */
	public Stemming(PreprocessFilter crtfilter, String lang) {
		this.crtString = crtfilter.getCrtString();
		String newString = process(crtString, lang);
		crtfilter.setCrtString(newString);
	}

	/**
	 * Tronque les mots de la chaine de caractère traité par un autre filtre par
	 * la méthode de porter
	 * 
	 * @param crtfilter
	 * @param lang
	 */
	public Stemming(PreprocessFilter crtfilter) {
		this.crtString = crtfilter.getCrtString();
		String newString = process(crtString);
		crtfilter.setCrtString(newString);
	}

	/**
	 * Tronque les mots de la chaine de caractère entré en paramètre par une
	 * méthode spécialisé pour la langué précisée
	 * 
	 * @param crtfilter
	 * @param lang
	 */
	public Stemming(String text, String lang) {
		this.crtString = text;
		this.crtString = this.process(this.crtString, lang);
	}

	/**
	 * @deprecated Tronque les mots de la chaine de caractère entré en paramètre
	 *             par la méthode de porter
	 * 
	 * @param crtfilter
	 * @param lang
	 */
	public Stemming(String text) {
		this.crtString = text;
		this.crtString = this.process(this.crtString);
	}

	/**
	 * @deprecated Tronque les mots de l'attribut crtString par la méthode de
	 *             porter
	 */
	public Stemming() {
		if (crtString != null) {
			process(crtString);
		}
	}

	@Override
	/**
	 * Méthode qui tronque les mots de la chaine de caractère entré en paramètre 
	 * avec la méthode de porter
	 */
	public String process(String text) {
		// TODO Auto-generated method stub
		PorterStemmer stem = new PorterStemmer();
		String result = "non initialisé";
		String newText = "";
		List<String> tempList = getList();
		for (int i = 0; i < tempList.size(); i++) {
			stem.setCurrent(tempList.get(i));
			stem.stem();
			result = stem.getCurrent();
			if (i == 0) {
				newText = result;
			} else {
				newText = newText + " " + result;
			}
		}
		this.crtString = newText;
		return newText;
	}

	/**
	 * Méthode qui tronque les mots de la chaine de caractère entré en paramètre
	 * en fonction de la langue précisé.
	 * 
	 * Les langue peuvent être l'anglais par en le français par fr le russe par
	 * ru
	 */
	public String process(String text, String lang) {
		// TODO Auto-generated method stub
		SnowballProgram stem = null;
		if (lang.equalsIgnoreCase("fr")) {
			stem = new FrenchStemmer();
		} else if (lang.equalsIgnoreCase("en")) {
			stem = new EnglishStemmer();
		} else if (lang.equalsIgnoreCase("ru")) {
			stem = new RussianStemmer();
		}
		String result = "non initialisé";
		String newText = "";
		List<String> tempList = getList();
		for (int i = 0; i < tempList.size(); i++) {
			stem.setCurrent(tempList.get(i));
			stem.stem();
			result = stem.getCurrent();
			if (i == 0) {
				newText = result;
			} else {
				newText = newText + " " + result;
			}
		}
		this.crtString = newText;
		return newText;
	}
}
