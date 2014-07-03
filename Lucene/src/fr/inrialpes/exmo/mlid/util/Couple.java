package fr.inrialpes.exmo.mlid.util;

public class Couple {
	
	/**
	 * élément 1 du couple
	 */
	private String elt1;
	
	/**
	 * élément 2 du couple
	 */
	private String elt2;

	public Couple(String a, String b){
		this.elt1 = a;
		this.elt2 = b;
	}

	/**
	 * @return the elt1
	 */
	public String getElt1() {
		return elt1;
	}

	/**
	 * @param elt1 the elt1 to set
	 */
	public void setElt1(String elt1) {
		this.elt1 = elt1;
	}

	/**
	 * @return the elt2
	 */
	public String getElt2() {
		return elt2;
	}

	/**
	 * @param elt2 the elt2 to set
	 */
	public void setElt2(String elt2) {
		this.elt2 = elt2;
	}
	
}
