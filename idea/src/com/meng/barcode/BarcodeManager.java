package com.meng.barcode;

import java.io.File;
import javax.imageio.ImageIO;

import com.google.zxing.Result;
import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class BarcodeManager {

    public BarcodeManager() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg, File[] images) {
        try {
            if (enc(fromGroup, fromQQ, msg)) {
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

    private boolean enc(long fromGroup, long fromQQ, String msg) throws Exception {
        File barcode;
        if (msg.startsWith("生成QR ")) {
            File files = new File(Autoreply.instence.createdImageFolder);
            if (!files.exists()) {
                files.mkdirs();
            }
            barcode = new File(Autoreply.instence.createdImageFolder + Autoreply.instence.random.nextInt() + ".png");
            ImageIO.write(BarcodeUtils.createQRCode(msg.substring(5)), "png", barcode);
        } else if (msg.startsWith("生成PDF417 ")) {
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
                Autoreply.instence.picSearchManager.sendMsg(fromGroup, fromQQ, "二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
                return true;
            }
        }
        return false;
    }

}
