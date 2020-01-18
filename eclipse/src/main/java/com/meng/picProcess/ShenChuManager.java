package com.meng.picProcess;

import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import com.meng.*;

public class ShenChuManager {

    public ShenChuManager() { 
        File files = new File(Autoreply.appDirectory + "shenchu\\");
        if (!files.exists()) {
            files.mkdirs();
        }
    }

	public File create(File headFile) {
		return create(headFile, System.currentTimeMillis());
	}

    public File create(File headFile, long id) {
		try {
			File retFile = new File(Autoreply.appDirectory + "shenchu\\" + id + ".jpg");
			BufferedImage src;
			src = ImageIO.read(headFile);
			BufferedImage des1 = new BufferedImage(228, 228, BufferedImage.TYPE_INT_ARGB);
			des1.getGraphics().drawImage(src, 0, 0, 228, 228, null);
			Image im = ImageIO.read(new File(Autoreply.appDirectory + "pic\\shenchuback.png"));
			BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			b.getGraphics().drawImage(im, 0, 0, null);
			b.getGraphics().drawImage(des1, 216, -20, null);
			ImageIO.write(b, "png", retFile); 
			return retFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
}
