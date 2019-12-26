package com.meng;

import com.meng.config.*;
import com.sobte.cqp.jcq.entity.*;

public class GroupMemberChangerListener {

    public GroupMemberChangerListener() {
    }

    public void checkIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        PersonInfo personInfo = ConfigManager.ins.getPersonInfoFromQQ(beingOperateQQ);
        if (personInfo != null && personInfo.name.equals("熊哥")) {
            Autoreply.sendMessage(Autoreply.mainGroup, 0, "熊加入了群" + fromGroup);
            return;
        }
        if (personInfo != null) {
            Autoreply.sendMessage(fromGroup, 0, "欢迎" + ConfigManager.ins.getNickName(beingOperateQQ) + "来到本群");
        } else {
            Autoreply.sendMessage(fromGroup, 0, "欢迎新人");
        }
		String wel=ConfigManager.ins.getWelcome(fromGroup);
		if (wel != null) {
			Autoreply.sendMessage(fromGroup, 0, wel);
		}
	}

    public void checkDecrease(int subtype, int sendTime, final long fromGroup, final long fromQQ, long beingOperateQQ) {
        if (subtype == 1) {
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            Autoreply.sendMessage(fromGroup, 0, ConfigManager.ins.getNickName(beingOperateQQ)  + "(" + qInfo.getQqId() + ")" + "去寻找更适合" + (qInfo.getGender() == 1 ?"她": "他") + "的地方了");
        } else if (subtype == 2) {
            if (beingOperateQQ == Autoreply.CQ.getLoginQQ()) {
                ConfigManager.ins.addBlack(fromGroup, fromQQ);
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.CQ.getStrangerInfo(fromQQ);
			Autoreply.sendMessage(fromGroup, 0, ConfigManager.ins.getNickName(beingOperateQQ) + "(" + qInfo.getQqId() + ")" + "被" + ConfigManager.ins.getNickName(fromQQ) + "(" + qInfo2.getQqId() + ")" + "踢了一脚");
        }
    }


}
