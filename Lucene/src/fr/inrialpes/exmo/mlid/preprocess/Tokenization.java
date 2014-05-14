package fr.inrialpes.exmo.mlid.preprocess;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

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
		Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_46,
				new StringReader(text));
		ArrayList<String> wordList0 = new ArrayList<String>();
		String wordList = "";

		final CharTermAttribute charTermAttribute = tokenizer
				.addAttribute(CharTermAttribute.class);

		try {
			tokenizer.reset();
			int i = 0;
			while (tokenizer.incrementToken()) {
				final String token = charTermAttribute.toString().toString();
				wordList0.add(token);
				if (i == 0) {
					wordList = wordList + token;
					i++;
				} else {
					wordList = wordList + " " + token;
				}
			}
			this.crtList=wordList0;
			this.crtString = wordList;
			return wordList;
		} catch (IOException e) { // TODO Auto-generated catch block
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
	
	/*
	 * public String process(String text) { Tokenizer tokenizer = new
	 * StandardTokenizer(Version.LUCENE_46, new StringReader(text));
	 * ArrayList<String> wordList = new ArrayList<String>(); final
	 * StandardFilter standardFilter = new StandardFilter( Version.LUCENE_46,
	 * tokenizer); final StopFilter stopFilter = new
	 * StopFilter(Version.LUCENE_46, standardFilter,
	 * StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	 * 
	 * final CharTermAttribute charTermAttribute = tokenizer
	 * .addAttribute(CharTermAttribute.class);
	 * 
	 * try { stopFilter.reset(); while (stopFilter.incrementToken()) { final
	 * String token = charTermAttribute.toString().toString();
	 * //System.out.println("token: " + token); wordList.add(token); } return
	 * wordList.toString(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } return null; }
	 */

}
