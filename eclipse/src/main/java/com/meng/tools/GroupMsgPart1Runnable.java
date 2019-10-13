package com.meng.tools;

import com.meng.Autoreply;
import com.meng.MessageSender;
import com.meng.groupChat.FanPoHaiManager;
import com.sobte.cqp.jcq.entity.CQImage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

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
    private File[] imageFiles = null;

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
        List<CQImage> images = Autoreply.instence.CC.getCQImages(msg);
        if (images.size() != 0) {
            imageFiles = new File[images.size()];
            for (int i = 0, imagesSize = images.size(); i < imagesSize; i++) {
                Autoreply.instence.useCount.incPic(fromQQ);
                Autoreply.instence.groupCount.incPic(fromGroup);
                CQImage image = images.get(i);
                try {
                    imageFiles[i] = Autoreply.instence.fileTypeUtil.checkFormat(image.download(Autoreply.appDirectory + "downloadImages/", image.getMd5()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		if (msg.equals("-help")){
			sendMessage(fromGroup, 0, Autoreply.instence.adminMessageProcessor.userPermission.toString());
			return;
		}
        if (Autoreply.instence.fph.check(fromQQ, fromGroup, msg, msgId, imageFiles)) {
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
        if (Autoreply.instence.configManager.isAdmin(fromQQ) && msg.startsWith("ocr")) {
            if (Autoreply.instence.ocrManager.checkOcr(fromGroup, fromQQ, msg, imageFiles)) {
                return;
            }
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
            Autoreply.instence.messageMap.put(fromQQ, new MessageSender(fromGroup, fromQQ, msg, System.currentTimeMillis(), msgId, imageFiles));
        }
    }
}
