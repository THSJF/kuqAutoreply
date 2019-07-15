package com.meng.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.config.javabeans.GroupConfig;
import com.sobte.cqp.jcq.entity.Member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BanListener {

    private HashMap<String, HashSet<Long>> sleepSet = new HashMap<>();
    ;
    private String configPath = Autoreply.appDirectory + "configV3_sleep.json";

    public BanListener() {
        File jsonBaseConfigFile = new File(configPath);
        if (!jsonBaseConfigFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<HashMap<String, HashSet<Long>>>() {
        }.getType();
        sleepSet = new Gson().fromJson(Methods.readFileToString(configPath), type);
        Autoreply.instence.threadPool.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2592000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<String, HashSet<Long>> entry : sleepSet.entrySet()) {
                    Methods.ban(Long.parseLong(entry.getKey()), entry.getValue(), 2592000);
                }
            }
        });
    }

    public void check(long fromGroup, long fromQQ, String msg) {
        long tmp = Autoreply.instence.CC.getAt(msg);
        if (msg.startsWith("夏眠")) {
            checkSleepMsg(fromGroup, fromQQ, msg);
        }
        if (fromQQ == 2482513293L && msg.startsWith("复读警察,出动!") && tmp != -1000) {
            //Autoreply.instence.CC.getAt(msg) == 3119583925L
            Methods.ban(fromGroup, tmp, 0);
        }
        if (fromQQ != 1000000) {
            return;
        }
        switch (msg) {
            case "管理员已禁止群内匿名聊天":
                onAnonymousForbid(fromGroup);
                break;
            case "管理员已允许群内匿名聊天":
                onAnonymousAllow(fromGroup);
                break;
            case "管理员开启了全员禁言，只有群主和管理员才能发言":
                onAllBan(fromGroup);
                break;
            case "管理员关闭了全员禁言":
                onAllRelease(fromGroup);
                break;
            default:
                String keyWord = "被管理员禁言";
                if (msg.contains(keyWord)) {
                    long qq = Long.parseLong(msg.substring(msg.lastIndexOf("(") + 1, msg.lastIndexOf(")")));
                    Autoreply.instence.useCount.incBanCount(qq);
                    Autoreply.instence.groupCount.incBanCount(fromGroup);
                    String timeStr = msg.substring(msg.indexOf(keyWord) + keyWord.length());
                    if (timeStr.equals("1月")) {
                        Autoreply.sendToMaster("在群" + fromGroup + "中" + qq + "被禁言一个月");
                        return;
                    }
                    String[] time = timeStr.split("(天|小时)");
                    int minute = 0;
                    switch (time.length) {
                        case 1:
                            if (timeStr.contains("天")) {
                                minute = Integer.parseInt(time[0]) * 1440;
                            } else if (timeStr.contains("小时")) {
                                minute = Integer.parseInt(time[0]) * 60;
                            } else if (timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0].replace("分钟", ""));
                            }
                            break;
                        case 2:
                            if (timeStr.contains("天") && timeStr.contains("小时")) {
                                minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1]) * 60;
                            } else if (timeStr.contains("天") && timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1].replace("分钟", ""));
                            } else if (timeStr.contains("小时") && timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1].replace("分钟", ""));
                            }
                            break;
                        case 3:
                            minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2].replace("分钟", ""));
                            break;
                    }
                    onBan(fromGroup, qq, minute);
                } else if (msg.contains("被管理员解除禁言")) {
                    long qq = Long.parseLong(msg.substring(msg.lastIndexOf("(") + 1, msg.lastIndexOf(")")));
                    onRelease(fromGroup, qq);
                }
        }
    }

    private void onAnonymousAllow(long fromGroup) {
        Autoreply.sendToMaster(fromGroup + "允许匿名");
    }

    private void onAnonymousForbid(long fromGroup) {
        Autoreply.sendToMaster(fromGroup + "禁止匿名");
    }

    private void onAllBan(long fromGroup) {
        Autoreply.sendToMaster(fromGroup + "全员禁言");
    }

    private void onAllRelease(long fromGroup) {
        Autoreply.sendToMaster(fromGroup + "解除全员禁言");
    }

    private void onBan(long fromGroup, long banQQ, int minute) {
        if (banQQ == Autoreply.CQ.getLoginQQ()) {
            Autoreply.instence.configManager.getGroupConfig(fromGroup).reply = false;
            Autoreply.sendToMaster("在群" + fromGroup + "被禁言,关闭了回复群");
            Autoreply.instence.configManager.saveConfig();
        } else {
            Autoreply.sendToMaster("在群" + fromGroup + "中" + banQQ + "被禁言" + minute + "min");
        }
    }

    private void onRelease(long fromGroup, long banQQ) {
        if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
            return;
        }
        Autoreply.sendToMaster("在群" + fromGroup + "中" + banQQ + "无罪释放");
        if (checkSleepMsg(fromGroup, banQQ)) {
            return;
        }
        Autoreply.sendMessage(fromGroup, banQQ, "恭喜出狱");
    }

    public boolean checkSleepMsg(long fromGroup, long fromQQ) {
        boolean contain = false;
        HashSet<Long> hse = sleepSet.get(String.valueOf(fromGroup));
        if (hse == null) {
            return false;
        }
        for (long qq : hse) {
            if (qq == fromQQ) {
                contain = true;
                break;
            }
        }
        if (contain) {
            Methods.ban(fromGroup, fromQQ, 2592000);
        }
        return contain;
    }

    private void checkSleepMsg(long fromGroup, long fromQQ, String msg) {
        GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
        if (groupConfig == null || !groupConfig.isSleep() || Autoreply.instence.configManager.isNotReplyWord(msg)) {
            return;
        }
        Member qqInfo = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, fromQQ);
        if (!Autoreply.instence.configManager.isAdmin(fromQQ) && qqInfo.getAuthority() == 1) {
            return;
        }
        if (msg.equals("夏眠结束")) {
            HashSet<Long> hashSet = sleepSet.get(String.valueOf(fromGroup));
            if (hashSet != null) {
                for (long qq : hashSet) {
                    Methods.ban(fromGroup, qq, 0);
                }
            }
            return;
        }
        if (msg.equals("夏眠列表")) {
            Autoreply.sendMessage(fromGroup, fromQQ, new Gson().toJson(sleepSet));
            return;
        }
        long tmp = Autoreply.instence.CC.getAt(msg);
        if (msg.startsWith("夏眠结束[CQ:at,qq=")) {
            HashSet<Long> hs = sleepSet.get(String.valueOf(fromGroup));
            if (hs == null) {
                Autoreply.sendMessage(fromGroup, fromQQ, "本群没有夏眠名单");
            } else {
                hs.remove(tmp);
                Methods.ban(fromGroup, tmp, 0);
            }
            saveConfig();
            return;
        }
        if (msg.startsWith("夏眠[CQ:at,qq=")) {
            HashSet<Long> hs = sleepSet.get(String.valueOf(fromGroup));
            if (hs == null) {
                hs = new HashSet<>();
                hs.add(tmp);
                sleepSet.put(String.valueOf(fromGroup), hs);
            } else {
                hs.add(tmp);
            }
            Methods.ban(fromGroup, tmp, 2592000);
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            File file = new File(configPath);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(sleepSet));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
