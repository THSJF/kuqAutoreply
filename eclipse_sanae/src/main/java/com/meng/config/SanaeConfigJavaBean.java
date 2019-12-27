package com.meng.config;

import com.meng.messageProcess.*;
import com.meng.tools.*;
import java.util.*;

public class SanaeConfigJavaBean {
	public HashMap<Long,String> welcomeMap = new HashMap<>();
	public HashSet<Long> botOff = new HashSet<>();
	public HashSet<Long> zanSet = new HashSet<>();
	public ArrayList<ReportBean> reportList=new ArrayList<>();
	public ArrayList<BugReportBean> bugReportList=new ArrayList<>();
	public HashMap<Long,Boolean> dicRegex = new HashMap<>();
	public ArrayList<MessageWaitManager.MessageWait> delayMsg=new ArrayList<>();

	void addReport(long fromGroup, long fromQQ, String content) {
		ReportBean report=new ReportBean();
		report.t = System.currentTimeMillis();
		report.g = fromGroup;
		report.q = fromQQ;
		report.c = content;
		reportList.add(report);
	}

	void addBugReport(long fromGroup, long fromQQ, String content) {
		BugReportBean bugReport=new BugReportBean();
		bugReport.t = System.currentTimeMillis();
		bugReport.g = fromGroup;
		bugReport.q = fromQQ;
		bugReport.c = content;
		bugReportList.add(bugReport);
	}

	void removeReport() {
		if (reportList.size() == 0) {
			return;
		}
		reportList.remove(0);
		ConfigManager.ins.saveSanaeConfig();
	}

	void reportToLast() {
		if (reportList.size() == 0) {
			return;
		}
		reportList.add(reportList.remove(0));
		ConfigManager.ins.saveSanaeConfig();
	}

	ReportBean getReport() {
		if (reportList.size() == 0) {
			return null;
		}
		return reportList.get(0);
	}

	void removeBugReport() {
		if (bugReportList.size() == 0) {
			return;
		}
		bugReportList.remove(0);
		ConfigManager.ins.saveSanaeConfig();
	}

	void bugReportToLast() {
		if (bugReportList.size() == 0) {
			return;
		}
		bugReportList.add(bugReportList.remove(0));
		ConfigManager.ins.saveSanaeConfig();
	}

	BugReportBean getBugReport() {
		if (bugReportList.size() == 0) {
			return null;
		}
		return bugReportList.get(0);
	}

	public class ReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", Tools.CQ.getTime(t), g, q, c);
		}
	}

	public class BugReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", Tools.CQ.getTime(t), g, q, c);
		}
	}
}

