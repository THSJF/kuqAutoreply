package com.meng;

public class MainSwitch {
	public static int checkSwitch(long fromGroup,String msg) {
		if (msg.equals(".stop")) {
			Autoreply.sendGroupMessage(fromGroup, "disabled");
			Autoreply.enable = false;
			return Autoreply.MSG_IGNORE;
		}
		if (msg.equals(".start")) {
			Autoreply.enable = true;
			Autoreply.sendGroupMessage(fromGroup, "enabled");
			return Autoreply.MSG_IGNORE;
		}
		
		return 3;
	}

}
