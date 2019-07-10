package com.meng;

import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;
import com.meng.picEdit.JingShenZhiZhuManager;
import com.meng.picEdit.ShenChuManager;
import com.meng.tools.MoShenFuSong;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.Group;
import com.sobte.cqp.jcq.entity.Member;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static com.meng.Autoreply.sendMessage;

public class AdminMessageProcessor {
    private ConfigManager configManager;

    public AdminMessageProcessor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean check(long fromGroup, long fromQQ, String msg) {
        if (configManager.isMaster(fromQQ)) {
            if (msg.startsWith("更新提醒[CQ:at,qq=")) {
                List<Long> tipQQs = Autoreply.instence.CC.getAts(msg);
                HashSet<Long> succeed = new HashSet<>();
                for (long qq : tipQQs) {
                    for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
                        if (qq == personInfo.qq) {
                            if (personInfo.bid == 0) {
                                continue;
                            }
                            Autoreply.instence.updateListener.addTipPerson(fromGroup, personInfo.bid);
                            succeed.add(qq);
                            break;
                        }
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("成功：");
                for (long l : succeed) {
                    stringBuilder.append("\n").append(l);
                }
                sendMessage(fromGroup, fromQQ, stringBuilder.toString());
                return true;
            }
            if (msg.startsWith("直播提醒[CQ:at,qq=")) {
                List<Long> tipQQs = Autoreply.instence.CC.getAts(msg);
                HashSet<Long> succeed = new HashSet<>();
                for (long qq : tipQQs) {
                    for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
                        if (qq == personInfo.qq) {
                            if (personInfo.bid == 0) {
                                continue;
                            }
                            Autoreply.instence.liveListener.addTipPerson(fromGroup, personInfo.bid);
                            succeed.add(qq);
                            break;
                        }
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("成功：");
                for (long l : succeed) {
                    stringBuilder.append("\n").append(l);
                }
                sendMessage(fromGroup, fromQQ, stringBuilder.toString());
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
            if (msg.startsWith("ocr")) {
                if (Autoreply.instence.ocrManager.checkOcr(fromGroup, fromQQ, msg)) {
                    return true;
                }
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
            if ("精神支柱".equals(msg)) {
                sendMessage(fromGroup, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\alice.png")));
                return true;
            }
            if ("大芳法 芳神复诵".equals(msg)) {
                Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 5));
                return true;
            }
            String[] strings = msg.split("\\.");
            if (strings[0].equals("send")) {
                switch (strings[2]) {
                    case "喵":
                        sendMessage(Long.parseLong(strings[1]), 0, Autoreply.instence.CC.record("miao.mp3"));
                        break;
                    case "娇喘":
                        sendMessage(Long.parseLong(strings[1]), 0, Autoreply.instence.CC.record("mmm.mp3"));
                        break;
                    default:
                        sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
                        break;
                }
                return true;
            }
            if (msg.startsWith("精神支柱[CQ:image")) {
                String finalMsg = msg;
                Autoreply.instence.threadPool.execute(() -> new JingShenZhiZhuManager(fromGroup, finalMsg));
                return true;
            }
            if (msg.startsWith("神触[CQ:image")) {
                String finalMsg = msg;
                Autoreply.instence.threadPool.execute(() -> new ShenChuManager(fromGroup, finalMsg));
                return true;
            }
        }
        if (configManager.isAdmin(fromQQ)) {
            if (msg.equals("鬼人正邪统计")) {
                sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(Autoreply.CQ.getLoginQQ()));
                return true;
            }
            if (msg.startsWith("findInAll:")) {
                String finalMsg1 = msg;
                Autoreply.instence.threadPool.execute(() -> {
                    HashSet<Group> hashSet = new HashSet<>();
                    long qq;
                    try {
                        qq = Long.parseLong(finalMsg1.substring(10));
                    } catch (Exception e) {
                        PersonInfo personInfo = configManager.getPersonInfoFromName(finalMsg1.substring(10));
                        if (personInfo == null) {
                            sendMessage(fromGroup, fromQQ, "no info");
                            return;
                        }
                        qq = personInfo.qq;
                    }
                    List<Group> groups = Autoreply.CQ.getGroupList();
                    sendMessage(fromGroup, fromQQ, "running");
                    for (Group group : groups) {
                        if (group.getId() == 959615179L || group.getId() == 666247478L) {
                            continue;
                        }
                        ArrayList<Member> members = (ArrayList<Member>) Autoreply.CQ.getGroupMemberList(group.getId());
                        for (Member member : members) {
                            if (member.getQqId() == qq) {
                                hashSet.add(group);
                                break;
                            }
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(qq).append("在这些群中出现");
                    for (Group l : hashSet) {
                        stringBuilder.append("\n").append(l.getId()).append(l.getName());
                    }
                    sendMessage(fromGroup, fromQQ, stringBuilder.toString());
                });
                return true;
            }
            if (msg.contains("迫害图[CQ:image")) {
                String pohaituName = msg.substring(0, msg.indexOf("[CQ:image") - 3);
                String[] fileNames = msg.substring(msg.indexOf("["), msg.length() - 1).replace("[CQ:image,file=", "").split("]");
                List<CQImage> imgList = Autoreply.instence.CC.getCQImages(msg);
                for (int i = 0; i < imgList.size(); ++i) {
                    switch (pohaituName) {
                        case "零食":
                            msg = "鸽鸽";
                            break;
                        case "旭东":
                            msg = "天星厨";
                            break;
                        case "星小渚":
                            msg = "杏子";
                            break;
                        default:
                            break;
                    }
                    try {
                        imgList.get(i).download(Autoreply.appDirectory + File.separator + "pohai/" + pohaituName, fileNames[i]);
                    } catch (IOException e) {
                        e.printStackTrace();
                        sendMessage(fromGroup, fromQQ, e.toString());
                        return true;
                    }
                }
                sendMessage(fromGroup, fromQQ, imgList.size() + "张图添加成功");
                return true;
            }
            if (msg.contains("色图[CQ:image")) {
                String setuName = msg.substring(0, msg.indexOf("[CQ:image") - 2);
                String[] fileNames = msg.substring(msg.indexOf("["), msg.length() - 1).replace("[CQ:image,file=", "").split("]");
                List<CQImage> imgList = Autoreply.instence.CC.getCQImages(msg);
                for (int i = 0; i < imgList.size(); ++i) {
                    try {
                        imgList.get(i).download(Autoreply.appDirectory + File.separator + "setu/" + setuName, fileNames[i]);
                    } catch (IOException e) {
                        e.printStackTrace();
                        sendMessage(fromGroup, fromQQ, e.toString());
                        return true;
                    }
                }
                sendMessage(fromGroup, fromQQ, imgList.size() + "张图添加成功");
                return true;
            }
        }
        return false;
    }

}
