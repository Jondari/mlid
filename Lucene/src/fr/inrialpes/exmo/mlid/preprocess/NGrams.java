package fr.inrialpes.exmo.mlid.preprocess;

import java.util.ArrayList;
import java.util.List;

public class NGrams extends PreprocessFilter {

	List<String> crtList = null;

	public NGrams(PreprocessFilter crtfilter, int n) {
		this.crtString = crtfilter.getCrtString();
		this.crtList = process(crtString, n);
		// String newString = crtList.toString();
		// crtfilter.setCrtString(newString);
	}

	public NGrams(String text, int n) {
		this.crtString = text;
		this.crtList = process(this.crtString, n);
	}

	@Override
	public String process(String text) {
		// TODO Auto-generated method stub
		System.out
				.println("Cette méthode est déprécié utilisé process(String text, int n) !");
		return null;
	}

	public List<String> process(String text, int n) {

			String[] grams = getNgrams(text, n);
			List<String> wordList = new ArrayList<String>();
			for(int i=0;i<grams.length;i++) {
					wordList.add(grams[i]);
			}
			return wordList;
	}
	
	/**
	 * Méthode qui retourne un tableau de tout les ngrams de taille entré en paramètre
	 * @param text 
	 * 				texte devant être de plus d'un caractère
	 * @param length
	 * 				taille du gramme
	 * @return
	 */
	private String[] getNgrams(String text, int length) {
	    String[] parts = text.split(" ");
	    String[] result = new String[parts.length - length + 1];
	    for(int i = 0; i < parts.length - length + 1; i++) {
	       StringBuilder sb = new StringBuilder();
	       for(int k = 0; k < length; k++) {
	           if(k > 0) sb.append(' ');
	           sb.append(parts[i+k]);
	       }
	       result[i] = sb.toString();
	    }
	    return result;
	}
	
	/**
	 * @return the crtList
	 */
	public List<String> getCrtList() {
		return crtList;
	}
}
