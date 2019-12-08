package com.meng;

import com.meng.config.*;
import com.sobte.cqp.jcq.entity.*;

import static com.meng.Autoreply.sendMessage;

public class GroupMemberChangerListener {

    public GroupMemberChangerListener() {
    }

    public void checkIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
        if (personInfo != null && personInfo.name.equals("熊哥")) {
            sendMessage(Autoreply.mainGroup, 0, "熊加入了群" + fromGroup, true);
            return;
        }
        if (personInfo != null) {
            sendMessage(fromGroup, 0, "欢迎" + Autoreply.instence.configManager.getNickName(beingOperateQQ) + "来到本群", true);
        } else {
            sendMessage(fromGroup, 0, "欢迎新人", true);
        }
	}

    public void checkDecrease(int subtype, int sendTime, final long fromGroup, final long fromQQ, long beingOperateQQ) {
        if (subtype == 1) {
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            sendMessage(fromGroup, 0, Autoreply.instence.configManager.getNickName(beingOperateQQ)  + "(" + qInfo.getQqId() + ")" + "去寻找更适合" + (qInfo.getGender() == 1 ?"她": "他") + "的地方了", true);
        } else if (subtype == 2) {
            if (beingOperateQQ == Autoreply.CQ.getLoginQQ()) {
                Autoreply.instence.configManager.addBlack(fromGroup, fromQQ);
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.CQ.getStrangerInfo(fromQQ);
			sendMessage(fromGroup, 0, Autoreply.instence.configManager.getNickName(beingOperateQQ) + "(" + qInfo.getQqId() + ")" + "被" + Autoreply.instence.configManager.getNickName(fromQQ) + "(" + qInfo2.getQqId() + ")" + "踢了一脚", true);
        }
    }


}
