package com.meng.tip;

import com.meng.*;
import com.meng.config.*;
import java.util.*;

public class TimeTip implements Runnable {
    
    public TimeTip() {
    }

    @Override
    public void run() {
        while (true) {
            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.MINUTE) == 0) {
                if (c.get(Calendar.HOUR_OF_DAY) == 23) {
                    Autoreply.instence.threadPool.execute(new Runnable() {
							@Override
							public void run() {
								for (GroupConfig groupConfig : Autoreply.instence.configManager.configJavaBean.groupConfigs) {
									if (groupConfig.reply) {
										if (Autoreply.sendMessage(groupConfig.groupNumber, 0, "少女休息中...", true) < 0) {
											continue;
										}
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
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
								for (GroupConfig groupConfig : Autoreply.instence.configManager.configJavaBean.groupConfigs) {
									if (groupConfig.reply) {
										if (Autoreply.sendMessage(groupConfig.groupNumber, 0, "大家早上好啊", true) < 0) {
											continue;
										}
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
								Autoreply.sleeping = false;
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
