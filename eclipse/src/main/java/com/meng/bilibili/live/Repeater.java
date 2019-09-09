package com.meng.bilibili.live;

import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.groupChat.*;
import com.meng.tools.*;
import com.meng.tools.gifHelper.*;
import java.io.*;

public class Repeater {
    private String lastMessageRecieved = "";
    private boolean lastStatus = false;
	private int repeatCount=0;

    public Repeater() {
        
	  }

    public String dealMsg(String msg) {
        String b = null;
        if (!lastStatus && lastMessageRecieved.equals(msg)) {
            b = repeatStart(msg);
		  } else if (lastStatus && lastMessageRecieved.equals(msg)) {
            b = repeatRunning(msg);
		  } else if (lastStatus && !lastMessageRecieved.equals(msg)) {
            b = repeatEnd(msg);
		  }
        lastStatus = lastMessageRecieved.equals(msg);
		lastMessageRecieved = msg;
        return b;
	  }

    private String repeatEnd(String msg) {
		return null;
	  }

    private String repeatRunning(String msg) {
        return null;
	  }

    private String repeatStart(String msg) {   
		++repeatCount;
		if (msg.contains("蓝") || msg.contains("藍")) {
            return null;
		  }
		if (msg.contains("此生无悔入东方")) {
    		return msg;
		  }
        if (repeatCount < 3) {
            ++repeatCount;
			return msg;
		  } else if (repeatCount == 3) {
			++repeatCount;
			return "你直播间天天复读";
		  } else {
            String newmsg = new StringBuilder(msg).reverse().toString();
			repeatCount = 0;
			return newmsg.equals(msg) ? newmsg + " " : newmsg;
		  }
	  }
  }
