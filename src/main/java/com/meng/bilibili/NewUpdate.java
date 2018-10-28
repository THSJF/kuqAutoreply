package com.meng.bilibili;

import java.util.HashMap;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.bilibili.NewUpdateJavaBean.Data.Vlist;

public class NewUpdate {

	private HashMap<Integer, BiliUpJavaBean> hm = new HashMap<>();
	private String[] words = new String[] { "更了吗", "出来更新", "什么时候更新啊", "在？看看更新", "怎么还不更新", "更新啊草绳" };
	private int mapFlag = 0;

	public NewUpdate() {

	}

	public void addData(BiliUpJavaBean person) {
		hm.put(mapFlag, person);
		mapFlag++;
	}

	public boolean check(long fromGroup, String msg) {
		if (isUpper(msg) && msg.contains("今天") && msg.contains("更") && msg.contains("吗")) {
			String jsonStr = Methods.getSourceCode(
					"https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + getUpId(msg) + "&page=1&pagesize=1")
					.replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":");
			Gson gson = new Gson();
			NewUpdateJavaBean njb = gson.fromJson(jsonStr, NewUpdateJavaBean.class);
			Vlist vlist = njb.data.vlist.get(0);
			if (System.currentTimeMillis() - Long.parseLong(vlist.created) * 1000 < 86400000) {
				Autoreply.sendGroupMessage(fromGroup, "更新莉，，，https://www.bilibili.com/video/av" + vlist.aid);
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(getUpQQ(msg)) + Methods.rfa(words));
				Autoreply.sendGroupMessage(fromGroup, "你都"
						+ ((System.currentTimeMillis() - Long.parseLong(vlist.created) * 1000) / 86400000) + "天没更新了");
			}
			return true;
		}
		return false;
	}

	public boolean isUpper(String msg) {
		for (int key : hm.keySet()) {
			BiliUpJavaBean bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return true;
			}
		}
		return false;
	}

	public long getUpId(String msg) {
		for (int key : hm.keySet()) {
			BiliUpJavaBean bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.bid;
			}
		}
		return 0;
	}

	public String getUpName(String msg) {
		for (int key : hm.keySet()) {
			BiliUpJavaBean bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.name;
			}
		}
		return "";
	}

	public long getUpQQ(String msg) {
		for (int key : hm.keySet()) {
			BiliUpJavaBean bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.QQ;
			}
		}
		return 0;
	}
}
