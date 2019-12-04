package com.meng.messageProcess;

import com.meng.*;
import com.meng.config.*;
import com.meng.dice.*;
import com.meng.tools.override.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;
import java.util.concurrent.*;

import static com.meng.Autoreply.sendMessage;

public class AdminMessageProcessor {
    private ConfigManager configManager;
	private MyLinkedHashMap<String,String> masterPermission=new MyLinkedHashMap<>();
	private MyLinkedHashMap<String,String> adminPermission=new MyLinkedHashMap<>();
	public MyLinkedHashMap<String,String> userPermission=new MyLinkedHashMap<>();

    public AdminMessageProcessor(ConfigManager configManager) {
        this.configManager = configManager;
		masterPermission.put(".start|.stop", "总开关");
		masterPermission.put("find:[QQ号]", "在配置文件中查找此人");
		masterPermission.put("block[艾特一人]", "屏蔽列表");
		masterPermission.put("black[艾特一人]", "黑名单");
		masterPermission.put("blackgroup [群号]", "群加入黑名单,多群用空格隔开");
		masterPermission.put("群广播:[字符串]", "在所有回复的群里广播");
		masterPermission.put("send.[群号].[内容]", "内容转发至指定群");
		masterPermission.put("移除成就 [成就名] [艾特一人]", "移除此人的该成就");

		adminPermission.put("线程数", "线程池信息");
		adminPermission.put(".on|.off", "不修改配置文件的单群开关");
		adminPermission.put(".admin enable|.admin disable", "修改配置文件的单群开关");
		userPermission.put(".nn [名字]", "设置鬼人正邪对你的称呼,如果不设置则恢复默认称呼");
		userPermission.put("-int [int] [+|-|*|/|<<|>>|>>>|%|^|&||] [int]", "int运算(溢出)");
		userPermission.put("-uint [int]", "int字节转uint(boom)");
		userPermission.put("抽卡", "抽卡");
		userPermission.put("给鬼人正邪master幻币转账", "抽卡，1币3卡");
		userPermission.put("查看成就", "查看成就列表");
		userPermission.put("查看符卡", "查看已获得的符卡,会刷屏，少用");
		userPermission.put("成就条件 [成就名]", "查看获得条件");
		userPermission.put("幻币兑换 [整数]", "本地幻币兑换至小律影");
		userPermission.put("~coins", "查看幻币数量");
		userPermission.put("幻币抽卡 [整数]", "使用本地幻币抽卡");
		userPermission.put("购买符卡 [符卡名]", "购买指定符卡,除lastword");
		userPermission.put("原曲认知 [E|N|H|L]", "原曲认知测试,回答时用\"原曲认知回答 答案\"进行回答，只能回答自己的问题");

		masterPermission.putAll(adminPermission);
		masterPermission.putAll(userPermission);
		adminPermission.putAll(userPermission);
	}

