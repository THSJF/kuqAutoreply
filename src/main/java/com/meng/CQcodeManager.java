package com.meng;

public class CQcodeManager {

	public boolean check(long fromGroup, String msg) {
		if (checkMusic(fromGroup, msg)) {
			return true;
		} else if (checkLink(fromGroup, msg)) {
			return true;
		}
		return false;
	}

	// 分享音乐
	private boolean checkMusic(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:music")) {
			int i = Autoreply.instence.random.nextInt(3);
			switch (i) {
			case 0:
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.music(22636603, "163", false));
				break;
			case 1:
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.music(103744845, "qq", false));
				break;
			case 2:
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.music(103744852, "qq", false));
				break;
			}
			return true;
		}
		return false;
	}

	// 有人发送分享链接时
	private boolean checkLink(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:share,url=")) {// 分享链接的特征
			// 截取相关字符串
			// String link = msg.substring(msg.indexOf("http"),
			// msg.indexOf(",title="));
			// String title = msg.substring(msg.indexOf("title=") + 6,
			// msg.indexOf(",content"));
			// String describe = msg.substring(msg.indexOf("content=") + 8,
			// msg.indexOf(",image"));
			String picture = msg.substring(msg.lastIndexOf("http"), msg.lastIndexOf("]"));
			// 发送消息
			// Autoreply.sendMessage(fromGroup,0,
			// "标题:" + title + "\n链接:" + link + "\n封面图:" + picture + "\n描述:" +
			// describe);
			Autoreply.sendMessage(fromGroup, 0, "封面图:" + picture);
			return true;
		}
		return false;
	}

}
