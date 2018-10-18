package com.meng;

import com.google.gson.Gson;

public class BiliVideoInfo {
	public BiliVideoInfo() {
		// TODO Auto-generated constructor stub
	}

	public boolean check(long fromGroup, String msg) {
		try {
			String res = Methods.getRealUrl(msg.substring(msg.indexOf("http"), msg.indexOf(",text=")));
			if (msg.contains("www.bilibili.com/video/") || res.contains("www.bilibili.com/video/")) {// 判断是否为哔哩哔哩视频链接
				String avString = getAv(res);
				if (avString.equals("")) {
					return false;
				}
				Gson gson = new Gson();
				// 读取视频信息的json并生成javabean对象
				BiliVideoInfoJavaBean bilibiliJson = gson.fromJson(
						Methods.openUrlWithHttps(
								"http://api.bilibili.com/archive_stat/stat?aid=" + avString + "&type=jsonp"),
						BiliVideoInfoJavaBean.class);
				Autoreply.sendGroupMessage(fromGroup, bilibiliJson.toString());
				if (!msg.contains("[CQ:share,url=") && !msg.contains("[CQ:rich,url=")) {// 如果不是分享链接就拦截消息
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
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
		return msg.substring(msg.indexOf("av") + 2, msg.indexOf("av") + 2 + i);
	}

}
