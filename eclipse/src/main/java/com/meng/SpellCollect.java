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

		archList.add(new Archievement("东方红魔乡全符卡收集", "收集该作全部符卡", ArchievementBean.th6All, DiceImitate.sp6.size(), DiceImitate.sp6));
		archList.add(new Archievement("东方妖妖梦全符卡收集", "收集该作全部符卡",  ArchievementBean.th7All, DiceImitate.sp7.size(), DiceImitate.sp7));
		archList.add(new Archievement("东方永夜抄全符卡收集", "收集lastspell和lastword外全部符卡",  ArchievementBean.th8All, DiceImitate.sp8.size(), DiceImitate.sp8));
		archList.add(new Archievement("东方风神录全符卡收集", "收集该作全部符卡",  ArchievementBean.th10All, DiceImitate.sp10.size(), DiceImitate.sp10));
		archList.add(new Archievement("东方地灵殿全符卡收集", "收集该作全部符卡",  ArchievementBean.th11All, DiceImitate.sp11.size(), DiceImitate.sp11));
		archList.add(new Archievement("东方星莲船全符卡收集", "收集该作全部符卡",  ArchievementBean.th12All, DiceImitate.sp12.size(), DiceImitate.sp12));
		archList.add(new Archievement("东方神灵庙全符卡收集", "收集overdrive外全部符卡",  ArchievementBean.th13All, DiceImitate.sp13.size(), DiceImitate.sp13));
		archList.add(new Archievement("东方辉针城全符卡收集", "收集该作全部符卡",  ArchievementBean.th14All, DiceImitate.sp14.size(), DiceImitate.sp14));
		archList.add(new Archievement("东方绀珠传全符卡收集", "收集该作全部符卡",  ArchievementBean.th15All, DiceImitate.sp15.size(), DiceImitate.sp15));
		archList.add(new Archievement("东方天空璋全符卡收集", "收集该作全部符卡",  ArchievementBean.th16All, DiceImitate.sp16.size(), DiceImitate.sp16));
		archList.add(new Archievement("东方鬼形兽全符卡收集", "收集该作全部符卡",  ArchievementBean.th17All, DiceImitate.sp17.size(), DiceImitate.sp17));
		archList.add(new Archievement("纯化的神灵", "收集Lunatic难度纯狐所有符卡", ArchievementBean.JunkoSpells, 10, "「掌上的纯光」", "「杀意的百合」", "「现代的神灵界」", "「战栗的寒冷之星」", "「纯粹的疯狂」", "「地上秽的纯化」", "纯符「纯粹的弹幕地狱」"));
		archList.add(new Archievement("Perfect Cherry Blossom", "收集Lunatic难度西行寺幽幽子所有符卡", ArchievementBean.yoyoko, 10, "亡乡「亡我乡 -自尽-」", "亡舞「生者必灭之理 -魔境-」",  "华灵「蝶幻」", "幽曲「埋骨于弘川 -神灵-」", "樱符「完全墨染的樱花 -开花-」", "「反魂蝶 -八分咲-」"));
		archList.add(new Archievement("樱花飞舞", "收集樱符「完全墨染的樱花 -封印-」,樱符「完全墨染的樱花 -亡我-」,樱符「完全墨染的樱花 -春眠-」,樱符「完全墨染的樱花 -开花-」", ArchievementBean.sakura, 15, "樱符「完全墨染的樱花 -封印-」", "樱符「完全墨染的樱花 -亡我-」", "樱符「完全墨染的樱花 -春眠-」", "樱符「完全墨染的樱花 -开花-」"));
		archList.add(new Archievement("春天来了", "获得春符「惊春之喜」", ArchievementBean.LilyWhite, 10, "春符「惊春之喜」"));
		archList.add(new Archievement("素质三连", "收集「红色的幻想乡」,神光「无忤为宗」,「纯粹的疯狂」", ArchievementBean.threeHits, 8, "「红色的幻想乡」", "神光「无忤为宗」", "「纯粹的疯狂」"));
		archList.add(new Archievement("信仰之山", "获得「信仰之山」" , ArchievementBean.MountainOfFaith, 5, "「信仰之山」"));
		archList.add(new Archievement("极冰盛宴", "获得冰符「冰瀑」,雹符「冰雹暴风」,冻符「完美冻结」,雪符「钻石风暴」,冰符「冰袭方阵」,冰符「Ultimate Blizzard」,「里·Perfect Summer Ice」" , ArchievementBean.ice, 25, "冰符「冰瀑」", "雹符「冰雹暴风」", "冻符「完美冻结」", "雪符「钻石风暴」", "冰符「冰袭方阵」", "冰符「Ultimate Blizzard」", "「里·Perfect Summer Ice」"));
		archList.add(new Archievement("伪物理学家", "获得想起「波与粒的境界」" , ArchievementBean.physics, 5, "想起「波与粒的境界」"));
		archList.add(new Archievement("城管执法", "获得博丽灵梦lastspell和lastword外所有符卡" , ArchievementBean.reimu, 20, "梦符「二重结界」", "梦境「二重大结界」", "灵符「梦想封印　散」", "散灵「梦想封印　寂」", "梦符「封魔阵」", "神技「八方鬼缚阵」", "神技「八方龙杀阵」", "灵符「梦想封印　集」", "回灵「梦想封印　侘」", "境界「二重弹幕结界」", "大结界「博丽弹幕结界」"));
		archList.add(new Archievement("星光爆裂", "获得雾雨魔理沙lastspell和lastword外所有符卡", ArchievementBean.marisa, 20, "魔符「银河」", "魔空「小行星带」", "魔符「星尘幻想」", "黑魔「黑洞边缘」", "恋符「非定向光线」", "恋风「星光台风」", "恋符「极限火花」", "恋心「二重火花」", "光符「地球光」", "光击「击月」"));
		archList.add(new Archievement("春之岸边", "获得棒符「忙碌探知棒」", ArchievementBean.spring, 5, "棒符「忙碌探知棒」"));
		archList.add(new Archievement("神绮的影子", "获得大魔法「魔神复诵」", ArchievementBean.moshenfusong, 5, "大魔法「魔神复诵」"));
		archList.add(new Archievement("撸猫", "获得橙和燐的所有符卡", ArchievementBean.cat, 27, DiceImitate.cat));
		archList.add(new Archievement("时间都去哪了", "获得十六夜咲夜的所有符卡", ArchievementBean.time, 9, "奇术「误导」", "奇术「幻惑误导」", "幻在「钟表的残骸」", "幻幽「迷幻杰克」", "幻象「月神之钟」", "幻世「世界」", "女仆秘技「操弄玩偶」", "女仆秘技「杀人玩偶」", "奇术「永恒的温柔」"));
		archList.add(new Archievement("漂移轮椅", "获得「里·Crazy Fall Wind」", ArchievementBean.piaoyilunyi, 9, "「里·Crazy Fall Wind」"));
		archList.add(new Archievement("四个季节", "获得「里·Breeze Cherry Blossom」,「里·Perfect Summer Ice」,「里·Crazy Fall Wind」,「里·Extreme Winter」", ArchievementBean.fourSeasons, 9, "「里·Breeze Cherry Blossom」", "「里·Perfect Summer Ice」", "「里·Crazy Fall Wind」", "「里·Extreme Winter」"));
		archList.add(new Archievement("Hidden Bugs in Four Spells", "获得尽符「血腥的深山谋杀」,笹符「七夕星祭」,冥加「在你背后」,舞符「背后之祭」", ArchievementBean.hiddenBugInFourSpells, 16, "尽符「血腥的深山谋杀」", "笹符「七夕星祭」", "冥加「在你背后」", "舞符「背后之祭」"));
		archList.add(new Archievement("信仰是为了虚幻之人", "获得东方风神录中东风谷早苗Lunatic所有符卡", ArchievementBean.sanae, 16, "秘术「一脉相传的弹幕」", "奇迹「客星辉煌之夜」", "开海「摩西之奇迹」", "准备「召请建御名方神」", "大奇迹「八坂之神风」"));
		archList.add(new Archievement("赏月", "获得狱符「地狱之蚀」,「阿波罗捏造说」,月「月狂冲击」", ArchievementBean.moon, 8, "狱符「地狱之蚀」", "「阿波罗捏造说」", "月「月狂冲击」"));

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
				String s=DiceImitate.spells[r.nextInt(DiceImitate.spells.length)];
				tmpSet.add(s);
				sb.append("\n").append(s);
			}
			saveConfig();
			checkArchievement(fromGroup, fromQQ, chan.get(0), tmpSet);
			if (sb.toString().length() > 100){
				return true;
			}
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
			int i=0;
			for (String s:DiceImitate.spells) {
				if (gtdspl.contains(s)) {
					sb.append("\n").append(s);
					++i;
					if (i > 40){
						Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
						sb.setLength(0);
						i = 0;
					}
				}
			}
			Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
			return true;	
		}

		if (msg.equals("查看成就")) {
			StringBuilder sb=new StringBuilder();
			ArchievementBean ab=archiMap.get(String.valueOf(fromQQ));
			sb.append("你获得了:");
			for (Archievement ac:archList){
				if (ab.isArchievementGot(ac.archNum)){
					sb.append("\n").append(ac.name);
				}
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
				Autoreply.sendMessage(fromGroup, toQQ, "获得成就:" + ac.name + "\n~addcoins " + ac.coins + " " + Autoreply.instence.CC.at(toQQ) + "\n条件:" + ac.describe);	
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
