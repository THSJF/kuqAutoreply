package com.meng.messageProcess;

import com.meng.*;
import com.meng.config.*;
import com.meng.tools.*;

public class ReportManager {
	public static ReportManager ins;

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (ConfigManager.ins.isMaster(fromQQ)) {
			if (msg.equals("-留言查看")) {
				SanaeConfigJavaBean.ReportBean rb=ConfigManager.ins.getReport();
				Autoreply.sendMessage(fromGroup, fromQQ, rb == null ?"无留言": rb.toString());
				return true;
			}
			if (msg.equalsIgnoreCase("-留言查看 t")) {
				SanaeConfigJavaBean.ReportBean rb = ConfigManager.ins.getReport();
				FaithManager.ins.addFaith(rb.q, 5);
				ConfigManager.ins.removeReport();
				MessageWaitManager.ins.addTip(rb.q, String.format("%d在%s的留言「%s」已经处理,获得5信仰奖励", rb.q, Tools.CQ.getTime(rb.t), rb.c));
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
			if (msg.equals("-留言查看 f")) {
				ConfigManager.ins.removeReport();
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
			if (msg.equalsIgnoreCase("-留言查看 w")) {
				SanaeConfigJavaBean.ReportBean rb = ConfigManager.ins.getReport();
				MessageWaitManager.ins.addTip(rb.q, String.format("%d在%s的留言「%s」已经处理,开发者认为目前还不是处理此留言的时候", rb.q, Tools.CQ.getTime(rb.t), rb.c));
				ConfigManager.ins.reportToLast();
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
			if (msg.equals("-反馈查看")) {
				SanaeConfigJavaBean.BugReportBean brb=ConfigManager.ins.getBugReport();
				Autoreply.sendMessage(fromGroup, fromQQ, brb == null ?"无反馈": brb.toString());
				return true;
			}
			if (msg.equalsIgnoreCase("-反馈查看 t")) {
				SanaeConfigJavaBean.BugReportBean brb = ConfigManager.ins.getBugReport();
				FaithManager.ins.addFaith(brb.q, 10);
				ConfigManager.ins.removeBugReport();
				MessageWaitManager.ins.addTip(brb.q, String.format("%d在%s的反馈「%s」已经处理,获得10信仰奖励", brb.q, Tools.CQ.getTime(brb.t), brb.c));
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
			if (msg.equals("-反馈查看 f")) {
				ConfigManager.ins.removeBugReport();
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
			if (msg.equalsIgnoreCase("-反馈查看 w")) {
				SanaeConfigJavaBean.BugReportBean brb = ConfigManager.ins.getBugReport();
				MessageWaitManager.ins.addTip(brb.q, String.format("%d在%s的反馈「%s」已经处理,开发者认为暂时不需要处理此问题", brb.q, Tools.CQ.getTime(brb.t), brb.c));
				ConfigManager.ins.bugReportToLast();
				Autoreply.sendMessage(fromGroup, 0, "处理成功");
				return true;
			}
		} else {
			if (msg.startsWith("-留言 ")) {
				ConfigManager.ins.addReport(fromGroup, fromQQ, msg);
				Autoreply.sendMessage(fromGroup, fromQQ, "留言成功");
				return true;
			}
			if (msg.startsWith("-问题反馈 ")) {
				ConfigManager.ins.addBugReport(fromGroup, fromQQ, msg);
				Autoreply.sendMessage(fromGroup, fromQQ, "反馈成功");
				return true;
			}
		}
		return false;
	}
}
