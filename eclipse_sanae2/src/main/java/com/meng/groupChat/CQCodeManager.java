package com.meng.groupChat;

import com.meng.*;

public class CQCodeManager {

	public boolean check(long fromGroup, String msg) {
		/*    if (msg.startsWith("[CQ:share,url=")) {
		 String picture = msg.substring(msg.lastIndexOf("http"), msg.lastIndexOf("]"));
		 Autoreply.sendMessage(fromGroup, 0, "封面图:" + picture);
		 return true;
		 } else*/ if (msg.startsWith("[CQ:music")) {
            switch (Autoreply.ins.random.nextInt(3)) {
                case 0:
                    Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().music(22636603, "163", 0));
                    break;
                case 1:
                    Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().music(103744845, "qq", 0));
                    break;
                case 2:
                    Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().music(103744852, "qq", 0));
                    break;
            }
            return true;
        } else if (msg.startsWith("[CQ:location,lat=")) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().location(35.594993, 118.869838, 15, "守矢神社", "此生无悔入东方 来世愿生幻想乡"));
			return true;
		}
		return false;
	}

}
