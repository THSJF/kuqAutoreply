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

	public DiceImitate() {
		spells = new String[]{
			//th6
			"「红色的幻想乡」",		
			//th7
			"雅符「春之京人偶」",

			"亡舞「生者必灭之理 -眩惑-」",
			"亡舞「生者必灭之理 -死蝶-」",
			"亡舞「生者必灭之理 -毒蛾-」",
			"亡舞「生者必灭之理 -魔境-」",
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

			"魍魉「二重黑死蝶」",
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
			"秘神摩多罗 ~ Hidden Star in All Seasons."};

		name = new String[]{
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
			"梅露兰",
			"露娜萨",
			"莉莉卡",
			"魂魄妖梦",
			"西行寺幽幽子",
			"八云蓝",
			"八云紫",
			//th8
			"莉格露",
			"萝蕾拉",
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
			//th13
			"鬼火",
			"幽谷响子",
			"宫古芳香",
			"霍青娥",
			"苏我屠自古",
			"物部布都",
			"丰聪耳神子",
			"二岩瑞藏",
			//th14
			"若鹭姬",
			"赤蛮奇",
			"今泉影狼",
			"九十九八桥",
			"九十九弁弁",
			"鬼人正邪",
			"少名针妙丸",
			"堀川雷鼓",
			//th15
			"清兰",
			"铃瑚",
			"哆来咪苏伊特",
			"稀神探女",
			"克劳恩皮丝",
			"纯狐",
			"赫卡提亚拉碧斯拉祖利",
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
				if (c == '0' || fromQQ == 2856986197l) {
					fpro = 99.61f;
				} else if (c == '1') {
					fpro = 97.60f;
				} else {
					fpro = ((float)(md5Random(fromQQ) % 10001)) / 100;
				}
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%.2f%%处疮痍", pname, fpro));
				return true;	
			case "。jrrp":
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, spells[md5Random(fromQQ) %  spells.length]));
		   		return true;
		}

		if (msg.startsWith(".draw")) {
			String drawcmd=msg.substring(6);
			switch (drawcmd) {
				case "help":
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma");
					return true;
				case "spell":
					Autoreply.sendMessage(fromGroup, 0, spells[new Random().nextInt(spells.length)]);
					return true;
				case "neta":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜打%s", pname, neta[md5Random(fromQQ) % neta.length]));
					return true;
				case "music":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜听%s", pname, music[md5Random(fromQQ) % music.length]));
					return true;
				case "grandma":
					if (fromQQ == 2856986197L) {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认键山雏当奶奶", pname));
						return true;
					}
					String grandmaMd5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
					char grandmaMd5c=grandmaMd5.charAt(0);
					if (grandmaMd5c == '0') {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认八云紫当奶奶", pname));
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认%s当奶奶", pname, name[md5Random(fromQQ) % name.length]));
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
