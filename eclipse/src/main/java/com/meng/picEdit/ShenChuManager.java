package com.meng.picEdit;

import com.meng.Autoreply;
import com.meng.tools.gifHelper.AnimatedGifEncoder;
import com.meng.tools.gifHelper.GifDecoder;
import com.sobte.cqp.jcq.entity.CQImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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
            if (msg.contains(".gif")) {
                resultImageFile = cm.download(Autoreply.appDirectory + "shenchu\\" + System.currentTimeMillis() + ".jpg");
                Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(animGif2to1(resultImageFile)));
            } else {
                resultImageFile = cm.download(Autoreply.appDirectory + "shenchu\\" + System.currentTimeMillis() + ".jpg");
                start(resultImageFile);
            }
        } catch (IOException e) {
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

    // 按比例压缩图片
    private BufferedImage chgPic(BufferedImage img, int newSize) {
        BufferedImage img2 = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
        img2.getGraphics().drawImage(img, 0, 0, newSize, newSize, null);
        return img2;
    }

    private BufferedImage chgPic(BufferedImage img, int newW, int newH) {
        BufferedImage img2 = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        img2.getGraphics().drawImage(img, 0, 0, newW, newH, null);
        return img2;
    }

    private File animGif2to1(File gifFile) throws FileNotFoundException {
        BufferedImage backImage;
        try {
            backImage = ImageIO.read(new File(Autoreply.appDirectory + "pic\\shenchuback.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        GifDecoder gifDecoder = new GifDecoder();
        FileInputStream fis = new FileInputStream(gifFile);
        gifDecoder.read(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);// start
        localAnimatedGifEncoder.setRepeat(0);// 设置生成gif的开始播放时间。0为立即开始播放
        for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
            localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
            localAnimatedGifEncoder.addFrame(processFrame(gifDecoder.getFrame(i), backImage));
        }
        localAnimatedGifEncoder.finish();
        try {
            FileOutputStream fos = new FileOutputStream(gifFile);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (Exception e) {
        }
        return gifFile;
    }

    private BufferedImage processFrame(BufferedImage gifFrame, BufferedImage background) {
        double frameH = ((double) gifFrame.getHeight(null)) / gifFrame.getWidth(null) * 190;
        background.getGraphics().drawImage(chgPic(gifFrame, 190, (int) frameH).getScaledInstance(190, 190, Image.SCALE_SMOOTH), 216, 0, null);
        return background;
    }
}
