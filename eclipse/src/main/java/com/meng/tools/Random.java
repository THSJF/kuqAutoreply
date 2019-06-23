package com.meng.tools;

public class Random {

	public Random() {
	}

	public int nextInt() {
		return new java.util.Random().nextInt();
	}

	public int nextInt(int border) {
		return new java.util.Random().nextInt(border);
	}
}
