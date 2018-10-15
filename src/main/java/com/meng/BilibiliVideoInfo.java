package com.meng;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

public class BilibiliVideoInfo {
	public BilibiliVideoInfo() {
		// TODO Auto-generated constructor stub
	}

	public boolean check(long fromGroup, String msg) throws KeyManagementException, NoSuchAlgorithmException {
		if (msg.indexOf("bilibili.com/video/av") != -1) {
			String avString = getAv(msg);
			if (avString.equals("")) {
				return false;
			}
			Gson gson = new Gson();
			BilibiliVideoInfoJavaBean bilibiliJson = gson.fromJson(
					Methods.open("http://api.bilibili.com/archive_stat/stat?aid=" + avString + "&type=jsonp"),
					BilibiliVideoInfoJavaBean.class);
			Autoreply.sendGroupMessage(fromGroup, bilibiliJson.toString());
			if (msg.indexOf("[CQ:share,url=") == -1) {
				return true;
			}
		}
		return false;
	}

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

	private String getAv(String msg, int i) {
		return msg.substring(msg.indexOf("av") + 2, msg.indexOf("av") + 2 + i);
	}
}
