package com.meng.messageProcess;

import com.meng.*;
import java.util.*;

import static com.meng.Autoreply.sendMessage;

public class WarnMessageProcessor {
	private String[] warningMsgs = new String[]{
		"监听直播",	
    };

    public WarnMessageProcessor() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg) {
        boolean b = false;
        if (isWarnMsg(msg)) {
			b = true;
		}
        return b;
    }

    private boolean isAtme(String msg) {
        List<Long> list = Autoreply.instence.CC.getAts(msg);
        long me = Autoreply.CQ.getLoginQQ();
        for (long l : list) {
            if (l == me) {
                return true;
            }
        }
        return false;
    }

    private boolean isWarnMsg(String msg) {
        for (String s : warningMsgs) {
            if (msg.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
