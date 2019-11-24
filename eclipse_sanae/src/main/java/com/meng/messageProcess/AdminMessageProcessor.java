package com.meng.messageProcess;

import com.google.gson.*;
import com.meng.*;
import com.meng.bilibili.live.*;
import com.meng.config.*;
import com.meng.config.javabeans.*;
import com.meng.dice.*;
import com.meng.tools.*;
import com.meng.tools.override.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.jsoup.*;

import static com.meng.Autoreply.sendMessage;

public class AdminMessageProcessor {
    private ConfigManager configManager;
	private MyLinkedHashMap<String,String> masterPermission=new MyLinkedHashMap<>();
	private MyLinkedHashMap<String,String> adminPermission=new MyLinkedHashMap<>();
	public MyLinkedHashMap<String,String> userPermission=new MyLinkedHashMap<>();

    public AdminMessageProcessor(ConfigManager configManager) {
        this.configManager = configManager;
		masterPermission.put("小律影专用指令:setconnect", "");
		masterPermission.put(".start|.stop", "总开关");
		masterPermission.put("find:[QQ号]", "在配置文件中查找此人");
		masterPermission.put("z.add[艾特至少一人]", "点赞列表");
		masterPermission.put("zan-now", "立即启动点赞线程,尽量不要用");
		masterPermission.put("block[艾特一人]", "屏蔽列表");
		masterPermission.put("black[艾特一人]", "黑名单");
		masterPermission.put("System.gc();", "System.gc();");
		masterPermission.put("-live.[start|stop]", "开关直播(hina)");
		masterPermission.put("-live.rename.[字符串]", "直播改名(hina)");
		masterPermission.put("blackgroup [群号]", "群加入黑名单,多群用空格隔开");
		masterPermission.put("cookie.[称呼].[cookie字符串]", "设置cookie,可选值Sunny,Luna,Star,XingHuo,Hina,grzx");
		masterPermission.put("send.[群号].[内容]", "内容转发至指定群");
		masterPermission.put("mother.[字符串]", "直播间点歌问候");
		masterPermission.put("lban.[直播间号|直播间主人].[被禁言UID|被禁言者称呼].[时间]", "直播间禁言,单位为小时");
		masterPermission.put("移除成就 [成就名] [艾特一人]", "移除此人的该成就");

		adminPermission.put("findInAll:[QQ号]", "查找共同群");
		adminPermission.put("ban.[QQ号|艾特].[时间]|ban.[群号].[QQ号].[时间]", "禁言,单位为秒");
		adminPermission.put("线程数", "线程池信息");
		adminPermission.put(".on|.off", "不修改配置文件的单群开关");
		adminPermission.put(".admin enable|.admin disable", "修改配置文件的单群开关");
		adminPermission.put(".live", "不管配置文件如何,都回复直播列表");

		userPermission.put(".live", "正在直播列表");
		userPermission.put(".nn [名字]", "设置早苗对你的称呼,如果不设置则恢复默认称呼");
		userPermission.put("-int [int] [+|-|*|/|<<|>>|>>>|%|^|&||] [int]", "int运算(溢出)");
		userPermission.put("-uint [int]", "int字节转uint(boom)");
		userPermission.put("抽卡", "抽卡");
		userPermission.put("查看成就", "查看成就列表");
		userPermission.put("查看符卡", "查看已获得的符卡,会刷屏，少用");
		userPermission.put("成就条件 [成就名]", "查看获得条件");
		userPermission.put("~coins", "查看幻币数量");
		userPermission.put("幻币抽卡 [整数]", "使用本地幻币抽卡");
		userPermission.put("购买符卡 [符卡名]", "购买指定符卡,除lastword");

		masterPermission.putAll(adminPermission);
		masterPermission.putAll(userPermission);
		adminPermission.putAll(userPermission);
	}

