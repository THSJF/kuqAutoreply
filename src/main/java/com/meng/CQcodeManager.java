package com.meng;

import java.io.File;
import java.io.IOException;

public class CQcodeManager {

	public boolean check(long fromGroup, String msg) {
		if (checkSign(fromGroup, msg)) {
			return true;
		} else if (checkMusic(fromGroup, msg)) {
			return true;
		} else if (checkLink(fromGroup, msg)) {
			return true;
		}
		return false;
	}

	// 签到
	private boolean checkSign(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:sign")) {
			if (fromGroup == 424838564L || fromGroup == 312342896L) {
				try {
					Autoreply.sendGroupMessage(fromGroup, "签到成功 这是你的签到奖励"
							+ Autoreply.CC.image(new File(Autoreply.appDirectory + "pic/qiaodaodnf.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Autoreply.sendGroupMessage(fromGroup, "image:pic/qiandao.jpg");
			}
			return true;
		}
		return false;
	}

	// 分享音乐
	private boolean checkMusic(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:music")) {
			int i = Autoreply.random.nextInt(3);
			switch (i) {
			case 0:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(22636603, "163", false));
				break;
			case 1:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(103744845, "qq", false));
				break;
			case 2:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(103744852, "qq", false));
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
			// Autoreply.sendGroupMessage(fromGroup,
			// "标题:" + title + "\n链接:" + link + "\n封面图:" + picture + "\n描述:" +
			// describe);
			Autoreply.sendGroupMessage(fromGroup, "封面图:" + picture);
			return true;
		}
		return false;
	}

}
