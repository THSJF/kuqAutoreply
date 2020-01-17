package com.meng;

import com.meng.config.*;
import org.meowy.cqp.jcq.entity.*;

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
            QQInfo qInfo = Autoreply.ins.getCoolQ().getStrangerInfo(beingOperateQQ);
            Autoreply.sendMessage(fromGroup, 0, ConfigManager.ins.getNickName(beingOperateQQ)  + "(" + qInfo.getQQId() + ")" + "去寻找更适合" + (qInfo.getGender().value() == 1 ?"她": "他") + "的地方了");
        } else if (subtype == 2) {
            if (beingOperateQQ == Autoreply.ins.getCoolQ().getLoginQQ()) {
                ConfigManager.ins.addBlack(fromGroup, fromQQ);
                return;
            }
            QQInfo qInfo = Autoreply.ins.getCoolQ().getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.ins.getCoolQ().getStrangerInfo(fromQQ);
			Autoreply.sendMessage(fromGroup, 0, ConfigManager.ins.getNickName(beingOperateQQ) + "(" + qInfo.getQQId() + ")" + "被" + ConfigManager.ins.getNickName(fromQQ) + "(" + qInfo2.getQQId() + ")" + "踢了一脚");
        }
    }


}
