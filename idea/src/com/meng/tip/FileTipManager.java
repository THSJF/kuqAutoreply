package com.meng.tip;

import java.util.HashMap;
import java.util.HashSet;

import com.meng.Autoreply;
import com.meng.Methods;

public class FileTipManager extends Thread {

    public HashSet<FileTipUploader> dataMap = new HashSet<>();

    private String[] stringsC5 = new String[]{"更新了吗", "出来打牌", "在？看看牌", "把你的打牌图给我交了"};
    private String[] strings = new String[]{"更新了吗", "出来更新"};

    public FileTipManager() {
    }

    // 新文件上传时
    public void onUploadFile(long groupNumber, long QQNumber) {
        for (FileTipUploader tftu : dataMap) {
            if (tftu.groupNumber == groupNumber && tftu.QQNumber == QQNumber) {
                tftu.fileLastUpload = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void run() {
        try {
            sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            for (FileTipUploader tftu : dataMap) {
                if (System.currentTimeMillis() - tftu.fileLastUpload > 86400000 && System.currentTimeMillis() - tftu.fileLastTipTime > 7200000) {
                    if (tftu.groupNumber == 807242547L && tftu.QQNumber == 1592608126L) {
                        Autoreply.sendMessage(tftu.groupNumber, 0, Autoreply.instence.CC.at(tftu.QQNumber) + Methods.rfa(stringsC5));
                        tftu.fileLastTipTime = System.currentTimeMillis();
                    } else {
                        Autoreply.sendMessage(tftu.groupNumber, 0, Autoreply.instence.CC.at(tftu.QQNumber) + Methods.rfa(strings));
                        tftu.fileLastTipTime = System.currentTimeMillis();
                    }
                }
            }
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
