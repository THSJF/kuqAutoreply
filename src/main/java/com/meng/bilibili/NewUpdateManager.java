package com.meng.bilibili;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.BilibiliUser;
import com.meng.config.ConfigManager;

public class NewUpdateManager {

	private String[] words = new String[] { "更了吗", "出来更新", "什么时候更新啊", "在？看看更新", "怎么还不更新", "更新啊草绳" };
	private ConfigManager configManager;

	public NewUpdateManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public boolean check(long fromGroup, String msg) {
		if (isUpper(msg) && msg.contains("今天") && msg.contains("更") && msg.contains("吗")) {
			long videoUpdateTime = 0;
			long articalUpdateTime = 0;
			Gson gson = new Gson();
			NewVideoBean.Data.Vlist vlist = null;
			NewArticleBean.Data.Articles articles = null;
			try {
				vlist = gson
						.fromJson(Methods
								.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid="
										+ getUpId(msg) + "&page=1&pagesize=1")
								.replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"),
								NewVideoBean.class).data.vlist.get(0);
			} catch (Exception e) {
				System.out.println("no videos");
			}
			try {
				articles = gson
						.fromJson(
								Methods.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + getUpId(msg)
										+ "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"),
								NewArticleBean.class).data.articles.get(0);
			} catch (Exception e) {
				System.out.println("no articles");
			}

			if (vlist != null && articles == null) {
				videoUpdateTime = Long.parseLong(vlist.created) * 1000;
				tipVideo(fromGroup, msg, videoUpdateTime, vlist);
			} else if (vlist == null && articles != null) {
				articalUpdateTime = Long.parseLong(articles.publish_time) * 1000;
				tipArticle(fromGroup, msg, articalUpdateTime, articles);
			} else if (vlist != null && articles != null) {
				videoUpdateTime = Long.parseLong(vlist.created) * 1000;
				articalUpdateTime = Long.parseLong(articles.publish_time) * 1000;
				if (articalUpdateTime > videoUpdateTime) {
					tipArticle(fromGroup, msg, articalUpdateTime, articles);
				} else {
					tipVideo(fromGroup, msg, videoUpdateTime, vlist);
				}
			}

			return true;
		}
		return false;
	}

	private void tipVideo(long fromGroup, String msg, long videoUpdateTime, NewVideoBean.Data.Vlist vlist) {
		if (System.currentTimeMillis() - videoUpdateTime < 86400000) {
			Autoreply.sendMessage(fromGroup, 0, "更新莉，，，https://www.bilibili.com/video/av" + vlist.aid);
		} else {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(getUpQQ(msg)) + Methods.rfa(words));
			Autoreply.sendMessage(fromGroup, 0,
					"你都" + ((System.currentTimeMillis() - videoUpdateTime) / 86400000) + "天没更新了");
		}
	}

	private void tipArticle(long fromGroup, String msg, long articalUpdateTime, NewArticleBean.Data.Articles articles) {
		if (System.currentTimeMillis() - articalUpdateTime < 86400000) {
			Autoreply.sendMessage(fromGroup, 0, "更新莉，，，https://www.bilibili.com/read/cv" + articles.id);
		} else {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(getUpQQ(msg)) + Methods.rfa(words));
			Autoreply.sendMessage(fromGroup, 0,
					"你都" + ((System.currentTimeMillis() - articalUpdateTime) / 86400000) + "天没更新了");
		}
	}

	private boolean isUpper(String msg) {
		for (BilibiliUser cb : configManager.configJavaBean.mapBiliUser) {
			if (msg.contains(cb.name)) {
				return true;
			}
		}
		return false;
	}

	private int getUpId(String msg) {
		for (BilibiliUser cb : configManager.configJavaBean.mapBiliUser) {
			if (cb.bid == 0) {
				continue;
			}
			if (msg.contains(cb.name)) {
				return cb.bid;
			}
		}
		return 0;
	}

	private long getUpQQ(String msg) {
		for (BilibiliUser cb : configManager.configJavaBean.mapBiliUser) {
			if (cb.qq == 0) {
				continue;
			}
			if (msg.contains(cb.name)) {
				return cb.qq;
			}
		}
		return 0;
	}
}
