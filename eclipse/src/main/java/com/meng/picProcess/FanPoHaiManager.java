package com.meng.picProcess;

import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class FanPoHaiManager {
    private HashSet<FingerPrint> fingerPrints = new HashSet<>(64);
    private int pohaicishu = 0;
    private int alpohai = Autoreply.instence.random.nextInt(5) + 2;

    public FanPoHaiManager() {
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					File[] pohaitu = new File(Autoreply.appDirectory + "fan\\").listFiles();
					if (pohaitu != null) {
						for (File file : pohaitu) {
							try {
								fingerPrints.add(new FingerPrint(ImageIO.read(file)));
							} catch (Exception e) {
								System.out.println(file.getAbsolutePath());
							}
						}
					}
					System.out.println("反迫害启动完成");
					Autoreply.sleeping = false;
					Autoreply.sendMessage(Autoreply.mainGroup, 0, "~reconnect");
					Autoreply.instence.enable();
				}
			});
    }

    public boolean check(long fromQQ, long fromGroup, String msg, long msgID, File[] imageFiles) {
        try {
            boolean bpohai = false;
            if (msg.contains("迫害") && !msg.contains("反迫害")) {
                ++pohaicishu;
                if (pohaicishu == alpohai) {
                    bpohai = true;
                    pohaicishu = 0;
                    alpohai = Autoreply.instence.random.nextInt() % 5 + 2;
                }
            }
            // 判定图片相似度
            if (!bpohai && imageFiles != null) {
                float simi = 0.0f;
                for (File file : imageFiles) {
                    FingerPrint fp1 = new FingerPrint(ImageIO.read(file));
                    for (FingerPrint fingerPrint : fingerPrints) {
                        if (fingerPrint == null) {
                            continue;
                        }
                        float tf = fingerPrint.compare(fp1);
                        if (tf > simi) {
                            simi = tf;
                        }
                    }
                    bpohai = simi > 0.95f;
                }
            }
            if (bpohai || msg.equals("[CQ:bface,p=11361,id=1188CED678E40F79A536C60658990EE7]")) {
                String folder = "";
                PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(fromQQ);
                if (personInfo != null) {
                    folder = Autoreply.appDirectory + "pohai/" + personInfo.name + "/";
                }
                File file = new File(folder);
                if (msgID != -1) {
                    GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
                    if (groupConfig != null && groupConfig.isCheHuiMoTu()) {
                        Member me = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                        Member ban = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                        if (me.getAuthority() - ban.getAuthority() > 0) {
                            Autoreply.CQ.deleteMsg(msgID);
                        }
                    }
                }
                if (folder.equals("") || !file.exists()) {
                    switch (Autoreply.instence.random.nextInt(3)) {
                        case 0:
                            Autoreply.sendMessage(fromGroup, 0, "鬼鬼");
                            break;
                        case 1:
                            Autoreply.sendMessage(fromGroup, 0, "除了迫害和膜你还知道什么");
                            break;
                        case 2:
                            Autoreply.sendMessage(fromGroup, 0, "草绳");
                            break;
                    }
                    return true;
                } else {
                    File[] files = file.listFiles();
                    if (files != null) {
                        Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)));
                        Autoreply.instence.useCount.incPohaitu(Autoreply.CQ.getLoginQQ());
                        Autoreply.instence.groupCount.incPohaitu(fromGroup);
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
