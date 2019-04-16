package com.meng.groupChat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.meng.config.javabeans.GroupConfig;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;

public class RepeaterBanner {
	private int repeatCount = 0;
	private int banCount = 6;
	private String lastMessageRecieved = "";
	private int reverseFlag = Autoreply.instence.random.nextInt(4);
	private boolean lastStatus = false;
	private FingerPrint thisFp;
	private FingerPrint lastFp;
	private File imgFile;
	public long groupNumber = 0;

	public RepeaterBanner(long groupNumber) {
		this.groupNumber = groupNumber;
	}

	public boolean check(long fromGroup, String msg, long fromQQ) {
		GroupConfig groupConfig = Autoreply.instence.configManager.configHashMap.get(fromGroup);
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
		if (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi)) {
			lastStatus = true;
		} else {
			lastStatus = false;
		}
		return b;
	}

	private boolean repeatEnd(long qq, String msg) {
		boolean b;
		b = false;
		Autoreply.instence.useCount.incRepeatBreaker(qq);
		return b;
	}

	private boolean repeatRunning(long qq, String msg) {
		boolean b;
		b = false;
		Autoreply.instence.useCount.incFudu(qq);
		banCount--;
		return b;
	}

	private boolean repeatStart(long group, long qq, String msg) throws IOException {
		boolean b;
		banCount = 6;
		Autoreply.sendMessage(0, 2856986197L, msg);
		Autoreply.instence.useCount.incFudujiguanjia(qq);
		b = true;
		reply(group, qq, msg);
		return b;
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
		if (!msg.contains(".gif")) {
			String imgCode = Autoreply.instence.CC.image(rePic(imgFile));
			String ms = new StringBuilder(msg.replaceAll("\\[CQ.*\\]", "")).reverse().toString();
			if (ms.indexOf("蓝") != -1 || ms.indexOf("藍") != -1) {
				return;
			}
			Autoreply.sendMessage(group, 0, msg.startsWith("[") ? ms + imgCode : imgCode + ms);
			repeatCount = repeatCount > 2 ? 0 : repeatCount + 1;
		} else {
			Autoreply.sendMessage(group, 0, msg);
		}
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
		if (reverseFlag == 0) {
			reverseFlag++;
			return file;
		}
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
				switch (reverseFlag % 4) {
				case 0:

					break;
				case 1:
					b2.setRGB(w - 1 - x, y, rgb);// 左右
					break;
				case 2:
					b2.setRGB(x, h - 1 - y, rgb);// 上下
					break;
				case 3:
					b2.setRGB(w - 1 - x, h - 1 - y, rgb);// 旋转
					break;
				}
			}
		}
		reverseFlag++;
		ImageIO.write(b2, "png", file);
		return file;
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
				imgFile = cm.download(Autoreply.appDirectory + "reverse\\" + System.currentTimeMillis() + "recr.jpg");
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
		return msg.replaceAll("\\[.*\\]", "");
	}

	private boolean isPicMsgRepeat(String lastMsg, String msg, float simi) {
		return getMsgText(msg).equals(getMsgText(lastMsg)) && msg.length() == lastMsg.length() && simi > 0.97f;
	}
}
