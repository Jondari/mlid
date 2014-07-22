package fr.inrialpes.exmo.mlid.preprocess;

/**
 * Classe abstraite permettant d'effectuer des traitements sur un texte.
 * 
 *  Dans le cas de plusieurs traitements sucessifs par exemple : Traitement3(Traitement2(Traitement1(tex))).
 *  C'est d'abord Traitement1 qui est exécuté puis Traitement2 et finalement Traitement3.
 */
import java.util.Arrays;
import java.util.List;

public abstract class PreprocessFilter {

	protected String crtString = null;

	public abstract String process(String text);

	/**
	 * @return the crtString
	 */
	public String getCrtString() {
		return crtString;
	}

	/**
	 * @param crtString
	 *            the crtString to set
	 */
	public void setCrtString(String crtString) {
		this.crtString = crtString;
	}

	public List<String> getList() {
		String temp = crtString.replace(" ", ";");
		temp = temp.replace(";;", "");
		String[] tabTest = temp.split(";");
		List<String> list = Arrays.asList(tabTest);
		list.set(0, list.get(0).replace("[", ""));
		list.set(list.size() - 1, list.get(list.size() - 1).replace("]", ""));
		return list;
	}
}
