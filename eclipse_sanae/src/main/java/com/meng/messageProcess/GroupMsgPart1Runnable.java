package com.meng.messageProcess;

import com.meng.*;

import static com.meng.Autoreply.sendMessage;

public class GroupMsgPart1Runnable implements Runnable {
    private int subType = 0;
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String fromAnonymous = "";
    private String msg = "";
    private int font = 0;
    private long timeStamp = 0;

    public GroupMsgPart1Runnable(MessageSender ms) {
        font = ms.font;
        fromGroup = ms.fromGroup;
        fromQQ = ms.fromQQ;
        msg = ms.msg;
        msgId = ms.msgId;
        subType = ms.subType;
        timeStamp = ms.timeStamp;
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

        if (Autoreply.instence.messageMap.get(fromQQ) == null) {
            Autoreply.instence.messageMap.put(fromQQ, new MessageSender(fromGroup, fromQQ, msg, System.currentTimeMillis(), msgId));
        }
    }
}
