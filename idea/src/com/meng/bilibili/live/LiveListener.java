package com.meng.bilibili.live;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.bilibili.main.SpaceToLiveJavaBean;
import com.meng.tools.Methods;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class LiveListener implements Runnable {

    public ArrayList<LivePerson> livePerson = new ArrayList<>();
    private boolean loadFinish = false;
    public static boolean liveStart = true;
    private HashMap<String, String> liveTimeMap = new HashMap<>();

    public LiveListener(ConfigManager configManager) {
        System.out.println("直播检测启动中");
        Autoreply.instence.threadPool.execute(() -> {
            for (PersonInfo cb : configManager.configJavaBean.personInfo) {
                LiveListener.this.addPerson(cb);
            }
            loadFinish = true;
            System.out.println("直播检测启动完成");
        });
        File liveTimeFile = new File(Autoreply.appDirectory + "liveTime.json");
        if (!liveTimeFile.exists()) {
            saveLiveTime();
        }
        try {
            Type token = new TypeToken<HashMap<String, String>>() {
            }.getType();
            liveTimeMap = new Gson().fromJson(Methods.readFileToString(liveTimeFile.getAbsolutePath()), token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPerson(PersonInfo personInfo) {
        if (personInfo.bliveRoom == -1) {
            return;
        }
        if (personInfo.bliveRoom == 0) {
            if (personInfo.bid != 0) {
                SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + personInfo.bid), SpaceToLiveJavaBean.class);
                if (sjb.data.roomid == 0) {
                    personInfo.bliveRoom = -1;
                    Autoreply.instence.configManager.saveConfig();
                    return;
                }
                personInfo.bliveRoom = sjb.data.roomid;
                Autoreply.instence.configManager.saveConfig();
                System.out.println("检测到用户" + personInfo.name + "(" + personInfo.bid + ")的直播间" + personInfo.bliveRoom);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
        livePerson.add(new LivePerson(personInfo.name, personInfo.bid, personInfo.bliveRoom, personInfo.autoTip));
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!loadFinish) {
                    Thread.sleep(1000);
                    continue;
                }
                if (liveStart) {
                    for (LivePerson lPerson : livePerson) {
                        SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + lPerson.bid), SpaceToLiveJavaBean.class);
                        boolean living = sjb.data.liveStatus == 1;
                        lPerson.liveUrl = sjb.data.url;
                        lPerson.living = living;
                        if (lPerson.flag != 0) {
                            if (living && lPerson.needStartTip) {
                                lPerson.flag = 1;
                            } else if (living && !lPerson.needStartTip) {
                                lPerson.flag = 2;
                            } else if (!living && !lPerson.needStartTip) {
                                lPerson.flag = 3;
                            } else if (!living && lPerson.needStartTip) {
                                lPerson.flag = 4;
                            }
                        }
                        sendMsg(lPerson);
                        Thread.sleep(2000);
                    }
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                System.out.println("直播监视出了问题：");
                e.printStackTrace();
            }
        }
    }

    public void saveNow() {
        liveStart = false;
        for (LivePerson lp : livePerson) {
            if (!lp.living) {
                continue;
            }
            lp.flag = 3;
            try {
                sendMsg(lp);
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMsg(LivePerson p) {
        switch (p.flag) {
            case 0:
                if (p.living) {
                    p.liveStartTimeStamp = System.currentTimeMillis() / 1000;
                    p.needStartTip = false;
                    p.flag = 2;
                } else {
                    p.needStartTip = true;
                    p.flag = 4;
                }
                break;
            case 1:
                tipStart(p);
                p.liveStartTimeStamp = System.currentTimeMillis() / 1000;
                p.needStartTip = false;
                // 勿添加break;
            case 2:
                p.living = true;
                break;
            case 3:
                tipFinish(p);
                p.needStartTip = true;
                if (liveTimeMap.get(p.name) == null) {
                    liveTimeMap.put(p.name, String.valueOf(System.currentTimeMillis() / 1000 - p.liveStartTimeStamp));
                    p.liveStartTimeStamp = 0;
                } else {
                    long time = Long.parseLong(liveTimeMap.get(p.name));
                    time += System.currentTimeMillis() / 1000 - p.liveStartTimeStamp;
                    liveTimeMap.put(p.name, String.valueOf(time));
                }
                saveLiveTime();
                // 勿添加break;
            case 4:
                p.living = false;
                break;
        }
    }

    private void tipStart(LivePerson p) {

        //	switch (p.getName()) {
        //	case "台长":
        // Autoreply.sendMessage(859561731L, 0, "想看台混矫正器");
        //		break;
        //	}
        // try {
        // if (p.autoTip) {
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieSunny, "发发发");
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieLuna, "发发发");
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieStar, "发发发");
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // Autoreply.sendMessage(0, 2856986197L, p.getName() + "开始直播" +
        // p.roomId);
        Autoreply.sendMessage(1023432971, 0, p.name + "开始直播" + p.roomId, true);
        ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromName(p.name).tipIn;
        for (long group : groupList) {
            Autoreply.sendMessage(group, 0, p.name + "开始直播" + p.roomId, true);
        }
    }

    private void tipFinish(LivePerson p) {
        //	switch (p.getName()) {
        //	case "台长":
        // Autoreply.sendMessage(859561731L, 0, "呜呜呜");
        //		break;
        //	}
        // try {
        // if (p.autoTip) {
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieSunny, "呜呜呜");
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieLuna, "呜呜呜");
        // Autoreply.instence.naiManager.sendDanmaku(p.roomId,
        // Autoreply.instence.naiManager.cookieStar, "呜呜呜");
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        Autoreply.sendMessage(1023432971, 0, p.name + "直播结束" + p.roomId, true);
        ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromName(p.name).tipIn;
        for (long group : groupList) {
            Autoreply.sendMessage(group, 0, p.name + "直播结束" + p.roomId, true);
        }
    }

    public String getLiveTimeCount() {
        Iterator<Entry<String, String>> iterator = liveTimeMap.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append(cal(Integer.parseInt(entry.getValue()))).append("\n");
        }
        return stringBuilder.toString();
    }

    private String cal(int second) {
        int h = 0;
        int min = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp > 60) {
                min = temp / 60;
            }
        } else {
            min = second / 60;
        }
        if (h == 0) {
            return min + "分";
        } else {
            return h + "时" + min + "分";
        }
    }

    private void saveLiveTime() {
        try {
            File file = new File(Autoreply.appDirectory + "liveTime.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(liveTimeMap));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
