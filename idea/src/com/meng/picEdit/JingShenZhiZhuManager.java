package com.meng.picEdit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		if (msg.contains(".gif")) {
			return;
		}
		try {
			f = cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg");
			startCreate(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startCreate(File file) {
		try {
			BufferedImage src = null;
			src = ImageIO.read(file);
			BufferedImage des1 = chgPic(rotate(src, 346), 190);
			Image im = null;
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
				srcHeight=srcHeight^srcWidth;
				srcWidth=srcHeight^srcWidth;
				srcHeight=srcHeight^srcWidth;
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
	public BufferedImage chgPic(BufferedImage img, int newSize) {
		BufferedImage img2 = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		img2.getGraphics().drawImage(img, 0, 0, newSize, newSize, null);
		return img2;
	}

}
