package com.meng.gameData.TouHou;
import java.util.*;
import com.meng.*;

public class SpellCardInfo {
	public SpellCard sc =null;
	public String ps=null;
	public SpellCardInfo(SpellCard spc) {
		sc = spc;
		ps = Autoreply.instence.spellCollect.infoMap.get(sc.n);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(sc.n).append("是").append(sc.m);
		if (sc.d != SpellCard.Ls && sc.d != SpellCard.Lw) {
			sb.append("在");
			if ((sc.d & SpellCard.E) == SpellCard.E) {
				sb.append(" easy");
			}
			if ((sc.d & SpellCard.N) == SpellCard.N) {
				sb.append(" normal");
			}
			if ((sc.d & SpellCard.H) == SpellCard.H) {
				sb.append(" hard");
			}
			if ((sc.d & SpellCard.L) == SpellCard.L) {
				sb.append(" lunatic");
			}
			if (sc.d == SpellCard.Ex) {
				sb.append(" extra");
			}
			if (sc.d == SpellCard.Ph) {
				sb.append(" phantasm");
			}
			if (sc.d == SpellCard.O) {
				sb.append(" overdrive");
			}
			sb.append("难度下的符卡");
		} else {
			if (sc.d == SpellCard.Ls) {
				sb.append("的lastspell");
			} else if (sc.d == SpellCard.Lw) {
				sb.append("的lastword");
			}
		}
		sb.append("\n附加:\n");
		sb.append(ps);
		return sb.toString();
	}

}
