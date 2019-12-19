package com.meng.config;

import java.text.*;
import java.util.*;

public class SanaeConfigJavaBean {
	public HashMap<Long,String> welcomeMap = new HashMap<>();
	public HashSet<Long> botOff = new HashSet<>();
	public ArrayList<ReportBean> reportList=new ArrayList<>();
	public ArrayList<BugReportBean> bugReportList=new ArrayList<>();

	public void addReport(long fromGroup, long fromQQ, String content) {
		ReportBean report=new ReportBean();
		report.t = System.currentTimeMillis();
		report.g = fromGroup;
		report.q = fromQQ;
		report.c = content;
		reportList.add(report);
	}

	public void addBugReport(long fromGroup, long fromQQ, String content) {
		BugReportBean bugReport=new BugReportBean();
		bugReport.t = System.currentTimeMillis();
		bugReport.g = fromGroup;
		bugReport.q = fromQQ;
		bugReport.c = content;
		bugReportList.add(bugReport);
	}

	public String getReport() {
		if (reportList.size() == 0) {
			return "当前无内容";
		}
		ReportBean report = reportList.get(0);
		reportList.remove(0);
		return report.toString();
	}

	public String getBugReport() {
		if (bugReportList.size() == 0) {
			return "当前无内容";
		}
		BugReportBean bugReport = bugReportList.get(0);
		bugReportList.remove(0);
		return bugReport.toString();
	}

	private class ReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), g, q, c);
		}
	}

	private class BugReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), g, q, c);
		}
	}
}

