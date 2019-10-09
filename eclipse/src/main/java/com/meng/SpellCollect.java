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
			checkArchievement(fromGroup, fromQQ, tmpSet);
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

	private void checkArchievement(long fromGroup, long fromQQ, HashSet<String> gotSpell) {
		ArchievementBean ab=archiMap.get(String.valueOf(fromQQ));
		if (ab == null) {
			ab = new ArchievementBean();
			archiMap.put(String.valueOf(fromQQ), ab);
		}
		if (!ab.isArchievementGot(ArchievementBean.th6All) && checkTh06All(gotSpell)) {
			ab.addArchievement(ArchievementBean.th6All);
			Autoreply.sendMessage(fromGroup, fromQQ, "th06Got");
		}

		if (!ab.isArchievementGot(ArchievementBean.th10All) && checkTh10All(gotSpell)) {
			ab.addArchievement(ArchievementBean.th10All);
			Autoreply.sendMessage(fromGroup, fromQQ, "th10Got,coins:");
			giveCoins(fromGroup, fromQQ, 2);
		}
		saveArchiConfig();
	}

	private boolean checkTh06All(HashSet<String> gotSpell) {
		return isSetContains(gotSpell, Autoreply.instence.diceImitate.sp6);
	}

	private boolean checkTh10All(HashSet<String> gotSpell) {
		return isSetContains(gotSpell, Autoreply.instence.diceImitate.sp10);
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
