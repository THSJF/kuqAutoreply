package com.meng.messageProcess;

import com.meng.*;
import com.meng.config.*;
import com.meng.gameData.TouHou.*;
import com.meng.groupChat.*;
import com.meng.tools.*;

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
			Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.adminMessageProcessor.userPermission.toString());
			return;
		}
		if (msg.equals(".live")) {
			Autoreply.sendMessage(fromGroup, fromQQ, ConfigManager.ins.getLiveList());
			return;
		}
		if (msg.startsWith("-留言 ")) {
			ConfigManager.ins.addReport(fromGroup, fromQQ, msg);
			Autoreply.sendMessage(fromGroup, fromQQ, "留言成功");
			return;
		}
		if (msg.startsWith("-问题反馈 ")) {
			ConfigManager.ins.addBugReport(fromGroup, fromQQ, msg);
			Autoreply.sendMessage(fromGroup, fromQQ, "反馈成功");
			return;
		}
		MessageWaitManager.ins.check(fromGroup, fromQQ);
        if (ConfigManager.ins.SanaeConfig.botOff.contains(fromGroup)) {
            return;
        }
		if (FaithManager.ins.check(fromGroup, fromQQ, msg)) {
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
			String name=msg.substring(4);
			if (name.length() > 30) {
				Autoreply.sendMessage(fromGroup, 0, "太长了,记不住");
				return;
			}
			ConfigManager.ins.setNickName(fromQQ, name);
			Autoreply.sendMessage(fromGroup, 0, "我以后会称呼你为" + name);
			return;
		}
		if (msg.equals(".nn")) {
			ConfigManager.ins.setNickName(fromQQ, null);
			Autoreply.sendMessage(fromGroup, 0, "我以后会用你的QQ昵称称呼你");
			return;
		}
		if (Tools.CQ.checkAt(fromGroup, fromQQ, msg)) {//@
			return;
		}
		if (TouHouDataManager.ins.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (Autoreply.ins.spellCollect.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (RepeaterManager.ins.check(fromGroup, fromQQ, msg)) {// 复读
			return;
		}
		if (GuessSpell.ins.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (TouHouKnowledge.ins.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (Autoreply.ins.CQcodeManager.check(fromGroup, msg)) {// 特殊信息(签到分享等)
			return;
		}
		if (msg.equals("查看活跃数据")) {
			Autoreply.sendMessage(fromGroup, fromQQ, "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + fromGroup);
			return;
		}
		if (Autoreply.ins.diceImitate.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (SeqManager.ins.check(fromGroup, fromQQ, msg)) {
			return;
		}
		if (DicReply.ins.check(fromGroup, fromQQ, msg)) {
			return;
		}
	}
}
