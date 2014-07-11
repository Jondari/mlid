package fr.inrialpes.exmo.mlid.util;

public class Couple<T> {
	
	/**
	 * élément 1 du couple
	 */
	private T elt1;
	
	/**
	 * élément 2 du couple
	 */
	private T elt2;

	public Couple(T a, T b){
		this.elt1 = a;
		this.elt2 = b;
	}

	/**
	 * @return the elt1
	 */
	public T getElt1() {
		return elt1;
	}

	/**
	 * @param elt1 the elt1 to set
	 */
	public void setElt1(T elt1) {
		this.elt1 = elt1;
	}

	/**
	 * @return the elt2
	 */
	public T getElt2() {
		return elt2;
	}

	/**
	 * @param elt2 the elt2 to set
	 */
	public void setElt2(T elt2) {
		this.elt2 = elt2;
	}
	
}
