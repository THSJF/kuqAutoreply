package com.meng.jingshenzhizhu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class JingShenZhiZhuManager {

	long fromGroup = 0;
	String msg = "";
	File f;

	public JingShenZhiZhuManager(long fromGroup, String msg) {
		this.fromGroup = fromGroup;
		this.msg = msg;

		CQImage cm = Autoreply.instence.CC.getCQImage(msg);
		if (cm == null)
			return;
		File files = new File(Autoreply.appDirectory + "jingshenzhizhu\\");
		if (!files.exists()) {
			files.mkdirs();
		}
		if (msg.contains(".gif"))
			return;
		try {
			f = cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg");
			a(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void a(File file) {
		try {
			BufferedImage src = null;
			src = ImageIO.read(file);
			BufferedImage des1 = chgPic(Rotate(src, 346), 190);
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

	public BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src_height;
				src_height = src_width;
				src_width = temp;
			}
		}
		double r = Math.sqrt(src_height * src_height + src_width * src_width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel % 90) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel % 90)) / 2;
		double angel_dalta_width = Math.atan((double) src_height / src_width);
		double angel_dalta_height = Math.atan((double) src_width / src_height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src_width + len_dalta_width * 2;
		int des_height = src_height + len_dalta_height * 2;
		Rectangle rect_des = new Rectangle(new Dimension(des_width, des_height));
		BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = res.createGraphics();
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
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
