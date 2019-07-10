package com.meng.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.tools.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class ZanManager {
    private HashSet<Long> hashSet = new HashSet<>();
    private String configPath = Autoreply.appDirectory + "configV3_zan.json";

    public ZanManager() {
        File jsonBaseConfigFile = new File(configPath);
        if (!jsonBaseConfigFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<HashSet<Long>>() {
        }.getType();
        try {
            hashSet = new Gson().fromJson(Methods.readFileToString(configPath), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendZan() {
        int ii;
        for (long l : Autoreply.instence.configManager.configJavaBean.masterList) {
            for (int i = 0; i < 10; i++) {
                ii = Autoreply.CQ.sendLike(l);
                if (ii != 0) {
                    Autoreply.sendToMaster("赞" + l + "失败,返回值" + ii);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (long l : Autoreply.instence.configManager.configJavaBean.adminList) {
            for (int i = 0; i < 10; i++) {
                ii = Autoreply.CQ.sendLike(l);
                if (ii != 0) {
                    Autoreply.sendToMaster("赞" + l + "失败,返回值" + ii);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (long l : hashSet) {
            for (int i = 0; i < 10; i++) {
                ii = Autoreply.CQ.sendLike(l);
                if (ii != 0) {
                    Autoreply.sendToMaster("赞" + l + "失败,返回值" + ii);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkAdd(long fromGroup, long fromQQ, String msg) {
        if (Autoreply.instence.configManager.isMaster(fromQQ)) {
            if (msg.startsWith("z.add")) {
                hashSet.addAll(Autoreply.instence.CC.getAts(msg));
                saveConfig();
                Autoreply.sendMessage(fromGroup, fromQQ, "已添加至赞列表");
                return true;
            }
        }
        return false;
    }

    private void saveConfig() {
        try {
            File file = new File(configPath);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(hashSet));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
