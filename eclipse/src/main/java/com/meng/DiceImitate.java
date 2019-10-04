package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;
import com.meng.config.javabeans.*;

public class DiceImitate {
	private String[] spells;
	private String[] neta;
	private String[] music;
	private String[] name;

	private String[] pl02 = new String[]{"范围重视型", "高灵击伤害 平衡型", "威力重视型"};
    private String[] pl03 = new String[]{"博丽灵梦", "魅魔", "雾雨魔理沙", "爱莲", "小兔姬", "卡娜·安娜贝拉尔", "朝仓理香子", "北白河千百合", "冈崎梦美"};
    private String[] pl04 = new String[]{"博丽灵梦 诱导", "博丽灵梦 大范围", "雾雨魔理沙 激光", "雾雨魔理沙 高速射击"};
    private String[] pl05 = new String[]{"博丽灵梦", "雾雨魔理沙", "魅魔", "幽香"};
    private String[] pl06 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符"};
    private String[] pl07 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符", "十六夜咲夜 幻符", "十六夜咲夜 时符"};
    private String[] pl08 = new String[]{"幻想的结界组", "咏唱禁咒组", "梦幻的红魔组", "幽冥之住人组", "博丽灵梦", "八云紫", "雾雨魔理沙", "爱丽丝·玛格特罗依德", "蕾米莉亚·斯卡蕾特", "十六夜咲夜", "西行寺幽幽子", "魂魄妖梦"};
    private String[] pl09 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜", "魂魄妖梦", "铃仙·优昙华院·因幡", "琪露诺", "莉莉卡·普莉兹姆利巴", "梅露兰·普莉兹姆利巴", "露娜萨·普莉兹姆利巴", "米斯蒂娅·萝蕾拉", "因幡帝", "射命丸文", "梅蒂欣·梅兰可莉", "风见幽香", "小野冢小町", "四季映姬·亚玛萨那度"};
    private String[] pl10 = new String[]{"博丽灵梦 诱导装备", "博丽灵梦 前方集中装备", "博丽灵梦 封印装备", "雾雨魔理沙 高威力装备", "雾雨魔理沙 贯通装备", "雾雨魔理沙 魔法使装备"};
    private String[] pl11 = new String[]{"博丽灵梦 八云紫", "博丽灵梦 伊吹萃香", "博丽灵梦 射命丸文", "雾雨魔理沙 爱丽丝·玛格特罗依德", "雾雨魔理沙 帕秋莉", "雾雨魔理沙 河城荷取"};
    private String[] pl12 = new String[]{"博丽灵梦 诱导型", "博丽灵梦 威力重视型", "雾雨魔理沙 贯通型", "雾雨魔理沙 范围重视型", "东风谷早苗 诱导型", "东风谷早苗 广范围炸裂型"};
    private String[] pl13 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "魂魄妖梦"};
    private String[] pl14 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜"};
    private String[] pl14s = new String[]{"使用妖器", "不使用妖器"};
    private String[] pl15 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "铃仙·优昙华院·因幡"};
    private String[] pl16 = new String[]{"博丽灵梦", "琪露诺", "射命丸文", "雾雨魔理沙"};
    private String[] pl16s = new String[]{"春", "夏", "秋", "冬"};
    private String[] pl17 = new String[]{"博丽灵梦", "雾雨魔理沙", "魂魄妖梦"};
    private String[] pl17s = new String[]{"[CQ:emoji,id=128059]哥", "[CQ:emoji,id=128037]哥", "[CQ:emoji,id=128054]哥"};


	public DiceImitate() {
		spells = new String[]{
			//th6
			"「红色的幻想乡」",		
			//th7
			"冰符「冰袭方阵」",

			"寒符「延长的冬日」",
			"冬符「花之凋零」",
			"白符「波状光」",
			"怪符「桌灵转」",

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

			"操符「少女文乐」",
			"苍符「博爱的法兰西人偶」",
			"苍符「博爱的奥尔良人偶」",
			"红符「红发的荷兰人偶」",
			"白符「白垩的俄罗斯人偶」",
			"暗符「雾之伦敦人偶」",
			"回符「轮回的西藏人偶」",
			"雅符「春之京人偶」",
			"诅咒「魔彩光的上海人偶」",
			"诅咒「上吊的蓬莱人偶」",

			"弦奏「Guarneri del Gesù」",
			"神弦「Stradivarius」",
			"伪弦「Pseudo Stradivarius」",

			"管灵「日野幻想」",
			"冥管「灵之克里福德」",
			"管灵「灵之克里福德」",

			"冥键「法吉奥里冥奏」",
			"键灵「蓓森朵芙神奏」",

			"骚符「幽灵絮语」",
			"骚符「活着的骚灵」",
			"合葬「棱镜协奏曲」",
			"骚葬「冥河边缘」",
			"大合葬「灵车大协奏曲」",
			"大合葬「灵车大协奏曲改」",
			"大合葬「灵车大协奏曲怪」",

			"幽鬼剑「妖童饿鬼之断食」",
			"饿鬼剑「饿鬼道草纸」",
			"饿王剑「饿鬼十王的报应」",
			"狱界剑「二百由旬之一闪」",
			"狱炎剑「业风闪影阵」",
			"狱神剑「业风神闪斩」",
			"畜趣剑「无为无策之冥罚」",
			"修罗剑「现世妄执」",
			"人界剑「悟入幻想」",
			"人世剑「大悟显晦」",
			"人神剑「俗谛常住」",
			"天上剑「天人之五衰」",
			"天界剑「七魄忌讳」",
			"天神剑「三魂七魄」",

			"六道剑「一念无量劫」",
			"亡乡「亡我乡 -彷徨的灵魂-」",
			"亡乡「亡我乡 -宿罪-」",
			"亡乡「亡我乡 -无道之路-」",
			"亡乡「亡我乡 -自尽-」",
			"亡舞「生者必灭之理 -眩惑-」",
			"亡舞「生者必灭之理 -死蝶-」",
			"亡舞「生者必灭之理 -毒蛾-」",
			"亡舞「生者必灭之理 -魔境-」",
			"华灵「死蝶」",
			"华灵「燕尾蝶」",
			"华灵「深固难徙之蝶」",
			"华灵「蝶幻」",
			"幽曲「埋骨于弘川 -伪灵-」",
			"幽曲「埋骨于弘川 -亡灵-」",
			"幽曲「埋骨于弘川 -幻灵-」",
			"幽曲「埋骨于弘川 -神灵-」",
			"樱符「完全墨染的樱花 -封印-」",
			"樱符「完全墨染的樱花 -亡我-」",
			"樱符「完全墨染的樱花 -春眠-」",
			"樱符「完全墨染的樱花 -开花-」",
			"「反魂蝶 -一分咲-」",
			"「反魂蝶 -三分咲-」",
			"「反魂蝶 -五分咲-」",
			"「反魂蝶 -八分咲-」",

			"鬼符「青鬼赤鬼」",
			"鬼神「飞翔毘沙门天」",		
			"式神「仙狐思念」",
			"式神「十二神将之宴」",
			"式辉「狐狸妖怪激光」",
			"式辉「迷人的四面楚歌」",
			"式辉「天狐公主 -Illusion-」",
			"式弹「往生极乐的佛教徒」",
			"式弹「片面义务契约」」",
			"式神「橙」",
			"「狐狗狸的契约」",
			"幻神「饭纲权现降临」",

			"式神「前鬼后鬼的守护」",	
			"式神「凭依荼吉尼天」",
			"结界「梦境与现实的诅咒」",
			"结界「动与静的均衡」」",
			"结界「光明与黑暗的网目」",
			"罔两「直与曲的梦乡」",
			"罔两「八云紫的神隐」",
			"罔两「栖息于禅寺的妖蝶」",
			"魍魉「二重黑死蝶」",
			"式神「八云蓝」",
			"「人与妖的境界」",
			"结界「生与死的境界」",
			"紫奥义「弹幕结界」",

			//th8
			"秘术「天文密葬法」",
			"神宝「蓬莱的玉枝　-梦色之乡-」",	
			//th10		
			"叶符「狂舞的落叶」",

			"疵符「破裂的护符」",
			"疵痕「损坏的护身符」",
			"悲运「大钟婆之火」",
			"创符「流刑人偶」",

			"「河童的幻想大瀑布」",

			"「幻想风靡」",
			"「无双风神」",

			"奇迹「神之风」",
			"大奇迹「八坂之神风」",

			"神祭「扩展御柱」",
			"筒粥「神の粥」",
			"神谷「神灵之谷」",
			"神秘「葛井之清水」",
			"神秘「大和茅环」",	
			"天流「天水奇迹」",
			"天龙「雨之源泉」",
			"「信仰之山」",
			"「风神之神德」",

			//th11	
			"想起「二重黑死蝶」",

			"「地狱的托卡马克」",
			"「地狱的人工太阳」",
			"「地底太阳」",	
			//th12	
			"超人「圣白莲」",
			"飞钵「传说的飞空圆盘」",		
			//th13
			"「星辰降落的神灵庙」",
			"「新生的神灵」",
			//th14
			"「七个一寸法师」",	
			//th15
			"铳符「月狂之枪」",

			"「掌上的纯光」",
			"「杀意的百合」",
			"「现代的神灵界」",
			"「原始的神灵界」",
			"「战栗的寒冷之星」",
			"「纯粹的疯狂」",
			"「溢出的暇秽」",
			"「地上秽的纯化」",
			"纯符「单纯的子弹地狱」",
			"纯符「纯粹的弹幕地狱」",

			"月「阿波罗反射镜」",
			"月「月狂冲击」",
			"「最初与最后的无名弹幕」",		
			//th16
			"「里·Breeze Cherry Blossom」",
			"「里·Perfect Summer Ice」",
			"「里·Crazy Fall Wind」",
			"「里·Extreme Winter」",	
			//th17	
			"「Idola Diabolus」"};
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
			"竹取飞翔",
			//th10
			"眷爱众生之神",
			"众神眷恋的幻想乡",
			"少女曾见的日本原风景",
			"信仰是为了虚幻之人",
			"神圣庄严的古战场",
			//th11
			"漫步在地狱的街道",
			"尸体旅行",
			//th12
			"春之岸边",
			"感情的摩天轮",
			//th13
			"古老的元神",
			"圣德传说　～ True Administrator",
			//th14
			"幻想净琉璃",
			"辉光之针的小人族",
			//th15
			"忘不了，那曾依籍的绿意",
			"九月的南瓜",
			"遥遥38万公里的航程",
			"星条旗的小丑",
			"Pure Furies ~ 心之所在",
			//th16
			"被隐匿的四个季节",
			"门再也进不去了",
			"秘神摩多罗 ~ Hidden Star in All Seasons.",
			//th17
			"寄世界于偶像 ~ Idoratrize World",
		};

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
			"帕秋莉诺蕾姬",
			"十六夜咲夜",
			"蕾米莉亚斯卡雷特",
			"芙兰朵露斯卡雷特",
			//th7
			"蕾蒂",
			"橙",
			"爱丽丝玛格特罗依德",
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
			"铃仙优昙华院因幡",
			"八意永琳",
			"蓬莱山辉夜",
			"藤原妹红",
			//th9
			"梅蒂欣梅兰可莉",
			"风见幽香",
			"小野冢小町",
			"四季映姬",
			//th10
			"秋静叶",
			"秋穰子",
			"键山雏",
			"河城荷取",
			"犬走椛",
			"射命丸文",
			"东风谷早苗",
			"八坂神奈子",
			"洩矢诹访子",
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
			"桑尼米尔克",
			"露娜切露德",
			"斯塔萨菲雅",
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
			"宇佐见堇子",
			//th15
			"清兰",
			"铃瑚",
			"哆来咪苏伊特",
			"稀神探女",
			"克劳恩皮丝",
			"纯狐",
			"赫卡提亚拉碧斯拉祖利",
			//th15.5
			"依神紫苑",
			"依神女苑",
			//th16
			"爱塔妮缇拉尔瓦",
			"坂田合欢",
			"高丽野阿吽",
			"矢田寺成美",
			"尔子田里乃",
			"丁礼田舞",
			"摩多罗隐岐奈",
			//th17
			"戎璎花",
			"牛崎润美",
			"庭渡久侘歌",
			"吉吊八千慧",
			"杖刀偶磨弓",
			"埴安神袿姬",
			"骊驹早鬼"};

	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		String pname=getName(fromGroup, fromQQ);
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
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma game");
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
					String grandmaMd5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
					if (grandmaMd5.charAt(0) == '0') {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认八云紫当奶奶", pname));
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name)));
					return true;
				case "game":
					int gameNo=md5Random(fromQQ) % 16 + 2;
					String gameName = null;
					String charaName = null;
					switch (gameNo) {
						case 2:
							gameName = "封魔录";
							charaName = md5RanStr(fromQQ, pl02);
							break;
						case 3:
							gameName = "梦时空";
							charaName = md5RanStr(fromQQ, pl03);
							break;
						case 4:
							gameName = "幻想乡";
							charaName = md5RanStr(fromQQ, pl04);
							break;
						case 5:
							gameName = "怪绮谈";
							charaName = md5RanStr(fromQQ, pl05);
							break;
						case 6:
							gameName = "红魔乡";
							charaName = md5RanStr(fromQQ, pl06);
							break;
						case 7:
							gameName = "妖妖梦";
							charaName = md5RanStr(fromQQ, pl07);
							break;
						case 8:
							gameName = "永夜抄";
							charaName = md5RanStr(fromQQ, pl08);
							break;
						case 9:
							gameName = "花映冢";
							charaName = md5RanStr(fromQQ, pl09);
							break;
						case 10:
							gameName = "风神录";
							charaName = md5RanStr(fromQQ, pl10);
							break;
						case 11:
							gameName = "地灵殿";
							charaName = md5RanStr(fromQQ, pl11);
							break;
						case 12:
							gameName = "星莲船";
							charaName = md5RanStr(fromQQ, pl12);
							break;
						case 13:
							gameName = "神灵庙";
							charaName = md5RanStr(fromQQ, pl13);
							break;
						case 14:
							gameName = "辉针城";
							charaName = md5RanStr(fromQQ, pl14);
							Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl14s), gameName));
							return true;
						case 15:
							gameName = "绀珠传";
							charaName = md5RanStr(fromQQ, pl15);
							break;
						case 16:
							gameName = "天空璋";
							charaName = md5RanStr(fromQQ, pl16);
							Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl16s), gameName));
							return true;
						case 17:
							gameName = "鬼形兽";
							charaName = md5RanStr(fromQQ, pl17);
							Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl17s), gameName));		
							return true;
						default:
							return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜用%s打%s", pname, charaName, gameName));
					return true;
				default:
					Autoreply.sendMessage(fromGroup, 0, "可用.draw help查看帮助");
					break;
			}	
			return true;
		}

		return false;
	}

	private int md5Random(long fromQQ) {
		String md5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		return Integer.parseInt(md5.substring(26), 16);
	}

	private String md5RanStr(long fromQQ, String[] arr) {
		return arr[md5Random(fromQQ) % arr.length];
	}

	private String getName(long fromGroup, long fromQQ) {
		PersonInfo personInfo=Autoreply.instence.configManager.getPersonInfoFromQQ(fromQQ);
		Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup, fromQQ);
		if (personInfo != null) {
			return personInfo.name;
		} else if (m != null) {
			return m.getNick();
		} else {
			return "你";
		}
	}
}
