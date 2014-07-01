package fr.inrialpes.exmo.mlid.test;

import junit.framework.TestCase;
import fr.inrialpes.exmo.mlid.preprocess.LowerCase;

public class LowCaseTest extends TestCase {

	LowerCase testLowCase = null;
	String testCase = "ABCDE";
	String testCase2 = "aBcD12P";
	/* chaîne anglaise */
	String testEn = "Why?";
	/* chaîne russe */
	String testRuss = "ПРИВЕТ";
	/* chaîne portugaise */
	String testport = "OLÁ";

	public LowCaseTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		testLowCase = new LowerCase();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetCrtString() {
		testLowCase = new LowerCase(testCase);
		assertTrue(testLowCase.getCrtString().equals("abcde"));

		testLowCase = new LowerCase(testCase2);
		assertTrue(testLowCase.getCrtString().equals("abcd12p"));

		testLowCase = new LowerCase(testEn);
		assertTrue(testLowCase.getCrtString().equals("why?"));

		testLowCase = new LowerCase(testRuss);
		assertTrue(testLowCase.getCrtString().equals("привет"));

		testLowCase = new LowerCase(testport);
		assertTrue(testLowCase.getCrtString().equals("olá"));

		// fail("Not yet implemented");
	}

}
