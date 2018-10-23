package com.meng.groupChat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;

public class RecordBanner {
	private int repeatCount = 0;
	private int banCount = 6;
	private String lastMessageRecieved = "";
	private long groupNum = 0;
	private boolean working = false;
	private int reverseFlag = 0;
	private int banRecorderMode = 0;
	private boolean lastStatus = false;
	private FingerPrint thisFp;
	private FingerPrint lastFp;
	private File imgFile;

	public RecordBanner(long group, int mode) {
		groupNum = group;
		banRecorderMode = mode;
	}

	public RecordBanner(long group) {
		groupNum = group;
	}

	public boolean check(long group, String msg, long QQ) throws Exception {
		boolean b = false;
		if (group == groupNum && (Autoreply.CC.getAt(msg) != 1620628713L) && (!msg.contains("禁言"))) {
			if (msg.equalsIgnoreCase("ban0")) {
				banRecorderMode = 0;
				Autoreply.sendGroupMessage(group, "取消禁言复读机");
			} else if (msg.equalsIgnoreCase("ban1")) {
				banRecorderMode = 1;
				Autoreply.sendGroupMessage(group, "复读轮盘");
			} else if (msg.equalsIgnoreCase("ban2")) {
				banRecorderMode = 2;
				Autoreply.sendGroupMessage(group, "禁言所有复读机");
			}
			float simi = getPicSimilar(msg);// 当前消息中图片和上一条消息中图片相似度
			switch (banRecorderMode) {
			case 0:

				break;
			case 1:
				if (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi))) { // 上一条消息和当前消息相同或两张图片相似度过高都是复读
					if (Autoreply.random.nextInt() % banCount == 0) {
						int time = Autoreply.random.nextInt(60) + 1;
						Autoreply.CQ.setGroupBan(group, QQ, time);
						banCount = 6;
						Autoreply.sendPrivateMessage(QQ, "你从“群复读轮盘”中获得了" + time + "秒禁言套餐");
					}
				}
				break;
			case 2:
				if (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
					int time = Autoreply.random.nextInt(60) + 1;
					Autoreply.CQ.setGroupBan(group, QQ, time);
					Autoreply.sendPrivateMessage(QQ, "你因复读获得了" + time + "秒禁言套餐");
				}
				lastMessageRecieved = msg;
				return true;
			}
			// 直到处理完当前消息之前会忽略其他消息 防止消息过多爆炸
			if (!working) {
				working = true;
				b = checkRepeatStatu(group, msg, simi);
				working = false;
			}
			lastMessageRecieved = msg;
		}
		return b;
	}

	// 复读状态
	private boolean checkRepeatStatu(long group, String msg, float simi) throws IOException {
		boolean b = false;
		if (!lastStatus && (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi)))) {
			System.out.println("复读开始");
			b = true;
			reply(group, msg, simi);
		}
		if (lastStatus && (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi)))) {
			System.out.println("复读持续中");
			b = true;
			if (banCount < 1) {
				banCount = 6;
			}
			banCount--;
		}
		if (lastStatus && !lastMessageRecieved.equals(msg) && !(isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
			System.out.println("复读结束");
			b = false;
			banCount = 6;
		}
		if (lastMessageRecieved.equals(msg) || (isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
			lastStatus = true;
		} else {
			lastStatus = false;
		}
		return b;
	}

	// 回复
	private boolean reply(long group, String msg, float simi) throws IOException {
		if (msg.contains("[CQ:image,file=")) {
			replyPic(group, msg);
		} else {
			replyText(group, msg);
		}
		return true;
	}

	// 如果是图片复读
	private void replyPic(long group, String msg) throws IOException {
		if (!msg.contains(".gif")) {
			String imgCode = Autoreply.CC.image(rePic(imgFile));
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

	// 如果是文本复读
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

	// 反转图片
	private File rePic(File file) throws IOException {
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
					b2.setRGB(w - 1 - x, y, rgb);// 左右
				} else if (reverseFlag % 3 == 1) {
					b2.setRGB(x, h - 1 - y, rgb);// 上下
				} else if (reverseFlag % 3 == 2) {
					b2.setRGB(w - 1 - x, h - 1 - y, rgb);// 上下和左右（旋转）
				}
			}
		}
		reverseFlag++;
		ImageIO.write(b2, "jpg", file);// 也可以使用png但体积太大
		return file;
	}

	// 图片相似度判断
	private float getPicSimilar(String msg) {
		CQImage cm = null;
		try {
			cm = Autoreply.CC.getCQImage(msg);
		} catch (NumberFormatException e) {
		}
		if (cm != null) {// 如果当前消息有图片则开始处理
			try {
				File files = new File(Autoreply.appDirectory + "reverse\\");
				if (!files.exists()) {
					files.mkdirs();
				}
				imgFile = cm.download(Autoreply.appDirectory + "reverse\\" + groupNum + "recr.jpg");
			} catch (IOException e) {
			}
			if (thisFp != null) {
				lastFp = thisFp;
			}
			try {
				thisFp = new FingerPrint(
						ImageIO.read(new File(Autoreply.appDirectory + "reverse\\" + groupNum + "recr.jpg")));
			} catch (IOException e) {
			}
			if (lastFp != null) {
				return thisFp.compare(lastFp);
			}
		} else {// 如果当前消息没有图片则删除临时的图片文件防止跨消息图片复读
			thisFp = null;
			lastFp = null;
			(new File(Autoreply.appDirectory + "reverse\\" + groupNum + "recr.jpg")).delete();
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
