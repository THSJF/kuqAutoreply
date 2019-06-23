package com.meng.config;

import java.util.ArrayList;

public class ListQQJavaBean {
	ArrayList<Long> qqList = new ArrayList<>();

	public void add(int i) {
		qqList.add((long) i);
	}

	public void add(long i) {
		qqList.add(i);
	}
}
