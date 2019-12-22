package com.meng.picEdit;

import com.meng.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
			f = cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg");
			startCreate(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void startCreate(File file) throws Exception {
		BufferedImage src;
		src = ImageIO.read(file);
		BufferedImage des1 = chgPic(JingShenZhiZhuQQManager.rotateImage(src, 346), 190);
		Image im = ImageIO.read(new File(Autoreply.appDirectory + "pic\\6.png"));
		BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		b.getGraphics().drawImage(im, 0, 0, null);
		b.getGraphics().drawImage(des1, -29, 30, null);
		ImageIO.write(b, "png", f);
		Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(f));
    }
	
	private BufferedImage chgPic(BufferedImage img, int newSize) {
        BufferedImage img2 = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
        img2.getGraphics().drawImage(img, 0, 0, newSize, newSize, null);
        return img2;
    }
}
