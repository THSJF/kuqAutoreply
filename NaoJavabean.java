package com.meng;

public class NaoJavabean {
	int eleStart = 0;
	int eleEnd = 0;
	String luesuotu = "";
	String similar = "";
	String Pid = "";
	String Uid = "";
	String picTitle = null;

	public NaoJavabean(String elementString) {

		eleStart = elementString.indexOf("resultimage") + "resultimage".length();
		eleEnd = eleStart;

		eleStart = elementString.indexOf("src=\"", eleEnd) + "src=\"".length();
		eleEnd = elementString.indexOf("\"", eleStart);
		luesuotu = elementString.substring(eleStart, eleEnd);

		eleStart = elementString.indexOf("href=\"", eleEnd) + "href=\"".length();
		eleEnd = elementString.indexOf("\"", eleStart);
		Pid = elementString.substring(eleStart, eleEnd);

		eleStart = elementString.indexOf("href=\"", eleEnd) + "href=\"".length();
		eleEnd = elementString.indexOf("\"", eleStart);
		Uid = elementString.substring(eleStart, eleEnd);

		eleStart = elementString.indexOf("resultsimilarityinfo\">") + "resultsimilarityinfo\">".length();
		eleEnd = elementString.indexOf("</div>", eleStart);
		similar = elementString.substring(eleStart, eleEnd);

	}

	public String getLuesuotu() {
		return luesuotu;
	}

	public String getSimilar() {
		return similar;
	}

	public String getPid() {
		return Pid;
	}

	public String getUid() {
		return Uid;
	}

	public String getPicTitle() {
		return picTitle;
	}
}
