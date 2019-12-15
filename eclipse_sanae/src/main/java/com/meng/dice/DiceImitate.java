package com.meng.dice;
import com.meng.*;
import com.meng.gameData.TouHou.zun.*;
import com.meng.tools.*;
import java.util.*;
import com.meng.gameData.TouHou.*;
import java.security.cert.*;
import java.lang.reflect.*;

public class DiceImitate {
	public static SpellCard[] spells;
	public static String[] neta;
	public static String[] music;
	public static TouhouCharacter[] name;
	public static String[] wayToGoodEnd;
	private static String[] pl01 = new String[]{"别打砖块了，来飞机"};
	private static String[] pl02 = new String[]{"范围重视型", "高灵击伤害 平衡型", "威力重视型"};
    private static String[] pl03 = new String[]{"博丽灵梦", "魅魔", "雾雨魔理沙", "爱莲", "小兔姬", "卡娜·安娜贝拉尔", "朝仓理香子", "北白河千百合", "冈崎梦美"};
    private static String[] pl04 = new String[]{"博丽灵梦 诱导", "博丽灵梦 大范围", "雾雨魔理沙 激光", "雾雨魔理沙 高速射击"};
    private static String[] pl05 = new String[]{"博丽灵梦", "雾雨魔理沙", "魅魔", "幽香"};
    private static String[] pl09 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜", "魂魄妖梦", "铃仙·优昙华院·因幡", "琪露诺", "莉莉卡·普莉兹姆利巴", "梅露兰·普莉兹姆利巴", "露娜萨·普莉兹姆利巴", "米斯蒂娅·萝蕾拉", "因幡帝", "射命丸文", "梅蒂欣·梅兰可莉", "风见幽香", "小野冢小町", "四季映姬·亚玛萨那度"};
 	private static String[] plDiff = new String[]{"easy", "normal", "hard", "lunatic"};

	public static HashSet<SpellCard> cat=new HashSet<>();
	public static HashSet<SpellCard> memory=new HashSet<>();
	public static HashSet<SpellCard> pachouli=new HashSet<>();


