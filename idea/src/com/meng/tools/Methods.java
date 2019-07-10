package com.meng.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.meng.Autoreply;
import com.meng.picEdit.JingShenZhiZhuQQManager;
import com.meng.picEdit.ShenChuQQManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.meng.bilibili.live.LiveListener;
import com.meng.lookGroup.IPGetter;

import com.meng.diaoXiongJiHua.XiongIPGetter;

public class Methods {
    private static String motmp = "";
    private static String meng2tmp = "";
    // static int arrayFlag = new java.util.Random().nextInt(5000);

    // 主开关
    public static boolean checkSwitch(long fromGroup, String msg) {
        if (msg.equals(".stop")) {
            Autoreply.sendMessage(fromGroup, 0, "disabled");
            Autoreply.instence.enable = false;
            return true;
        }
        if (msg.equals(".livestop")) {
            Autoreply.sendMessage(fromGroup, 0, "livedisabled");
            LiveListener.liveStart = false;
            return true;
        }
        if (msg.equals(".start")) {
            Autoreply.instence.enable = true;
            Autoreply.sendMessage(fromGroup, 0, "enabled");
            return true;
        }
        if (msg.equals(".livestart")) {
            LiveListener.liveStart = true;
            Autoreply.sendMessage(fromGroup, 0, "liveenabled");
            return true;
        }
        return false;
    }

