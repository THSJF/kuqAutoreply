package com.meng;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.bilibili.live.LiveListener;
import com.meng.bilibili.live.LivePerson;
import com.meng.bilibili.main.SpaceToLiveJavaBean;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;
import com.meng.picEdit.JingShenZhiZhuManager;
import com.meng.picEdit.ShenChuManager;
import com.meng.tools.DeleteMessageRunnable;
import com.meng.tools.Methods;
import com.meng.tools.MoShenFuSong;
import com.sobte.cqp.jcq.entity.CQImage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.meng.Autoreply.sendMessage;

public class AdminMessageProcessor {
    private ConfigManager configManager;

    public AdminMessageProcessor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean check(final long fromGroup, final long fromQQ, String message) {
        final String msg;
        if (message.endsWith("喵")) {
            msg = message.substring(0, message.length() - 1);
        } else {
            msg = message;
        }
        if (configManager.isMaster(fromQQ)) {
		  if(msg.equals("-help")){
			String s=".stop .start 总开关|block艾特一人 加入屏蔽列表|black艾特一人 加入黑名单|blackgroup加空格加群号 群加入黑名单 多群号中间空格隔开|findInAll:QQ号 查找共同群|"+
			"ban.QQ.时间 ban.群号.QQ.时间 禁言,单位为秒|find: 在配置文件中查找人信息"+
			".on .off 不修改配置文件的单群开关|.admin enable  .admin disable 修改配置文件的单群开关|添加图片|查看统计的直播时间|查看鬼人正邪发言统计|线程信息|夏眠";
			Autoreply.sendMessage(fromGroup,0,s);
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
            if (msg.startsWith("findInAll:")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Methods.findQQInAllGroup(fromGroup, fromQQ, msg);
                    }
                });
                return true;
            }
            
