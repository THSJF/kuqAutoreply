package com.meng.barcode;

import com.google.zxing.*;
import com.meng.*;
import com.meng.barcode.*;
import com.meng.tools.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BarcodeManager {

    public BarcodeManager() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg, File[] images) {
        try {
            if (enc(fromGroup, fromQQ, msg, images)) {
                return true;
            }
            if (dec(fromGroup, fromQQ, images)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean enc(long fromGroup, long fromQQ, String msg, File[] imageFiles) throws Exception {
        File barcode;
        if (msg.startsWith("生成QR ")) {
            File files = new File(Autoreply.instence.createdImageFolder);
            if (!files.exists()) {
                files.mkdirs();
            }
            barcode = new File(Autoreply.instence.createdImageFolder + Autoreply.instence.random.nextInt() + ".png");
            ImageIO.write(BarcodeUtils.createQRCode(msg.substring(5)), "png", barcode);
        } else /*if (msg.startsWith("生成QR[CQ:image")) {
            File files = new File(Autoreply.instence.createdImageFolder);
            if (!files.exists()) {
                files.mkdirs();
            }
            barcode = new File(Autoreply.instence.createdImageFolder + Autoreply.instence.random.nextInt() + ".png");
            if (!Autoreply.instence.fileTypeUtil.getFileType(imageFiles[0]).equals("gif")) {
                barcode = new File(Autoreply.instence.createdImageFolder + Autoreply.instence.random.nextInt() + ".png");
                ImageIO.write(
                        BarcodeUtils.createAwesome(
                                msg.substring(9),
                                500,
                                0.3f,
                                0xffffffff,
                                ImageIO.read(imageFiles[0]))
                        , "png", barcode);

            }
        } else */if (msg.startsWith("生成PDF417 ")) {
            File files = new File(Autoreply.instence.createdImageFolder);
            if (!files.exists()) {
                files.mkdirs();
            }
            barcode = new File(Autoreply.instence.createdImageFolder + Autoreply.instence.random.nextInt() + ".png");
            ImageIO.write(BarcodeUtils.createPDF417(msg.substring(9)), "png", barcode);
        } else {
            return false;
        }
        Autoreply.instence.picSearchManager.sendMsg(fromGroup, fromQQ, Autoreply.instence.CC.image(barcode));
        return true;
    }

    private boolean dec(long fromGroup, long fromQQ, File[] imageFiles) {
        if (imageFiles == null) {
            return false;
        }
        for (File barcode : imageFiles) {
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