    public static void ban(long fromGroup, long fromQQ, int time) {
        if (fromQQ == 2558395159L) {
            return;
        }
        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, time);
    }

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
            if (files.length > 0) {
                Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                Autoreply.instence.useCount.incPohaitu(fromQQ);
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
            File folder = (File) rfa(files);
            File[] pics = folder.listFiles();
            Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(pics)));
            Autoreply.instence.useCount.incSetu(fromQQ);
            Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
        } else if (msg.endsWith("色图")) {
            File[] files = (new File(Autoreply.appDirectory + "setu/" + msg.replace("色图", ""))).listFiles();
            if (files.length > 0) {
                Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                Autoreply.instence.useCount.incSetu(fromQQ);
                Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
            }
            return true;
        }

        return false;
    }

    // randomFromArray 随机返回数组中的一项
    public static Object rfa(Object[] array) {
        return array[Autoreply.instence.random.nextInt(2147483647) % array.length];
    }

    // 有@的时候
    public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
        if (Autoreply.instence.CC.getAt(msg) == Autoreply.CQ.getLoginQQ()) {
            if (msg.startsWith("精神支柱[CQ:at")) {
                new JingShenZhiZhuQQManager(fromGroup, fromQQ, Autoreply.instence.CC.at(fromQQ));
                return true;
            } else if (msg.startsWith("神触[CQ:at")) {
                new ShenChuQQManager(fromGroup, fromQQ, Autoreply.instence.CC.at(fromQQ));
                return true;
            }
            // 过滤特定的文字
            // @消息发送者并复读内容
            if (msg.contains("蓝") || msg.contains("藍") || msg.contains("赠送")) {
                return true;
            }
            if (fromQQ == 2558395159L) {
                return true;
            }
            Autoreply.sendMessage(fromGroup, 0, msg.replace("[CQ:at,qq=1620628713]", "[CQ:at,qq=" + fromQQ + "]"));

            // if (msg.contains("野兽先辈") || msg.contains("仰望星空派") ||
            // msg.contains("英国") || msg.contains("鬼杀酒")
            // || msg.contains("羊杂碎布丁") || msg.contains("昏睡红茶") ||
            // msg.contains("英式鳗鱼冻")) {
            // Autoreply.sendMessage(fromGroup, 0,
            // Autoreply.instence.CC.at(fromQQ) + msg.substring(msg.indexOf(" ")
            // + 1));
            // return true;
            // }
            // return true;
        }
        return false;
    }

    // 读取文本文件
    public static String readFileToString(String fileName) throws IOException, UnsupportedEncodingException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        long filelength = file.length();
        byte[] filecontent = new byte[(int) filelength];
        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();
        return new String(filecontent, StandardCharsets.UTF_8);
    }

    // 删除字符串中指定位置的文字
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    // 获取字符串中指定位置的文字
    public static String getStringBetween(String str, String start, String end, int index) {

        int flagA = str.indexOf(start, index);
        int flagB = str.indexOf(end, flagA + 1);
        if (flagA < 0 || flagB < 0) {
            return null;
        } else {
            flagA = flagA + start.length();
            if (flagA < 0 || flagB < 0) {
                return null;
            }
            return str.substring(flagA, flagB);
        }
    }

    // 删除字符串两端
    public static String removeCharAtStartAndEnd(String s) {
        String tmp = removeCharAt(s, 0);
        return removeCharAt(tmp, tmp.length() - 1);
    }

    // 暴力行为
    public static boolean checkGou(long fromGroup, String msg) {
        if (msg.equals("苟") || msg.equals("苟？") || msg.equals("苟?")) {
            motmp = "利";
            Autoreply.sendMessage(fromGroup, 0, "利");
            return true;
        } else if (msg.equals("国") && motmp.equals("利")) {
            motmp = "家";
            Autoreply.sendMessage(fromGroup, 0, "家");
            return true;
        } else if (msg.equals("生") && motmp.equals("家")) {
            motmp = "死";
            Autoreply.sendMessage(fromGroup, 0, "死");
            return true;
        } else if (msg.equals("以") && motmp.equals("死")) {
            motmp = "岂";
            Autoreply.sendMessage(fromGroup, 0, "岂");
            return true;
        } else if (msg.equals("因") && motmp.equals("岂")) {
            motmp = "祸";
            Autoreply.sendMessage(fromGroup, 0, "祸");
            return true;
        } else if (msg.equals("福") && motmp.equals("祸")) {
            motmp = "避";
            Autoreply.sendMessage(fromGroup, 0, "避");
            return true;
        } else if (msg.equals("趋") && motmp.equals("避")) {
            motmp = "之";
            Autoreply.sendMessage(fromGroup, 0, "之");
            return true;
        } else if (msg.equals("苟利国家生死以")) {
            Autoreply.sendMessage(fromGroup, 0, "岂因祸福避趋之");
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

    // 萌二
    public static boolean checkMeng2(long fromGroup, String msg) {

        if (msg.equals("此")) {
            meng2tmp = "生";
            Autoreply.sendMessage(fromGroup, 0, "生");
            return true;
        } else if (msg.equals("无") && meng2tmp.equals("生")) {
            meng2tmp = "悔";
            Autoreply.sendMessage(fromGroup, 0, "悔");
            return true;
        } else if (msg.equals("入") && meng2tmp.equals("悔")) {
            meng2tmp = "东";
            Autoreply.sendMessage(fromGroup, 0, "东");
            return true;
        } else if (msg.equals("方") && meng2tmp.equals("东")) {
            meng2tmp = "来";
            Autoreply.sendMessage(fromGroup, 0, "来");
            return true;
        } else if (msg.equals("世") && meng2tmp.equals("来")) {
            meng2tmp = "愿";
            Autoreply.sendMessage(fromGroup, 0, "愿");
            return true;
        } else if (msg.equals("生") && meng2tmp.equals("愿")) {
            meng2tmp = "幻";
            Autoreply.sendMessage(fromGroup, 0, "幻");
            return true;
        } else if (msg.equals("想") && meng2tmp.equals("幻")) {
            meng2tmp = "乡";
            Autoreply.sendMessage(fromGroup, 0, "乡");
            return true;
        } else if (msg.equals("此生无悔入东方")) {
            Autoreply.sendMessage(fromGroup, 0, "来世愿生幻想乡");
            return true;
        }
        return false;
    }

    public static void setRandomPop() {
        Methods.getSourceCode("http://logic.content.qq.com/bubble/setup?callback=&id=" + new Random().nextInt(269) + "&g_tk=" + Autoreply.CQ.getCsrfToken(), Autoreply.CQ.getCookies());
    }

    public static Map<String, String> cookieToMap(String value) {
        Map<String, String> map = new HashMap<String, String>();
        String[] values = value.split("; ");
        for (String val : values) {
            String[] vals = val.split("=");
            if (vals.length == 2) {
                map.put(vals[0], vals[1]);
            } else if (vals.length == 1) {
                map.put(vals[0], "");
            }
        }
        return map;
    }

    public static String getRealUrl(String surl) throws Exception {
        URL url = new URL(surl);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String nurl = conn.getURL().toString();
        System.out.println("realUrl" + nurl);
        in.close();
        return nurl;
    }

    public static String getSourceCode(String url) {
        return getSourceCode(url, null);
    }

    public static String getSourceCode(String url, String cookie) {
        Connection.Response response = null;
        Connection connection = null;
        try {
            connection = Jsoup.connect(url);
            if (cookie != null) {
                connection.cookies(cookieToMap(cookie));
            }
            connection.ignoreContentType(true).method(Connection.Method.GET);
            response = connection.execute();
            //  if (response.statusCode() != 200) {
            //  }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.body();
        } else {
            return null;
        }
    }
    // public static String getG_tk(String skey) {
    // int hash = 5381;
    // int flag = skey.length();
    // for (int i = 0; i < flag; i++) {
    // hash = hash + hash * 32 + skey.charAt(i);
    // }
    // return String.valueOf(hash & 0x7fffffff);
    // }
}
