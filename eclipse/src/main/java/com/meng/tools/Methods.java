package com.meng.tools;

import com.meng.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Methods {

    public static String executeCmd(String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /c " + command);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line = null;
        StringBuilder build = new StringBuilder();
        while ((line = br.readLine()) != null) {
            build.append(line);
        }
        return build.toString();
    }

    public static boolean isPohaitu(long fromGroup, long fromQQ, String msg) {
        if (msg.equals("迫害图")) {
            String[] strings = (new File(Autoreply.appDirectory + "pohai/")).list();
            StringBuilder sBuilder = new StringBuilder("现在有");
            for (String s : strings) {
                sBuilder.append(" ").append(s);
            }
            sBuilder.append("的迫害图");
            Autoreply.sendMessage(fromGroup, fromQQ, sBuilder.toString());
            return true;
        }
        if (msg.endsWith("迫害图")) {
            switch (msg) {
                case "零食迫害图":
                    msg = "鸽鸽迫害图";
                    break;
                case "旭东迫害图":
                    msg = "天星厨迫害图";
                    break;
                case "星小渚迫害图":
                    msg = "杏子迫害图";
                    break;
            }
            File[] files = (new File(Autoreply.appDirectory + "pohai/" + msg.replace("迫害图", ""))).listFiles();
            if (files != null && files.length > 0) {
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
                Autoreply.instence.useCount.incPohaitu(fromQQ);
                Autoreply.instence.groupCount.incPohaitu(fromGroup);
                Autoreply.instence.useCount.incPohaitu(Autoreply.CQ.getLoginQQ());
            }
            return true;
        }
        return false;
    }

    public static boolean isSetu(long fromGroup, long fromQQ, String msg) {
        if (msg.equals("色图")) {
            String[] strings = (new File(Autoreply.appDirectory + "setu/")).list();
            StringBuilder sBuilder = new StringBuilder("现在有");
            for (String s : strings) {
                sBuilder.append(" ").append(s);
            }
            sBuilder.append("的色图");
            Autoreply.sendMessage(fromGroup, fromQQ, sBuilder.toString());
            return true;
        } else if (msg.equals("随机色图")) {
            File[] files = (new File(Autoreply.appDirectory + "setu/")).listFiles();
            File folder = (File) Tools.ArrayTool.rfa(files);
            File[] pics = folder.listFiles();
            Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(pics)))));
            Autoreply.instence.useCount.incSetu(fromQQ);
            Autoreply.instence.groupCount.incSetu(fromGroup);
            Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
        } else if (msg.endsWith("色图")) {
            File[] files = (new File(Autoreply.appDirectory + "setu/" + msg.replace("色图", ""))).listFiles();
            if (files != null && files.length > 0) {
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
                Autoreply.instence.useCount.incSetu(fromQQ);
                Autoreply.instence.groupCount.incSetu(fromGroup);
                Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
            }
            return true;
        }
        return false;
    }

    public static boolean isNvZhuang(long fromGroup, long fromQQ, String msg) {
        if (msg.equals("随机女装")) {
            File[] files = (new File(Autoreply.appDirectory + "nvzhuang/")).listFiles();
            File folder = (File) Tools.ArrayTool.rfa(files);
            File[] pics = folder.listFiles();
            Autoreply.instence.useCount.incSetu(fromQQ);
            Autoreply.instence.groupCount.incSetu(fromGroup);
            Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
            Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(pics)))));
        } else if (msg.endsWith("女装")) {
            File[] files = (new File(Autoreply.appDirectory + "nvzhuang/" + msg.replace("女装", ""))).listFiles();
            if (files != null && files.length > 0) {
                Autoreply.instence.useCount.incSetu(fromQQ);
                Autoreply.instence.groupCount.incSetu(fromGroup);
                Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
            }
            return true;
        }

        return false;
    }

    // 窥屏检测
    public static boolean checkLook(long fromGroup, String msg) {
        if (msg.equals("有人吗") || msg.equalsIgnoreCase("testip") || msg.equalsIgnoreCase("窥屏检测")) {
            int port = Autoreply.instence.random.nextInt(5000);
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "窥屏检测", "滴滴滴", "http://123.207.65.93:" + (port + 4000) + "/111.jpg"));
            final IPGetter ipGetter = new IPGetter(fromGroup, port);
            Autoreply.instence.threadPool.execute(ipGetter);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Autoreply.sendMessage(ipGetter.fromGroup, 0, "当前有" + ipGetter.hSet.size() + "个小伙伴看了群聊");
						ipGetter.running = false;
					}
				}, 20000);
            return true;
        }
        return false;
    }

    public static boolean checkXiong(long fromQQ, String msg) {
        if (Autoreply.instence.configManager.isAdmin(fromQQ)) {
            if (msg.equals("吊熊")) {
                int port = Autoreply.instence.random.nextInt(5000);
                Autoreply.sendMessage(0, fromQQ, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "东方绀珠传LNN", "东方绀珠传LNN", "http://123.207.65.93:" + (port + 4000) + "/1111.jpg"));
                final XiongIPGetter ipGetter = new XiongIPGetter(fromQQ, port);
                Autoreply.instence.threadPool.execute(ipGetter);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
						@Override
						public void run() {
							ipGetter.running = false;
						}
					}, 200000);
                return true;
            }
            if (msg.equals("吊熊2")) {
                int port = Autoreply.instence.random.nextInt(5000);
                Autoreply.sendMessage(0, fromQQ, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "东方绀珠传LNN", "东方绀珠传LNN", "http://123.207.65.93:" + (port + 4000) + "/1111.jpg"));
                XiongIPGetter ipGetter = new XiongIPGetter(fromQQ, port);
                Autoreply.instence.threadPool.execute(ipGetter);
                return true;
            }
        }
        return false;
    }
}
