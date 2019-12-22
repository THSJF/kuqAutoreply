package com.meng.picEdit;

import com.meng.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class JingShenZhiZhuQQManager {

    private long fromGroup = 0;
    private File resultImageFile;
    private File headImageFile;

    public JingShenZhiZhuQQManager(long fromGroup, long fromQQ, String msg) {
        this.fromGroup = fromGroup;
        File files = new File(Autoreply.appDirectory + "jingshenzhizhu\\");
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
        resultImageFile = new File(Autoreply.appDirectory + "jingshenzhizhu\\" + id + ".jpg");
        headImageFile = new File(Autoreply.appDirectory + "user\\" + id + ".jpg");
        boolean needRecreate = downloadPicture("http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin="
											   + id + "&;dst_uin=" + id + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC")
			| !resultImageFile.exists();
        if (needRecreate) {
            start(headImageFile);
        } else {
			if (fromGroup == -1) {
				return;
			}
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
        }
    }

    private void start(File headFile) {
        try {
            BufferedImage headImage = null;
            headImage = ImageIO.read(headFile);
            BufferedImage des1 = chgPic(rotateImage(headImage, 346), 190);
            Image baseImage = ImageIO.read(new File(Autoreply.appDirectory + "pic\\jingshenzhizhuback.png"));
            BufferedImage b = new BufferedImage(baseImage.getWidth(null), baseImage.getHeight(null),
												BufferedImage.TYPE_INT_ARGB);
            b.getGraphics().drawImage(baseImage, 0, 0, null);
            b.getGraphics().drawImage(des1, -29, 30, null);
            ImageIO.write(b, "png", resultImageFile);
			if (fromGroup == -1) {
				return;
			}
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
        } catch (Exception e) {
        }
    }

    static BufferedImage rotateImage(Image src, int angel) {
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