            if (msg.startsWith("av更新时间:")) {
                Autoreply.sendMessage(fromGroup, fromQQ, String.valueOf(Autoreply.instence.updateManager.getAVLastUpdateTime(msg.substring(7))));
                return true;
            }
            if (msg.startsWith("avJson:")) {
                Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.updateManager.getAVJson(msg.substring(7)));
                return true;
            }
            if (msg.startsWith("cv更新时间:")) {
                Autoreply.sendMessage(fromGroup, fromQQ, String.valueOf(Autoreply.instence.updateManager.getCVLastUpdateTime(msg.substring(7))));
                return true;
            }
            if (msg.startsWith("cvJson:")) {
                Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.updateManager.getCVJson(msg.substring(7)));
                return true;
            }
            if (msg.startsWith("直播状态lid:")) {
                String html = Methods.getSourceCode("https://live.bilibili.com/" + msg.substring(8));
                String jsonInHtml = html.substring(html.indexOf("{\"roomInitRes\":"), html.lastIndexOf("}") + 1);
                JsonObject data = new JsonParser().parse(jsonInHtml).getAsJsonObject().get("baseInfoRes").getAsJsonObject().get("data").getAsJsonObject();
                Autoreply.sendMessage(fromGroup, fromQQ, data.get("live_status").getAsInt() == 1 ? "true" : "false");
                return true;
            }
            if (Autoreply.instence.biliLinkInfo.checkOgg(fromGroup, fromQQ, msg)) {
                return true;
            }
            if (msg.startsWith("直播状态bid:")) {
                SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + msg.substring(8)), SpaceToLiveJavaBean.class);
                Autoreply.sendMessage(fromGroup, fromQQ, sjb.data.liveStatus == 1 ? "true" : "false");
                return true;
            }
            if (msg.startsWith("获取直播间:")) {
                Autoreply.sendMessage(fromGroup, fromQQ, Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + msg.substring(6)));
                return true;
            }
            if (msg.equals(".live")) {
                String msgSend;
                final StringBuilder stringBuilder = new StringBuilder();
                Autoreply.instence.liveListener.livePersonMap.forEach(new BiConsumer<Integer, LivePerson>() {
                    @Override
                    public void accept(Integer key, LivePerson livePerson) {
                        if (livePerson.lastStatus) {
                            stringBuilder.append(Autoreply.instence.configManager.getPersonInfoFromBid(key).name).append("正在直播").append(livePerson.liveUrl).append("\n");
                        }
                    }
                });
                msgSend = stringBuilder.toString();
                Autoreply.sendMessage(fromGroup, fromQQ, msgSend.equals("") ? "居然没有飞机佬直播" : msgSend);
                return true;
            }
            if (msg.startsWith("add{")) {
                PersonInfo personInfo;
                try {
                    personInfo = new Gson().fromJson(msg.substring(3), PersonInfo.class);
                } catch (Exception e) {
                    Autoreply.sendMessage(fromGroup, fromQQ, e.toString());
                    return true;
                }
                if (personInfo != null) {
                    Autoreply.instence.configManager.configJavaBean.personInfo.add(personInfo);
                    Autoreply.instence.configManager.saveConfig();
                    Autoreply.sendMessage(fromGroup, fromQQ, msg + "成功");
                } else {
                    Autoreply.sendMessage(fromGroup, fromQQ, "一个玄学问题导致了失败");
                }
                return true;
            }
            if (msg.startsWith("del{")) {
                PersonInfo p;
                try {
                    p = new Gson().fromJson(msg.substring(3), PersonInfo.class);
                } catch (Exception e) {
                    Autoreply.sendMessage(fromGroup, fromQQ, e.toString());
                    return true;
                }
                if (p != null) {
                    Autoreply.instence.configManager.configJavaBean.personInfo.remove(p);
                    Autoreply.instence.configManager.saveConfig();
                    Autoreply.sendMessage(fromGroup, fromQQ, msg + "成功");
                } else {
                    Autoreply.sendMessage(fromGroup, fromQQ, "一个玄学问题导致了失败");
                }
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
                Autoreply.sendMessage(fromGroup, fromQQ, new Gson().toJson(hashSet));
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
            if(msg.equalsIgnoreCase("System.gc();")){
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
            if (msg.equals("livesave")) {
                Autoreply.instence.liveListener.saveNow();
                return true;
            }
            if (msg.startsWith("nai.")) {
                String[] sarr = msg.split("\\.");
                PersonInfo pInfo = configManager.getPersonInfoFromName(sarr[1]);
                if (pInfo != null) {
                    Autoreply.instence.naiManager.check(fromGroup, pInfo.bliveRoom, fromQQ, sarr[2]);
                } else {
                    Autoreply.instence.naiManager.check(fromGroup, Integer.parseInt(sarr[1]), fromQQ, sarr[2]);
                }
                return true;
            }
            if (msg.equals("精神支柱")) {
                sendMessage(fromGroup, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\alice.png")));
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
            if (msg.equals("大芳法 芳神复诵")) {
                Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 6));
                return true;
            }
            String[] strings = msg.split("\\.");
            if (strings[0].equals("send")) {
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
            if (msg.startsWith("精神支柱[CQ:image")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        new JingShenZhiZhuManager(fromGroup, msg);
                    }
                });
                return true;
            }
            if (msg.startsWith("神触[CQ:image")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        new ShenChuManager(fromGroup, msg);
                    }
                });
                return true;
            }
            if (msg.startsWith("设置群头衔[CQ:at")) {
                String title = msg.substring(msg.indexOf("]") + 1);
                System.out.println(Autoreply.CQ.setGroupSpecialTitle(fromGroup, Autoreply.instence.CC.getAt(msg), title, -1));
                return true;
            }
            if (msg.startsWith("设置群名片[CQ:at")) {
                String title = msg.substring(msg.indexOf("]") + 1);
                System.out.println(Autoreply.CQ.setGroupCard(fromGroup, Autoreply.instence.CC.getAt(msg), title));
                return true;
            }
        }
        if (configManager.isAdmin(fromQQ)) {
		  
			if(msg.equals("-help")){
				String s="ban.QQ.时间 ban.群号.QQ.时间 禁言,单位为秒|.on .off 不修改配置文件的单群开关|.admin enable  .admin disable 修改配置文件的单群开关|添加图片|查看统计的直播时间|查看鬼人正邪发言统计|";
				Autoreply.sendMessage(fromGroup,0,s);
				return true;
			  }
			if (msg.startsWith("ban")) {
                String[] arr = msg.split("\\.");
                if (arr.length == 3 || arr.length == 4) {
                    Autoreply.instence.banner.checkBan(fromGroup, fromQQ, msg);
				  }
                return true;
			  }
            if (msg.equals("鬼人正邪统计")) {
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
            if (msg.contains("迫害图[CQ:image")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        String pohaituName = msg.substring(0, msg.indexOf("[CQ:image") - 3);
                        switch (pohaituName) {
                            case "零食":
                                pohaituName = "鸽鸽";
                                break;
                            case "旭东":
                                pohaituName = "天星厨";
                                break;
                            case "星小渚":
                                pohaituName = "杏子";
                                break;
                            default:
                                break;
                        }
                        List<CQImage> imgList = Autoreply.instence.CC.getCQImages(msg);
                        for (CQImage cqImage : imgList) {
                            try {
                                Autoreply.instence.fileTypeUtil.checkFormat(cqImage.download(Autoreply.appDirectory + File.separator + "pohai/" + pohaituName, cqImage.getMd5()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                sendMessage(fromGroup, fromQQ, e.toString());
                                return;
                            }
                        }
                        sendMessage(fromGroup, fromQQ, imgList.size() + "张图添加成功");
                    }
                });
                return true;
            }
            if (msg.contains("色图[CQ:image")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        String setuName = msg.substring(0, msg.indexOf("[CQ:image") - 2);
                        List<CQImage> imgList = Autoreply.instence.CC.getCQImages(msg);
                        for (CQImage cqImage : imgList) {
                            try {
                                Autoreply.instence.fileTypeUtil.checkFormat(cqImage.download(Autoreply.appDirectory + File.separator + "setu/" + setuName, cqImage.getMd5()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                sendMessage(fromGroup, fromQQ, e.toString());
                                return;
                            }
                        }
                        sendMessage(fromGroup, fromQQ, imgList.size() + "张图添加成功");
                    }
                });
                return true;
            }
            if (msg.contains("女装[CQ:image")) {
                Autoreply.instence.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        String setuName = msg.substring(0, msg.indexOf("[CQ:image") - 2);
                        List<CQImage> imgList = Autoreply.instence.CC.getCQImages(msg);
                        for (CQImage cqImage : imgList) {
                            try {
                                Autoreply.instence.fileTypeUtil.checkFormat(cqImage.download(Autoreply.appDirectory + File.separator + "nvzhuang/" + setuName, cqImage.getMd5()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                sendMessage(fromGroup, fromQQ, e.toString());
                                return;
                            }
                        }
                        sendMessage(fromGroup, fromQQ, imgList.size() + "张图添加成功");
                    }
                });
                return true;
            }
        }
        return false;
    }

}
