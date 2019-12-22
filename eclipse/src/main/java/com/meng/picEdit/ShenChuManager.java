package com.meng.picEdit;

import com.meng.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ShenChuManager {

    private long fromGroup = 0;
    private File resultImageFile;

    public ShenChuManager(long fromGroup, String msg) {
        this.fromGroup = fromGroup;
        CQImage cm = Autoreply.instence.CC.getCQImage(msg);
        if (cm == null) {
            return;
        }
        File files = new File(Autoreply.appDirectory + "shenchu\\");
        if (!files.exists()) {
            files.mkdirs();
        }
        try {
			resultImageFile = cm.download(Autoreply.appDirectory + "shenchu\\" + System.currentTimeMillis() + ".jpg");
			start(resultImageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(File headFile) {
        try {
            BufferedImage src;
            src = ImageIO.read(headFile);
            BufferedImage des1 = new BufferedImage(228, 228, BufferedImage.TYPE_INT_ARGB);
            des1.getGraphics().drawImage(src, 0, 0, 228, 228, null);
            Image im = ImageIO.read(new File(Autoreply.appDirectory + "pic\\shenchuback.png"));
            BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            b.getGraphics().drawImage(im, 0, 0, null);
            b.getGraphics().drawImage(des1, 216, -20, null);
            ImageIO.write(b, "png", resultImageFile);
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
        } catch (Exception e) {
        }
    }
}
