package com.meng;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.meng.Autoreply.sendMessage;

public class AdminMessageProcessor {
    private ConfigManager configManager;

    public AdminMessageProcessor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean check(long fromGroup, long fromQQ, final String message) {
        String msg;
        if (message.endsWith("喵")) {
            msg = message.substring(0, message.length() - 1);
        } else {
            msg = message;
        }
        if (configManager.isMaster(fromQQ)) {
            if (msg.startsWith("findInAll:")) {
                Autoreply.instence.threadPool.execute(() -> Methods.findQQInAllGroup(fromGroup, fromQQ, msg));
                return true;
            }
            if (msg.startsWith("ban")) {
                String[] arr = msg.split("\\.");
                if (arr.length == 3 || arr.length == 4) {
                    Autoreply.instence.banner.checkBan(fromGroup, fromQQ, msg);
                }
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
                String msgSend = Autoreply.instence.liveListener.livePerson.stream().filter(livePerson -> livePerson.living).map(livePerson -> livePerson.name + "正在直播" + livePerson.liveUrl + "\n").collect(Collectors.joining());
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
            if (msg.equals("zan-now")) {
                System.out.println("start");
                sendMessage(fromGroup, fromQQ, "start");
                Autoreply.instence.threadPool.execute(() -> {
                    Autoreply.instence.zanManager.sendZan();
                    sendMessage(fromGroup, fromQQ, "finish");
                    System.out.println("finish");
                });
                return true;
            }
            if (Autoreply.instence.zanManager.checkAdd(fromGroup, fromQQ, msg)) {
                return true;
            }
            // 手动更新设置，不再需要重启
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
                Autoreply.instence.threadPool.execute(() -> new JingShenZhiZhuManager(fromGroup, msg));
                return true;
            }
            if (msg.startsWith("神触[CQ:image")) {
                Autoreply.instence.threadPool.execute(() -> new ShenChuManager(fromGroup, msg));
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
            if (msg.equals("鬼人正邪统计")) {
                sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(Autoreply.CQ.getLoginQQ()));
                return true;
            }
            if (msg.startsWith("findInAll:")) {
                Autoreply.instence.threadPool.execute(() -> Methods.findQQInAllGroup(fromGroup, fromQQ, msg));
                return true;
            }
            if (msg.contains("迫害图[CQ:image")) {
                Autoreply.instence.threadPool.execute(() -> {
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
                });
                return true;
            }
            if (msg.contains("色图[CQ:image")) {
                Autoreply.instence.threadPool.execute(() -> {
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
                });
                return true;
            }
            if (msg.contains("女装[CQ:image")) {
                Autoreply.instence.threadPool.execute(() -> {
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
                });
                return true;
            }
        }
        return false;
    }

}
