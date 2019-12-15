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
	public static String[] name;
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
		name = new String[]{
			//th2
			"里香",
			"明罗",
			"魅魔",
			//th3
			"爱莲", 
			"小兔姬", 
			"卡娜·安娜贝拉尔",
			"朝仓理香子", 
			"北白河千百合", 
			"冈崎梦美",
			//th4
			"奥莲姬",
			"胡桃",
			"艾丽",
			"梦月",
			"幻月",
			//th5
			"萨拉",
			"露易兹",
			"雪",
			"舞",
			"梦子",
			"神绮"};
		name = Tools.ArrayTool.mergeArray(name, TH06GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH07GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH08GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new String[]{
											  //th9
											  "梅蒂欣·梅兰可莉",
											  "风见幽香",
											  "小野冢小町",
											  "四季映姬"});
		name = Tools.ArrayTool.mergeArray(name, TH10GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH11GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH12GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new String[]{
											  //th12.8
											  "桑尼·米尔克",
											  "露娜·切露德",
											  "斯塔·萨菲雅"});
		name = Tools.ArrayTool.mergeArray(name, TH13GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new String[]{
											  //th13.5
											  "秦心"});
		name = Tools.ArrayTool.mergeArray(name, TH14GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new String[]{
											  //th14.5
											  "宇佐见堇子"});
		name = Tools.ArrayTool.mergeArray(name, TH15GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, new String[]{
											  //th15.5
											  "依神紫苑",
											  "依神女苑"});
		name = Tools.ArrayTool.mergeArray(name, TH16GameData.charaName);
		name = Tools.ArrayTool.mergeArray(name, TH17GameData.charaName);
		cat.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("橙"));
		cat.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("火焰猫燐"));
		memory.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("古明地觉", "想起「恐怖的回忆」", "想起「恐怖催眠术」"));
		pachouli.addAll(Autoreply.instence.touHouDataManager.getCharaSpellCard("帕秋莉·诺蕾姬"));

	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		String[] ss = msg.split("\\.");
        if (ss.length > 1) {
            if (ss[0].equals("roll")) {
                switch (ss[1]) {
                    case "pl":
                    case "plane":
                    case "player":
                        if (ss.length == 3) {
                            rollPlane(ss[2], fromGroup);
                        } else if (ss.length == 4) {
                            rollPlane(ss[2] + "." + ss[3], fromGroup);
                        }
                        break;
                    case "游戏":
                    case "game":
                        Autoreply.sendMessage(fromGroup, 0, "th" + (Autoreply.instence.random.nextInt(16) + 1));
                        break;
                    case "diff":
                    case "difficult":
                    case "难度":
                        Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(plDiff));
                        break;
                    case "stage":
                    case "关卡":
                    case "面数":
                        rollStage(ss, fromGroup);
                        break;
                    case "help":
                    case "帮助":
                        String str = "\nroll.game roll.游戏 可以随机选择游戏\nroll.difficult roll.diff roll.难度 可以随机选择难度\nroll.player roll.pl roll.plane接作品名或编号可随机选择机体（仅官方整数作）\nroll.stage roll.关卡 roll.面数 加玩家名可用来接力时随机选择面数，多个玩家名之间用.隔开\n";
                        Autoreply.sendMessage(fromGroup, 0, str);
                        break;
                }
                return true;
            }
        }
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

	private String randomGame(String pname, long fromQQ, boolean goodAt) {
		int gameNo=md5Random(fromQQ) % 16 + 2;
		String gameName = null;
		String charaName = null;
		switch (gameNo) {
			case 2:
				gameName = "封魔录";
				charaName = md5RanStr(fromQQ + 2, pl02);
				break;
			case 3:
				gameName = "梦时空";
				charaName = md5RanStr(fromQQ + 2, pl03);
				break;
			case 4:
				gameName = "幻想乡";
				charaName = md5RanStr(fromQQ + 2, pl04);
				break;
			case 5:
				gameName = "怪绮谈";
				charaName = md5RanStr(fromQQ + 2, pl05);
				break;
			case 6:
				gameName = "红魔乡";
				charaName = md5RanStr(fromQQ + 2, TH06GameData.players);
				break;
			case 7:
				gameName = "妖妖梦";
				charaName = md5RanStr(fromQQ + 2, TH07GameData.players);
				break;
			case 8:
				gameName = "永夜抄";
				charaName = md5RanStr(fromQQ + 2, TH08GameData.players);
				break;
			case 9:
				gameName = "花映冢";
				charaName = md5RanStr(fromQQ + 2, pl09);
				break;
			case 10:
				gameName = "风神录";
				charaName = md5RanStr(fromQQ + 2, TH10GameData.players);
				break;
			case 11:
				gameName = "地灵殿";
				charaName = md5RanStr(fromQQ + 2, TH11GameData.players);
				break;
			case 12:
				gameName = "星莲船";
				charaName = md5RanStr(fromQQ + 2, TH12GameData.players);
				break;
			case 13:
				gameName = "神灵庙";
				charaName = md5RanStr(fromQQ + 2, TH13GameData.players);
				break;
			case 14:
				gameName = "辉针城";
				charaName = md5RanStr(fromQQ + 2, TH14GameData.players);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, TH14GameData.playerSub), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, TH14GameData.playerSub), gameName);
				}
			case 15:
				gameName = "绀珠传";
				charaName = md5RanStr(fromQQ + 2, TH15GameData.players);
				break;
			case 16:
				gameName = "天空璋";
				charaName = md5RanStr(fromQQ + 2, TH16GameData.players);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, TH16GameData.playerSub), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, TH16GameData.playerSub), gameName);
				}
			case 17:
				gameName = "鬼形兽";
				charaName = md5RanStr(fromQQ + 2, TH17GameData.players);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, TH17GameData.playerSub), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, TH17GameData.playerSub), gameName);
				}
			default:
				return "";
		}
		if (goodAt) {
			return String.format("%s今天宜用%s打%s", pname, charaName, gameName);
		} else {
			return String.format("忌用%s打%s", charaName, gameName);
		}
	}

	private void rollPlane(String ss, long fromGroup) {
        switch (ss) {
            case "东方灵异传":
            case "th1":
            case "th01":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl01));
                break;
            case "东方封魔录":
            case "th2":
            case "th02":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl02));
                break;
            case "东方梦时空":
            case "th3":
            case "th03":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl03));
                break;
            case "东方幻想乡":
            case "th4":
            case "th04":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl04));
                break;
            case "东方怪绮谈":
            case "th5":
            case "th05":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl05));
                break;
            case "东方红魔乡":
            case "th6":
            case "th06":
            case "tEoSD":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH06GameData.players));
                break;
            case "东方妖妖梦":
            case "th7":
            case "th07":
            case "PCB":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH07GameData.players));
                break;
            case "东方永夜抄":
            case "th8":
            case "th08":
            case "IN":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH08GameData.players));
                break;
            case "东方花映冢":
            case "th9":
            case "th09":
            case "PoFV":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(pl09));
                break;
            case "东方风神录":
            case "th10":
            case "MoF":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH10GameData.players));
                break;
            case "东方地灵殿":
            case "th11":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH11GameData.players));
                break;
            case "东方星莲船":
            case "th12":
            case "UFO":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH12GameData.players));
                break;
            case "东方神灵庙":
            case "th13":
            case "TD":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH13GameData.players));
                break;
            case "东方辉针城":
            case "th14":
            case "DDC":
                Autoreply.sendMessage(fromGroup, 0, Tools.ArrayTool.rfa(TH14GameData.players) + " " + Tools.ArrayTool.rfa(TH14GameData.playerSub));
                break;
            case "东方绀珠传":
            case "th15":
            case "LoLK":
                Autoreply.sendMessage(fromGroup, 0, (String) Tools.ArrayTool.rfa(TH15GameData.players));
                break;
            case "东方天空璋":
            case "th16":
            case "HSiFS":
                Autoreply.sendMessage(fromGroup, 0, Tools.ArrayTool.rfa(TH16GameData.players) + " " + Tools.ArrayTool.rfa(TH16GameData.playerSub));
                break;
            case "东方鬼形兽":
            case "th17":
            case "WBaWC":
                Autoreply.sendMessage(fromGroup, 0, Tools.ArrayTool.rfa(TH17GameData.players) + "+" + Tools.ArrayTool.rfa(TH17GameData.playerSub));
                break;

            case "东方文花帖":
            case "th9.5":
            case "StB":
                //       case "东方文花帖DS":
                //       case "th12.5":
                //       case "DS":
            case "妖精大战争":
            case "th12.8":
            case "弹幕天邪鬼":
            case "th14.3":
            case "ISC":
            case "秘封噩梦日记":
            case "th16.5":
            case "VD":
                Autoreply.sendMessage(fromGroup, 0, "就一个飞机你roll你[CQ:emoji,id=128052]呢");
                break;
            default:
                Autoreply.sendMessage(fromGroup, 0, "只有2un飞机游戏");
                break;
        }
    }

    private void rollStage(String[] ss, long fromGroup) {
        HashMap<Integer, String> hMap = new HashMap<>();
        for (int i = 2; i < ss.length; i++) {
            hMap.put(Autoreply.instence.random.nextInt(), ss[i]);
        }
        int flag = 1;
        StringBuilder sBuilder = new StringBuilder();
        for (Integer key : hMap.keySet()) {
            sBuilder.append("stage").append(flag).append(":").append(hMap.get(key)).append("\n");
            flag++;
        }
        Autoreply.sendMessage(fromGroup, 0, sBuilder.append("完成").toString());
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

}
