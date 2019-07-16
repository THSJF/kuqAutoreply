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
        if (Autoreply.instence.fph.check(fromQQ, fromGroup, msg, msgId, imageFiles)) {
            return;
        }
        if (msg.equals(".on")) {
            if (Autoreply.instence.botOff.contains(fromGroup)) {
                Autoreply.instence.botOff.remove(fromGroup);
                sendMessage(fromGroup, 0, "已启用");
                return;
            }
        } else if (msg.equals(".off")) {
            Autoreply.instence.botOff.add(fromGroup);
            sendMessage(fromGroup, 0, "已停用");
            return;
        }
        if (Autoreply.instence.botOff.contains(fromGroup)) {
            return;
        }
        while (Autoreply.instence.using) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Autoreply.instence.messageMap.get(fromQQ) == null) {
            Autoreply.instence.messageMap.put(fromQQ, new MessageSender(fromGroup, fromQQ, msg, System.currentTimeMillis(), msgId, imageFiles));
        }
    }
}
