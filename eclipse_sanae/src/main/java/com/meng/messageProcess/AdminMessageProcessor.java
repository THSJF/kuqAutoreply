package com.meng.messageProcess;

import com.meng.*;
import com.meng.config.*;
import com.meng.tools.*;
import com.meng.tools.override.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;
import java.util.concurrent.*;
import com.meng.groupChat.*;
import java.io.*;

public class AdminMessageProcessor {
	private MyLinkedHashMap<String,String> masterPermission=new MyLinkedHashMap<>();
	private MyLinkedHashMap<String,String> adminPermission=new MyLinkedHashMap<>();
	public MyLinkedHashMap<String,String> userPermission=new MyLinkedHashMap<>();

    public AdminMessageProcessor() {
		masterPermission.put("-start|-stop", "总开关");
		masterPermission.put("find:[QQ号]", "在配置文件中查找此人");
		masterPermission.put("-留言查看|-反馈查看", "查看留言或bug反馈");
		masterPermission.put("block[艾特一人]", "屏蔽列表");
		masterPermission.put("black[艾特一人]", "黑名单");
		masterPermission.put("blackgroup [群号]", "群加入黑名单,多群用空格隔开");
		masterPermission.put("群广播:[字符串]", "在所有回复的群里广播");
		masterPermission.put("send.[群号].[内容]", "内容转发至指定群");
		adminPermission.put("-发言数据 yyyy-MM-dd", "每小时发言信息");
		//adminPermission.put("线程数", "线程池信息");
		adminPermission.put("-bot on|-bot off", "设置是否回复本群");
		adminPermission.put("-regex", "词库使用正则表达式匹配key");
		userPermission.put(".nn [名字]", "设置早苗对你的称呼,如果不设置则恢复默认称呼");
		//userPermission.put("-int [int] [+|-|*|/|<<|>>|>>>|%|^|&||] [int]", "int运算(溢出)");
		userPermission.put("-留言", "给开发者留言");
		userPermission.put("-问题反馈", "给开发者反馈问题,使用时发现错误内容或者错误的消息处理可以使用此项向开发者反馈,其他内容请使用留言功能");
		userPermission.put("-问答添加 [问题] ［t|f］", "添加问答问题,审核通过后将会加入题库");
		adminPermission.put(".dissmiss 2528419891", "让早苗退出此群");

		masterPermission.putAll(adminPermission);
		masterPermission.putAll(userPermission);
		adminPermission.putAll(userPermission);
	}

