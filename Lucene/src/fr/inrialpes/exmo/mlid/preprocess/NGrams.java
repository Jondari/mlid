package fr.inrialpes.exmo.mlid.preprocess;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

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

	@SuppressWarnings("resource")
	public List<String> process(String text, int n) {
		TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_46,
				new StringReader(text));
		tokenizer = new ShingleFilter(tokenizer, n, n);
		CharTermAttribute charTermAttribute = tokenizer
				.addAttribute(CharTermAttribute.class);

		try {
			tokenizer.reset();
			List<String> wordList = new ArrayList<String>();
			while (tokenizer.incrementToken()) {
				String token = charTermAttribute.toString();
				if (token.contains(" ")) {
					// System.out.println("token : " + token);
					wordList.add(token);
				}
			}
			return wordList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return the crtList
	 */
	public List<String> getCrtList() {
		return crtList;
	}
}
