package com.meng.tools;

import com.meng.*;

public class ZanManager {
	
    public void sendZan() {
        for (long l : Autoreply.instence.configManager.RanConfig.masterList) {
            Autoreply.CQ.sendLikeV2(l, 10);
        }
        for (long l : Autoreply.instence.configManager.RanConfig.adminList) {
            Autoreply.CQ.sendLikeV2(l, 10);
        }
        for (long l : Autoreply.instence.configManager.SanaeConfig.zanSet) {
            Autoreply.CQ.sendLikeV2(l, 10);
        }
    }

    public boolean checkAdd(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("z.add")) {
			Autoreply.instence.configManager.SanaeConfig.zanSet.addAll(Autoreply.instence.CC.getAts(msg));
			Autoreply.instence.configManager.saveSanaeConfig();
			Autoreply.sendMessage(fromGroup, fromQQ, "已添加至赞列表");
			return true;
        }
        return false;
    }
}
