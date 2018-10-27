package com.meng;

@SuppressWarnings("serial")
public class Random extends java.util.Random {
	public int getNextInt(int bound) {
		return Methods.arrayFlag++ % bound;
	}
}
