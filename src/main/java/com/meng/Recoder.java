package com.meng;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.message.CQCode;

public class Recoder {

	private int repeatCount = 0;
	private String lastMessage = "";
	private String lastReply = "";
	private long groupNum = 0;
	private FingerPrint thisFp;
	private FingerPrint lastFp;
	private float tf = 0;

	public Recoder(long group) {
		groupNum = group;
	}

	public boolean check(long group, String msg, CQCode CC, String appdirectory) throws IOException {
		boolean b = false;
		if (group == groupNum && (!lastReply.equals(msg)) && (CC.getAt(msg) != 1620628713L)) {
			CQImage cm = CC.getCQImage(msg);
			if (cm != null) {
				String imgCode = CC
						.image(rePic(appdirectory, cm.download(appdirectory + "reverse\\" + groupNum + "rectmp.jpg")));
				if (thisFp != null) {
					lastFp = thisFp;
				}
				thisFp = new FingerPrint(ImageIO.read(new File(appdirectory + "reverse\\" + groupNum + "rectmp.jpg")));
				tf = 0;
				if (lastFp != null) {
					tf = thisFp.compare(lastFp);
				}
				if (tf > 0.9f) {
					String ms = new StringBuilder(msg.replaceAll("\\[CQ.*\\]", "")).reverse().toString();
					if (ms.indexOf("蓝") != -1 || ms.indexOf("藍") != -1) {
						return true;
					}
					if (msg.indexOf("[") == 0) {
						Autoreply.sendGroupMessage(group, ms + imgCode);
					} else {
						Autoreply.sendGroupMessage(group, imgCode + ms);
					}
					repeatCount++;
					if (repeatCount > 3) {
						repeatCount = 0;
					}
					lastReply = msg;
					b = true;
				}

			} else {
				thisFp = null;
				lastFp = null;
				if (lastMessage.equals(msg)) {
					if (repeatCount < 3) {
						Autoreply.sendGroupMessage(group, msg);
						repeatCount++;
					} else if (repeatCount == 3) {
						Autoreply.sendGroupMessage(group, "你群天天复读");
						repeatCount++;
					} else {
						String newmsg = new StringBuilder(msg).reverse().toString();
						if (newmsg.indexOf("蓝") != -1 || newmsg.indexOf("藍") != -1) {
							return true;
						}
						if (newmsg.equals(msg)) {
							newmsg += " ";
						}
						Autoreply.sendGroupMessage(group, newmsg);
						repeatCount = 0;
					}
					lastReply = msg;
					b = true;
				}
			}

			if (!(lastMessage.equals(msg) || tf < 0.9f)) {
				lastReply = "";
				System.out.println("break");
			}
			tf=0;
			lastMessage = msg;
		}
		return b;
	}

	private File rePic(String appdirectory, File file) throws IOException {
		Image im = ImageIO.read(file);
		int w = im.getWidth(null);
		int h = im.getHeight(null);
		BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		BufferedImage b2 = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b2.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		for (int x = 0; x < h; x++)
			for (int y = 0; y < w; y++) {
				int rgb = b.getRGB(y, x);
				b2.setRGB(w - 1 - y, x, rgb);
			}
		File newwPic = new File(appdirectory + "reverse\\" + groupNum + "recr.jpg");
		ImageIO.write(b2, "jpg", newwPic);
		return newwPic;
	}

}
