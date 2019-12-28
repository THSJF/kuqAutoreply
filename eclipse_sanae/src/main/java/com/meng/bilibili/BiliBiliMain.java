package com.meng.bilibili;

import com.google.gson.*;
import com.meng.*;
import com.meng.bilibili.main.*;
import com.meng.config.*;
import com.meng.tools.*;

public class BiliBiliMain {
	public static BiliBiliMain ins;

	public boolean check(long fromGroup, long fromQQ, String msg) {
		try {
			if (msg.startsWith("-订阅b站 ")) {
				String uid=msg.substring(6);
				String name=new JsonParser().parse(Tools.Network.getSourceCode("https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp")).getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString();
				BiliMaster bm=ConfigManager.ins.SanaeConfig.biliMaster.get(Integer.parseInt(uid));
				if (bm == null) {
					bm = new BiliMaster();
					bm.uid = Integer.parseInt(uid);
					SpaceToLiveJavaBean sjb = Autoreply.gson.fromJson(Tools.Network.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + bm.uid), SpaceToLiveJavaBean.class);
					if (sjb.data.roomid == 0) {
						bm.roomID = -1;
					} else {
						bm.roomID = sjb.data.roomid;
					}
					ConfigManager.ins.SanaeConfig.biliMaster.put(bm.uid, bm);
				}
				bm.addFans(fromGroup, fromQQ);
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, 0, String.format("订阅%s(uid:%d)成功", name, bm.uid));
				return true;
			}
		} catch (Exception e) {
			Autoreply.sendMessage(fromGroup, 0, e.toString());
		}
		return false;
	}
}
