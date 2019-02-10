package com.meng.tools;

public class Random {

	public Random() {
	}

	public int nextInt() {
		return new Random().nextInt();
	}

	public int nextInt(int border) {
		return new java.util.Random(border).nextInt(border);
	}
}