    public boolean check(final long fromGroup, final long fromQQ, final String msg) {
		Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup, fromQQ);
		boolean isGroupAdmin=m.getAuthority() > 1;
		if (ConfigManager.ins.isMaster(fromQQ)) {
			if (msg.equals("-help")) {
				Autoreply.sendMessage(fromGroup, 0, masterPermission.toString());
				return true;
			}
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
			if (msg.startsWith("-群广播:")) {
				String broadcast=msg.substring(5);
				HashSet<Group> hs=new HashSet<>();
				List<Group> glist=Autoreply.CQ.getGroupList();
				for (Group g:glist) {
					Autoreply.sendMessage(g.getId(), 0, broadcast);
					hs.add(g);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {}
				}
				String result="在以下群发送了广播:";
				for (Group g:hs) {
					result += "\n";
					result += g.getId();
					result += ":";
					result += g.getName();
				}
				Autoreply.sendMessage(fromGroup, 0, result);
				return true;
			}
            if (msg.startsWith("block[CQ:at")) {
                StringBuilder sb = new StringBuilder();
                List<Long> qqs = Autoreply.ins.CC.getAts(msg);
                sb.append("屏蔽列表添加:");
                for (int i = 0, qqsSize = qqs.size(); i < qqsSize; i++) {
                    long qq = qqs.get(i);
                    sb.append(qq).append(" ");
				}
                ConfigManager.ins.RanConfig.QQNotReply.addAll(qqs);
                Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
                return true;
			}
            if (msg.startsWith("black[CQ:at")) {
                StringBuilder sb = new StringBuilder();
                List<Long> qqs = Autoreply.ins.CC.getAts(msg);
                sb.append("黑名单添加:");
                for (int i = 0, qqsSize = qqs.size(); i < qqsSize; i++) {
                    long qq = qqs.get(i);
                    sb.append(qq).append(" ");
				}
                ConfigManager.ins.RanConfig.blackListQQ.addAll(qqs);
                Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
                return true;
			}
            if (msg.startsWith("blackgroup")) {
                StringBuilder sb = new StringBuilder();
                String[] groups = msg.split(" ");
                sb.append("黑名单群添加:");
                int le = groups.length;
                for (int i = 1; i < le; ++i) {
                    sb.append(groups[i]).append(" ");
                    ConfigManager.ins.RanConfig.blackListGroup.add(Long.parseLong(groups[i]));
				}
                Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
                return true;
			}
            if (msg.equals("线程数")) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Autoreply.ins.threadPool;
                String s = "taskCount：" + threadPoolExecutor.getTaskCount() + "\n" +
					"completedTaskCount：" + threadPoolExecutor.getCompletedTaskCount() + "\n" +
					"largestPoolSize：" + threadPoolExecutor.getLargestPoolSize() + "\n" +
					"poolSize：" + threadPoolExecutor.getPoolSize() + "\n" +
					"activeCount：" + threadPoolExecutor.getActiveCount();
				Autoreply.sendMessage(fromGroup, fromQQ, s);
                return true;
			}
            if (msg.equalsIgnoreCase("System.gc();")) {
            	System.gc();
				Autoreply.sendMessage(fromGroup, fromQQ, "gc start");
                return true;
			}
            String[] strings = msg.split("\\.", 3);
            if (strings[0].equals("send")) {
				Autoreply.sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
                return true;
			}
			if (msg.startsWith("-z.add")) {
				ConfigManager.ins.SanaeConfig.zanSet.addAll(Autoreply.ins.CC.getAts(msg));
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, fromQQ, "已添加至赞列表");
				return true;
			}
			if (msg.startsWith("-z.remove")) {
				ConfigManager.ins.SanaeConfig.zanSet.removeAll(Autoreply.ins.CC.getAts(msg));
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, fromQQ, "已从赞列表移除");
				return true;
			}
		}
        if (ConfigManager.ins.isAdmin(fromQQ) || isGroupAdmin) {
			if (msg.equals("-help")) {
				Autoreply.sendMessage(fromGroup, 0, adminPermission.toString());
				return true;
			}
			if (msg.startsWith("-addDic ")) {
				String[] array=msg.split(" ");
				String[] arr=new String[array.length - 2];
				for (int i=0;i < arr.length;++i) {
					arr[i] = array[i + 2];
				}
				DicReply.ins.addKV(fromGroup, array[1], arr);
				Autoreply.sendMessage(fromGroup, fromQQ, "KV已添加");
				return true;
			}
			if (msg.startsWith("-removeDic ")) {
				DicReply.ins.removeK(fromGroup, msg.substring(11));
				Autoreply.sendMessage(fromGroup, fromQQ, "Key已移除");
				return true;
			}
			if (msg.equals("-regex")) {
				if (ConfigManager.ins.SanaeConfig.dicRegex.get(fromGroup) != null) {
					if (ConfigManager.ins.SanaeConfig.dicRegex.get(fromGroup)) {
						ConfigManager.ins.SanaeConfig.dicRegex.put(fromGroup, false);
						Autoreply.sendMessage(fromGroup, 0, "词库正则表达式已停用");
					} else {
						ConfigManager.ins.SanaeConfig.dicRegex.put(fromGroup, true);
						Autoreply.sendMessage(fromGroup, 0, "词库正则表达式已启用");
					}
				} else {
					ConfigManager.ins.SanaeConfig.dicRegex.put(fromGroup, true);
					Autoreply.sendMessage(fromGroup, 0, "词库正则表达式已启用");
				}
			}
			if (msg.equals("-发言数据")) {
				HashMap<Integer,Integer> hashMap = GroupCounter.ins.getSpeak(fromGroup, Tools.CQ.getDate());
				if (hashMap == null || hashMap.size() == 0) {
					Autoreply.sendMessage(fromGroup, 0, "无数据");
					return true;
				}
				StringBuilder sb=new StringBuilder(String.format("群内共有%d条消息,今日消息情况:\n", GroupCounter.ins.groupsMap.get(fromGroup).all));
				for (int i=0;i < 24;++i) {
					if (hashMap.get(i) == null) {
						continue;
					}
					sb.append(String.format("%d:00-%d:00  共%d条消息\n", i, i + 1, hashMap.get(i)));
				}
				Autoreply.sendMessage(fromGroup, 0, sb.toString());
				try {
					File pic=Chartww.ins.check(GroupCounter.ins.groupsMap.get(fromGroup));
					Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.CC.image(pic));
					pic.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}

			if (msg.equals("-本月发言数据")) {
				try {
					File pic=MonthChart.ins.check(GroupCounter.ins.groupsMap.get(fromGroup));
					Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.CC.image(pic));
					pic.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}

			if (msg.startsWith("-发言数据 ")) {
				if (msg.length() != 16) {
					Autoreply.sendMessage(fromGroup, 0, "日期格式错误");
					return true;
				}
				HashMap<Integer,Integer> hashMap = GroupCounter.ins.getSpeak(fromGroup, msg.substring(6));
				if (hashMap == null || hashMap.size() == 0) {
					Autoreply.sendMessage(fromGroup, 0, "无数据");
					return true;
				}
				StringBuilder sb=new StringBuilder(String.format("群内共有%d条消息,今日消息情况:\n", GroupCounter.ins.groupsMap.get(fromGroup).all));
				for (int i=0;i < 24;++i) {
					if (hashMap.get(i) == null) {
						continue;
					}
					sb.append(String.format("%d:00-%d:00  共%d条消息\n", i, i + 1, hashMap.get(i)));
				}
				Autoreply.sendMessage(fromGroup, 0, sb.toString());
				return true;
			}
			if (msg.equals(".早苗说话") || msg.equals(".bot on")) {
				if (ConfigManager.ins.SanaeConfig.botOff.contains(fromGroup)) {
					ConfigManager.ins.SanaeConfig.botOff.remove(fromGroup);
					ConfigManager.ins.saveSanaeConfig();
					Autoreply.sendMessage(fromGroup, 0, "稳了");
					return true;
				}
			}
			if (msg.equals(".早苗闭嘴") || msg.equals(".bot off")) {
				ConfigManager.ins.SanaeConfig.botOff.add(fromGroup);
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, 0, "好吧");
				return true;
			}
			if (msg.equals(".dissmiss 2528419891") || msg.equals(".dissmiss")) {
				Autoreply.ins.threadPool.execute(new Runnable(){

						@Override
						public void run() {
							Autoreply.sendMessage(fromGroup, 0, "我很快就会离开这里");
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}
							Autoreply.CQ.setGroupLeave(fromGroup, false);
						}
					});
			}
			if (msg.startsWith(".welcome ")) {
				String wel=msg.substring(9);
				if (wel.length() > 100) {
					Autoreply.sendMessage(fromGroup, 0, "太长了");
					return true;
				}
				ConfigManager.ins.setWelcome(fromGroup, wel);
				Autoreply.sendMessage(fromGroup, 0, String.format("已设置入群欢迎词为「%s」", wel));
				return true;
			}
		}
        return false;
	}
}
