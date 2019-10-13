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
	private ArrayList<Archievement> archList=new ArrayList<>();
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

		archList.add(new Archievement("东方红魔乡全符卡收集", "收集该作全部符卡", ArchievementBean.th6All, Autoreply.instence.diceImitate.sp6.size(), Autoreply.instence.diceImitate.sp6));
		archList.add(new Archievement("东方妖妖梦全符卡收集", "收集该作全部符卡",  ArchievementBean.th7All, Autoreply.instence.diceImitate.sp7.size(), Autoreply.instence.diceImitate.sp7));
		archList.add(new Archievement("东方永夜抄全符卡收集", "收集lastword外全部符卡",  ArchievementBean.th8All, Autoreply.instence.diceImitate.sp8.size(), Autoreply.instence.diceImitate.sp8));
		archList.add(new Archievement("东方风神录全符卡收集", "收集该作全部符卡",  ArchievementBean.th10All, Autoreply.instence.diceImitate.sp10.size(), Autoreply.instence.diceImitate.sp10));
		archList.add(new Archievement("东方地灵殿全符卡收集", "收集该作全部符卡",  ArchievementBean.th11All, Autoreply.instence.diceImitate.sp11.size(), Autoreply.instence.diceImitate.sp11));
		archList.add(new Archievement("东方星莲船全符卡收集", "收集该作全部符卡",  ArchievementBean.th12All, Autoreply.instence.diceImitate.sp12.size(), Autoreply.instence.diceImitate.sp12));
		archList.add(new Archievement("东方神灵庙全符卡收集", "收集overdrive外全部符卡",  ArchievementBean.th13All, Autoreply.instence.diceImitate.sp13.size(), Autoreply.instence.diceImitate.sp13));
		archList.add(new Archievement("东方辉针城全符卡收集", "收集该作全部符卡",  ArchievementBean.th14All, Autoreply.instence.diceImitate.sp14.size(), Autoreply.instence.diceImitate.sp14));
		archList.add(new Archievement("东方绀珠传全符卡收集", "收集该作全部符卡",  ArchievementBean.th15All, Autoreply.instence.diceImitate.sp15.size(), Autoreply.instence.diceImitate.sp15));
		archList.add(new Archievement("东方天空璋全符卡收集", "收集该作全部符卡",  ArchievementBean.th16All, Autoreply.instence.diceImitate.sp16.size(), Autoreply.instence.diceImitate.sp16));
		archList.add(new Archievement("东方鬼形兽全符卡收集", "收集该作全部符卡",  ArchievementBean.th17All, Autoreply.instence.diceImitate.sp17.size(), Autoreply.instence.diceImitate.sp17));
		archList.add(new Archievement("纯化的神灵", "收集Lunatic难度纯狐所有符卡", ArchievementBean.JunkoSpells, 10, Autoreply.instence.diceImitate.JunkoSpells));
		archList.add(new Archievement("Perfect Cherry Blossom", "收集Lunatic难度西行寺幽幽子所有符卡", ArchievementBean.yoyoko, 10, Autoreply.instence.diceImitate.yykSpells));
		archList.add(new Archievement("樱花飞舞", "收集樱符「完全墨染的樱花 -封印-」,樱符「完全墨染的樱花 -亡我-」,樱符「完全墨染的樱花 -春眠-」,樱符「完全墨染的樱花 -开花-」", ArchievementBean.sakura, 15, Autoreply.instence.diceImitate.sakura));
		archList.add(new Archievement("春天来了", "获得春符「惊春之喜」", ArchievementBean.LilyWhite, 10, "春符「惊春之喜」"));
		archList.add(new Archievement("素质三连", "收集「红色的幻想乡」,神光「无忤为宗」,「纯粹的疯狂」", ArchievementBean.threeHits, 5, "「红色的幻想乡」", "神光「无忤为宗」", "「纯粹的疯狂」"));
		archList.add(new Archievement("信仰之山", "获得「信仰之山」" , ArchievementBean.MountainOfFaith, 5, "「信仰之山」"));
		archList.add(new Archievement("极冰盛宴", "获得冰符「冰瀑」,雹符「冰雹暴风」,冻符「完美冻结」,雪符「钻石风暴」,冰符「冰袭方阵」,冰符「Ultimate Blizzard」" , ArchievementBean.ice, 5, "冰符「冰瀑」", "雹符「冰雹暴风」", "冻符「完美冻结」", "雪符「钻石风暴」", "冰符「冰袭方阵」", "冰符「Ultimate Blizzard」"));
		archList.add(new Archievement("伪物理学家", "获得想起「波与粒的境界」" , ArchievementBean.physics, 5, "想起「波与粒的境界」"));
		


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

		if (msg.equals("查看成就")) {
			StringBuilder sb=new StringBuilder();
			ArchievementBean ab=archiMap.get(String.valueOf(fromQQ));
			sb.append("你获得了:");
			if (ab.isArchievementGot(ArchievementBean.th6All)) {
				sb.append("\n东方红魔乡全符卡收集");
			}

			if (ab.isArchievementGot(ArchievementBean.JunkoSpells)) {
				sb.append("\n纯化的神灵");
			}
			Autoreply.sendMessage(fromGroup, 0, sb.toString());
			return true;	
		}

		return false;
	}

	private void checkArchievement(long fromGroup, long fromQQ, long toQQ, HashSet<String> gotSpell) {
		ArchievementBean ab=archiMap.get(String.valueOf(toQQ));
		if (ab == null) {
			ab = new ArchievementBean();
			archiMap.put(String.valueOf(toQQ), ab);
		}
		for (Archievement ac:archList) {
			if (ac.getNewArchievement(ab, gotSpell)) {
				ab.addArchievement(ac.archNum);
				Autoreply.sendMessage(fromGroup, toQQ, "获得成就:" + ac.name + "\n条件:" + ac.describe);
				Autoreply.sendMessage(fromGroup, 0, "~addcoins " + ac.coins + " " + Autoreply.instence.CC.at(toQQ));		
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
                writer1.write(new Gson().toJson(chm));
                writer1.flush();
                fos1.close();			
				File ar=new File(archiFile.getAbsolutePath() + ".bak" + System.currentTimeMillis());
				FileOutputStream fos = new FileOutputStream(ar);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(archiMap));
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
