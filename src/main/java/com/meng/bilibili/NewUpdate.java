package com.meng.bilibili;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;

public class NewUpdate {

	private HashMap<Integer, BiliUp> hm = new HashMap<>();
	private String[] words = new String[] { "更了吗", "出来更新", "什么时候更新啊", "在？看看更新" };
	private int mapFlag = 0;

	public NewUpdate() {

	}

	public void addData(BiliUp person) {
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
			Date date = stampToDate(Long.parseLong(njb.data.vlist.get(0).getCreated()));
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.YEAR) == (1900 + date.getYear()) && c.get(Calendar.MONTH) == date.getMonth()
					&& c.get(Calendar.DAY_OF_MONTH) == date.getDate()) {
				Autoreply.sendGroupMessage(fromGroup,
						"更新莉，，，https://www.bilibili.com/video/av" + njb.data.vlist.get(0).getAid());
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(getUpQQ(msg)) + Methods.rfa(words));
			}
			Autoreply.sendGroupMessage(fromGroup, "最后更新为" + (1900 + date.getYear()) + "年" + (date.getMonth() + 1) + "月"
					+ date.getDate() + "日" + date.getHours() + "时" + date.getMinutes() + "分" + date.getSeconds() + "秒");
			return true;
		}
		return false;
	}

	public boolean isUpper(String msg) {
		for (int key : hm.keySet()) {
			BiliUp bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return true;
			}
		}
		return false;
	}

	public long getUpId(String msg) {
		for (int key : hm.keySet()) {
			BiliUp bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.bid;
			}
		}
		return 0;
	}

	public String getUpName(String msg) {
		for (int key : hm.keySet()) {
			BiliUp bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.name;
			}
		}
		return "";
	}

	public long getUpQQ(String msg) {
		for (int key : hm.keySet()) {
			BiliUp bUp = hm.get(key);
			if (msg.contains(bUp.name)) {
				return bUp.QQ;
			}
		}
		return 0;
	}

	public static Date stampToDate(Long s) {
		Date date = new Date(s * 1000);
		return date;
	}
}
