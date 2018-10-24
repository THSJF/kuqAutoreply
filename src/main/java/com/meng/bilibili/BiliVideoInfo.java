package com.meng.bilibili;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meng.Autoreply;
import com.meng.Methods;

public class BiliVideoInfo {
	public BiliVideoInfo() {
		// TODO Auto-generated constructor stub
	}

	public boolean check(long fromGroup, String msg) {
		String res = "";
		String res2 = "";
		try {
			res = Methods.getRealUrl(msg.substring(msg.indexOf("http"), msg.indexOf(",text=")));
		} catch (Exception e) {
		}
		try {
			res2 = Methods.getRealUrl(msg.substring(msg.indexOf("http"), msg.indexOf(",title=")));
		} catch (Exception e) {
		}
		if (msg.toLowerCase().contains("www.bilibili.com/video/")
				|| res.toLowerCase().contains("www.bilibili.com/video/")
				|| res2.toLowerCase().contains("www.bilibili.com/video/")) {// 判断是否为哔哩哔哩视频链接
			String avString = "";
			try {
				avString = getAv(msg).equals("") ? getAv(res) : getAv(msg);
				if (avString.equals("")) {
					avString = getAv(res2);
					if (avString.equals("")) {
						return false;
					}
				}
			} catch (Exception e) {
			}
			Gson gson = new Gson();
			// 读取视频信息的json并生成javabean对象
			BiliVideoInfoJavaBean bilibiliJson;
			try {
				bilibiliJson = gson.fromJson(
						Methods.openUrlWithHttps(
								"http://api.bilibili.com/archive_stat/stat?aid=" + avString + "&type=jsonp"),
						BiliVideoInfoJavaBean.class);
				Autoreply.sendGroupMessage(fromGroup, bilibiliJson.toString());
			} catch (JsonSyntaxException | KeyManagementException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (!msg.contains("[CQ:share,url=")) {// 如果不是分享链接就拦截消息
				return true;
			}
		}
		return false;
	}

	// 截取av号
	private String getAv(String msg) {
		String result = getAv(msg, 8);
		try {
			Integer.parseInt(result);
		} catch (Exception e) {
			result = getAv(msg, 7);
			try {
				Integer.parseInt(result);
			} catch (Exception e1) {
				result = getAv(msg, 6);
				try {
					Integer.parseInt(result);
				} catch (Exception e2) {
					result = getAv(msg, 5);
					try {
						Integer.parseInt(result);
					} catch (Exception e3) {
						result = "";
					}
				}
			}
		}
		return result;
	}

	// msg消息 i视频aid位数
	private String getAv(String msg, int i) {
		String tmp = "";
		try {
			tmp = msg.substring(msg.indexOf("av") + 2, msg.indexOf("av") + 2 + i);
		} catch (Exception e) {
		}
		return tmp;
	}

}
