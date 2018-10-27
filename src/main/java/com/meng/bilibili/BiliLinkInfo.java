package com.meng.bilibili;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;

public class BiliLinkInfo {

	private final String videoUrl = "www.bilibili.com/video/";
	private final String articleUrl = "www.bilibili.com/read/";

	public BiliLinkInfo() {
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
		if (msg.toLowerCase().contains(videoUrl) || res.toLowerCase().contains(videoUrl)
				|| res2.toLowerCase().contains(videoUrl) || msg.toLowerCase().contains(articleUrl)
				|| res.toLowerCase().contains(articleUrl) || res2.toLowerCase().contains(articleUrl)) {// 判断是否为哔哩哔哩链接
			String avString = "";
			String cvString = "";
			Gson gson = new Gson();
			try {
				avString = getIdString(msg, res, res2, "av");
				if (avString.equals("")) {
					cvString = getIdString(msg, res, res2, "cv");
					if (cvString.equals("")) {
						System.out.println("empty result");
						return false;
					} else {
						BiliArticleInfoJavaBean biliJson = gson
								.fromJson(
										Methods.getSourceCode("https://api.bilibili.com/x/article/viewinfo?id="
												+ cvString + "&mobi_app=pc&jsonp=jsonp"),
										BiliArticleInfoJavaBean.class);
						Autoreply.sendGroupMessage(fromGroup, biliJson.toString());
					}

				} else {
					BiliVideoInfoJavaBean biliJson = gson.fromJson(
							Methods.getSourceCode(
									"http://api.bilibili.com/archive_stat/stat?aid=" + avString + "&type=jsonp"),
							BiliVideoInfoJavaBean.class);
					Autoreply.sendGroupMessage(fromGroup, biliJson.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 读取视频信息的json并生成javabean对象
			if (!msg.contains("[CQ:share,url=")) {// 如果不是分享链接就拦截消息
				return true;
			}
		}
		return false;
	}

	public String getIdString(String text1, String text2, String text3, String type) {
		String tmp = getId(text1, type).equals("") ? getId(text2, type) : getId(text1, type);
		return tmp.equals("") ? getId(text3, type) : tmp;
	}

	// 截取id号
	private String getId(String msg, String type) {
		String result = getId(msg, type, 8);
		try {
			Integer.parseInt(result);
		} catch (Exception e) {
			result = getId(msg, type, 7);
			try {
				Integer.parseInt(result);
			} catch (Exception e1) {
				result = getId(msg, type, 6);
				try {
					Integer.parseInt(result);
				} catch (Exception e2) {
					result = getId(msg, type, 5);
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
	// msg消息 i视频id位数

	private String getId(String msg, String type, int i) {
		String tmp = "";
		try {
			tmp = msg.substring(msg.indexOf(type) + 2, msg.indexOf(type) + 2 + i);
		} catch (Exception e) {
		}
		return tmp;
	}
}