	public DiceImitate() {
		spells = new SpellCard[]{};
		spells = Tools.ArrayTool.mergeArray(spells, TH06GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH07GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH08GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH10GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH11GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH12GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH13GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH14GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH15GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH16GameData.spellcards);
		spells = Tools.ArrayTool.mergeArray(spells, TH17GameData.spellcards);
		neta = new String[]{
			"红lnb",
			"红lnm",
			"妖lnm",
			"妖lnn",
			"永lnm",
			"风lnm",
			"风lnn",
			"殿lnm",
			"船lnm",
			"船lnn",
			"庙lnm",
			"城lnm",
			"绀lnm",
			"璋lnn"};
		wayToGoodEnd = new String[]{
			"红魔乡normal",
			"妖妖梦easy",
			"永夜抄6B",
			"风神录normal",
			"地灵殿normal",
			"星莲船normal",
			"神灵庙normal",
			"辉针城灵梦B",
			"辉针城魔理沙B",
			"辉针城咲夜B",
			"绀珠传no miss",
			"天空璋extra",
			"鬼形兽normal"
		};
		music = new String[]{
			//th4
			"bad apple",
		};
		music = Tools.ArrayTool.mergeArray(music, TH06GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH07GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH08GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH10GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH11GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH12GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH13GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH14GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH15GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH16GameData.musicName);
		music = Tools.ArrayTool.mergeArray(music, TH17GameData.musicName);
		name = new TouhouCharacter[]{
			//th2
			new TouhouCharacter("里香", "东方封魔录"),
			new TouhouCharacter("明罗", "东方封魔录"),
			new TouhouCharacter("魅魔", "东方封魔录"),
			//th3
			new TouhouCharacter("爱莲", "东方梦时空"),
			new TouhouCharacter("小兔姬", "东方梦时空"),
			new TouhouCharacter("卡娜·安娜贝拉尔", "东方梦时空"),
			new TouhouCharacter("朝仓理香子", "东方梦时空"),
			new TouhouCharacter("北白河千百合", "东方梦时空"),
			new TouhouCharacter("冈崎梦美", "东方梦时空"),
			//th4
			new TouhouCharacter("奥莲姬", "东方幻想乡"),
			new TouhouCharacter("胡桃", "东方幻想乡"),
			new TouhouCharacter("艾丽", "东方幻想乡"),
			new TouhouCharacter("梦月", "东方幻想乡"),
			new TouhouCharacter("幻月", "东方幻想乡"),
			//th5
			new TouhouCharacter("萨拉", "东方怪绮谈"),
			new TouhouCharacter("露易兹", "东方怪绮谈"),
			new TouhouCharacter("雪", "东方怪绮谈"),
			new TouhouCharacter("舞", "东方怪绮谈"),
			new TouhouCharacter("梦子", "东方怪绮谈"),
			new TouhouCharacter("神绮", "东方怪绮谈")};
		name = Tools.ArrayTool.mergeArray(name, TH06GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH07GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH08GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new TouhouCharacter[]{
											  //th9
											  new TouhouCharacter("梅蒂欣·梅兰可莉", "东方花映冢"),
											  new TouhouCharacter("风见幽香", "东方花映冢"),
											  new TouhouCharacter("小野冢小町", "东方花映冢"),
											  new TouhouCharacter("四季映姬", "东方花映冢")});
		name = Tools.ArrayTool.mergeArray(name, TH10GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH11GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH12GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new TouhouCharacter[]{
											  //th12.8
											  new TouhouCharacter("桑尼·米尔克", "妖精大战争"),
											  new TouhouCharacter("露娜·切露德", "妖精大战争"),
											  new TouhouCharacter("斯塔·萨菲雅", "妖精大战争")});
		name = Tools.ArrayTool.mergeArray(name, TH13GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new TouhouCharacter[]{
											  //th13.5
											  new TouhouCharacter("秦心", "东方心绮楼")});
		name = Tools.ArrayTool.mergeArray(name, TH14GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new TouhouCharacter[]{
											  //th14.5
											  new TouhouCharacter("宇佐见堇子", "东方深秘录")});
		name = Tools.ArrayTool.mergeArray(name, TH15GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new TouhouCharacter[]{
											  //th15.5
											  new TouhouCharacter("依神紫苑", "东方凭依华"),
											  new TouhouCharacter("依神女苑", "东方凭依华")});
		name = Tools.ArrayTool.mergeArray(name, TH16GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH17GameData.charaName);
		cat.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("橙"));
		cat.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("火焰猫燐"));
		memory.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("古明地觉", "想起「恐怖的回忆」", "想起「恐怖催眠术」"));
		pachouli.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("帕秋莉·诺蕾姬"));

	}
	public boolean check(long fromGroup, long fromQQ, String msg) {

		String pname=Autoreply.instence.configManager.getNickName(fromGroup, fromQQ);
		String md5=Tools.Hash.toMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		char c=md5.charAt(0);
		switch (msg) {
			case ".jrrp":
				float fpro=0f;
				if (c == '0') {
					fpro = 99.61f;
				} else if (c == '1') {
					fpro = 97.60f;
				} else {
					fpro = ((float)(md5Random(fromQQ) % 10001)) / 100;
				}
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%.2f%%处疮痍", pname, fpro));
				return true;	
			case "。jrrp":
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, md5RanStr(fromQQ, spells)));
		   		return true;
		}

		if (msg.startsWith(".draw")) {
			String drawcmd=msg.substring(6);
			switch (drawcmd) {
				case "help":
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma game");
					return true;
				case "spell":
					Autoreply.sendMessage(fromGroup, 0, spells[new Random().nextInt(spells.length)].n);
					return true;
				case "neta":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, neta)));
					return true;
				case "game":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, wayToGoodEnd)));
					return true;
				case "music":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜听%s", pname, md5RanStr(fromQQ, music)));
					return true;
				case "grandma":
					if (Tools.Hash.toMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认八云紫当奶奶", pname));
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name)));
					return true;
				case "jrrp":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, md5RanStr(fromQQ, spells)));
					return true;

				default:
					if (drawcmd.startsWith("spell ")) {	
						int i=-1;
						try {
							i = Integer.parseInt(drawcmd.substring(6));
							String spellResult=pname + "抽到了:";
							Random r=new Random();
							for (int ii=0;ii < i;ii++) {
								spellResult += "\n";
								spellResult += spells[r.nextInt(spells.length)];			
							}	
							Autoreply.sendMessage(fromGroup, 0, spellResult);
						} catch (NumberFormatException e) {
							Autoreply.sendMessage(fromGroup, 0, e.toString());
						}
					} else {
						Autoreply.sendMessage(fromGroup, 0, "可用.draw help查看帮助");						
					}
					break;
			}	
			return true;
		}

		return false;
	}

	private int md5Random(long fromQQ) {
		String md5=Tools.Hash.toMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		return Integer.parseInt(md5.substring(26), 16);
	}

	public String md5RanStr(long fromQQ, String[] arr) {
		return arr[md5Random(fromQQ) % arr.length];
	}

	public String md5RanStr(long fromQQ, SpellCard[] arr) {
		return arr[md5Random(fromQQ) % arr.length].n;
	}

	private String md5RanStr(long fromQQ, TouhouCharacter[] arr) {
		return arr[md5Random(fromQQ) % arr.length].charaName;
	}
}
