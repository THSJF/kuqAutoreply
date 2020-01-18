package com.meng.picProcess;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.meng.*;

public class JingShenZhiZhuManager {

	public JingShenZhiZhuManager() {
        File files = new File(Autoreply.appDirectory + "jingshenzhizhu\\");
        if (!files.exists()) {
            files.mkdirs();
        }
    }

	public File create(File headFile) {
		return create(headFile, System.currentTimeMillis());
	}

    public File create(File headFile, long id) {
    	try {
    		File retFile = new File(Autoreply.appDirectory + "jingshenzhizhu\\" + id + ".jpg");
			BufferedImage src;
			src = ImageIO.read(headFile);
			BufferedImage des1 = chgPic(rotatePic(src, 346), 190);
			Image im = ImageIO.read(new File(Autoreply.appDirectory + "pic\\6.png"));
			BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			b.getGraphics().drawImage(im, 0, 0, null);
			b.getGraphics().drawImage(des1, -29, 30, null);
			ImageIO.write(b, "png", retFile);
			return retFile;
    	} catch (IOException e) {
    		return null;
		}
	}

	private BufferedImage rotatePic(Image src, int angel) {
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

	private BufferedImage chgPic(BufferedImage img, int newSize) {
        BufferedImage img2 = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
        img2.getGraphics().drawImage(img, 0, 0, newSize, newSize, null);
        return img2;
    }
}
