package com.meng.counter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UserCounter {
    private HashMap<String, UserInfo> countMap = new HashMap<>();
    private File file;

    public UserCounter() {
        file = new File(Autoreply.appDirectory + "properties\\UserCount.json");
        if (!file.exists()) {
            saveData();
        }
        try {
            Type type = new TypeToken<HashMap<String, UserInfo>>() {
            }.getType();
            countMap = new Gson().fromJson(Methods.readFileToString(file.getAbsolutePath()), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            while (true) {
                sleep(60000);
                saveData();
            }
        }).start();
        new Thread(() -> {
            while (true) {
                sleep(86400000);
                backupData();
            }
        }).start();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void incSetu(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.setu;
    }

    public void incPohaitu(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.pohai;
    }

    public void incFudujiguanjia(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.repeatStart;
    }

    public void incFudu(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.repeat;
    }

    public void incRepeatBreaker(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.repeatBreak;
    }

    public void incSearchPicture(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.sp;
    }

    public void incBilibiliLink(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.biliLink;
    }

    public void incSpeak(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(String.valueOf(qq), userInfo);
        }
        ++userInfo.speak;
    }

    public String getMyCount(long qq) {
        UserInfo userInfo = countMap.get(String.valueOf(qq));
        StringBuilder stringBuilder = new StringBuilder();
        if (qq != Autoreply.CQ.getLoginQQ()) {
            stringBuilder.append("你共");
        }
        if (userInfo.speak != 0) {
            stringBuilder.append("水群").append(userInfo.speak).append("句");
        }
        if (userInfo.repeat != 0) {
            stringBuilder.append("\n").append("复读").append(userInfo.repeat).append("次");
        }
        if (userInfo.pohai != 0) {
            stringBuilder.append("\n").append("迫害").append(userInfo.pohai).append("次");
        }
        if (userInfo.repeatStart != 0) {
            stringBuilder.append("\n").append("带领复读").append(userInfo.repeatStart).append("次");
        }
        if (userInfo.repeatBreak != 0) {
            stringBuilder.append("\n").append("打断复读").append(userInfo.repeatBreak).append("次");
        }
        if (userInfo.setu != 0) {
            stringBuilder.append("\n").append("色图").append(userInfo.setu).append("次");
        }
        if (userInfo.sp != 0) {
            stringBuilder.append("\n").append("搜图").append(userInfo.sp).append("次");
        }
        if (userInfo.biliLink != 0) {
            stringBuilder.append("\n").append("发送哔哩哔哩链接").append(userInfo.biliLink).append("次");
        }
        return stringBuilder.toString();

    }

    public String getTheFirst() {
        int setu = 0;
        int pohai = 0;
        int repeatStart = 0;
        int repeat = 0;
        int repeatBreaker = 0;
        int biliLink = 0;
        int sp = 0;
        int speak = 0;
        String setuq = null;
        String pohaiq = null;
        String repeatStartq = null;
        String repeatq = null;
        String repeatBreakerq = null;
        String biliLinkq = null;
        String spq = null;
        String speakq = null;

        for (Entry<String, UserInfo> entry : countMap.entrySet()) {
            if (entry.getKey().equals(String.valueOf(Autoreply.CQ.getLoginQQ()))) {
                continue;
            }
            UserInfo userInfo = entry.getValue();
            if (userInfo.speak > speak) {
                speak = userInfo.speak;
                speakq = entry.getKey();
            }
            if (userInfo.setu > setu) {
                setu = userInfo.setu;
                setuq = entry.getKey();
            }
            if (userInfo.pohai > pohai) {
                pohai = userInfo.pohai;
                pohaiq = entry.getKey();
            }
            if (userInfo.repeatStart > repeatStart) {
                repeatStart = userInfo.repeatStart;
                repeatStartq = entry.getKey();
            }
            if (userInfo.repeat > repeat) {
                repeat = userInfo.repeat;
                repeatq = entry.getKey();
            }
            if (userInfo.repeatBreak > repeatBreaker) {
                repeatBreaker = userInfo.repeatBreak;
                repeatBreakerq = entry.getKey();
            }
            if (userInfo.biliLink > biliLink) {
                biliLink = userInfo.biliLink;
                biliLinkq = entry.getKey();
            }
            if (userInfo.sp > sp) {
                sp = userInfo.sp;
                spq = entry.getKey();
            }
        }
        StringBuilder sb = new StringBuilder();
        if (speakq != null) {
            sb.append(speakq).append("水群").append(speak).append("句");
        }
        if (setuq != null) {
            sb.append("\n").append(setuq).append("色图").append(setu).append("次");
        }
        if (pohaiq != null) {
            sb.append("\n").append(pohaiq).append("迫害").append(pohai).append("次");
        }
        if (repeatStartq != null) {
            sb.append("\n").append(repeatStartq).append("带领复读").append(repeatStart).append("次");
        }
        if (repeatq != null) {
            sb.append("\n").append(repeatq).append("复读").append(repeat).append("次");
        }
        if (repeatBreakerq != null) {
            sb.append("\n").append(repeatBreakerq).append("打断复读").append(repeatBreaker).append("次");
        }
        if (biliLinkq != null) {
            sb.append("\n").append(biliLinkq).append("发送哔哩哔哩链接").append(biliLink).append("次");
        }
        if (spq != null) {
            sb.append("\n").append(spq).append("搜图").append(sp).append("次");
        }
        return sb.toString();
    }

    private void saveData() {
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

    private void backupData() {
        try {
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
