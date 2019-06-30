package com.meng.groupChat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import com.meng.Autoreply;
import com.meng.config.javabeans.GroupConfig;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.Member;

public class RepeaterBanner {
    private int repeatCount = 0;
    private int banCount = 6;
    private String lastMessageRecieved = "";
    private int reverseFlag = Autoreply.instence.random.nextInt(4);
    private boolean lastStatus = false;
    private FingerPrint thisFp;
    private FingerPrint lastFp;
    private File imgFile;
    long groupNumber = 0;

    public RepeaterBanner(long groupNumber) {
        this.groupNumber = groupNumber;
    }

    public boolean check(long fromGroup, String msg, long fromQQ) {
        GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
        if (groupConfig == null) {
            return false;
        }
        boolean b = false;
        try {
            if (Autoreply.instence.CC.getAt(msg) == 1620628713L || msg.contains("禁言") || fromGroup != groupNumber) {
                return true;
            }
            float simi = getPicSimilar(msg);// 当前消息中图片和上一条消息中图片相似度
            switch (groupConfig.repeatMode) {
                case 0:

                    break;
                case 1:
                    Member qqInfo = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                    if (qqInfo.getAuthority() != 2 && qqInfo.getAuthority() != 3) {
                        break;
                    }
                    if (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi))) { // 上一条消息和当前消息相同或两张图片相似度过高都是复读
                        if (Autoreply.instence.random.nextInt() % banCount == 0) {
                            int time = Autoreply.instence.random.nextInt(60) + 1;
                            Autoreply.CQ.setGroupBan(fromGroup, fromQQ, time);
                            banCount = 6;
                            Autoreply.sendMessage(0, fromQQ, "你从“群复读轮盘”中获得了" + time + "秒禁言套餐");
                        }
                    }
                    break;
                case 2:
                    Member qqInfo2 = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                    if (qqInfo2.getAuthority() != 2 && qqInfo2.getAuthority() != 3) {
                        break;
                    }
                    if (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
                        int time = Autoreply.instence.random.nextInt(60) + 1;
                        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, time);
                        Autoreply.sendMessage(0, fromQQ, "你因复读获得了" + time + "秒禁言套餐");
                    }
                    lastMessageRecieved = msg;
                    return true;
            }
            b = checkRepeatStatu(fromGroup, fromQQ, msg, simi);
            lastMessageRecieved = msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    // 复读状态
    private boolean checkRepeatStatu(long group, long qq, String msg, float simi) throws IOException {
        boolean b = false;
        if (!lastStatus && (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
            b = repeatStart(group, qq, msg);
        }
        if (lastStatus && (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
            b = repeatRunning(qq, msg);
        }
        if (lastStatus && !lastMessageRecieved.equals(msg) && !isPicMsgRepeat(lastMessageRecieved, msg, simi)) {
            b = repeatEnd(qq, msg);
        }
        lastStatus = lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi);
        return b;
    }

    private boolean repeatEnd(long qq, String msg) {
        Autoreply.instence.useCount.incRepeatBreaker(qq);
        return false;
    }

    private boolean repeatRunning(long qq, String msg) {
        Autoreply.instence.useCount.incFudu(qq);
        banCount--;
        return false;
    }

    private boolean repeatStart(long group, long qq, String msg) throws IOException {
        banCount = 6;
        Autoreply.instence.useCount.incFudujiguanjia(qq);
        reply(group, qq, msg);
        Autoreply.instence.useCount.incFudu(Autoreply.CQ.getLoginQQ());
        return true;
    }

    // 回复
    private boolean reply(long group, long qq, String msg) throws IOException {
        if (msg.contains("[CQ:image,file=")) {
            replyPic(group, qq, msg);
        } else {
            replyText(group, qq, msg);
        }
        return true;
    }

    // 如果是图片复读
    private void replyPic(long group, long qq, String msg) throws IOException {
        String ms = new StringBuilder(msg.replaceAll("\\[CQ.*]", "")).reverse().toString();
        if (ms.contains("蓝") || ms.contains("藍")) {
            return;
        }
        if (imgFile.getName().contains(".gif")) {
            String imgCode = Autoreply.instence.CC.image(reverseGIF(imgFile));
            Autoreply.sendMessage(group, 0, msg.startsWith("[") ? ms + imgCode : imgCode + ms);
            // Autoreply.sendMessage(group, 0, msg);
        } else {
            String imgCode = Autoreply.instence.CC.image(rePic(imgFile));
            Autoreply.sendMessage(group, 0, msg.startsWith("[") ? ms + imgCode : imgCode + ms);
        }
        repeatCount = repeatCount > 2 ? 0 : repeatCount + 1;
        reverseFlag++;
    }

    // 如果是文本复读
    private void replyText(Long group, long qq, String msg) {
        if (repeatCount < 3) {
            Autoreply.sendMessage(group, 0, msg);
            repeatCount++;
        } else if (repeatCount == 3) {
            Autoreply.sendMessage(group, 0, "你群天天复读");
            repeatCount++;
        } else {
            String newmsg = new StringBuilder(msg).reverse().toString();
            if (newmsg.contains("蓝") || newmsg.contains("藍")) {
                return;
            }
            Autoreply.sendMessage(group, 0, newmsg.equals(msg) ? newmsg + " " : newmsg);
            repeatCount = 0;
        }
    }

    // 反转图片
    private File rePic(File file) throws IOException {
        Image im = ImageIO.read(file);
        int w = im.getWidth(null);
        int h = im.getHeight(null);
        BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        b.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        BufferedImage b2 = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        b2.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

        switch (reverseFlag % 4) {
            case 0:
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        b2.setRGB(w - 1 - x, y, b.getRGB(x, y));// 镜之国
                    }
                }
                break;
            case 1:
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        b2.setRGB(x, h - 1 - y, b.getRGB(x, y));// 天地
                    }
                }
                break;
            case 2:
                int halfH = h / 2;
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        b2.setRGB(x, y < halfH ? y + halfH : y - halfH, b.getRGB(x, y));// 天壤梦弓
                    }
                }
                break;
            case 3:
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        b2.setRGB(w - 1 - x, h - 1 - y, b.getRGB(x, y));// Reverse_Hierarchy
                    }
                }
                break;
        }
        ImageIO.write(b2, "png", file);
        return file;
    }

    private File reverseGIF(File gifFile) throws FileNotFoundException {
        GifDecoder gifDecoder = new GifDecoder();
        FileInputStream fis = new FileInputStream(gifFile);
        gifDecoder.read(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);// start
        localAnimatedGifEncoder.setRepeat(0);// 设置生成gif的开始播放时间。0为立即开始播放
        BufferedImage cacheImage = gifDecoder.getFrame(0);
        float fa = (float) cacheImage.getHeight() / (gifDecoder.getFrameCount());
        switch (reverseFlag % 4) {
            case 0:// 镜之国
                cacheImage = spell1(gifDecoder.getFrame(0));
                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
                    localAnimatedGifEncoder.addFrame(spell1(gifDecoder.getFrame(i), cacheImage));
                }
                break;
            case 1:// 天地
                cacheImage = spell2(gifDecoder.getFrame(0));
                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
                    localAnimatedGifEncoder.addFrame(spell2(gifDecoder.getFrame(i), cacheImage));
                }
                break;
            case 2:// 天壤梦弓
                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
                    localAnimatedGifEncoder.addFrame(
                            spell3(gifDecoder.getFrame(i), (int) (fa * (gifDecoder.getFrameCount() - i)), cacheImage));
                }
                break;
            case 3:// Reverse Hierarchy
                cacheImage = spell4(gifDecoder.getFrame(0));
                for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                    localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
                    localAnimatedGifEncoder.addFrame(spell4(gifDecoder.getFrame(i), cacheImage));
                }
                break;
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

    private BufferedImage spell1(BufferedImage current, BufferedImage cache) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = current.getRGB(x, y);
                bmp.setRGB(w - 1 - x, y, i);// 镜之国
            }
        }
        cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        return bmp;
    }

    private BufferedImage spell1(BufferedImage current) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = current.getRGB(x, y);
                if (i != 0) {
                    bmp.setRGB(w - 1 - x, y, i);// 镜之国
                }
            }
        }
        return bmp;
    }

    private BufferedImage spell2(BufferedImage current, BufferedImage cache) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                bmp.setRGB(x, h - 1 - y, current.getRGB(x, y));// 天地
            }
        }
        cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        return bmp;
    }

    private BufferedImage spell2(BufferedImage current) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                bmp.setRGB(x, h - 1 - y, current.getRGB(x, y));// 天地
            }
        }
        return bmp;
    }

    private BufferedImage spell3(BufferedImage current, int px, BufferedImage cache) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bmp.getGraphics().drawImage(spell3_at1(cache, px).getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        spell3Process(current, px, w, h, bmp);
        cache.getGraphics().drawImage(spell3_at1(bmp, -px).getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        return bmp;
    }

    private BufferedImage spell3_at1(BufferedImage cache, int px) {
        int w = cache.getWidth(null);
        int h = cache.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int j;
        if (px > 0) {
            spell3Process(cache, px, w, h, bmp);
        } else {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    j = y + px;
                    if (j >= 0) {
                        bmp.setRGB(x, j, cache.getRGB(x, y));
                    } else {
                        bmp.setRGB(x, j + h, cache.getRGB(x, y));
                    }
                }
            }
        }
        return bmp;
    }

    private void spell3Process(BufferedImage cache, int px, int w, int h, BufferedImage bmp) {
        int j;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                j = y + px;
                if (j < h) {
                    bmp.setRGB(x, j, cache.getRGB(x, y));
                } else {
                    bmp.setRGB(x, j - h, cache.getRGB(x, y));
                }
            }
        }
    }

    private BufferedImage spell4(BufferedImage current, BufferedImage cache) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        spell4Process(current, w, h, bmp);
        cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        return bmp;
    }

    private BufferedImage spell4(BufferedImage current) {
        int w = current.getWidth(null);
        int h = current.getHeight(null);
        BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        spell4Process(current, w, h, bmp);
        return bmp;
    }

    private void spell4Process(BufferedImage current, int w, int h, BufferedImage bmp) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                bmp.setRGB(w - 1 - x, h - 1 - y, current.getRGB(x, y));// Reverse_Hierarchy
            }
        }
    }

    // 图片相似度判断
    private float getPicSimilar(String msg) {
        try {
            CQImage cm = Autoreply.instence.CC.getCQImage(msg);
            if (cm != null) {// 如果当前消息有图片则开始处理
                File files = new File(Autoreply.appDirectory + "reverse\\");
                if (!files.exists()) {
                    files.mkdirs();
                }
                if (msg.contains(".gif")) {
                    imgFile = cm
                            .download(Autoreply.appDirectory + "reverse\\" + System.currentTimeMillis() + "recr.gif");
                } else {
                    imgFile = cm
                            .download(Autoreply.appDirectory + "reverse\\" + System.currentTimeMillis() + "recr.jpg");
                }
                if (thisFp != null) {
                    lastFp = thisFp;
                }
                thisFp = new FingerPrint(ImageIO.read(imgFile));
                if (lastFp != null) {
                    return thisFp.compare(lastFp);
                }
            } else {// 如果当前消息没有图片则删除临时的图片文件防止跨消息图片复读
                thisFp = null;
                lastFp = null;
                imgFile.delete();
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private String getMsgText(String msg) {
        return msg.replaceAll("\\[.*]", "");
    }

    private boolean isPicMsgRepeat(String lastMsg, String msg, float simi) {
        return getMsgText(msg).equals(getMsgText(lastMsg)) && msg.length() == lastMsg.length() && simi > 0.97f;
    }
}
