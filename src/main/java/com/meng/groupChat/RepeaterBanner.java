package com.meng.groupChat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
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
		GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
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
		String ms = new StringBuilder(msg.replaceAll("\\[CQ.*\\]", "")).reverse().toString();
		if (ms.indexOf("蓝") != -1 || ms.indexOf("藍") != -1) {
			return;
		}
		if (imgFile.getName().contains(".gif")) {
			String imgCode = Autoreply.instence.CC.image(reverseGIF(imgFile));
			Autoreply.sendMessage(group, 0, msg.startsWith("[") ? ms + imgCode : imgCode + ms);
			// Autoreply.sendMessage(group, 0, msg);
		} else {
			String imgCode = Autoreply.instence.CC.image(rePic(imgFile));
			Autoreply.sendMessage(group, 0, msg.startsWith("[") ? ms + imgCode : imgCode + ms);
		}
		repeatCount = repeatCount > 2 ? 0 : repeatCount + 1;
		reverseFlag++;
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
		Image im = ImageIO.read(file);
		int w = im.getWidth(null);
		int h = im.getHeight(null);
		BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		BufferedImage b2 = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		b2.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

		switch (reverseFlag % 4) {
		case 0:
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					b2.setRGB(w - 1 - x, y, b.getRGB(x, y));// 镜之国
				}
			}
			break;
		case 1:
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					b2.setRGB(x, h - 1 - y, b.getRGB(x, y));// 天地
				}
			}
			break;
		case 2:
			int halfH = h / 2;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					b2.setRGB(x, y < halfH ? y + halfH : y - halfH, b.getRGB(x, y));// 天壤梦弓
				}
			}
			break;
		case 3:
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					b2.setRGB(w - 1 - x, h - 1 - y, b.getRGB(x, y));// Reverse_Hierarchy
				}
			}
			break;
		}
		ImageIO.write(b2, "png", file);
		return file;
	}

	public File reverseGIF(File gifFile) throws FileNotFoundException {
		GifDecoder gifDecoder = new GifDecoder();
		FileInputStream fis = new FileInputStream(gifFile);
		gifDecoder.read(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
		localAnimatedGifEncoder.start(baos);// start
		localAnimatedGifEncoder.setRepeat(0);// 设置生成gif的开始播放时间。0为立即开始播放
		BufferedImage bImage = gifDecoder.getFrame(0);
		float fa = (float) bImage.getHeight() / (gifDecoder.getFrameCount());
		switch (reverseFlag % 4) {
		case 0:// 镜之国
			for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
				localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
				localAnimatedGifEncoder.addFrame(spell1(gifDecoder.getFrame(i), bImage));
			}
			break;
		case 1:// 天地
			for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
				localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
				localAnimatedGifEncoder.addFrame(spell2(gifDecoder.getFrame(i), bImage));
			}
			break;
		case 2:// 天壤梦弓
			for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
				localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
				localAnimatedGifEncoder.addFrame(
						spell3(gifDecoder.getFrame(i), (int) (fa * (gifDecoder.getFrameCount() - i)), bImage));
			}
			break;
		case 3:// Reverse Hierarchy
			for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
				localAnimatedGifEncoder.setDelay(gifDecoder.getDelay(i));
				localAnimatedGifEncoder.addFrame(spell4(gifDecoder.getFrame(i), bImage));
			}
			break;
		}

		localAnimatedGifEncoder.finish();
		try {
			FileOutputStream fos = new FileOutputStream(gifFile);
			baos.writeTo(fos);
			baos.flush();
			fos.flush();
			baos.close();
			fos.close();
		} catch (Exception e) {
		}
		return gifFile;
	}

	public BufferedImage spell1(BufferedImage current, BufferedImage cache) {
		int w = current.getWidth(null);
		int h = current.getHeight(null);
		BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = current.getRGB(x, y);
				if (i != 0) {
					bmp.setRGB(w - 1 - x, y, i);// 镜之国
				}
			}
		}
		cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		return bmp;
	}

	public BufferedImage spell2(BufferedImage current, BufferedImage cache) {
		int w = current.getWidth(null);
		int h = current.getHeight(null);
		BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = current.getRGB(x, y);
				if (i != 0) {
					bmp.setRGB(x, h - 1 - y, i);// 天地
				}
			}
		}
		cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		return bmp;
	}

	public BufferedImage spell3(BufferedImage current, int px, BufferedImage cache) {
		int w = current.getWidth(null);
		int h = current.getHeight(null);
		BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		bmp.getGraphics().drawImage(spell3_at1(cache, px).getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		for (int y = 0; y < h; y++) {// 天壤梦弓
			for (int x = 0; x < w; x++) {
				int i = current.getRGB(x, y);
				if (i != 0) {
					if (y < h - px) {
						bmp.setRGB(x, y + px, current.getRGB(x, y));
					} else {
						bmp.setRGB(x, y - (h - px), current.getRGB(x, y));
					}
				}
			}
		}
		cache.getGraphics().drawImage(spell3_at1(bmp, -px).getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		return bmp;
	}

	public BufferedImage spell3_at1(BufferedImage cache, int px) {
		int w = cache.getWidth(null);
		int h = cache.getHeight(null);
		BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		if (px > 0) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (y < h - px) {
						bmp.setRGB(x, y + px, cache.getRGB(x, y));
					} else {
						bmp.setRGB(x, y - (h - px), cache.getRGB(x, y));
					}
				}
			}
		} else {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (y >= -px) {
						bmp.setRGB(x, y + px, cache.getRGB(x, y));
					} else {
						bmp.setRGB(x, y + h + px, cache.getRGB(x, y));
					}
				}
			}
		}
		return bmp;
	}

	public BufferedImage spell4(BufferedImage current, BufferedImage cache) {
		int w = current.getWidth(null);
		int h = current.getHeight(null);
		BufferedImage bmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		bmp.getGraphics().drawImage(cache.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = current.getRGB(x, y);
				if (i != 0) {
					bmp.setRGB(w - 1 - x, h - 1 - y, current.getRGB(x, y));// Reverse_Hierarchy
				}
			}
		}
		cache.getGraphics().drawImage(bmp.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		return bmp;
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
				if (msg.contains(".gif")) {
					imgFile = cm
							.download(Autoreply.appDirectory + "reverse\\" + System.currentTimeMillis() + "recr.gif");
				} else {
					imgFile = cm
							.download(Autoreply.appDirectory + "reverse\\" + System.currentTimeMillis() + "recr.jpg");
				}
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
