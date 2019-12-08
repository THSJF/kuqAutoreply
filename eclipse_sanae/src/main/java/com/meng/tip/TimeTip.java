package com.meng.tip;

import com.meng.*;
import com.meng.config.*;
import java.util.*;
import com.sobte.cqp.jcq.entity.*;

public class TimeTip implements Runnable {

    public TimeTip() {
    }

    @Override
    public void run() {
        while (true) {
            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.MINUTE) == 0) {
                if (c.get(Calendar.HOUR_OF_DAY) == 22) {
                    Autoreply.instence.threadPool.execute(new Runnable() {
							@Override
							public void run() {
								List<Group> groupList=Autoreply.CQ.getGroupList();
								for (Group g:groupList) {
									if (Autoreply.sendMessage(g.getId(), 0, "大家晚安...", true) < 0) {
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
                    Autoreply.instence.threadPool.execute(new Runnable() {
							@Override
							public void run() {
								Autoreply.sleeping = false;
								List<Group> groupList=Autoreply.CQ.getGroupList();
								for (Group g:groupList) {
									if (Autoreply.sendMessage(g.getId(), 0, "大家早上好啊...", true) < 0) {
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
