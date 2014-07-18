package fr.inrialpes.exmo.mlid.preprocess;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.ChineseTokenizer;
import org.apache.lucene.util.Version;

public class Tokenization extends PreprocessFilter {

	List<String> crtList = null;

	public Tokenization(PreprocessFilter crtfilter, String lang) {
		this.crtString = crtfilter.getCrtString();
		if (lang.equalsIgnoreCase("zh")) {
			process(crtString, lang);
		} else {
			this.crtString = process(crtString);
		}
		// crtfilter.setCrtString(newString);
	}

	public Tokenization(String text, String lang) {
		this.crtString = text;
		if (lang.equalsIgnoreCase("zh")) {
			process(crtString, lang);
		} else {
			this.crtString = this.process(crtString);
		}
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

	public void process(String text, String lang) {
		if (lang.equalsIgnoreCase("zh")) {
			// tokenizeString(new ChineseAnalyzer(), text);
			processChineseString(text);
		} else {
			process(text);
		}

	}

	/**
	 * Méthode qui effectue une tokenisation sur la chaîne de caractère entré en
	 * paramètre en fonction de l'analyzer spécifé
	 * 
	 * @param analyzer
	 *            analyzer de la langue du texte
	 * @param string
	 *            texte à tokenizer
	 * @return
	 */
	/*
	 * private List<String> tokenizeString(Analyzer analyzer, String string) {
	 * List<String> wordList0 = new ArrayList<String>(); String wordList = "";
	 * String token = ""; try { TokenStream stream = analyzer.tokenStream(null,
	 * new StringReader( string)); stream.reset(); int i = 0; while
	 * (stream.incrementToken()) { token = stream.toString().substring(2, 3);
	 * wordList0.add(token); if (i == 0) { wordList = wordList + token; i++; }
	 * else { wordList = wordList + " " + token; } } } catch (IOException e) {
	 * throw new RuntimeException(e); } this.crtList = wordList0; this.crtString
	 * = wordList; return wordList0; }
	 */

	private List<String> processChineseString(String string) {
		List<String> wordList0 = new ArrayList<String>();

		CJKAnalyzer zhAnalyzer = new CJKAnalyzer(Version.LUCENE_29);
		String wordList = "";
		String token = "";
		try {
			TokenStream stream = zhAnalyzer.tokenStream(null, new StringReader(
					string));
			stream.reset();
			int i = 0;
			while (stream.incrementToken()) {
				token = stream.toString().substring(2, 4);
				wordList0.add(token);
				if (i == 0) {
					wordList = wordList + token;
					i++;
				} else {
					wordList = wordList + " " + token;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.crtList = wordList0;
		this.crtString = wordList;
		return wordList0;
	}

	/**
	 * @return the crtList
	 */
	public List<String> getCrtList() {
		return crtList;
	}

}
