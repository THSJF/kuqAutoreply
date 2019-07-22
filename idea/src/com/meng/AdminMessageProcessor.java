package com.meng;

import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;
import com.meng.picEdit.JingShenZhiZhuManager;
import com.meng.picEdit.ShenChuManager;
import com.meng.tools.Methods;
import com.meng.tools.MoShenFuSong;
import com.sobte.cqp.jcq.entity.CQImage;

import java.io.File;
import java.io.IOException;
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
                Autoreply.instence.threadPool.execute(() -> new JingShenZhiZhuManager(fromGroup, msg));
                return true;
            }
            if (msg.startsWith("神触[CQ:image")) {
                Autoreply.instence.threadPool.execute(() -> new ShenChuManager(fromGroup, msg));
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
        }
        return false;
    }

}
