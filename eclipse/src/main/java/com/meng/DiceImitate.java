package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

public class DiceImitate {
	private ArrayList<String> spells=new ArrayList<String>();
	public DiceImitate(){
		spells.add("「红色的幻想乡」");
		spells.add("「反魂蝶 -八分咲-」");
		spells.add("秘术「天文密葬法」");
		spells.add("神宝「蓬莱的玉枝　-梦色之乡-」");
		spells.add("「地底太阳」");
		spells.add("飞钵「传说的飞空圆盘」");	
		spells.add("「新生的神灵」");
		spells.add("「七个一寸法师」");
		spells.add("纯符「纯粹的弹幕地狱」");
		spells.add("「里·Breeze Cherry Blossom」");
		spells.add("「里·Perfect Summer Ice」");
		spells.add("「里·Crazy Fall Wind」");
		spells.add("「里·Extreme Winter」");
		
		
	}
	public boolean check (long fromGroup, long fromQQ, String msg) {
		switch(msg) {
			case ".jrrp":
				Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup,fromQQ);				
				String md5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
				float fpro=0f;
				char c=md5.charAt(0);
				if(c == '0' || fromQQ == 2856986197l) {
					fpro = 99.61f;
				} else if(c == '1') {
					fpro = 97.60f;
				} else {
					fpro = ((float)(Integer.parseInt(md5.substring(26),16) % 10001)) / 100;
				}
				if(m != null) {
					Autoreply.sendMessage(fromGroup,0,String.format("%s今天会在%.2f%%处疮痍",m.getNick(),fpro));
				} else {
					Autoreply.sendMessage(fromGroup,0,String.format("你今天会在%.2f%%处疮痍",fpro));
				}
				return true;	
			case "。jrrp":
				Member m2=Autoreply.CQ.getGroupMemberInfo(fromGroup,fromQQ);				
				String md52=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
				char c2=md52.charAt(0);
				int spell=0;
				if(c2 == '0' || fromQQ == 2856986197l) {
					spell=8;
				} else if(c2 == '1') {
					spell=0;
				} else {
				spell=Integer.parseInt(md52.substring(26),16) %  spells.size();
				}
				if(m2 != null) {
					Autoreply.sendMessage(fromGroup,0,String.format("%s今天会在%s疮痍",m2.getNick(),spells.get(spell)));
				} else {
					Autoreply.sendMessage(fromGroup,0,String.format("你今天会在%s疮痍",spells.get(spell)));
				}
				return true;
		}
		return false;
	}
}
