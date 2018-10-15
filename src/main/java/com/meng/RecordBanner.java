package com.meng;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.CoolQ;
import com.sobte.cqp.jcq.message.CQCode;

public class RecordBanner {
	private int repeatCount = 0;
	private int banCount = 6;
	private String lastMessageRecieved = "";
	private long groupNum = 0;
	private CoolQ CQ;
	private boolean working = false;
	private int reverseFlag = 0;
	private int banRecorder = 0;
	private boolean lastStatus = false;
	private CQCode CC;
	private FingerPrint thisFp;
	private FingerPrint lastFp;
	private File imgFile;

	public RecordBanner(long group, CoolQ CQ, CQCode CC, int status) {
		this.CQ = CQ;
		this.CC = CC;
		groupNum = group;
		banRecorder = status;
	}

	public RecordBanner(long group, CoolQ CQ, CQCode CC) {
		this.CQ = CQ;
		this.CC = CC;
		groupNum = group;
	}

	public boolean check(long group, String msg, CQCode CC, String appdirectory, long QQ) throws Exception {
		boolean b = false;
		if (group == groupNum && (CC.getAt(msg) != 1620628713L) && (!msg.contains("禁言"))) {
			if (msg.equalsIgnoreCase("banRecorder0")) {
				banRecorder = 0;
				Autoreply.sendGroupMessage(group, "取消禁言复读机");
			} else if (msg.equalsIgnoreCase("banRecorder1")) {
				banRecorder = 1;
				Autoreply.sendGroupMessage(group, "每个复读机都有1/6的几率被禁言");
			} else if (msg.equalsIgnoreCase("banRecorder2")) {
				banRecorder = 2;
				Autoreply.sendGroupMessage(group, "禁言所有复读机");
			}

			float simi = getPicSimilar(msg, appdirectory);
			switch (banRecorder) {
			case 0:

				break;
			case 1:
				if (lastMessageRecieved.equals(msg) || simi > 0.97f) {
					if (Autoreply.random.nextInt() % banCount == 0) {
						int time = Autoreply.random.nextInt(121);
						CQ.setGroupBan(group, QQ, time);
						banCount = 6;
						Autoreply.sendPrivateMessage(QQ, "你从“群复读轮盘”中获得了" + time + "秒禁言套餐");
					}
				}
				break;
			case 2:
				if (lastMessageRecieved.equals(msg) || simi > 0.97f) {
					int time = Autoreply.random.nextInt(121);
					CQ.setGroupBan(group, QQ, time);
					Autoreply.sendPrivateMessage(QQ, "你因复读获得了" + time + "秒禁言套餐");
				}
				lastMessageRecieved = msg;
				return true;
			}

			if (!working) {
				working = true;
				b = checkRepeatStatu(group, msg, CC, appdirectory, simi);
				working = false;
			}
			lastMessageRecieved = msg;
		}
		return b;
	}

	private boolean checkRepeatStatu(long group, String msg, CQCode CC, String appdirectory, float simi)
			throws IOException {
		boolean b = false;
		if (!lastStatus && (lastMessageRecieved.equals(msg) || simi > 0.97f)) {
			System.out.println("复读开始");
			b = true;
			reply(group, msg, CC, appdirectory, simi);
		}
		if (lastStatus && (lastMessageRecieved.equals(msg) || simi > 0.97f)) {
			System.out.println("复读持续中");
			b = true;
			if (banCount < 1) {
				banCount = 6;
			}
			banCount--;
		}
		if (lastStatus && (!lastMessageRecieved.equals(msg) && simi < 0.97f)) {
			System.out.println("复读结束");
			b = false;
			banCount = 6;
		}
		if (lastMessageRecieved.equals(msg) || simi > 0.97f) {
			lastStatus = true;
		} else {
			lastStatus = false;
		}
		return b;
	}

	private void replyPic(long group, String msg, CQCode CC, String appdirectory) throws IOException {
		if (!msg.contains(".gif")) {
			String imgCode = CC.image(rePic(appdirectory, imgFile));
			String ms = new StringBuilder(msg.replaceAll("\\[CQ.*\\]", "")).reverse().toString();
			if (ms.indexOf("蓝") != -1 || ms.indexOf("藍") != -1) {
				return;
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
		} else {
			Autoreply.sendGroupMessage(group, msg);
		}
	}

	private void replyText(Long group, String msg) {
		if (repeatCount < 3) {
			Autoreply.sendGroupMessage(group, msg);
			repeatCount++;
		} else if (repeatCount == 3) {
			Autoreply.sendGroupMessage(group, "你群天天复读");
			repeatCount++;
		} else {
			String newmsg = new StringBuilder(msg).reverse().toString();
			if (newmsg.indexOf("蓝") != -1 || newmsg.indexOf("藍") != -1) {
				return;
			}
			if (newmsg.equals(msg)) {
				newmsg += " ";
			}
			Autoreply.sendGroupMessage(group, newmsg);
			repeatCount = 0;
		}
	}

	private boolean reply(long group, String msg, CQCode CC, String appdirectory, float simi) throws IOException {
		if (msg.contains("[CQ:image,file=")) {
			replyPic(group, msg, CC, appdirectory);
		} else {
			replyText(group, msg);
		}
		return true;
	}

	private File rePic(String appdirectory, File file) throws IOException {
		Image im = ImageIO.read(file);
		int w = im.getWidth(null);
		int h = im.getHeight(null);
		BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		BufferedImage b2 = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b2.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int rgb = b.getRGB(x, y);
				if (reverseFlag % 3 == 0) {
					b2.setRGB(w - 1 - x, y, rgb);
				} else if (reverseFlag % 3 == 1) {
					b2.setRGB(x, h - 1 - y, rgb);
				} else if (reverseFlag % 3 == 2) {
					b2.setRGB(w - 1 - x, h - 1 - y, rgb);
				}
			}
		}
		reverseFlag++;
		// File newwPic = new File(appdirectory + "reverse\\" + groupNum +
		// "recr.jpg");
		ImageIO.write(b2, "jpg", file);
		return file;
	}

	private float getPicSimilar(String msg, String appdirectory) throws Exception {
		CQImage cm = CC.getCQImage(msg);
		if (cm != null) {
			imgFile = cm.download(appdirectory + "reverse\\" + groupNum + "recr.jpg");
			if (thisFp != null) {
				lastFp = thisFp;
			}
			thisFp = new FingerPrint(ImageIO.read(new File(appdirectory + "reverse\\" + groupNum + "recr.jpg")));
			if (lastFp != null) {
				return thisFp.compare(lastFp);
			}
		} else {
			thisFp = null;
			lastFp = null;
			(new File(appdirectory + "reverse\\" + groupNum + "recr.jpg")).delete();
		}
		return 0;
	}

}
