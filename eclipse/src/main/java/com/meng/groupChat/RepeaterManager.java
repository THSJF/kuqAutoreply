package com.meng.groupChat;

import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.messageProcess.*;
import com.meng.picProcess.*;
import com.meng.tools.*;
import java.io.*;
import java.util.*;

public class RepeaterManager {

    private HashMap<Long, RepeaterBanner> repeaters = new HashMap<>();
	private PicReverser picReverser=new PicReverser();
    public RepeaterManager() {

    }

	public void addRepeater(long group) {
		repeaters.put(group, new RepeaterBanner(group));
	}

    // 遍历集合查看是否需要复读
    public boolean check(long group, long qq, String msg, File[] imageFiles) {
        RepeaterBanner repeaterBanner = repeaters.get(group);
        if (repeaterBanner == null) {
            repeaterBanner = new RepeaterBanner(group);
            addRepeater(group);
        }
        return repeaterBanner.check(group, qq, msg, imageFiles);
    }

	class RepeaterBanner {
		private int repeatCount = 0;
		private int banCount = 6;
		private String lastMessageRecieved = "";
		private int reverseFlag = Autoreply.instence.random.nextInt(4);
		private boolean lastStatus = false;
		private File[] thisFp;
		private File[] lastFp;
		long groupNumber = 0;
		private WarnMessageProcessor warnMessageProcessor;

		public RepeaterBanner(long groupNumber) {
			this.groupNumber = groupNumber;
			warnMessageProcessor = new WarnMessageProcessor();
		}

		public boolean check(long fromGroup, long fromQQ, String msg, File[] imageFiles) {
			GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
			if (groupConfig == null) {
				return false;
			}
			if (warnMessageProcessor.check(fromGroup, fromQQ, msg)) {
				return true;
			}
			if (msg.contains("~转账") || msg.contains("～转账")) {
				return true;
			}
			boolean b = false;
			try {
				if (msg.contains("禁言") || fromGroup != groupNumber) {
					return true;
				}
				float simi = 0;
				if (thisFp != null) {
					lastFp = thisFp;
				}
				thisFp = imageFiles;
				if (thisFp != null && lastFp != null && thisFp.length == lastFp.length) {
					for (int iii = 0; iii < imageFiles.length; ++iii) {
						float f = picReverser.getPicSimilar(thisFp[iii], lastFp[iii]);
						if (simi > f) {
							simi = f;
						}	
					}
				}	
				switch (groupConfig.repeatMode) {
					case 0:

						break;
					case 1:
						if (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi)) {
							if (Autoreply.instence.random.nextInt() % banCount == 0) {
								int time = Autoreply.instence.random.nextInt(60) + 1;
								banCount = 6;
								if (Tools.CQ.ban(fromGroup, fromQQ, time)) {
									Autoreply.sendMessage(0, fromQQ, String.format("你从“群复读轮盘”中获得了%d秒禁言套餐", time));
								}
							}
						}
						break;
					case 2:
						if (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi)) {
							int time = Autoreply.instence.random.nextInt(60) + 1;
							if (Tools.CQ.ban(fromGroup, fromQQ, time)) {
								Autoreply.sendMessage(0, fromQQ, String.format("你因复读获得了%d秒禁言套餐", time));
							}
						}
						lastMessageRecieved = msg;
						return true;
				}
				b = checkRepeatStatu(fromGroup, fromQQ, msg, imageFiles, simi);
				lastMessageRecieved = msg;
				return b;
			} catch (Exception e) {

			}
			return false;
		}

		// 复读状态
		private boolean checkRepeatStatu(long group, long qq, String msg, File[] imageFiles, float simi) {
			boolean b = false;
			if (!lastStatus && (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
				b = repeatStart(group, qq, msg, imageFiles);
			}
			if (lastStatus && (lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi))) {
				b = repeatRunning(group, qq, msg);
			}
			if (lastStatus && !lastMessageRecieved.equals(msg) && !isPicMsgRepeat(lastMessageRecieved, msg, simi)) {
				b = repeatEnd(group, qq, msg);
			}
			lastStatus = lastMessageRecieved.equals(msg) || isPicMsgRepeat(lastMessageRecieved, msg, simi);
			return b;
		}

		private boolean repeatEnd(long group, long qq, String msg) {
			Autoreply.instence.useCount.incRepeatBreaker(qq);
			Autoreply.instence.groupCount.incRepeatBreaker(group);
			return false;
		}

		private boolean repeatRunning(long group, long qq, String msg) {
			Autoreply.instence.useCount.incFudu(qq);
			Autoreply.instence.groupCount.incFudu(group);
			banCount--;
			return false;
		}

		private boolean repeatStart(final long group, final long qq, final String msg, final File[] imageFiles) {
			banCount = 6;
			Autoreply.instence.useCount.incFudujiguanjia(qq);
			Autoreply.instence.groupCount.incFudu(group);
			Autoreply.instence.threadPool.execute(new Runnable() {
					@Override
					public void run() {
						RepeaterBanner.this.reply(group, qq, msg, imageFiles);
					}
				});
			Autoreply.instence.useCount.incFudu(Autoreply.CQ.getLoginQQ());
			return true;
		}

		// 回复
		private boolean reply(long group, long qq, String msg, File[] imageFiles) {
			if (msg.contains("蓝") || msg.contains("藍")) {
				return true;
			}
			if (imageFiles == null) {
				replyText(group, qq, msg);
			} else {
				try {
					replyPic(group, qq, msg, imageFiles);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		// 如果是图片复读
		private void replyPic(long group, long qq, String msg, File[] imageFiles) throws IOException {
			int index = 0;
			StringBuilder stringBuilder = new StringBuilder(msg);
			for (File imageFile : imageFiles) {
				index = msg.indexOf("[CQ:image,file=", index);
				if (Autoreply.instence.fileTypeUtil.getFileType(imageFile).equals("gif")) {
					stringBuilder.replace(index, index + 52, new StringBuilder(Autoreply.instence.CC.image(picReverser.reverseGIF(imageFile, reverseFlag))).reverse().toString());
				} else {
					stringBuilder.replace(index, index + 52, new StringBuilder(Autoreply.instence.CC.image(picReverser.reversePic(imageFile, reverseFlag))).reverse().toString());
				}
				index += 52;
			}
			Autoreply.sendMessage(group, 0, stringBuilder.reverse().toString());
			repeatCount = repeatCount > 2 ? 0 : repeatCount + 1;
			reverseFlag++;
		}

		// 如果是文本复读
		private void replyText(Long group, long qq, String msg) {
			if (msg.contains("此生无悔入东方")) {
				Autoreply.sendMessage(group, 0, msg);
				return;
			}
			if (repeatCount < 3) {
				Autoreply.sendMessage(group, 0, msg);
				repeatCount++;
			} else if (repeatCount == 3) {
				Autoreply.sendMessage(group, 0, "你群天天复读");
				repeatCount++;
			} else {
				String newmsg = new StringBuilder(msg).reverse().toString();
				Autoreply.sendMessage(group, 0, newmsg.equals(msg) ? newmsg + " " : newmsg);
				repeatCount = 0;
			}
		}

		private String getMsgText(String msg) {
			return msg.replaceAll("\\[.*]", "");
		}

		private boolean isPicMsgRepeat(String lastMsg, String msg, float simi) {
			return getMsgText(msg).equals(getMsgText(lastMsg)) && msg.length() == lastMsg.length() && simi > 0.97f;
		}
	}
}

