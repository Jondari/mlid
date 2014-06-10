package fr.inrialpes.exmo.mlid.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantStopWord {

	/**
	 * Liste des mots vide de la langue anglaise
	 */
	private static String english_list[] = { "a", "about", "above", "after",
			"again", "against", "all", "am", "an", "and", "any", "are",
			"aren't", "as", "at", "be", "because", "been", "before", "being",
			"below", "between", "both", "but", "by", "can't", "cannot",
			"could", "couldn't", "did", "didn't", "do", "does", "doesn't",
			"doing", "don't", "down", "during", "each", "few", "for", "from",
			"further", "had", "hadn't", "has", "hasn't", "have", "haven't",
			"having", "he", "he'd", "he'll", "he's", "her", "here", "here's",
			"hers", "herself", "him", "himself", "his", "how", "how's", "i",
			"i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't",
			"it", "it's", "its", "itself", "let's", "me", "more", "most",
			"mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on",
			"once", "only", "or", "other", "ought", "our", "ours", "ourselves",
			"out", "over", "own", "same", "shan't", "she", "she'd", "she'll",
			"she's", "should", "shouldn't", "so", "some", "such", "than",
			"that", "that's", "the", "their", "theirs", "them", "themselves",
			"then", "there", "there's", "these", "they", "they'd", "they'll",
			"they're", "they've", "this", "those", "through", "to", "too",
			"under", "until", "up", "very", "was", "wasn't", "we", "we'd",
			"we'll", "we're", "we've", "were", "weren't", "what", "what's",
			"when", "when's", "where", "where's", "which", "while", "who",
			"who's", "whom", "why", "why's", "with", "won't", "would",
			"wouldn't", "you", "you'd", "you'll", "you're", "you've", "your",
			"yours", "yourself", "yourselves" };

	/**
	 * Liste des mots vide de la langue chinoise
	 */
	private static String chinese_list[] = { "的", "一", "不", "在", "人", "有", "是",
			"为", "以", "于", "上", "他", "而", "后", "之", "来", "及", "了", "因", "下",
			"可", "到", "由", "这", "与", "也", "此", "但", "并", "个", "其", "已", "无",
			"小", "我", "们", "起", "最", "再", "今", "去", "好", "只", "又", "或", "很",
			"亦", "某", "把", "那", "你", "乃", "它", "吧", "被", "比", "别", "趁", "当",
			"从", "到", "得", "打", "凡", "儿", "尔", "该", "各", "给", "跟", "和", "何",
			"还", "即", "几", "既", "看", "据", "距", "靠", "啦", "了", "另", "么", "每",
			"们", "嘛", "拿", "哪", "那", "您", "凭", "且", "却", "让", "仍", "啥", "如",
			"若", "使", "谁", "虽", "随", "同", "所", "她", "哇", "嗡", "往", "哪", "些",
			"向", "沿", "哟", "用", "于", "咱", "则", "怎", "曾", "至", "致", "着", "诸",
			"自" };

	/**
	 * Liste des mots vide de la langue française
	 */
	private static String french_list[] = { "au", "aux", "avec", "ce", "ces",
			"dans", "de", "des", "du", "elle", "en", "et", "eux", "il", "je",
			"la", "le", "leur", "lui", "ma", "mais", "me", "même", "mes",
			"moi", "mon", "ne", "nos", "notre", "nous", "on", "ou", "par",
			"pas", "pour", "que", "qui", "sa", "se", "ses", "son", "sur", "ta",
			"te", "tes", "toi", "ton", "tu", "un", "une", "vos", "votre",
			"vous", "à", "y", "été", "étée", "étées", "étés", "étant", "suis",
			"es", "est", "sommes", "êtes", "sont", "serai", "seras", "sera",
			"serons", "serez", "seront", "serais", "serait", "serions",
			"seriez", "seraient", "étais", "était", "étions", "étiez",
			"étaient", "fus", "fut", "fûmes", "fûtes", "furent", "sois",
			"soit", "soyons", "soyez", "soient", "fusse", "fusses", "fût",
			"fussions", "fussiez", "fussent", "ayant", "eu", "eue", "eues",
			"eus", "ai", "as", "avons", "avez", "ont", "aurai", "auras",
			"aura", "aurons", "aurez", "auront", "aurais", "aurait", "aurions",
			"auriez", "auraient", "avais", "avait", "avions", "aviez",
			"avaient", "eut", "eûmes", "eûtes", "eurent", "aie", "aies", "ait",
			"ayons", "ayez", "aient", "eusse", "eusses", "eût", "eussions",
			"eussiez", "eussent", "ceci", "cela", "celà", "cet", "cette",
			"ici", "ils", "les", "leurs", "quel", "quels", "quelle", "quelles",
			"sans", "soi", "qu", "c", "d", "j", "l", "m", "n", "s", "t" };

	/**
	 * Liste des mots vide de la langue française
	 */
	private static String french_list2[] = { "qu'", "c'", "d'", "j'", "l'",
			"m'", "n'", "s'", "t'" };
	
	/**
	 * Liste des élément de ponctuation"
	 */
	public static String puntuation[] = { ",", ";", ".", "?", ":", "/", "!",
			"#", "\\", "(", ")", "{", "}", "[", "]", "°", "~", "-", "+", "=",
			"|", "¨", "*", "&", "%", "$", "€", "£", "¤", "§", "^", "<", ">",
			"\"", "`", "@", "'", "«", "»" };

	public static List<String> chinese_StopWord = new ArrayList<String>(
			Arrays.asList(chinese_list));;
	public static List<String> english_StopWord = new ArrayList<String>(
			Arrays.asList(english_list));;
	public static List<String> french_StopWord = new ArrayList<String>(
			Arrays.asList(french_list));;
	public static List<String> french_StopWord2 = new ArrayList<String>(
			Arrays.asList(french_list2));;
}
