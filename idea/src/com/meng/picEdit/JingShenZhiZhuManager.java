package com.meng.picEdit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class JingShenZhiZhuManager {

    private long fromGroup = 0;
    private File f;

    public JingShenZhiZhuManager(long fromGroup, String msg) {
        this.fromGroup = fromGroup;
        CQImage cm = Autoreply.instence.CC.getCQImage(msg);
        if (cm == null) {
            return;
        }
        File files = new File(Autoreply.appDirectory + "jingshenzhizhu\\");
        if (!files.exists()) {
            files.mkdirs();
        }
        try {
            if (msg.contains(".gif")) {
                f = cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".gif");
                Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(animGif2to1(f)));
            } else {
                f = cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg");
                startCreate(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCreate(File file) {
        try {
            BufferedImage src;
            src = ImageIO.read(file);
            BufferedImage des1 = chgPic(rotate(src, 346), 190);
            Image im;
            im = ImageIO.read(new File(Autoreply.appDirectory + "pic\\6.png"));
            BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            b.getGraphics().drawImage(im, 0, 0, null);
            b.getGraphics().drawImage(des1, -29, 30, null);
            ImageIO.write(b, "png", f);
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(f));
        } catch (Exception e) {
        }
    }

    private BufferedImage rotate(Image src, int angel) {
        int srcWidth = src.getWidth(null);
        int srcHeight = src.getHeight(null);
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                srcHeight = srcHeight ^ srcWidth;
                srcWidth = srcHeight ^ srcWidth;
                srcHeight = srcHeight ^ srcWidth;
            }
        }
        double r = Math.sqrt(srcHeight * srcHeight + srcWidth * srcWidth) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel % 90) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angel % 90)) / 2;
        double angelDaltaWidth = Math.atan((double) srcHeight / srcWidth);
        double angelDaltaHeight = Math.atan((double) srcWidth / srcHeight);
        int lenDaltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaWidth));
        int lenDaltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaHeight));
        int desWidth = srcWidth + lenDaltaWidth * 2;
        int desHeight = srcHeight + lenDaltaHeight * 2;
        Rectangle rectDes = new Rectangle(new Dimension(desWidth, desHeight));
        BufferedImage res = new BufferedImage(rectDes.width, rectDes.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = res.createGraphics();
        g2.translate((rectDes.width - srcWidth) / 2, (rectDes.height - srcHeight) / 2);
        g2.rotate(Math.toRadians(angel), srcWidth / 2, srcHeight / 2);
        g2.drawImage(src, null, null);
        return res;
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
            backImage = ImageIO.read(new File(Autoreply.appDirectory + "pic\\6.png"));
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
        //      System.out.println("w1:" + gifFrame.getWidth(null) + " h1:" + gifFrame.getHeight(null) + " w2:" + frameH);
        background.getGraphics().drawImage(chgPic(rotate(gifFrame, 346), 190, (int) frameH).getScaledInstance(190, 190, Image.SCALE_SMOOTH), -29, 30, null);
        return background;
    }
}
