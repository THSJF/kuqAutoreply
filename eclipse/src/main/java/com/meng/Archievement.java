package com.meng;
import java.util.*;

public class Archievement {
	public String name;
	public int archNum;
	public HashSet<String> spellsNeed;
	public int coins;

	public Archievement(String name, int archNum, int coins, String... spells) {
		this.name = name;
		this.coins = coins;
		this.archNum = archNum;
		for (String s:spells) {
			spellsNeed.add(s);
		}
	}

	public Archievement(String name, int archNum, int coins, HashSet<String> spells) {
		this.name = name;
		this.coins = coins;
		this.archNum = archNum;
		spellsNeed = spells;
	}

	public boolean getNewArchievement(ArchievementBean ab, HashSet<String> gotSpells) {
		return !ab.isArchievementGot(archNum) && isContains(gotSpells, spellsNeed);
	}

	public boolean getNewArchievement(ArchievementBean ab, String... gotSpells) {
		return !ab.isArchievementGot(archNum) && isContains(gotSpells, spellsNeed);
	}

	private boolean isContains(Set<String> gotSpells, Set<String> spNeed) {  
		for (String s:spNeed) {
			if (!gotSpells.contains(s)) {
				return false;
			}
		}
		return true;  
	}

	private boolean isContains(String[] gotSpells, Set<String> spNeed) {  
		for (String s:spNeed) {
			if (isContains(gotSpells, s)) {
				return true;
			}
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
}
