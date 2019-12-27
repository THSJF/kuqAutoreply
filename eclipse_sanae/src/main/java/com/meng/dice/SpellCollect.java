package com.meng.dice;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.*;
import com.meng.gameData.TouHou.*;
import com.meng.gameData.TouHou.zun.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class SpellCollect {
	private ConcurrentHashMap<Long,HashSet<SpellCard>> spellsMap=new ConcurrentHashMap<>();
	public ConcurrentHashMap<Long,ArchievementBean> archiMap=new ConcurrentHashMap<>();
	private HashSet<Long> todaySign=new HashSet<>();
	private File archiFile;
	private File spellFile;
	private File signedFile;
	public ArrayList<Archievement> archList=new ArrayList<>();
	public SpellCollect() {
		spellFile = new File(Autoreply.appDirectory + "/properties/sanaeSpells.json");
        if (!spellFile.exists()) {
            saveSpellConfig();
        }
        Type type = new TypeToken<ConcurrentHashMap<Long,HashSet<SpellCard>>>() {
        }.getType();
        spellsMap = Autoreply.gson.fromJson(Tools.FileTool.readString(spellFile), type);
		archiFile = new File(Autoreply.appDirectory + "/properties/archievement.json");
        if (!archiFile.exists()) {
            saveArchiConfig();
        }
        Type type2 = new TypeToken<ConcurrentHashMap<Long,ArchievementBean>>() {
        }.getType();
        archiMap = Autoreply.gson.fromJson(Tools.FileTool.readString(archiFile), type2);
		signedFile = new File(Autoreply.appDirectory + "/properties/sign.json");
		if (!signedFile.exists()) {
			saveSignConfig();
		}
		Type type3=new TypeToken<HashSet<Long>>(){	
		}.getType();
		todaySign = Autoreply.gson.fromJson(Tools.FileTool.readString(signedFile), type3);
		archList.add(new Archievement("恶魔领地", "收集东方红魔乡全部符卡", ArchievementBean.th6All, TH06GameData.spellcards.length, TH06GameData.spellcards));
		archList.add(new Archievement("完美樱花", "收集东方妖妖梦全部符卡", ArchievementBean.th7All, TH07GameData.spellcards.length, TH07GameData.spellcards));
		archList.add(new Archievement("永恒之夜", "收集东方永夜抄全部符卡", ArchievementBean.th8All, TH08GameData.spellcards.length, TH08GameData.spellcards));
		archList.add(new Archievement("信仰之山", "收集东方风神录全部符卡", ArchievementBean.th10All, TH10GameData.spellcards.length, TH10GameData.spellcards));
		archList.add(new Archievement("地底之灵", "收集东方地灵殿全部符卡", ArchievementBean.th11All, TH11GameData.spellcards.length, TH11GameData.spellcards));
		archList.add(new Archievement("未知物体", "收集东方星莲船全部符卡", ArchievementBean.th12All, TH12GameData.spellcards.length, TH12GameData.spellcards));
		archList.add(new Archievement("十个欲望", "收集东方神灵庙全部符卡", ArchievementBean.th13All, TH13GameData.spellcards.length, TH13GameData.spellcards));
		archList.add(new Archievement("两个选择", "收集东方辉针城全部符卡", ArchievementBean.th14All, TH14GameData.spellcards.length, TH14GameData.spellcards));
		archList.add(new Archievement("疯狂国度", "收集东方绀珠传全部符卡", ArchievementBean.th15All, TH15GameData.spellcards.length, TH15GameData.spellcards));
		archList.add(new Archievement("四季之星", "收集东方天空璋全部符卡", ArchievementBean.th16All, TH16GameData.spellcards.length, TH16GameData.spellcards));
		archList.add(new Archievement("狡猾之兽", "收集东方鬼形兽全部符卡", ArchievementBean.th17All, TH17GameData.spellcards.length, TH17GameData.spellcards));
		Autoreply.ins.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
		Autoreply.ins.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						Calendar c = Calendar.getInstance();
						if (c.get(Calendar.MINUTE) == 0 && c.get(Calendar.HOUR_OF_DAY) == 0) {
							todaySign.clear();
							saveSignConfig();
						}
						try {
							Thread.sleep(30000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-签到")) {
			if (todaySign.contains(fromQQ)) {
				Autoreply.sendMessage(fromGroup, 0, "你今天签到过啦");
				return true;
			}
			SpellCard sc=getRandomSpell();
			addSpell(fromQQ, sc);
			Autoreply.sendMessage(fromGroup, 0, String.format("%s获得了10信仰和 %s", ConfigManager.ins.getNickName(fromQQ), sc.n));
			saveSpellConfig();
			checkArchievement(fromGroup, fromQQ);
			FaithManager.ins.addFaith(fromQQ, 10);
			todaySign.add(fromQQ);
			saveSignConfig();
			return true;
		}

		if (msg.equals("-clean") && ConfigManager.ins.isAdmin(fromQQ)) {
			todaySign.clear();
			saveSignConfig();
		}

		if (msg.equals("-查看符卡")) {
			StringBuilder sb=new StringBuilder();
			HashSet<SpellCard> gotSpells=spellsMap.get(fromQQ);
			if (gotSpells == null) {
				Autoreply.sendMessage(fromGroup, 0, "你没有参加过抽卡");
				return true;
			}
			sb.append(ConfigManager.ins.getNickName(fromQQ));
			sb.append("获得了:");
			int i=0;
			for (SpellCard s:DiceImitate.spells) {
				if (gotSpells.contains(s)) {
					sb.append("\n").append(s.n);
					++i;
					if (i > 40) {
						Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
						sb.setLength(0);
						i = 0;
					}
				}
			}
			Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
			return true;	
		}
		if (msg.equals("-faith")) {
			Autoreply.sendMessage(fromGroup, 0, "你的信仰是:" + FaithManager.ins.getFaithCount(fromQQ));
			return true;
		}
		return false;
	}

	private void checkArchievement(long fromGroup, long toQQ) {
		HashSet<SpellCard> gotSpell=spellsMap.get(toQQ);
		ArchievementBean ab=archiMap.get(toQQ);
		if (ab == null) {
			ab = new ArchievementBean();
			archiMap.put(toQQ, ab);
		}
		for (Archievement ac:archList) {
			if (ac.getNewArchievement(ab, gotSpell)) {
				ab.addArchievement(ac.archNum);
				Autoreply.sendMessage(fromGroup, toQQ, "获得成就:" + ac.name + "\n获得奖励:" + ac.faith + "\n条件:" + ac.describe);	
				FaithManager.ins.addFaith(toQQ, ac.faith);
			}
		}
		saveArchiConfig();
	}

	private void backupData() {
        while (true) {
            try {
                Thread.sleep(86400000);
                File backup1= new File(spellFile.getAbsolutePath() + ".bak" + System.currentTimeMillis());
                FileOutputStream fos1 = new FileOutputStream(backup1);
                OutputStreamWriter writer1 = new OutputStreamWriter(fos1, StandardCharsets.UTF_8);
                writer1.write(Autoreply.gson.toJson(spellsMap));
                writer1.flush();
                fos1.close();			
				File ar=new File(archiFile.getAbsolutePath() + ".bak" + System.currentTimeMillis());
				FileOutputStream fos = new FileOutputStream(ar);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(Autoreply.gson.toJson(archiMap));
                writer.flush();
                fos.close();			
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	public SpellCard getRandomSpell() {
		SpellCard sc=DiceImitate.spells[new Random().nextInt(DiceImitate.spells.length)];
		ArrayList<Integer> ali=new ArrayList<>();
		if ((sc.d & SpellCard.E) == SpellCard.E) {
			ali.add(SpellCard.E);
		}
		if ((sc.d & SpellCard.N) == SpellCard.N) {
			ali.add(SpellCard.N);
		}
		if ((sc.d & SpellCard.H) == SpellCard.H) {
			ali.add(SpellCard.H);
		}
		if ((sc.d & SpellCard.L) == SpellCard.L) {
			ali.add(SpellCard.L);
		}
		if (sc.d == SpellCard.Ex) {
			ali.add(SpellCard.Ex);
		}
		if (sc.d == SpellCard.Ph) {
			ali.add(SpellCard.Ph);
		}
		if (sc.d == SpellCard.Ls) {
			ali.add(SpellCard.Ls);
		}
		if (sc.d == SpellCard.Lw) {
			ali.add(SpellCard.Lw);
		}
		if (sc.d == SpellCard.O) {
			ali.add(SpellCard.O);
		}
		return new SpellCard(sc.n, sc.m, ali.get(new Random().nextInt(ali.size())));	
	}

	public void addSpell(long qq, SpellCard spellToAdd) {
		HashSet<SpellCard> gotSpellsSet=spellsMap.get(qq);
		if (gotSpellsSet == null) {
			gotSpellsSet = new HashSet<SpellCard>();
			spellsMap.put(qq, gotSpellsSet);
		}
		SpellCard original=null;
		for (SpellCard orns:gotSpellsSet) {
			if (orns.n.equals(spellToAdd.n)) {
				original = orns;
				break;
			}
		}
		if (original == null) {
			gotSpellsSet.add(spellToAdd);
		} else {
			original.d |= spellToAdd.d;
		}
	}

	private void saveSpellConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(spellFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(Autoreply.gson.toJson(spellsMap));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void saveArchiConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(archiFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(Autoreply.gson.toJson(archiMap));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void saveSignConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(signedFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(Autoreply.gson.toJson(todaySign));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
