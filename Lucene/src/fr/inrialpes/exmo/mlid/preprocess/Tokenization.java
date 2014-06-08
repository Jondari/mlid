package fr.inrialpes.exmo.mlid.preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenization extends PreprocessFilter {

	List<String> crtList = null;

	public Tokenization(PreprocessFilter crtfilter) {
		this.crtString = crtfilter.getCrtString();
		String newString = process(crtString);
		// crtfilter.setCrtString(newString);
	}

	public Tokenization(String text) {
		this.crtString = text;
		this.crtString = this.process(this.crtString);
	}

	public Tokenization() {
		if (crtString != null) {
			process(crtString);
		}
	}

	@Override
	public String process(String text) {
		ArrayList<String> wordList0 = new ArrayList<String>();
		String wordList = "";
		String token = "";
		StringTokenizer st = new StringTokenizer(text);
		// nnombre de mot de la chaine de caractère
		// int numToken = st.countTokens();
		int i = 0;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			wordList0.add(token);
			if (i == 0) {
				wordList = wordList + token;
				i++;
			} else {
				wordList = wordList + " " + token;
			}
		}
		this.crtList = wordList0;
		this.crtString = wordList;
		return wordList;
	}

	/**
	 * @return the crtList
	 */
	public List<String> getCrtList() {
		return crtList;
	}

}
