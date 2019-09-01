package com.meng.picEdit;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import com.meng.Autoreply;

public class ShenChuQQManager {

    private long fromGroup = 0;
    private File resultImageFile;
    private File headImageFile;

    public ShenChuQQManager(long fromGroup, long fromQQ, String msg) {
        this.fromGroup = fromGroup;
        File files = new File(Autoreply.appDirectory + "shenchu\\");
        if (!files.exists()) {
            files.mkdirs();
        }
        File files2 = new File(Autoreply.appDirectory + "user\\");
        if (!files2.exists()) {
            files2.mkdirs();
        }
        long id = Autoreply.instence.CC.getAt(msg);
        if (id == -1000 || id == -1) {
            id = fromQQ;
        }
        resultImageFile = new File(Autoreply.appDirectory + "shenchu\\" + id + ".jpg");
        headImageFile = new File(Autoreply.appDirectory + "user\\" + id + ".jpg");
        boolean needRecreate = downloadPicture("http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin=" + id + "&;dst_uin=" + id + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC")
                | !resultImageFile.exists();
        if (needRecreate) {
            start(headImageFile);
        } else {
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
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

    private boolean downloadPicture(String urlList) {
        URL url;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            if (dataInputStream.available() == headImageFile.length()) {
                return false;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(headImageFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