    public boolean check(final long fromGroup, final long fromQQ, final String msg) {
		if (fromQQ == 2856986197L || fromQQ == 2528419891L) {
			if (msg.startsWith("bchat.")) {
				String[] strs=msg.split("\\.", 3);
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(strs[1]);
				String resu;
				if (pi == null) {
					resu = Autoreply.instence.naiManager.sendChat(strs[1], strs[2]);
				} else {
					resu = Autoreply.instence.naiManager.sendChat(pi.bliveRoom + "", strs[2]);
				}	
				if (!resu.equals("")) {
					Autoreply.sendMessage(fromGroup, 0, resu);
				}
				return true;
			}
			if (msg.startsWith("blink.")) {
				String[] strs=msg.split("\\.", 2);
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(strs[1]);
				if (pi == null) {	  
					JsonParser parser = new JsonParser();
					JsonObject obj = parser.parse(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/playUrl?cid=" + strs[1] + "&quality=4&platform=web")).getAsJsonObject();
					JsonArray ja = obj.get("data").getAsJsonObject().get("durl").getAsJsonArray();
					Autoreply.sendMessage(fromGroup, 0, ja.get(0).getAsJsonObject().get("url").getAsString());
				} else {
					JsonParser parser = new JsonParser();
					JsonObject obj = parser.parse(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/playUrl?cid=" + pi.bliveRoom + "&quality=4&platform=web")).getAsJsonObject();
					JsonArray ja = obj.get("data").getAsJsonObject().get("durl").getAsJsonArray();
					Autoreply.sendMessage(fromGroup, 0, ja.get(0).getAsJsonObject().get("url").getAsString());			  
				}	
				return true;
			} 
		}

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
				if (msg.contains("~") || msg.contains("～")) {
					Autoreply.sendMessage(fromGroup, 0, "包含屏蔽的字符");
					return true;
				}
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
			if (msg.startsWith("-live")) {
				String[] str=msg.split("\\.");
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromQQ(fromQQ);
				String name;
				if (pi == null) {
					name = "" + fromQQ;
				} else {
					name = pi.name;
				}
				switch (str[1]) {
					case "start":
						try {
							Autoreply.sendMessage(fromGroup, 0, start(9721948, Autoreply.instence.cookieManager.cookie.Hina));
							Autoreply.sendMessage(Autoreply.mainGroup, 0, name + "开启了直播");
						} catch (IOException e) {}
						break;
					case "stop":
						try {
							Autoreply.sendMessage(fromGroup, 0, stop(9721948, Autoreply.instence.cookieManager.cookie.Hina));
							Autoreply.sendMessage(Autoreply.mainGroup, 0, name + "关闭了直播");
						} catch (IOException e) {}
						break;
					case "rename":
						try {
							Autoreply.sendMessage(fromGroup, 0, rename(9721948, Autoreply.instence.cookieManager.cookie.Hina, str[2]));
							Autoreply.sendMessage(Autoreply.mainGroup, 0, name + "为直播改了名:" + str[2]);
						} catch (IOException e) {}
				}	
				return true;
			}
			if (msg.startsWith("lban.")) {
				String[] ss=msg.split("\\.");
				String rid=ss[1];
				String uid=ss[2];
				PersonInfo mas=Autoreply.instence.configManager.getPersonInfoFromName(ss[1]);
				if (mas != null) {
					rid = mas.bliveRoom + "";
				}
				PersonInfo ban=Autoreply.instence.configManager.getPersonInfoFromName(ss[2]);
				if (ban != null) {
					uid = ban.bid + "";
				}
				Autoreply.instence.liveListener.setBan(fromGroup, rid, uid, ss[3]);
				return true;
			}
			if (msg.startsWith("mother.")) {
				if (msg.length() > 7) {
					if (Autoreply.instence.danmakuListenerManager.addMotherWord(msg.substring(7))) {
						Autoreply.sendMessage(fromGroup, 0, msg.substring(7) + "已添加");
					} else {
						Autoreply.sendMessage(fromGroup, 0, "添加失败");
					}
				} else {
					Autoreply.sendMessage(fromGroup, 0, "参数有误");
				}
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
            if (msg.startsWith("findInAll:")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Methods.findQQInAllGroup(fromGroup, fromQQ, msg);
						}
					});
                return true;
			}
            if (msg.startsWith("find:")) {
                String name = msg.substring(5);
                HashSet<PersonInfo> hashSet = new HashSet<>();
                for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
                    if (personInfo.name.contains(name)) {
                        hashSet.add(personInfo);
					}
                    if (personInfo.qq != 0 && String.valueOf(personInfo.qq).contains(name)) {
                        hashSet.add(personInfo);
					}
                    if (personInfo.bid != 0 && String.valueOf(personInfo.bid).contains(name)) {
                        hashSet.add(personInfo);
					}
                    if (personInfo.bliveRoom != 0 && String.valueOf(personInfo.bliveRoom).contains(name)) {
                        hashSet.add(personInfo);
					}
				}
                Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.gson.toJson(hashSet));
                return true;
			}
            if (msg.equals("saveconfig")) {
                Autoreply.instence.configManager.saveConfig();
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
            if (msg.equals("zan-now")) {
                sendMessage(fromGroup, fromQQ, "start");
                Autoreply.instence.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Autoreply.instence.zanManager.sendZan();
							sendMessage(fromGroup, fromQQ, "finish");
						}
					});
                return true;
			}
            if (Autoreply.instence.zanManager.checkAdd(fromGroup, fromQQ, msg)) {
                return true;
			}
            if (msg.equals("直播时间统计")) {
                sendMessage(fromGroup, 0, Autoreply.instence.liveListener.getLiveTimeCount());
                return true;
			}
            if (msg.startsWith("nai.")) {
                String[] sarr = msg.split("\\.", 3);
                PersonInfo pInfo = configManager.getPersonInfoFromName(sarr[1]);
                if (pInfo != null) {
                    Autoreply.instence.naiManager.check(fromGroup, pInfo.bliveRoom + "", fromQQ, sarr[2]);
				} else {
                    Autoreply.instence.naiManager.check(fromGroup, sarr[1], fromQQ, sarr[2]);
				}
                return true;
			}
            String[] strings = msg.split("\\.", 3);
            if (strings[0].equals("send")) {
				if (msg.contains("~") || msg.contains("～")) {
					Autoreply.sendMessage(fromGroup, 0, "包含屏蔽的字符");
					return true;
				}
                switch (strings[2]) {
                    case "喵":
						Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(sendMessage(Long.parseLong(strings[1]), 0, Autoreply.instence.CC.record("miao.mp3"))));
						break;
                    case "娇喘":
						Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(sendMessage(Long.parseLong(strings[1]), 0, Autoreply.instence.CC.record("mmm.mp3"))));
						break;
                    default:
						sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
						break;
				}
                return true;
			}
			if (strings[0].equals("cookie")) {
				if (!Autoreply.instence.cookieManager.setCookie(strings[1], strings[2])) {
					Autoreply.sendMessage(fromGroup, 0, "添加失败");
					return true;
				}
				Autoreply.sendMessage(fromGroup, 0, "已为" + strings[1] + "设置cookie");
                return true;
			}
		}
        if (configManager.isAdmin(fromQQ)) {
			if (msg.equals("-help")) {
				Autoreply.sendMessage(fromGroup, 0, adminPermission.toString());
				return true;
			}
			if (msg.equals(".live")) {
				String msgSend;
				StringBuilder stringBuilder = new StringBuilder();
				for (Map.Entry<Integer,LivePerson> entry:Autoreply.instence.liveListener.livePersonMap.entrySet()) {	
					if (entry.getValue().lastStatus) {
						stringBuilder.append(Autoreply.instence.configManager.getPersonInfoFromBid(entry.getKey()).name).append("正在直播").append(entry.getValue().liveUrl).append("\n");
					}
				}
				msgSend = stringBuilder.toString();
				Autoreply.sendMessage(fromGroup, fromQQ, msgSend.equals("") ? "居然没有飞机佬直播" : msgSend);
				return true;
			}
			if (msg.startsWith("ban")) {
                String[] arr = msg.split("\\.");
                if (arr.length == 3 || arr.length == 4) {
                    Autoreply.instence.banner.checkBan(fromGroup, fromQQ, msg);
				}
                return true;
			}
            if (msg.equals("早苗统计")) {
                sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(Autoreply.CQ.getLoginQQ()));
                return true;
			}
            if (msg.startsWith("findInAll:")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Methods.findQQInAllGroup(fromGroup, fromQQ, msg);
						}
					});
                return true;
			}
		}
        return false;
	}

	public String start(int roomID, String cookie) throws IOException {
        Connection connection = Jsoup.connect("https://api.live.bilibili.com/room/v1/Room/startLive");
        String csrf = Methods.cookieToMap(cookie).get("bili_jct");
        Map<String, String> liveHead = new HashMap<>();
        liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://link.bilibili.com");
        connection.userAgent(Autoreply.instence.userAgent)
			.headers(liveHead)
			.ignoreContentType(true)
			.referrer("https://link.bilibili.com/p/center/index")
			.cookies(Methods.cookieToMap(cookie))
			.method(Connection.Method.POST)
			.data("room_id", String.valueOf(roomID))
			.data("platform", "pc")
			.data("area_v2", "235")
			.data("csrf_token", csrf)
			.data("csrf", csrf);
        Connection.Response response = connection.execute();

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(response.body()).getAsJsonObject();
		if (obj.get("code").getAsInt() == 0) {
			return "开播成功";
		}
		return obj.get("message").getAsString();
    }


    public String stop(int roomID, String cookie) throws IOException {
        Connection connection = Jsoup.connect("https://api.live.bilibili.com/room/v1/Room/stopLive");
        String csrf = Methods.cookieToMap(cookie).get("bili_jct");
        Map<String, String> liveHead = new HashMap<>();
        liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://link.bilibili.com");
        connection.userAgent(Autoreply.instence.userAgent)
			.headers(liveHead)
			.ignoreContentType(true)
			.referrer("https://link.bilibili.com/p/center/index")
			.cookies(Methods.cookieToMap(cookie))
			.method(Connection.Method.POST)
			.data("room_id", String.valueOf(roomID))
			.data("csrf_token", csrf)
			.data("csrf", csrf);
        Connection.Response response = connection.execute();

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(response.body()).getAsJsonObject();
		if (obj.get("code").getAsInt() == 0) {
			return "关闭成功";
		}
		return obj.get("message").getAsString();
    }

    public String rename(int roomID, String cookie, String newName) throws IOException {
        Connection connection = Jsoup.connect("https://api.live.bilibili.com/room/v1/Room/update");
        String csrf = Methods.cookieToMap(cookie).get("bili_jct");
        Map<String, String> liveHead = new HashMap<>();
        liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://link.bilibili.com");
        connection.userAgent(Autoreply.instence.userAgent)
			.headers(liveHead)
			.ignoreContentType(true)
			.referrer("https://link.bilibili.com/p/center/index")
			.cookies(Methods.cookieToMap(cookie))
			.method(Connection.Method.POST)
			.data("room_id", String.valueOf(roomID))
			.data("title", newName)
			.data("csrf_token", csrf)
			.data("csrf", csrf);
        Connection.Response response = connection.execute();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(response.body()).getAsJsonObject();
        return obj.get("message").getAsString();
    }

}
