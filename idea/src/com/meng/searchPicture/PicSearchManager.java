package com.meng.searchPicture;

import java.util.HashMap;

import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class PicSearchManager {

    private HashMap<Long, String> userNotSendPicture = new HashMap<>();

    public PicSearchManager() {
    }

    public boolean check(long fromGroup, long fromQQ, String msg) {
        if (msg.equalsIgnoreCase("sp.help")) {
            if (fromGroup != 0) {
                sendMsg(fromGroup, fromQQ, "使用方式已私聊发送");
            }
            sendMsg(0, fromQQ, "使用sp.图片数量.目标网站搜索图片。其中.图片数量和目标网站都可以省略但如果要选择目标网站必须加上图片数量。");
            sendMsg(0, fromQQ, "例--\n在所有网站中搜索一个结果:sp\n在所有网站中搜索3个结果:sp.3\n在Pixiv中搜索1个结果sp.1.5");
            sendMsg(0, fromQQ, "网站代号：\n0 H-Magazines\n2 H-Game CG\n3 DoujinshiDB\n5 pixiv Images\n8 Nico Nico Seiga\n9 Danbooru\n10 drawr Images\n11 Nijie Images\n12 Yande.re\n13 Openings.moe\n15 Shutterstock\n16 FAKKU\n18 H-Misc\n19 2D-Market\n20 MediBang\n21 Anime\n22 H-Anime\n23 Movies\n24 Shows\n25 Gelbooru\n26 Konachan\n27 Sankaku Channel\n28 Anime-Pictures.net\n29 e621.net\n30 Idol Complex\n31 bcy.net Illust\n32 bcy.net Cosplay\n33 PortalGraphics.net (Hist)\n34 deviantArt\n35 Pawoo.net\n36 Manga");
            return true;
        }
        CQImage cqImage = Autoreply.instence.CC.getCQImage(msg);
        if (cqImage != null && (msg.toLowerCase().startsWith("sp"))) {
            try {
                Autoreply.instence.useCount.incSearchPicture(fromQQ);
                Autoreply.instence.useCount.incSearchPicture(Autoreply.CQ.getLoginQQ());
                sendMsg(fromGroup, fromQQ, "土豆折寿中……");
                int needPic = 1;
                int database = 999;
                if (msg.startsWith("sp.")) {
                    String[] ss = msg.replaceAll("\\[.*]", "").split("\\.");
                    needPic = Integer.parseInt(ss[1]);
                    database = ss.length >= 3 ? Integer.parseInt(ss[2]) : 999;
                }
                Autoreply.instence.threadPool.execute(new SearchThread(fromGroup, fromQQ, cqImage.download(Autoreply.appDirectory + "picSearch\\" + fromQQ, Autoreply.instence.random.nextInt() + "pic.jpg"), needPic, database));
            } catch (Exception e) {
                sendMsg(fromGroup, fromQQ, e.toString());
            }
            return true;
        } else if (cqImage == null && (msg.startsWith("sp.") || msg.equals("sp"))) {
            userNotSendPicture.put(fromQQ, msg);
            sendMsg(fromGroup, fromQQ, "需要一张图片");
            return true;
        } else if (cqImage != null && userNotSendPicture.get(fromQQ) != null) {
            try {
                sendMsg(fromGroup, fromQQ, "土豆折寿中……");
                Autoreply.instence.useCount.incSearchPicture(fromQQ);
                Autoreply.instence.useCount.incSearchPicture(Autoreply.CQ.getLoginQQ());
                int needPic = 1;
                int database = 999;
                if (userNotSendPicture.get(fromQQ).startsWith("sp.")) {
                    String[] ss = userNotSendPicture.get(fromQQ).split("\\.");
                    needPic = Integer.parseInt(ss[1]);
                    database = ss.length >= 3 ? Integer.parseInt(ss[2]) : 999;
                }
                Autoreply.instence.threadPool.execute(new SearchThread(fromGroup, fromQQ, cqImage.download(Autoreply.appDirectory + "picSearch\\" + fromQQ, Autoreply.instence.random.nextInt() + "pic.jpg"), needPic, database));
            } catch (Exception e) {
                sendMsg(fromGroup, fromQQ, e.toString());
            }
            userNotSendPicture.remove(fromQQ);
            return true;
        }
        return false;
    }

    public void sendMsg(long fromGroup, long fromQQ, String msg) {
        if (fromGroup == 0) {
            Autoreply.sendMessage(0, fromQQ, msg);
        } else {
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.at(fromQQ) + msg);
        }
    }
}
