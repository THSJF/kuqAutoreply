package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;

public class DiceImitate {
	public boolean check (long fromGroup, long fromQQ, String msg) {
		switch(msg) {
			case ".jrrp":
			case "。jrrp":
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
		}
		return false;
	}
}
