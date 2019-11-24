package com.meng.dice;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

import com.meng.Autoreply;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import com.meng.gameData.TouHou.zun.*;

public class DiceImitate {
	public static String[] spells;
	public static String[] lastword;
	public static String[] overdrive;
	public static String[] neta;
	public static String[] music;
	public static String[] name;

	private static String[] pl01 = new String[]{"别打砖块了，来飞机"};
	private static String[] pl02 = new String[]{"范围重视型", "高灵击伤害 平衡型", "威力重视型"};
    private static String[] pl03 = new String[]{"博丽灵梦", "魅魔", "雾雨魔理沙", "爱莲", "小兔姬", "卡娜·安娜贝拉尔", "朝仓理香子", "北白河千百合", "冈崎梦美"};
    private static String[] pl04 = new String[]{"博丽灵梦 诱导", "博丽灵梦 大范围", "雾雨魔理沙 激光", "雾雨魔理沙 高速射击"};
    private static String[] pl05 = new String[]{"博丽灵梦", "雾雨魔理沙", "魅魔", "幽香"};
    private static String[] pl06 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符"};
    private static String[] pl07 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符", "十六夜咲夜 幻符", "十六夜咲夜 时符"};
    private static String[] pl08 = new String[]{"幻想的结界组", "咏唱禁咒组", "梦幻的红魔组", "幽冥之住人组", "博丽灵梦", "八云紫", "雾雨魔理沙", "爱丽丝·玛格特罗依德", "蕾米莉亚·斯卡蕾特", "十六夜咲夜", "西行寺幽幽子", "魂魄妖梦"};
    private static String[] pl09 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜", "魂魄妖梦", "铃仙·优昙华院·因幡", "琪露诺", "莉莉卡·普莉兹姆利巴", "梅露兰·普莉兹姆利巴", "露娜萨·普莉兹姆利巴", "米斯蒂娅·萝蕾拉", "因幡帝", "射命丸文", "梅蒂欣·梅兰可莉", "风见幽香", "小野冢小町", "四季映姬·亚玛萨那度"};
    private static String[] pl11 = new String[]{"博丽灵梦 八云紫", "博丽灵梦 伊吹萃香", "博丽灵梦 射命丸文", "雾雨魔理沙 爱丽丝·玛格特罗依德", "雾雨魔理沙 帕秋莉", "雾雨魔理沙 河城荷取"};
    private static String[] pl12 = new String[]{"博丽灵梦 诱导型", "博丽灵梦 威力重视型", "雾雨魔理沙 贯通型", "雾雨魔理沙 范围重视型", "东风谷早苗 诱导型", "东风谷早苗 广范围炸裂型"};
    private static String[] pl13 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "魂魄妖梦"};
    private static String[] pl14 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜"};
    private static String[] pl14s = new String[]{"使用妖器", "不使用妖器"};
    private static String[] pl17 = new String[]{"博丽灵梦", "雾雨魔理沙", "魂魄妖梦"};
    private static String[] pl17s = new String[]{"[CQ:emoji,id=128059]哥", "[CQ:emoji,id=128037]哥", "[CQ:emoji,id=128054]哥"};
	private static String[] plDiff = new String[]{"easy", "normal", "hard", "lunatic"};

	public static HashSet<String> sp6=new HashSet<>();
	public static HashSet<String> sp7=new HashSet<>();
	public static HashSet<String> sp8=new HashSet<>();
	public static HashSet<String> sp11=new HashSet<>();
	public static HashSet<String> sp12=new HashSet<>();
	public static HashSet<String> sp13=new HashSet<>();
	public static HashSet<String> sp14=new HashSet<>();
	public static HashSet<String> sp17=new HashSet<>();

	public static HashSet<String> cat=new HashSet<>();
	public static HashSet<String> memory=new HashSet<>();
	public static HashSet<String> pachouli=new HashSet<>();


	public DiceImitate() {
		lastword = new String[]{
			"「不合时令的蝶雨」",
			"「失明的夜雀」",
			"「日出之国的天子」",
			"「远古的骗术」",
			"「月的红眼」",
			"「天网蛛网捕蝶之法」",
			"「蓬莱的树海」",
			"「不死鸟重生」",
			"「似有似无的净化」",
			"「梦想天生」",
			"「彗星」",
			"「收缩的世界」",
			"「待宵反射卫星斩」",
			"「猎奇剧团的怪人」",
			"「绯红的宿命」",
			"「西行寺无余涅槃」",
			"「深弹幕结界 梦幻泡影」"
		};
		overdrive = new String[]{
			"樱符「樱吹雪地狱」",
			"山彦「山彦的发挥本领之回音」",
			"毒爪「不死的杀人鬼」",
			"道符「道胎动 ~道~」",
			"怨灵「入鹿之雷」",
			"圣童女「太阳神的贡品」",
			"「神灵大宇宙」",
			"「Wild Carpet」"
		};
		spells = new String[]{};
		spells = Methods.mergeArray(spells, TH06GameData.spells);
		spells = Methods.mergeArray(spells, TH07GameData.spells);
		spells = Methods.mergeArray(spells, TH08GameData.spells);
		spells = Methods.mergeArray(spells, TH10GameData.spells);
		spells = Methods.mergeArray(spells, TH11GameData.spells);
		spells = Methods.mergeArray(spells, TH12GameData.spells);
		spells = Methods.mergeArray(spells, TH13GameData.spells);
		spells = Methods.mergeArray(spells, TH14GameData.spells);
		spells = Methods.mergeArray(spells, TH15GameData.spells);
		spells = Methods.mergeArray(spells, TH16GameData.spells);
		spells = Methods.mergeArray(spells, TH17GameData.spells);
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
		music = new String[]{
			//th4
			"bad apple",
			//th6
			"月时计",
			"献给已逝公主的七重奏",
			//th7
			"天空的花之都",
			"幽灵乐团",
			"幽雅的绽放 墨染的樱花",
			"少女幻葬",
			"死亡幻想",
			//th8
			"少女绮想曲",
			"恋色 master spark",
			"竹取飞翔"};
		music = Methods.mergeArray(music, TH10GameData.musicName);
		music = Methods.mergeArray(music, new String[]{
									   //th11
									   "漫步在地狱的街道",
									   "尸体旅行",
									   //th12
									   "春之岸边",
									   "感情的摩天轮",
									   //th13
									   "古老的元神",
									   "圣德传说 ~ True Administrator",
									   //th14
									   "幻想净琉璃",
									   "辉光之针的小人族"});
		music = Methods.mergeArray(music, TH15GameData.musicName);
		music = Methods.mergeArray(music, TH16GameData.musicName);
		music = Methods.mergeArray(music, new String[]{
									   //th17
									   "寄世界于偶像 ~ Idoratrize World"
								   });
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
			"神绮",
			//th6
			"露米娅",
			"大妖精",
			"琪露诺",
			"红美铃",
			"帕秋莉·诺蕾姬",
			"十六夜咲夜",
			"蕾米莉亚·斯卡雷特",
			"芙兰朵露·斯卡雷特",
			//th7
			"蕾蒂",
			"橙",
			"爱丽丝·玛格特罗依德",
			"莉莉白",
			"莉莉卡·普莉兹姆利巴", 
			"梅露兰·普莉兹姆利巴",
			"露娜萨·普莉兹姆利巴",
			"魂魄妖梦",
			"西行寺幽幽子",
			"八云蓝",
			"八云紫",
			//th7.5
			"伊吹萃香",
			//th8
			"莉格露",
			"米斯蒂娅·萝蕾拉",
			"上白泽慧音",
			"博丽灵梦",
			"雾雨魔理沙",
			"因幡帝",
			"铃仙·优昙华院·因幡",
			"八意永琳",
			"蓬莱山辉夜",
			"藤原妹红",
			//th9
			"梅蒂欣·梅兰可莉",
			"风见幽香",
			"小野冢小町",
			"四季映姬"};
		name = Methods.mergeArray(name, TH10GameData.charaName);
		name = Methods.mergeArray(name, new String[]{
									  //th11
									  "琪斯美",
									  "黑谷山女",
									  "水桥帕露西",
									  "星熊勇仪",
									  "古明地觉",
									  "火焰猫燐",
									  "灵乌路空",
									  "古明地恋",
									  //th12
									  "纳兹琳",
									  "多多良小伞",
									  "云居一轮",
									  "村纱水蜜",
									  "寅丸星",
									  "圣白莲",
									  "封兽鵺",
									  //th12.8
									  "桑尼·米尔克",
									  "露娜·切露德",
									  "斯塔·萨菲雅",
									  //th13
									  "鬼火",
									  "幽谷响子",
									  "宫古芳香",
									  "霍青娥",
									  "苏我屠自古",
									  "物部布都",
									  "丰聪耳神子",
									  "二岩瑞藏",
									  //th13.5
									  "秦心",
									  //th14
									  "若鹭姬",
									  "赤蛮奇",
									  "今泉影狼",
									  "九十九八桥",
									  "九十九弁弁",
									  "鬼人正邪",
									  "少名针妙丸",
									  "堀川雷鼓",
									  //th14.5
									  "宇佐见堇子"});
		name = Methods.mergeArray(name, TH15GameData.charaName);
		name = Methods.mergeArray(name, new String[]{
									  //th15.5
									  "依神紫苑",
									  "依神女苑"});
		name = Methods.mergeArray(name, TH16GameData.charaName);
		name = Methods.mergeArray(name, new String[]{
									  //th17
									  "戎璎花",
									  "牛崎润美",
									  "庭渡久侘歌",
									  "吉吊八千慧",
									  "杖刀偶磨弓",
									  "埴安神袿姬",
									  "骊驹早鬼"});
		addArrayToSet(sp6, "月符「月光」", "QED「495年的波纹」");
		addArrayToSet(sp7, "冰符「冰袭方阵」", "紫奥义「弹幕结界」");
		addArrayToSet(sp8, "萤符「地上的流星」", "「蓬莱人形」");
		addArrayToSet(sp11, "怪奇「钓瓶落之怪」", "「Subterranean Rose」");
		addArrayToSet(sp12, "棒符「忙碌探知棒」", "恨弓「源三位赖政之弓」");
		addArrayToSet(sp13, "符牒「死蝶之舞」", "貉符「满月下的腹鼓舞」");
		addArrayToSet(sp14, "冰符「Ultimate Blizzard」", "「Pristine beat」");
		addArrayToSet(sp17, "石符「石林」", "「鬼畜生的所业」");
		addArrayToSet(memory, "想起「二重黑死蝶」", "想起「粼粼水底之心伤」");
		addArrayToSet(pachouli, "火符「火神之光」", "土&金符「翡翠巨石」");
		addArrayToSet(pachouli, "月符「静息的月神」", "火水木金土符「贤者之石」");

		Collections.addAll(cat, new String[]{
							   "仙符「凤凰卵」",
							   "仙符「凤凰展翅」",
							   "式符「飞翔晴明」",
							   "阴阳「道满晴明」",	
							   "阴阳「晴明大纹」",
							   "天符「天仙鸣动」",
							   "翔符「飞翔韦驮天」",
							   "童符「护法天童乱舞」",
							   "仙符「尸解永远」",
							   "鬼符「鬼门金神」",
							   "方符「奇门遁甲」",
							   "鬼符「青鬼赤鬼」",
							   "鬼神「飞翔毘沙门天」",
							   "猫符「猫的步伐」",
							   "猫符「怨灵猫乱步」",
							   "咒精「僵尸妖精」",
							   "咒精「怨灵凭依妖精」",
							   "恨灵「脾脏蛀食者」",
							   "尸灵「食人怨灵」",
							   "赎罪「旧地狱的针山」",
							   "赎罪「古时之针与痛楚的怨灵」",
							   "「死灰复燃」",
							   "「小恶灵复活」",
							   "妖怪「火焰的车轮」"
						   });

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
                        Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(plDiff));
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
		String md5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
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
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma game all");
					return true;
				case "spell":
					Autoreply.sendMessage(fromGroup, 0, spells[new Random().nextInt(spells.length)]);
					return true;
				case "neta":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, neta)));
					return true;
				case "music":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜听%s", pname, md5RanStr(fromQQ, music)));
					return true;
				case "grandma":
					if (MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认八云紫当奶奶", pname));
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name)));
					return true;
				case "game":
					String s=randomGame(pname, fromQQ, true);
					s += ",";
					s += randomGame(pname, fromQQ + 1, false);
					Autoreply.sendMessage(fromGroup, 0, s);
					return true;
				case "jrrp":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, md5RanStr(fromQQ, spells)));
					return true;
				case "all":
					String sss=String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, neta));
					sss += "\n";
					sss += String.format("%s今天宜听%s", pname, md5RanStr(fromQQ, music));
					sss += "\n";
					if (MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
						sss += String.format("%s今天宜认八云紫当奶奶", pname);
					} else {
						sss += String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name));
					}
					sss += "\n";
					sss += randomGame(pname, fromQQ, true);
					sss += ",";
					sss += randomGame(pname, fromQQ + 1, false);
					sss += "\n";
					float fpro=0f;
					if (c == '0') {
						fpro = 99.61f;
					} else if (c == '1') {
						fpro = 97.60f;
					} else {
						fpro = ((float)(md5Random(fromQQ) % 10001)) / 100;
					}
					sss += String.format("%s今天会在%.2f%%处疮痍", pname, fpro);
					Autoreply.sendMessage(fromGroup, 0, sss);
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
				charaName = md5RanStr(fromQQ + 2, pl06);
				break;
			case 7:
				gameName = "妖妖梦";
				charaName = md5RanStr(fromQQ + 2, pl07);
				break;
			case 8:
				gameName = "永夜抄";
				charaName = md5RanStr(fromQQ + 2, pl08);
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
				charaName = md5RanStr(fromQQ + 2, pl11);
				break;
			case 12:
				gameName = "星莲船";
				charaName = md5RanStr(fromQQ + 2, pl12);
				break;
			case 13:
				gameName = "神灵庙";
				charaName = md5RanStr(fromQQ + 2, pl13);
				break;
			case 14:
				gameName = "辉针城";
				charaName = md5RanStr(fromQQ + 2, pl14);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl14s), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, pl14s), gameName);
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
				charaName = md5RanStr(fromQQ + 2, pl17);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl17s), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, pl17s), gameName);
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
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl01));
                break;
            case "东方封魔录":
            case "th2":
            case "th02":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl02));
                break;
            case "东方梦时空":
            case "th3":
            case "th03":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl03));
                break;
            case "东方幻想乡":
            case "th4":
            case "th04":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl04));
                break;
            case "东方怪绮谈":
            case "th5":
            case "th05":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl05));
                break;
            case "东方红魔乡":
            case "th6":
            case "th06":
            case "tEoSD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl06));
                break;
            case "东方妖妖梦":
            case "th7":
            case "th07":
            case "PCB":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl07));
                break;
            case "东方永夜抄":
            case "th8":
            case "th08":
            case "IN":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl08));
                break;
            case "东方花映冢":
            case "th9":
            case "th09":
            case "PoFV":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl09));
                break;
            case "东方风神录":
            case "th10":
            case "MoF":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(TH10GameData.players));
                break;
            case "东方地灵殿":
            case "th11":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl11));
                break;
            case "东方星莲船":
            case "th12":
            case "UFO":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl12));
                break;
            case "东方神灵庙":
            case "th13":
            case "TD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl13));
                break;
            case "东方辉针城":
            case "th14":
            case "DDC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl14) + " " + Methods.rfa(pl14s));
                break;
            case "东方绀珠传":
            case "th15":
            case "LoLK":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(TH15GameData.players));
                break;
            case "东方天空璋":
            case "th16":
            case "HSiFS":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(TH16GameData.players) + " " + Methods.rfa(TH16GameData.playerSub));
                break;
            case "东方鬼形兽":
            case "th17":
            case "WBaWC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl17) + "+" + Methods.rfa(pl17s));
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
		String md5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		return Integer.parseInt(md5.substring(26), 16);
	}

	public String md5RanStr(long fromQQ, String[] arr) {
		return arr[md5Random(fromQQ) % arr.length];
	}

	private void addArrayToSet(Set<String> set, String start, String stop) {
		int istart=0;
		int istop=0;
		for (int i=0;i < spells.length;++i) {
			if (spells[i].equals(start)) {
				istart = i;
			} else if (spells[i].equals(stop)) {
				istop = i;
			}	
		}
		for (int i=istart;i <= istop;++i) {
			set.add(spells[i]);
		}
	}
}
