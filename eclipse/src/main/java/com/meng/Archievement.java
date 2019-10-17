package com.meng;
import java.util.*;

public class Archievement {
	public String name;
	public int archNum;
	public HashSet<String> spellsNeed=new HashSet<>();
	public int coins;
	public String describe;
	public int judge=0;

	public static final int judgeAnd=0;
	public static final int judgeOr=1;
	public static final int judgeNameContains=2;

	public Archievement(String name, String describe, int archNum, int coins, String... spells) {
		this.name = name;
		this.coins = coins;
		this.archNum = archNum;
		this.describe = describe;
		for (String s:spells) {
			spellsNeed.add(s);
		}
	}

	public Archievement(String name, String describe, int archNum, int coins, int judge, String... spells) {
		this.name = name;
		this.coins = coins;
		this.archNum = archNum;
		this.describe = describe;
		this.judge = judge;
		for (String s:spells) {
			spellsNeed.add(s);
		}
	}

	public Archievement(String name, String describe, int archNum, int coins, HashSet<String> spells) {
		this.name = name;
		this.coins = coins;
		this.archNum = archNum;
		this.describe = describe;
		spellsNeed = spells;
	}

	public boolean getNewArchievement(ArchievementBean ab, HashSet<String> gotSpells) {
		return !ab.isArchievementGot(archNum) && isContains(gotSpells, spellsNeed);
	}

	public boolean getNewArchievement(ArchievementBean ab, String... gotSpells) {
		return !ab.isArchievementGot(archNum) && isContains(gotSpells, spellsNeed);
	}

	private boolean isContains(Set<String> gotSpells, Set<String> spNeed) {  
		switch (judge) {
			case 0:
				for (String s:spNeed) {
					if (!gotSpells.contains(s)) {
						return false;
					}
				}
				break; 
			case 1:
				for (String s:spNeed) {
					if (gotSpells.contains(s)) {
						return true;
					}
				}
				break;
			case 2:
				boolean pachouliSpell=false;
				String s="";
				for (String ss:gotSpells) {
					s += ss;
					if (isPachouliSpell(s)) {
						pachouliSpell = true;
					}
				}
				//	if (pachouliSpell && s.contains("水") && s.contains("火")) {
				//	return true;
				//	}
				if (!pachouliSpell) {
					return false;
				}
				for (String spellStr:spNeed) {
					if (!s.contains(spellStr)) {
						return false;
					}
				}
				return true;
		}
		return false;
	}

	private boolean isContains(String[] gotSpells, Set<String> spNeed) {  
		switch (judge) {
			case 0:			
				for (String need:spNeed) {
					if (!isContains(gotSpells, need)) {
						return false;
					}
				}
				return true;  
			case 1:
				for (String need:spNeed) {
					if (isContains(gotSpells, need)) {
						return true;
					}
				}
				return false;  
		}
		return false;
	}

	private boolean isContains(String[] aar, String s) {
		for (String tmp:aar) {
			if (tmp.equals(s)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPachouliSpell(String s) {
		for (String spell:DiceImitate.pachouli) {
			if (s.equals(spell)) {
				return true;
			}
		}
		return false;
	}
}
