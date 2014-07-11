package fr.inrialpes.exmo.mlid.util;

import java.util.ArrayList;
import java.util.List;

public class Couples<T> {

	private List<Couple<T>> listOfCouples = new ArrayList<Couple<T>>();

	public void add(Couple<T> cpl) {
		listOfCouples.add(cpl);
	}

	public boolean exist(T elt1, T elt2) {
		Couple<T> cpl1 = new Couple<T>(elt1, elt2);
		Couple<T> cpl2 = new Couple<T>(elt2, elt1);
		
		for (Couple<T> cpl : listOfCouples) {
			if ((cpl.getElt1().equals(cpl1.getElt1()) && cpl.getElt2().equals(
					cpl1.getElt2()))
					|| (cpl.getElt1().equals(cpl2.getElt1()) && cpl.getElt2()
							.equals(cpl2.getElt2()))) {
				return true;
			}
		}

		return false;
	}
}
