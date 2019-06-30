package com.meng.groupChat;

import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.Member;

import javax.imageio.ImageIO;
import java.io.File;

public class FanPoHaiManager {
    private FingerPrint[] fts;// 存放图片指纹的数组 用于对比新收到的图片和样本相似度
    private int pohaicishu = 0;// 收到的消息包含迫害二字的次数
    private int alpohai = Autoreply.instence.random.nextInt(5) + 2;// 收到的消息包含迫害二字的次数到达此值也会触发反迫害

    public FanPoHaiManager() {
       new Thread(() -> {
           File[] pohaitu = new File(Autoreply.appDirectory + "fan\\").listFiles();
           if (pohaitu != null) {
               fts = new FingerPrint[pohaitu.length];
           }
           if (fts != null) {
               for (int i = 0; i < fts.length; i++) {
                   try {
                       fts[i] = new FingerPrint(ImageIO.read(pohaitu[i]));
                   } catch (Exception e) {
                       System.out.println(pohaitu[i].getAbsolutePath());
                   }
               }
           }
       }).start();
    }

    public boolean check(long fromQQ, long fromGroup, String msg, long msgID) {
        try {
            boolean bpohai = false;
            // 处理带有迫害二字的消息
            if (msg.contains("迫害") && !msg.contains("反迫害")) {
                pohaicishu++;
                if (pohaicishu == alpohai) {
                    bpohai = true;
                    pohaicishu = 0;
                    alpohai = Autoreply.instence.random.nextInt(2147483647) % 5 + 2;
                }
            }
            // 判定图片相似度
            if (!bpohai) {
                CQImage cmCqImage = Autoreply.instence.CC.getCQImage(msg);
                if (cmCqImage != null) {
                    float simi = 0.0f;
                    File tmpF = cmCqImage
                            .download(Autoreply.appDirectory + "fan1\\" + System.currentTimeMillis() + "phtmp.jpg");
                    FingerPrint fp1 = new FingerPrint(ImageIO.read(tmpF));
                    // 取值为所有样本中最高的相似度
                    for (FingerPrint fg : fts) {
                        if (fg == null) {
                            continue;
                        }
                        float tf = fg.compare(fp1);
                        if (tf > simi) {
                            simi = tf;
                        }
                    }
                    bpohai = simi > 0.95f;
                    tmpF.delete();
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
                        Member qqInfo = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
                        if (qqInfo.getAuthority() == 2 || qqInfo.getAuthority() == 3) {
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
                        Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image((File) Methods.rfa(files)));
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
