package com.meng.bilibili;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;

public class BiliArticleInfo {
	public BiliArticleInfo() {
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
		if (msg.toLowerCase().contains("www.bilibili.com/read") || res.toLowerCase().contains("www.bilibili.com/read")
				|| res2.toLowerCase().contains("www.bilibili.com/read")) {// 判断是否为哔哩哔哩文章链接
			String avString = "";
			try {
				avString = getCv(msg).equals("") ? getCv(res) : getCv(msg);
				if (avString.equals("")) {
					avString = getCv(res2);
					if (avString.equals("")) {
						System.out.println("empty result");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			// 读取视频信息的json并生成javabean对象
			try {
				BiliArticleInfoJavaBean biliJson = gson.fromJson(Methods.getSourceCode(
						"https://api.bilibili.com/x/article/viewinfo?id=" + avString + "&mobi_app=pc&jsonp=jsonp"),
						BiliArticleInfoJavaBean.class);
				Autoreply.sendGroupMessage(fromGroup, biliJson.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!msg.contains("[CQ:share,url=")) {// 如果不是分享链接就拦截消息
				return true;
			}
		}
		return false;
	}

	// 截取cv号
	private String getCv(String msg) {
		String result = getCv(msg, 8);
		try {
			Integer.parseInt(result);
		} catch (Exception e) {
			result = getCv(msg, 7);
			try {
				Integer.parseInt(result);
			} catch (Exception e1) {
				result = getCv(msg, 6);
				try {
					Integer.parseInt(result);
				} catch (Exception e2) {
					result = getCv(msg, 5);
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
	private String getCv(String msg, int i) {
		String tmp = "";
		try {
			tmp = msg.substring(msg.indexOf("cv") + 2, msg.indexOf("cv") + 2 + i);
		} catch (Exception e) {
		}
		return tmp;
	}

}
