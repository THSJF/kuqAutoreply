package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

public class DiceImitate {
	private ArrayList<String> spells=new ArrayList<String>();
	private ArrayList<String> neta=new ArrayList<String>();
	private ArrayList<String> music=new ArrayList<String>();
	private ArrayList<String> name=new ArrayList<String>();


	public DiceImitate() {
		spells.add("「红色的幻想乡」");
		spells.add("秘术「天文密葬法」");
		spells.add("神宝「蓬莱的玉枝　-梦色之乡-」");
		spells.add("「地底太阳」");
		spells.add("飞钵「传说的飞空圆盘」");	
		spells.add("「新生的神灵」");
		spells.add("「七个一寸法师」");
		spells.add("「里·Breeze Cherry Blossom」");
		spells.add("「里·Perfect Summer Ice」");
		spells.add("「里·Crazy Fall Wind」");
		spells.add("「里·Extreme Winter」");

		spells.add("「Idola Diabolus」");

		spells.add("「最初与最后的无名弹幕」");


		spells.add("月「阿波罗反射镜」");

		spells.add("月「月狂冲击」");


		spells.add("雅符「春之京人偶」");
		spells.add("紫奥义「弹幕结界」");
		spells.add("魍魉「二重黑死蝶」");
		spells.add("「地狱的人工太阳」");
		spells.add("「地狱的托卡马克」");
		spells.add("超人「圣白莲」");


		spells.add("铳符「月狂之枪」");


		spells.add("亡舞「生者必灭之理 -眩惑-」");
		spells.add("亡舞「生者必灭之理 -死蝶-」");
		spells.add("亡舞「生者必灭之理 -毒蛾-」");
		spells.add("亡舞「生者必灭之理 -魔境-」");

		spells.add("幽曲「埋骨于弘川 -伪灵-」");
		spells.add("幽曲「埋骨于弘川 -亡灵-」");
		spells.add("幽曲「埋骨于弘川 -幻灵-」");
		spells.add("幽曲「埋骨于弘川 -神灵-」");

		spells.add("樱符「完全墨染的樱花 -封印-」");
		spells.add("樱符「完全墨染的樱花 -忘我-」");
		spells.add("樱符「完全墨染的樱花 -春眠-」");
		spells.add("樱符「完全墨染的樱花 -开花-」");

		spells.add("「反魂蝶 -一分咲-」");
		spells.add("「反魂蝶 -三分咲-」");
		spells.add("「反魂蝶 -五分咲-」");
		spells.add("「反魂蝶 -八分咲-」");


		spells.add("「掌上的纯光」");
		spells.add("「杀意的百合」");
		spells.add("「现代的神灵界」");
		spells.add("「原始的神灵界」");
		spells.add("「战栗的寒冷之星」");
		spells.add("「纯粹的疯狂」");
		spells.add("「溢出的暇秽」");
		spells.add("「地上秽的纯化」");
		spells.add("纯符「单纯的子弹地狱」");
		spells.add("纯符「纯粹的弹幕地狱」");


		spells.add("想起「二重黑死蝶」");
		spells.add("「河童的幻想大瀑布」");
		spells.add("「信仰之山」");
		spells.add("「风神之神德」");

		spells.add("神祭「扩展御柱」");
		spells.add("筒粥「神の粥」");
		spells.add("神谷「神灵之谷」");
		spells.add("神秘「葛井之清水」");
		spells.add("神秘「大和茅环」");

		spells.add("叶符「狂舞的落叶」");


		spells.add("疵符「破裂的护符」");
		spells.add("疵痕「损坏的护身符」");
		spells.add("悲运「大钟婆之火」");

		spells.add("创符「流刑人偶」");

		spells.add("奇迹「神之风」");
		spells.add("大奇迹「八坂之神风」");


		spells.add("「星辰降落的神灵庙」");


		spells.add("天流「天水奇迹」");
		spells.add("天龙「雨之源泉」");
		spells.add("「无双风神」");
		spells.add("「幻想风靡」");

		neta.add("红lnb");
		neta.add("红lnm");
		neta.add("妖lnm");
		neta.add("妖lnn");
		neta.add("永lnm");
		neta.add("风lnm");
		neta.add("风lnn");
		neta.add("殿lnm");
		neta.add("船lnm");
		neta.add("船lnn");
		neta.add("庙lnm");
		neta.add("城lnm");
		neta.add("绀lnm");
		neta.add("璋lnn");

		music.add("bad apple");

		music.add("月时计");
		music.add("献给已逝公主的七重奏");

		music.add("天空的花之都");
		music.add("幽灵乐团");
		music.add("幽雅的绽放 墨染的樱花");
		music.add("少女幻葬");
		music.add("死亡幻想");

		music.add("少女绮想曲");
		music.add("恋色 master spark");
		music.add("竹取飞翔");

		music.add("眷爱众生之神");
		music.add("众神眷恋的幻想乡");
		music.add("少女曾见的日本原风景");
		music.add("信仰是为了虚幻之人");
		music.add("神圣庄严的古战场");

		music.add("漫步在地狱的街道");
		music.add("尸体旅行");

		music.add("春之岸边");
		music.add("感情的摩天轮");

		music.add("古老的元神");
		music.add("圣德传说　～ True Administrator");

		music.add("幻想净琉璃");
		music.add("辉光之针的小人族");

		music.add("忘不了，那曾依籍的绿意");
		music.add("九月的南瓜");
		music.add("遥遥38万公里的航程");
		music.add("星条旗的小丑");
		music.add("Pure Furies ~ 心之所在");

		music.add("被隐匿的四个季节");
		music.add("门再也进不去了");
		music.add("秘神摩多罗 ~ Hidden Star in All Seasons.");


		name.add("露米娅");
		name.add("大妖精");
		name.add("琪露诺");
		name.add("红美铃");
		name.add("帕秋莉诺蕾姬");
		name.add("十六夜咲夜");
		name.add("蕾米莉亚斯卡雷特");
		name.add("芙兰朵露斯卡雷特");

		name.add("蕾蒂");
		name.add("橙");
		name.add("爱丽丝玛格特罗依德");
		name.add("梅露兰");
		name.add("露娜萨");
		name.add("莉莉卡");
		name.add("魂魄妖梦");
		name.add("西行寺幽幽子");
		name.add("八云蓝");
		name.add("八云紫");

		name.add("莉格露");
		name.add("萝蕾拉");
		name.add("上白泽慧音");
		name.add("博丽灵梦");
		name.add("雾雨魔理沙");
		name.add("因幡帝");
		name.add("铃仙优昙华院因幡");
		name.add("八意永琳");
		name.add("蓬莱山辉夜");
		name.add("藤原妹红");

		name.add("秋静叶");
		name.add("秋穰子");
		name.add("键山雏");
		name.add("河城荷取");
		name.add("犬走椛");
		name.add("射命丸文");
		name.add("东风谷早苗");
		name.add("八坂神奈子");
		name.add("洩矢诹访子");

		name.add("琪斯美");
		name.add("黑谷山女");
		name.add("水桥帕露西");
		name.add("星熊勇仪");
		name.add("古明地觉");
		name.add("火焰猫燐");
		name.add("灵乌路空");
		name.add("古明地恋");

		name.add("纳兹琳");
		name.add("多多良小伞");
		name.add("云居一轮");
		name.add("村纱水蜜");
		name.add("寅丸星");
		name.add("圣白莲");
		name.add("封兽鵺");

		name.add("清兰");
		name.add("克劳恩皮丝");
		name.add("纯狐");

	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup, fromQQ);				
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
				if (m != null) {
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%.2f%%处疮痍", m.getNick(), fpro));
				} else {
					Autoreply.sendMessage(fromGroup, 0, String.format("你今天会在%.2f%%处疮痍", fpro));
				}
				return true;	
			case "。jrrp":
		 		if (m != null) {
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", m.getNick(), spells.get(md5Random(fromQQ) %  spells.size())));
				} else {
					Autoreply.sendMessage(fromGroup, 0, String.format("你今天会在%s疮痍", spells.get(md5Random(fromQQ) %  spells.size())));
				}
		   		return true;
		}

		if (msg.startsWith(".draw")) {
			String drawcmd=msg.substring(6);
			switch (drawcmd) {
				case "help":
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma");
					return true;
				case "spell":
					Autoreply.sendMessage(fromGroup, 0, spells.get(new Random().nextInt(spells.size())));
					return true;
				case "neta":
					Autoreply.sendMessage(fromGroup, 0, "今天宜打" + neta.get(md5Random(fromQQ) % neta.size()));
					return true;
				case "music":
					Autoreply.sendMessage(fromGroup, 0, "今天宜听" + music.get(md5Random(fromQQ) % music.size()));
					return true;
				case "grandma":
					if (fromQQ == 2856986197L) {
						Autoreply.sendMessage(fromGroup, 0, "今天宜认键山雏当奶奶");
						return true;
					}
					String grandmaMd5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
					char grandmaMd5c=grandmaMd5.charAt(0);
					if (grandmaMd5c == '0') {
						Autoreply.sendMessage(fromGroup, 0, "今天宜认八云紫当奶奶");
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, "今天宜认" + name.get(md5Random(fromQQ) % name.size()) + "当奶奶");
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

}
