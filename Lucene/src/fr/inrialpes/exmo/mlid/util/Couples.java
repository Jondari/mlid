package fr.inrialpes.exmo.mlid.util;

import java.util.ArrayList;
import java.util.List;

public class Couples {

	private List<Couple> listOfCouples = new ArrayList<Couple>();

	public void add(Couple cpl) {
		listOfCouples.add(cpl);
	}

	public boolean exist(String elt1, String elt2) {
		Couple cpl1 = new Couple(elt1, elt2);
		Couple cpl2 = new Couple(elt2, elt1);
		
		for (Couple cpl : listOfCouples) {
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
