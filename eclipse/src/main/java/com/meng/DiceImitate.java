package com.meng;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;

public class DiceImitate {
	public boolean check (long fromGroup, long fromQQ, String msg) {
		switch(msg) {
			case ".jrrp":
			case "。jrrp":
				Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup,fromQQ);
				long oneDay=24 * 60 * 60 * 1000;				
				String res;
				String md5=MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / oneDay));
				int ipro=Integer.parseInt(md5.substring(26),16) % 10001;
				float fpro=((float)ipro) / 100;
				int md5First=Integer.parseInt(md5.substring(0,1),16);
				if(md5First == 1) {
					fpro = 99.61f;
				}
				if(md5First == 2) {
					fpro = 97.60f;
				}
				if(fromQQ == 2856986197l) {
					fpro = 99.61f;
				}
				if(m != null) {
					res = String.format("%s今天会在%.2f%%处疮痍",m.getNick(),fpro);
				} else {
					res = String.format("你今天会在%.2f%%处疮痍",fpro);
				}
				Autoreply.sendMessage(fromGroup,0,res);
				return true;	
		}
		return false;
	}
}
