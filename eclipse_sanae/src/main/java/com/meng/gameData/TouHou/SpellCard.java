package com.meng.gameData.TouHou;
import com.meng.*;


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

	public int diffcult=0;
	public String name;
	public String master;

	public SpellCard(String name, String master, int diffcult) {
		this.name = name;
		this.master = master;
		this.diffcult = diffcult;
	}

	public SpellCard(String firstName, String lastName, String master, int diffcult) {
		name = firstName + "「" + lastName + "」";
		this.master = master;
		this.diffcult = diffcult;
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
		return name.equals(spc.name) && diffcult == spc.diffcult;
	}


}
