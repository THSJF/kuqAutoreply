package com.meng.messageProcess;

import com.meng.*;
import com.meng.tools.*;

import static com.meng.Autoreply.sendMessage;

public class MsgRunnable implements Runnable {
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String msg = "";

    public MsgRunnable(long fromGroup, long fromQQ, String msg, int msgId) {
        this.fromGroup = fromGroup;
        this.fromQQ = fromQQ;
        this.msg = msg;
        this.msgId = msgId;
    }

    @Override
    public synchronized void run() {
        check();
    }

    private void check() {
		if (msg.equals("-help")) {
			sendMessage(fromGroup, 0, Autoreply.instence.adminMessageProcessor.userPermission.toString());
			return;
		}
        if (msg.equals(".on") && (Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromQQ).getAuthority() > 1 || Autoreply.instence.configManager.isAdmin(fromQQ))) {
            if (Autoreply.instence.botOff.contains(fromGroup)) {
                Autoreply.instence.botOff.remove(fromGroup);
                sendMessage(fromGroup, 0, "已启用");
                return;
            }
        }
        if (msg.equals(".off") && (Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromQQ).getAuthority() > 1 || Autoreply.instence.configManager.isAdmin(fromQQ))) {
            Autoreply.instence.botOff.add(fromGroup);
            sendMessage(fromGroup, 0, "已停用");
            return;
        }
        if (msg.equals("权限检查")) {
            Autoreply.sendMessage(fromGroup, fromQQ, String.valueOf(Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromQQ).getAuthority()));
            return;
        }
        if (Autoreply.instence.botOff.contains(fromGroup)) {
            return;
        }

		if (msg.startsWith("-int ")) {
			try {
				String[] args=msg.split(" ", 4);
				int a1=Integer.parseInt(args[1]);
				int a2=Integer.parseInt(args[3]);
				String resu="failed";
				switch (args[2]) {
					case "+":
						resu = "result:" + (a1 + a2);
						break;
					case "-":
						resu = "result:" + (a1 - a2);
						break;
					case "*":
						resu = "result:" + (a1 * a2);
						break;
					case "/":
						resu = "result:" + (a1 / a2);
						break;
					case ">>":
						resu = "result:" + (a1 >> a2);
						break;
					case ">>>":
						resu = "result:" + (a1 >>> a2);
						break;
					case "<<":
						resu = "result:" + (a1 << a2);
						break;
					case "^":
						resu = "result:" + (a1 ^ a2);
						break;
					case "%":
						resu = "result:" + (a1 % a2);
						break;
					case "|":
						resu = "result:" + (a1 | a2);
						break;
					case "&amp;"://&
						resu = "result:" + (a1 & a2);
						break;
					case "~":
						resu = "result:" + (~a1);
						break;
				}
				Autoreply.sendMessage(fromGroup, 0, resu);
			} catch (Exception e) {
				Autoreply.sendMessage(fromGroup, 0, e.toString());
			}
			return;
		}
		if (msg.startsWith("-uint ")) {
			String[] args=msg.split("\\s", 2);
			try {
				Autoreply.sendMessage(fromGroup, 0, (Integer.parseInt(args[1]) & 0x00000000ffffffffL) + "");
			} catch (Exception e) {
				Autoreply.sendMessage(fromGroup, 0, e.toString());
			}
			return;
		}
		if (msg.startsWith(".nn ")) {
			if (msg.contains("~")) {
				return;
			}
			String name=msg.substring(4);
			if (name.length() > 30) {
				Autoreply.sendMessage(fromGroup, 0, "太长了,记不住");
				return;
			}
			Autoreply.instence.configManager.setNickName(fromQQ, name);
			Autoreply.sendMessage(fromGroup, 0, "我以后会称呼你为" + name);
			return;
		}
		if (msg.equals(".nn")) {
			Autoreply.instence.configManager.setNickName(fromQQ, null);
			Autoreply.sendMessage(fromGroup, 0, "我以后会用你的QQ昵称称呼你");
			return;
		}
		if (Methods.checkAt(fromGroup, fromQQ, msg)) {//@
			return;
		}
		if (Autoreply.instence.repeatManager.check(fromGroup, fromQQ, msg)) {// 复读
			return;
		}
		if (Autoreply.instence.spellCollect.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (msg.startsWith("[CQ:location,lat=")) {
			sendMessage(fromGroup, 0, Autoreply.instence.CC.location(35.594993, 118.869838, 15, "守矢神社", "此生无悔入东方 来世愿生幻想乡"));
			return;
		}
		if (Autoreply.instence.CQcodeManager.check(fromGroup, msg)) {// 特殊信息(签到分享等)
			return;
		}
		if (msg.equals("查看活跃数据")) {
			sendMessage(fromGroup, fromQQ, "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + fromGroup);
			return;
		}
		if (Autoreply.instence.diceImitate.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (Autoreply.instence.seqManager.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (Autoreply.instence.dicReply.check(fromGroup, fromQQ, msg)) {
			return;
		}
	}
}
