package com.meng.dice;
import com.meng.*;
import com.meng.gameData.TouHou.*;
import java.util.*;

public class Archievement {
	public String name;
	public int archNum;
	public HashSet<SpellCard> spellsNeed=new HashSet<>();
	public int faith;
	public String describe;
	public int judge=0;

	public static final int judgeAnd=0;
	public static final int judgeOr=1;
	public static final int judgeNameContains=2;

	public Archievement(String name, String describe, int archNum, int faith, SpellCard... spells) {
		this.name = name;
		this.faith = faith;
		this.archNum = archNum;
		this.describe = describe;
		for (SpellCard s:spells) {
			spellsNeed.add(s);
		}
	}

	public Archievement(String name, String describe, int archNum, int faith, HashSet<SpellCard> spells) {
		this.name = name;
		this.faith = faith;
		this.archNum = archNum;
		this.describe = describe;
		spellsNeed = spells;
	}

	public Archievement(String name, String describe, int archNum, int faith, int judge, String... spells) {
		this.name = name;
		this.faith = faith;
		this.archNum = archNum;
		this.describe = describe;
		this.judge = judge;
		for (String s:spells) {
			spellsNeed.add(Autoreply.instence.spellCollect.getSpellCard(s));
		}
	}

	public boolean getNewArchievement(ArchievementBean ab, HashSet<SpellCard> gotSpells) {
		return !ab.isArchievementGot(archNum) && isContains(gotSpells, spellsNeed);
	}

	/*	public boolean getNewArchievement(ArchievementBean ab, String... gotSpells) {
	 HashSet<SpellCard> scs=new HashSet<>();
	 for (String s:gotSpells) {
	 scs.add(Autoreply.instence.spellCollect.getSpellCard(s));
	 }
	 return getNewArchievement(ab, scs);
	 }
	 */
	private boolean isContains(HashSet<SpellCard> gotSpells, HashSet<SpellCard> spNeed) {  
		switch (judge) {
			case 0:
				for (SpellCard s:spNeed) {
					if (!gotSpells.contains(s)) {
						return false;
					}
				}
				return true;
			case 1:
				for (SpellCard s:spNeed) {
					if (gotSpells.contains(s)) {
						return true;
					}
				}
				return false;
			case 2:
				String spellNameAll="";
				for (SpellCard gotSpell:gotSpells) {
					if (isPachouliSpell(gotSpell)) {
						spellNameAll += gotSpell.name;
					}
				}
				for (SpellCard spellStr:spNeed) {
					if (!spellNameAll.contains(spellStr.name)) {
						return false;
					}
				}
				return true;
		}
		return false;
	}

	private boolean isPachouliSpell(SpellCard s) {
		for (SpellCard spell:DiceImitate.pachouli) {
			if (s.equals(spell)) {
				return true;
			}
		}
		return false;
	}
}
