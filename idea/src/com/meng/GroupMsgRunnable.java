package com.meng;

import com.meng.config.javabeans.GroupConfig;
import com.meng.tools.Methods;
import com.meng.tools.MoShenFuSong;

import java.io.File;
import java.util.Random;
import java.util.stream.Collectors;

import static com.meng.Autoreply.sendMessage;

public class GroupMsgRunnable implements Runnable {
    private int subType = 0;
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String fromAnonymous = "";
    private String msg = "";
    private int font = 0;
    private long timeStamp = 0;

    GroupMsgRunnable(MessageSender ms) {
        font = ms.font;
        fromGroup = ms.fromGroup;
        fromQQ = ms.fromQQ;
        msg = ms.msg;
        msgId = ms.msgId;
        subType = ms.subType;
        timeStamp = ms.timeStamp;
    }

    @Override
    public synchronized void run() {
        check();
    }

    private boolean check() {
        if (msg.equalsIgnoreCase("loaddic")) {
            Autoreply.instence.addGroupDic();
            sendMessage(fromGroup, fromQQ, "loaded");
            return true;
        }
        if (msg.equals("椰叶查询")) {
            sendMessage(fromGroup, fromQQ, "查询结果：" + Autoreply.instence.CC.at(fromQQ));
            return true;
        }
        GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
        if (groupConfig.isRepeat() && Autoreply.instence.repeatManager.check(fromGroup, fromQQ, msg)) {// 复读
            return true;
        }
        if (msg.equals(".live")) {
            String msgSend = Autoreply.instence.liveListener.livePerson.stream().filter(livePerson -> livePerson.living).map(livePerson -> livePerson.name + "正在直播" + livePerson.liveUrl + "\n").collect(Collectors.joining());
            sendMessage(fromGroup, fromQQ, msgSend.equals("") ? "居然没有飞机佬直播" : msgSend);
            return true;
        }
        if (msg.contains("大膜法")) {
            if (!groupConfig.isMoshenfusong()) {
                return true;
            }
            switch (msg) {
                case "大膜法 膜神复诵":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, new Random().nextInt(4)));
                    break;
                case "大膜法 膜神复诵 Easy":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 0));
                    break;
                case "大膜法 膜神复诵 Normal":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 1));
                    break;
                case "大膜法 膜神复诵 Hard":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 2));
                    break;
                case "大膜法 膜神复诵 Lunatic":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 3));
                    break;
                case "大膜法 c568连":
                    Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, 4));
                    break;
                default:
                    break;
            }
            return true;
        }
        if (groupConfig.isPohai() && Methods.isPohaitu(fromGroup, fromQQ, msg)) {
            return true;
        }
        if (groupConfig.isSetu() && Methods.isSetu(fromGroup, fromQQ, msg)) {
            return true;
        }
        if (groupConfig.isBarcode() && Autoreply.instence.barcodeManager.check(fromGroup, fromQQ, msg)) {// 二维码
            return true;
        }
        if (groupConfig.isSearchPic() && Autoreply.instence.picSearchManager.check(fromGroup, fromQQ, msg)) {// 搜索图片
            return true;
        }
        if (groupConfig.isKuiping() && Methods.checkLook(fromGroup, msg)) {// 窥屏检测
            return true;
        }
        if (groupConfig.isBilibiliCheck() && Autoreply.instence.biliLinkInfo.check(fromGroup, fromQQ, msg)) {// 比利比利链接详情
            return true;
        }
        if (groupConfig.isCqCode() && Autoreply.instence.CQcodeManager.check(fromGroup, msg)) {// 特殊信息(签到分享等)
            return true;
        }
        if (Autoreply.instence.banner.checkBan(fromQQ, fromGroup, msg)) {// 禁言
            return true;
        }
        if (Methods.checkGou(fromGroup, msg)) {// 苟
            return true;
        }
        if (Methods.checkMeng2(fromGroup, msg)) {// 萌2
            return true;
        }
        if (groupConfig.isCuigeng() && Autoreply.instence.updateManager.check(fromGroup, msg)) {
            return true;
        }
        if (Methods.checkAt(fromGroup, fromQQ, msg)) {//@
            return true;
        }
        if (Autoreply.instence.timeTip.check(fromGroup, fromQQ)) {// 根据时间提醒
            return true;
        }
        if (groupConfig.isRoll() && Autoreply.instence.rollPlane.check(fromGroup, msg)) {// roll
            return true;
        }
        if (msg.equals("提醒戒膜")) {
            sendMessage(fromGroup, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\jiemo.jpg")));
            return true;
        }
        if (msg.equals("查看统计")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(fromQQ));
            return true;
        }
        if (msg.equals("查看排行")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getTheFirst());
            return true;
        }
        if (msg.equals("查看活跃数据")) {
            sendMessage(fromGroup, fromQQ, "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + fromGroup);
            return true;
        }
        if (Autoreply.instence.picEditManager.check(fromGroup, fromQQ, msg)) {
            return true;
        }
        return groupConfig.isDic() && Autoreply.instence.dicReplyManager.check(fromGroup, fromQQ, msg);
    }
}
