package com.meng.messageProcess;

import com.meng.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

import static com.meng.Autoreply.sendMessage;

public class WarnMessageProcessor {
    private String lastMsg = "  ";
    private long lastSender = 2;
    private String[] warningMsgs = new String[]{
		"监听直播",
		"监听更新",
		"设置用户",
		"reload",
		"addsuperadmin",
		"addsuperop",
		"addprotect",
		"addtipgroup",
		"removesuperop",
		"removeprotect",
		"removetipgroup",
		"下载",
		"下载图像并发送",
		"广播",
		"广播审查",
		"广播审核",
		"审核广播",
		"审查广播",
		"广播图像",
		"广播图片",
		"addcoins",
		"op",
		"添加管理员",
		"deop",
		"移除管理员",
		"退出本群",
		"新增死鸽",
		"添加死鸽",
		"移除死鸽",
		"删除死鸽",
		"去除死鸽",
		"缓刑宰鸽",
		"thunder",
		"welcome",
		"申请提醒",
		"mute",
		"gag",
		"禁言",
		"mutegroupuser",
		"kick",
		"踢 ",
		"ag",
		"ban",
		"unban",
		"rpk",
		"rmk",
		"zan",
		"dezan",
		"转账",
		"幻币商城",
		"商城"
    };

	private String[] exceptMessages=new String[]{
		"转账"
	};

    public WarnMessageProcessor() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg) {
        boolean b = false;
        if (lastMsg.equals(msg) && isConmandMessage(msg)) {
            b = processRepeat(fromGroup, fromQQ, msg);
        } else if (isConmandMessage(msg) && !isExceptMsg(msg) && isAtme(msg)) {
			onMsgHighWarinig(fromGroup, fromQQ);
            return true;
        }
        lastMsg = msg;
        lastSender = fromQQ;
        return b;
    }

    private void onMsgHighWarinig(long fromGroup, long fromQQ) {
        Autoreply.instence.banListener.addSummerSleep(fromGroup, 2856986197L, fromQQ);
        try {
            Member m = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
            Member m2 = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromQQ);
            if (m.getAuthority() - m2.getAuthority() > 1) {
                sendMessage(fromGroup, fromQQ, "你的行为被判定为危险行为,请联系管理员解除夏眠");
            }
        } catch (Exception e) {
            Autoreply.instence.configManager.configJavaBean.blackListQQ.add(fromQQ);
            Tools.CQ.ban(fromGroup, fromQQ, 2592000);
            sendMessage(fromGroup, fromQQ, "你的行为被判定为危险行为");
        }
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

    private boolean processRepeat(long fromGroup, long fromqq, String msg) {
        if (isWarnMsg(msg)) {
            if (lastSender == fromqq) {
                onMsgHighWarinig(fromGroup, fromqq);
            } else {
                if (isWarnMsg(msg)) {
                    try {
                        Member m = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                        Member m2 = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromqq);
                        if (m.getAuthority() - m2.getAuthority() > 1) {
                            Tools.CQ.ban(fromGroup, fromqq, 60);
                            sendMessage(fromGroup, fromqq, "你的行为被判定为危险行为");
                        }
                    } catch (Exception e) {
                        Tools.CQ.ban(fromGroup, fromqq, 60);
                        sendMessage(fromGroup, fromqq, "你的行为被判定为危险行为");
                    }
                }
            }
            return true;
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

	private boolean isExceptMsg(String msg) {
        for (String s : exceptMessages) {
            if (msg.contains(s)) {
                return true;
            }
        }
        return false;
    }

	private boolean isConmandMessage(String msg) {
		return msg.contains("~") || msg.contains("～");
	}
}
