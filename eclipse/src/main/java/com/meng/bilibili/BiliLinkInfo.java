package com.meng.bilibili;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.Methods;

public class BiliLinkInfo {

	private final String videoUrl = "www.bilibili.com/video/";
	private final String videoUrl2 = "b23.tv/av";
	private final String articleUrl = "www.bilibili.com/read/";
	private final String liveUrl = "live.bilibili.com/";

	public BiliLinkInfo() {
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		String subedUrl = null;
		int ind1 = msg.indexOf(",text=");
		int ind2 = msg.indexOf(",title=");
		if (ind1 != -1) {
			subedUrl = msg.substring(msg.indexOf("http"), ind1);
		} else if (ind2 != -1) {
			subedUrl = msg.substring(msg.indexOf("http"), ind2);
		} else {
			subedUrl = msg;
		}
		String result = null;
		if (subedUrl.contains(videoUrl) || subedUrl.toLowerCase().contains(videoUrl2)) {
			result = processVideo(getVideoId(subedUrl));
		} else if (subedUrl.contains(articleUrl)) {
			result = processArtical(getArticalId(subedUrl));
		} else if (subedUrl.contains(liveUrl)) {
			result = processLive(getLiveId(subedUrl));
		}
		if (result != null) {
			Autoreply.sendMessage(fromGroup, 0, result);
			if (!msg.contains("[CQ:share,url=")) {// 如果不是分享链接就拦截消息
				return true;
			}
		}
		return false;
	}

	private String processVideo(String id) {
		VideoInfoBean videoInfoBean = new Gson().fromJson(
				Methods.getSourceCode("http://api.bilibili.com/archive_stat/stat?aid=" + id + "&type=jsonp"),
				VideoInfoBean.class);
		String vidInf = videoInfoBean.toString();
		String html = Methods.getSourceCode("https://www.bilibili.com/video/av" + id);
		int index = html.indexOf("\"pubdate\":") + "\"pubdate\":".length();
		int end = html.indexOf(",\"ctime\"", index);
		long stamp = Long.parseLong(html.substring(index, end));
		int days = (int) ((System.currentTimeMillis() - stamp * 1000) / 86400000);
		if (days == 0) {
			vidInf += "24小时内发布," + videoInfoBean.data.view + "次播放。";
		} else {
			vidInf += days + "天前发布，平均每天" + (Float.parseFloat(videoInfoBean.data.view) / days) + "次播放。";
		}
		return vidInf;
	}

	private String processArtical(String id) {
		return new Gson().fromJson(
				Methods.getSourceCode(
						"https://api.bilibili.com/x/article/viewinfo?id=" + id + "&mobi_app=pc&jsonp=jsonp"),
				ArticleInfoBean.class).toString();
	}

	private String processLive(String id) {
		String json = Methods
				.getSourceCode("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + id);
		String userName = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject().get("info")
				.getAsJsonObject().get("uname").getAsString();
		String html = Methods.getSourceCode("https://live.bilibili.com/" + id);
		String jsonInHtml = html.substring(html.indexOf("{\"roomInitRes\":"), html.lastIndexOf("}") + 1);
		JsonObject data = new JsonParser().parse(jsonInHtml).getAsJsonObject().get("baseInfoRes").getAsJsonObject()
				.get("data").getAsJsonObject();
		String roomTitle = data.get("title").getAsString();
		String part = data.get("parent_area_name").getAsString() + "-" + data.get("area_name").getAsString();
		String tags = data.get("tags").getAsString();
		return "房间号:" + id + "\n主播:" + userName + "\n房间标题:" + roomTitle + "\n分区:" + part + "\n标签:" + tags;
	}

	private String getVideoId(String url) {
		int ind = url.indexOf("?");
		if (ind == -1) {
			return url.substring(url.indexOf("av") + 2);
		} else {
			return url.substring(url.indexOf("av") + 2, ind);
		}
	}

	private String getArticalId(String url) {
		return url.substring(url.indexOf("cv") + 2);
	}

	private String getLiveId(String url) {
		int ind = url.indexOf("?");
		if (ind != -1) {
			return url.substring(url.indexOf(liveUrl) + liveUrl.length(), ind);
		} else {
			return url.substring(url.indexOf(liveUrl) + liveUrl.length());
		}

	}
}
