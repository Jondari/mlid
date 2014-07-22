package fr.inrialpes.exmo.mlid.preprocess;

import java.util.Locale;

public class LowerCase extends PreprocessFilter {
	public LowerCase() {
		if (crtString != null) {
			process(crtString);
		}
	}

	public LowerCase(String text) {
		this.crtString = text;
		this.crtString = this.process(this.crtString);
	}

	public LowerCase(PreprocessFilter crtfilter) {
		this.crtString = crtfilter.getCrtString();
		this.crtString = this.process(this.crtString);
	}

	public String process(String text) {
		this.crtString = text.toLowerCase(Locale.getDefault());
		return text.toLowerCase(Locale.getDefault());
	}

}
