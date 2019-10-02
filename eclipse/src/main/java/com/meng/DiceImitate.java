package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

public class DiceImitate {
	private ArrayList<String> spells=new ArrayList<String>();
	private ArrayList<String> neta=new ArrayList<String>();
	private ArrayList<String> music=new ArrayList<String>();

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
				int spell=0;
				if (c == '0') {
					spell = 8;
				} else if (c == '1') {
					spell = 0;
				} else {
					spell = md5Random(fromQQ) %  spells.size();
				}
		 		if (m != null) {
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", m.getNick(), spells.get(spell)));
				} else {
					Autoreply.sendMessage(fromGroup, 0, String.format("你今天会在%s疮痍", spells.get(spell)));
				}
		   		return true;
		}

		if (msg.startsWith(".draw")) {
			String drawcmd=msg.substring(5);
			switch (drawcmd) {
				case "help":
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music");
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
