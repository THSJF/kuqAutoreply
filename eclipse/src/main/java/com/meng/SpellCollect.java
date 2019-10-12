package com.meng;
import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class SpellCollect {
	private ConcurrentHashMap<String,HashSet<String>> chm=new ConcurrentHashMap<>();
	private ConcurrentHashMap<String,ArchievementBean> archiMap=new ConcurrentHashMap<>();
	private File archiFile;
	private File spellFile;
	public SpellCollect() {
		spellFile = new File(Autoreply.appDirectory + "/properties/spells.json");
        if (!spellFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<ConcurrentHashMap<String,HashSet<String>>>() {
        }.getType();
        chm = new Gson().fromJson(Methods.readFileToString(Autoreply.appDirectory + "/properties/spells.json"), type);

		archiFile = new File(Autoreply.appDirectory + "/properties/archievement.json");
        if (!archiFile.exists()) {
            saveArchiConfig();
        }
        Type type2 = new TypeToken<ConcurrentHashMap<String,ArchievementBean>>() {
        }.getType();
        archiMap = new Gson().fromJson(Methods.readFileToString(Autoreply.appDirectory + "/properties/archievement.json"), type2);

		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("#幻币转账") && fromQQ == Autoreply.instence.configManager.configJavaBean.ogg) {
			List<Long> chan=Autoreply.instence.CC.getAts(msg);
			if (!Autoreply.instence.configManager.isMaster(chan.get(1))) {
				return false;
			}
			int coins=0;
			try {
				coins = (int)Float.parseFloat(msg.substring(msg.indexOf("转账", 6) + 2, msg.indexOf("个幻币")));		
			} catch (NumberFormatException e) {
				Autoreply.sendMessage(fromGroup, 0, e.toString());
			}
			HashSet<String> tmpSet=chm.get(String.valueOf(chan.get(0)));
			if (tmpSet == null) {
				tmpSet = new HashSet<String>();
				chm.put(String.valueOf(chan.get(0)), tmpSet);
			}
			Random r=new Random();
			StringBuilder sb=new StringBuilder("你获得了:");
			for (int i=0;i < coins * 3;++i) {
				String s=Autoreply.instence.diceImitate.spells[r.nextInt(Autoreply.instence.diceImitate.spells.length)];
				tmpSet.add(s);
				sb.append("\n").append(s);
			}
			saveConfig();
			checkArchievement(fromGroup, fromQQ, chan.get(0), tmpSet);
			Autoreply.sendMessage(fromGroup, 0, sb.toString());
		}
		if (msg.equals("查看符卡")) {
			StringBuilder sb=new StringBuilder();
			HashSet<String> gtdspl=chm.get(String.valueOf(fromQQ));
			if (gtdspl == null) {
				Autoreply.sendMessage(fromGroup, 0, "你没有参加过抽卡");
				return true;
			}
			sb.append("你获得了:");
			for (String s:Autoreply.instence.diceImitate.spells) {
				if (gtdspl.contains(s)) {
					sb.append("\n").append(s);
				}
			}
			Autoreply.sendMessage(fromGroup, 0, sb.toString());
			return true;	
		}
		return false;
	}

	public boolean isSetContains(Set<String> bigset, Set<String> smallset) {  
		for (String s:smallset) {
			if (!bigset.contains(s)) {
				return false;
			}
		}
		return true;  
	}

	private void checkArchievement(long fromGroup, long fromQQ, long toQQ, HashSet<String> gotSpell) {
		ArchievementBean ab=archiMap.get(String.valueOf(toQQ));
		if (ab == null) {
			ab = new ArchievementBean();
			archiMap.put(String.valueOf(toQQ), ab);
		}
		if (!ab.isArchievementGot(ArchievementBean.th6All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp6)) {
			ab.addArchievement(ArchievementBean.th6All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方红魔乡全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp6.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th7All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp7)) {
			ab.addArchievement(ArchievementBean.th7All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方妖妖梦全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp7.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th8All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp8)) {
			ab.addArchievement(ArchievementBean.th8All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方永夜抄符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp8.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th10All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp10)) {
			ab.addArchievement(ArchievementBean.th10All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方风神录全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp10.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th11All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp11)) {
			ab.addArchievement(ArchievementBean.th11All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方地灵殿全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp11.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th12All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp12)) {
			ab.addArchievement(ArchievementBean.th12All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方星莲船全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp12.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th13All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp13)) {
			ab.addArchievement(ArchievementBean.th13All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方神灵庙全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp13.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th14All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp14)) {
			ab.addArchievement(ArchievementBean.th14All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方辉针城全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp14.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th15All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp15)) {
			ab.addArchievement(ArchievementBean.th15All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方绀珠传全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp15.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th16All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp16)) {
			ab.addArchievement(ArchievementBean.th16All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方天空璋全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp16.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.th17All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp17)) {
			ab.addArchievement(ArchievementBean.th17All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方鬼形兽全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp17.size());
		}

		if (!ab.isArchievementGot(ArchievementBean.JunkoSpells) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.JunkoSpells)) {
			ab.addArchievement(ArchievementBean.JunkoSpells);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:纯化的神灵");
			giveCoins(fromGroup, toQQ, 10);
		}

		if (!ab.isArchievementGot(ArchievementBean.th6All) &&
			isSetContains(gotSpell, Autoreply.instence.diceImitate.sp6)) {
			ab.addArchievement(ArchievementBean.th6All);
			Autoreply.sendMessage(fromGroup, toQQ, "获得成就:东方红魔乡全符卡收集");
			giveCoins(fromGroup, toQQ, Autoreply.instence.diceImitate.sp6.size());
		}
		



		if (!ab.isArchievementGot(ArchievementBean.JunkoSpells) &&
			checkJunko(gotSpell)) {
			ab.addArchievement(ArchievementBean.JunkoSpells);
			Autoreply.sendMessage(fromGroup, toQQ, "junko,coins:");
			giveCoins(fromGroup, toQQ, 2);
		}

		saveArchiConfig();
	}

	private boolean checkJunko(HashSet<String> gotSpell) {
		return isSetContains(gotSpell, Autoreply.instence.diceImitate.JunkoSpells);
	}



	private void giveCoins(long group, long toQQ, int coins) {
		Autoreply.sendMessage(group, 0, "~转账 " + coins + " " + Autoreply.instence.CC.at(toQQ));
	}
	private void backupData() {
        while (true) {
            try {
                Thread.sleep(86400000);
                File backup = new File(spellFile.getAbsolutePath() + ".bak" + System.currentTimeMillis());
                FileOutputStream fos = new FileOutputStream(backup);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(chm));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	private void saveConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(spellFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(chm));
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
            writer.write(new Gson().toJson(archiMap));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
