package com.meng.barcode;

import com.google.zxing.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;

public class BarcodeManager {

    public BarcodeManager() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg, File[] images) {
		if (images == null) {
            return false;
        }
        for (File barcode : images) {
            Result result = BarcodeUtils.decodeImage(barcode);
            if (result != null) {
                String barResult = result.getText();
                if (barResult.startsWith("https://qm.qq.com/cgi-bin/qm/qr?k=")) {
                    String html = Tools.Network.getSourceCode(barResult);
                    int flag = html.indexOf("var rawuin = ") + "var rawuin = ".length();
                    String groupNum = html.substring(flag, html.indexOf(";", flag));
                    Autoreply.instence.picSearchManager.sendMsg(fromGroup, fromQQ, "群号为:" + groupNum);
                } else {
                    Autoreply.instence.picSearchManager.sendMsg(fromGroup, fromQQ, "二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
                }
                return true;
            }
        }
        return false;
    }
}
