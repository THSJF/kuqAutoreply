package com.meng;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

public class BilibiliTest {
	public BilibiliTest() {
		// TODO Auto-generated constructor stub
	}

	public boolean check(long fromGroup, String msg) throws KeyManagementException, NoSuchAlgorithmException {
		if (msg.indexOf("bilibili.com/video/av") != -1 && msg.indexOf("[CQ:share,url=") == -1) {
			String avString = getAv(msg);
			if (avString.equals("")) {
				return false;
			}
			Gson gson = new Gson();
			BilibiliJson bilibiliJson = gson.fromJson(
					MainSwitch.open("http://api.bilibili.com/archive_stat/stat?aid=" + avString + "&type=jsonp"),
					BilibiliJson.class);
			Autoreply.sendGroupMessage(fromGroup, bilibiliJson.toString());
			return true;
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
