package com.meng.tools;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;

public class GroupCounter {
    private HashMap<String, GroupInfo> countMap = new HashMap<>();
    private File file;

    public class GroupInfo {
        public int speak = 0;
        public int pic = 0;
        public int biliLink = 0;
        public int repeat = 0;
        public int repeatBreak = 0;
        public int pohai = 0;
        public int sp = 0;
        public int setu = 0;
        public int mengEr = 0;
        public int ban = 0;
        public int time = 0;
        public int grass = 0;
    }

    public GroupCounter() {
        file = new File(Autoreply.appDirectory + "properties\\GroupCount.json");
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Type type = new TypeToken<HashMap<String, GroupInfo>>() {
        }.getType();
        countMap = new Gson().fromJson(Tools.FileTool.readString(file), type);
        Autoreply.instence.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                saveData();
            }
        });
        Autoreply.instence.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                backupData();
            }
        });
    }

    public void incSpeak(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.speak;
    }

    public void incPic(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.pic;
    }

    public void incSetu(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.setu;
    }

    public void incPohaitu(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.pohai;
    }

    public void incFudu(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.repeat;
    }

    public void incRepeatBreaker(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.repeatBreak;
    }

    public void incSearchPicture(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.sp;
    }

    public void incBilibiliLink(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.biliLink;
    }

    public void incMengEr(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.mengEr;
    }

    public void incBanCount(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.ban;
    }

    public void decLife(long qq) {
        GroupInfo groupInfo = getBean(qq);
        --groupInfo.time;
    }

    public void incGrass(long qq) {
        GroupInfo groupInfo = getBean(qq);
        ++groupInfo.grass;
    }

    private GroupInfo getBean(long qq) {
        GroupInfo groupInfo = countMap.get(String.valueOf(qq));
        if (groupInfo == null) {
            groupInfo = new GroupInfo();
            countMap.put(String.valueOf(qq), groupInfo);
        }
        return groupInfo;
    }

    public String getMyCount(long qq) {
        GroupInfo groupInfo = countMap.get(String.valueOf(qq));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("你群共");
        if (groupInfo.speak != 0) {
            stringBuilder.append("水群").append(groupInfo.speak).append("句");
        }
        if (groupInfo.pic != 0) {
            stringBuilder.append("\n").append("发图").append(groupInfo.pic).append("次");
        }
        if (groupInfo.repeat != 0) {
            stringBuilder.append("\n").append("复读").append(groupInfo.repeat).append("次");
        }
        if (groupInfo.pohai != 0) {
            stringBuilder.append("\n").append("迫害").append(groupInfo.pohai).append("次");
        }
        if (groupInfo.repeatBreak != 0) {
            stringBuilder.append("\n").append("打断复读").append(groupInfo.repeatBreak).append("次");
        }
        if (groupInfo.setu != 0) {
            stringBuilder.append("\n").append("色图").append(groupInfo.setu).append("次");
        }
        if (groupInfo.sp != 0) {
            stringBuilder.append("\n").append("搜图").append(groupInfo.sp).append("次");
        }
        if (groupInfo.biliLink != 0) {
            stringBuilder.append("\n").append("bilibili链接").append(groupInfo.biliLink).append("次");
        }
        if (groupInfo.mengEr != 0) {
            stringBuilder.append("\n").append("无悔发言").append(groupInfo.mengEr).append("次");
        }
        if (groupInfo.ban != 0) {
            stringBuilder.append("\n").append("口球").append(groupInfo.ban).append("次");
        }
        if (groupInfo.time != 0) {
            stringBuilder.append("\n").append("时间").append(groupInfo.time).append("秒");
        }
        if (groupInfo.grass != 0) {
            stringBuilder.append("\n").append("为绿化贡献").append(groupInfo.grass).append("棵草");
        }
        return stringBuilder.toString();

    }

    public String getTheFirst() {
        int setu = 0;
        int pic = 0;
        int pohai = 0;
        int repeat = 0;
        int repeatBreaker = 0;
        int biliLink = 0;
        int sp = 0;
        int speak = 0;
        int mengEr = 0;
        int ban = 0;
        int time = 0;
        int grass = 0;
        String setuq = null;
        String picq = null;
        String pohaiq = null;
        String repeatq = null;
        String repeatBreakerq = null;
        String biliLinkq = null;
        String spq = null;
        String speakq = null;
        String mengErq = null;
        String banq = null;
        String timeq = null;
        String grassq = null;

        for (Entry<String, GroupInfo> entry : countMap.entrySet()) {
            GroupInfo groupInfo = entry.getValue();
            if (groupInfo.speak > speak) {
                speak = groupInfo.speak;
                speakq = entry.getKey();
            }
            if (groupInfo.pic > pic) {
                pic = groupInfo.pic;
                picq = entry.getKey();
            }
            if (groupInfo.setu > setu) {
                setu = groupInfo.setu;
                setuq = entry.getKey();
            }
            if (groupInfo.pohai > pohai) {
                pohai = groupInfo.pohai;
                pohaiq = entry.getKey();
            }
            if (groupInfo.repeat > repeat) {
                repeat = groupInfo.repeat;
                repeatq = entry.getKey();
            }
            if (groupInfo.repeatBreak > repeatBreaker) {
                repeatBreaker = groupInfo.repeatBreak;
                repeatBreakerq = entry.getKey();
            }
            if (groupInfo.biliLink > biliLink) {
                biliLink = groupInfo.biliLink;
                biliLinkq = entry.getKey();
            }
            if (groupInfo.sp > sp) {
                sp = groupInfo.sp;
                spq = entry.getKey();
            }
            if (groupInfo.mengEr > mengEr) {
                mengEr = groupInfo.mengEr;
                mengErq = entry.getKey();
            }
            if (groupInfo.ban > ban) {
                ban = groupInfo.ban;
                banq = entry.getKey();
            }
            if (groupInfo.time < time) {
                time = groupInfo.time;
                timeq = entry.getKey();
            }
            if (groupInfo.grass > grass) {
                grass = groupInfo.grass;
                grassq = entry.getKey();
            }
        }
        StringBuilder sb = new StringBuilder();
        if (speakq != null) {
            sb.append(speakq).append("水群").append(speak).append("句");
        }
        if (picq != null) {
            sb.append("\n").append(picq).append("发图").append(pic).append("张");
        }
        if (setuq != null) {
            sb.append("\n").append(setuq).append("色图").append(setu).append("次");
        }
        if (pohaiq != null) {
            sb.append("\n").append(pohaiq).append("迫害").append(pohai).append("次");
        }
        if (repeatq != null) {
            sb.append("\n").append(repeatq).append("复读").append(repeat).append("次");
        }
        if (repeatBreakerq != null) {
            sb.append("\n").append(repeatBreakerq).append("打断复读").append(repeatBreaker).append("次");
        }
        if (biliLinkq != null) {
            sb.append("\n").append(biliLinkq).append("bilibili链接").append(biliLink).append("次");
        }
        if (spq != null) {
            sb.append("\n").append(spq).append("搜图").append(sp).append("次");
        }
        if (mengErq != null) {
            sb.append("\n").append(mengErq).append("无悔发言").append(mengEr).append("次");
        }
        if (banq != null) {
            sb.append("\n").append(banq).append("口球").append(ban).append("次");
        }
        if (timeq != null) {
            sb.append("\n").append(timeq).append("时间").append(time).append("秒");
        }
        if (grassq != null) {
            sb.append("\n").append(grassq).append("为绿化贡献").append(grass).append("棵草");
        }
        return sb.toString();
    }

    private void saveData() {
        while (true) {
            try {
                Thread.sleep(60000);
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void backupData() {
        while (true) {
            try {
                Thread.sleep(86400000);
                File backup = new File(file.getAbsolutePath() + ".bak" + System.currentTimeMillis());
                FileOutputStream fos = new FileOutputStream(backup);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
