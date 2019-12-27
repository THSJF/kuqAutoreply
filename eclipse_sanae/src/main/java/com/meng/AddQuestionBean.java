package com.meng;

public class AddQuestionBean {
	public String q;
	public long u;
	public Boolean a;
	public AddQuestionBean(long qq, String question, boolean result) {
		q = question;
		a = result;
		u = qq;
	}
}
