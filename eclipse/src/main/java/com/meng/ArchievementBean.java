package com.meng;

public class ArchievementBean {
	private int a1=0;
	private int a2=0;

	public static final int th6All=1;
	public static final int th7All=2;
	public static final int th8All=3;
	public static final int th10All=4;
	public static final int th11All=5;
	public static final int th12All=6;
	public static final int th13All=7;
	public static final int th14All=8;
	public static final int th15All=9;
	public static final int th16All=10;
	public static final int th17All=11;

	public static final int JunkoSpells=12;
	public static final int LilyWhite=13;
	public static final int MountainOfFaith=14;
	public static final int threeHits=15;
	public static final int yoyoko=16;
	public static final int sakura=17;
	public static final int ice=18;
	public static final int physics=19;
	public static final int reimu=20;
	public static final int marisa=21;
	public static final int spring=22;
	public static final int moshenfusong=23;
	public static final int cat=24;
	public static final int time=25;
	public static final int piaoyilunyi=26;
	public static final int fourSeasons=27;
	public static final int hiddenBugInFourSpells=28;
	public static final int _9961=29;
	public static final int fixBulletFixMiss=30;
	public static final int noSupport=31;
	public static final int shimiyomaru=32;
	public static final int sanae=33;
	public static final int moon=34;
	public static final int memory=35;
	public static final int koishi=36;
	public static final int rumia=37;
	public static final int hideTH7Ex=38;
	public static final int hideTH11=39;
	public static final int hideTH12=40;
	public static final int hideTH12Ex=41;
	public static final int hideTH13=42;
	public static final int shaoBing=43;
	public static final int iceAndFire=44;
	public static final int r18=45;
	public static final int shiningIce=46;
	public static final int elementMaster=47;
	public static final int smartSpell=48;
	public static final int randomSnipe=49;
	public static final int cxk=50;
	public static final int doubleKill=51;
	public static final int wardsMaster=52;

	public boolean isArchievementGot(int archievement) {
		if (archievement <= 31) {
			return (a1 & (1 << archievement)) != 0;
		} else { //if(archievement<=62){
			return (a2 & (1 << (archievement - 31))) != 0;
		}
	}

	public void addArchievement(int archievement) {
		if (archievement <= 31) {
			a1 = a1 | (1 << archievement);
		} else { //if(archievement<=62){
			a2 = a2 | (1 << (archievement - 31));
		}
	}

	public void deleteArchievment(int archievement) {
		if (!isArchievementGot(archievement)) {
			return;
		}
		if (archievement <= 31) {
			a1 = a1 - (1 << archievement);
		} else { //if(archievement<=62){
			a2 = a2 - (1 << (archievement - 31));
		}
	}

}
