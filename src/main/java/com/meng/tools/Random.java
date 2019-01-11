package com.meng.tools;

public class Random {

	public Random() {
	}

	public int nextInt() {
		return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
	}

	public int nextInt(int border) {
		return (int) (System.currentTimeMillis() % Integer.MAX_VALUE) % border;
	}
}
