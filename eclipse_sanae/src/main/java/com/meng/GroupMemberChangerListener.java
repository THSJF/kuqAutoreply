package com.meng;

import com.meng.config.javabeans.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;

import static com.meng.Autoreply.sendMessage;

public class GroupMemberChangerListener {

    public GroupMemberChangerListener() {
    }

    public void checkIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        if (Autoreply.instence.configManager.isBlackQQ(beingOperateQQ)) {
            Methods.ban(fromGroup, fromQQ, 300);
        }
        PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
        if (personInfo != null && personInfo.name.equals("熊哥")) {
            sendMessage(959615179L, 0, Autoreply.instence.CC.at(-1) + "熊加入了群" + fromGroup, true);
            return;
        }
        if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
            return;
        }
        if (personInfo != null) {
            sendMessage(fromGroup, 0, "欢迎" + Autoreply.instence.configManager.getNickName(beingOperateQQ), true);
        } else {
            sendMessage(fromGroup, 0, "欢迎新大佬", true);
        }
		Autoreply.instence.configManager.addAutoAllow(beingOperateQQ);
        Autoreply.instence.banListener.checkSleepMsg(fromGroup, beingOperateQQ);
        /*  if (fromGroup == 859561731L) { // 台长群
		 sendMessage(859561731L, 0, "芳赛服务器炸了", true);
		 try { sendMessage(859561731L, 0, CC.image(new File(appDirectory +
		 "pic/sjf9961.jpg"))); } catch (IOException e) {
		 e.printStackTrace(); }
		 } */
    }

    public void checkDecrease(int subtype, int sendTime, final long fromGroup, final long fromQQ, long beingOperateQQ) {
        if (subtype == 1) {
			//  if (beingOperateQQ == 2856986197L) {
			//	if(fromGroup==Autoreply.mainGroup){
			//		return;
			//	}
			//     Autoreply.CQ.setGroupLeave(fromGroup, false);
			//    }
            if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
                return;
            }
            if (Autoreply.instence.configManager.isBlackQQ(beingOperateQQ)) {
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            sendMessage(fromGroup, 0, Autoreply.instence.configManager.getNickName(beingOperateQQ)  + "(" + qInfo.getQqId() + ")" + "跑莉", true);
        } else if (subtype == 2) {
            if (beingOperateQQ == 2856986197L) {
                Autoreply.instence.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Autoreply.instence.configManager.addBlack(fromGroup, fromQQ);
							Autoreply.CQ.setGroupLeave(fromGroup, false);
						}
					});
                return;
            }
            if (beingOperateQQ == 2558395159L) {
                Autoreply.CQ.setGroupLeave(fromGroup, false);
                return;
            }
            if (beingOperateQQ == Autoreply.CQ.getLoginQQ()) {
                Autoreply.instence.configManager.addBlack(fromGroup, fromQQ);
                return;
            }
            if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
                return;
            }
            if (Autoreply.instence.configManager.isNotReplyQQ(beingOperateQQ)) {
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.CQ.getStrangerInfo(fromQQ);
            Autoreply.instence.configManager.removeAutoAllow(beingOperateQQ);
            sendMessage(fromGroup, 0, Autoreply.instence.configManager.getNickName(beingOperateQQ) + "(" + qInfo.getQqId() + ")" + "被" + Autoreply.instence.configManager.getNickName(fromQQ) + "(" + qInfo2.getQqId() + ")" + "玩完扔莉", true);
        }
    }


}
