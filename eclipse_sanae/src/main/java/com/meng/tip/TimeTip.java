package com.meng.tip;

import com.meng.*;
import com.meng.config.*;
import java.util.*;
import com.sobte.cqp.jcq.entity.*;
import com.meng.tools.*;
import com.meng.bilibili.*;

public class TimeTip implements Runnable {

	private String[] goodMorning = new String[]{
		"早上好",
		"早安",
		"早",
		"大家早上好",
		"大家早上好啊.."
	};
	private String[] goodEvening = new String[]{
		"晚安",
		"大家晚安",
		"晚安....",
		"大家晚安....",
		"大家早点休息吧"
	};
    public TimeTip() {
    }

    @Override
    public void run() {
        while (true) {
            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) == 11) {
					for (long l : ConfigManager.ins.RanConfig.adminList) {
						Autoreply.CQ.sendLikeV2(l, 10);
					}
					for (long l : ConfigManager.ins.SanaeConfig.zanSet) {
						Autoreply.CQ.sendLikeV2(l, 10);
					}
				}
				if (c.get(Calendar.HOUR_OF_DAY) == 0) {
					for (BiliMaster bm:ConfigManager.ins.SanaeConfig.biliMaster.values()) {
						for (BiliMaster.FansInGroup fans:bm.fans) {
							if (FaithManager.ins.getFaith(fans.qq) > 0) {
								FaithManager.ins.subFaith(fans.qq, 1);
							}
						}
					}
				}
				if (c.get(Calendar.HOUR_OF_DAY) == 22) {
					Autoreply.ins.threadPool.execute(new Runnable() {
							@Override
							public void run() {
								List<Group> groupList=Autoreply.CQ.getGroupList();
								for (Group g:groupList) {
									if (Autoreply.sendMessage(g.getId(), 0, goodEvening) < 0) {
										continue;
									}
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								Autoreply.sleeping = true;
							}
						});
				}
				if (c.get(Calendar.HOUR_OF_DAY) == 6) {
					Autoreply.ins.threadPool.execute(new Runnable() {
							@Override
							public void run() {
								Autoreply.sleeping = false;
								List<Group> groupList=Autoreply.CQ.getGroupList();
								for (Group g:groupList) {
									if (Autoreply.sendMessage(g.getId(), 0, goodMorning) < 0) {
										continue;
									}
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						});
				}          
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
