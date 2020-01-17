package com.meng.gameData.TouHou;

public class SpellCard {

	public static final int E=1 << 0;
	public static final int N=1 << 1;
	public static final int H=1 << 2;
	public static final int L=1 << 3;
	public static final int Ex=1 << 4;
	public static final int Ph=1 << 5;
	public static final int Ls=1 << 6;
	public static final int Lw=1 << 7;
	public static final int O=1 << 8;

	public int d=0;//difficult
	public String n;//spell name
	public String m;//spell master

	public SpellCard(String name, String master, int diffcult) {
		n = name;
		m = master;
		d = diffcult;
	}

	public SpellCard(String firstName, String lastName, String master, int diffcult) {
		n = firstName + "「" + lastName + "」";
		m = master;
		d = diffcult;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpellCard)) {
			return false;
		}
		SpellCard spc=(SpellCard)o;
		return n.equals(spc.n) && d == spc.d;
	}


}