    public boolean check(final long fromGroup, final long fromQQ, final String msg) {
        if (configManager.isMaster(fromQQ)) {
			if (msg.equals("-help")) {
				Autoreply.sendMessage(fromGroup, 0, masterPermission.toString());
				return true;
			}
			if (msg.startsWith("移除成就 ")) {
				String arch=msg.substring(5, msg.indexOf("[") - 1);
				long toQQ=Autoreply.instence.CC.getAt(msg);
				ArchievementBean ab=Autoreply.instence.spellCollect.archiMap.get(String.valueOf(toQQ));
				if (ab == null) {
					ab = new ArchievementBean();
					Autoreply.instence.spellCollect.archiMap.put(toQQ, ab);
				}
				for (Archievement ac:Autoreply.instence.spellCollect.archList) {
					if (ac.name.equals(arch)) {
						ab.deleteArchievment(ac.archNum);
						Autoreply.sendMessage(fromGroup, toQQ, "为" + toQQ + "移除成就" + arch);
						Autoreply.instence.spellCollect.saveArchiConfig();
						return true;
					}
				}
				return true;
			}
			if (msg.startsWith("群广播:")) {
				String broadcast=msg.substring(4);
				HashSet<Group> hs=new HashSet<>();
				List<Group> glist=Autoreply.CQ.getGroupList();
				for (Group g:glist) {
					GroupConfig gc=Autoreply.instence.configManager.getGroupConfig(g.getId());
					if (gc == null || !gc.reply) {
						continue;
					}
					Autoreply.sendMessage(gc.groupNumber, 0, broadcast, true);
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
            if (msg.equals(".stop")) {
                Autoreply.sendMessage(fromGroup, 0, "disabled");
                Autoreply.sleeping = true;
                return true;
			}
            if (msg.equals(".start")) {
                Autoreply.sleeping = false;
                Autoreply.sendMessage(fromGroup, 0, "enabled");
                return true;
			}
            if (msg.startsWith("block[CQ:at")) {
                StringBuilder sb = new StringBuilder();
                List<Long> qqs = Autoreply.instence.CC.getAts(msg);
                sb.append("屏蔽列表添加:");
                for (int i = 0, qqsSize = qqs.size(); i < qqsSize; i++) {
                    long qq = qqs.get(i);
                    sb.append(qq).append(" ");
				}
                configManager.configJavaBean.QQNotReply.addAll(qqs);
                configManager.saveConfig();
                Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
                return true;
			}
            if (msg.startsWith("black[CQ:at")) {
                StringBuilder sb = new StringBuilder();
                List<Long> qqs = Autoreply.instence.CC.getAts(msg);
                sb.append("黑名单添加:");
                for (int i = 0, qqsSize = qqs.size(); i < qqsSize; i++) {
                    long qq = qqs.get(i);
                    sb.append(qq).append(" ");
				}
                configManager.configJavaBean.blackListQQ.addAll(qqs);
                configManager.saveConfig();
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
                    configManager.configJavaBean.blackListGroup.add(Long.parseLong(groups[i]));
				}
                configManager.saveConfig();
                Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
                return true;
			}
            if (msg.equals("线程数")) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Autoreply.instence.threadPool;
                String s = "taskCount：" + threadPoolExecutor.getTaskCount() + "\n" +
					"completedTaskCount：" + threadPoolExecutor.getCompletedTaskCount() + "\n" +
					"largestPoolSize：" + threadPoolExecutor.getLargestPoolSize() + "\n" +
					"poolSize：" + threadPoolExecutor.getPoolSize() + "\n" +
					"activeCount：" + threadPoolExecutor.getActiveCount();
                sendMessage(fromGroup, fromQQ, s);
                return true;
			}
            if (msg.equalsIgnoreCase("System.gc();")) {
            	System.gc();
                sendMessage(fromGroup, fromQQ, "gc start");
                return true;
			}
            if (msg.startsWith("生成位置")) {
                String[] args = msg.split(",");
                if (args.length == 6) {
                    try {
                        sendMessage(fromGroup, 0,
									Autoreply.instence.CC.location(
										Double.parseDouble(args[2]),
										Double.parseDouble(args[1]),
										Integer.parseInt(args[3]),
										args[4],
										args[5]));
                        return true;
					} catch (Exception e) {
                        sendMessage(fromGroup, fromQQ, "参数错误,生成位置.经度double.纬度double.倍数int.名称string.描述string");
                        return true;
					}
				}
			}
            String[] strings = msg.split("\\.", 3);
            if (strings[0].equals("send")) {
						sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
                return true;
			}
		}
        if (configManager.isAdmin(fromQQ)) {
			if (msg.equals("-help")) {
				Autoreply.sendMessage(fromGroup, 0, adminPermission.toString());
				return true;
			}
            if (msg.equals("鬼人正邪统计")) {
                sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(Autoreply.CQ.getLoginQQ()));
                return true;
			}
		}
        return false;
	}
}
