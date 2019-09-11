package com.meng;

import com.meng.bilibili.live.LivePerson;
import com.meng.config.javabeans.GroupConfig;
import com.meng.tools.Methods;
import com.meng.tools.MoShenFuSong;

import java.io.File;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.meng.Autoreply.sendMessage;
import com.meng.ocr.sign.*;
import com.sobte.cqp.jcq.entity.*;

public class GroupMsgPart2Runnable implements Runnable {
    private int subType = 0;
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String fromAnonymous = "";
    private String msg = "";
    private int font = 0;
    private long timeStamp = 0;
    private File[] imageFiles = null;

    public GroupMsgPart2Runnable(MessageSender ms) {
        font = ms.font;
        fromGroup = ms.fromGroup;
        fromQQ = ms.fromQQ;
        msg = ms.msg;
        msgId = ms.msgId;
        subType = ms.subType;
        timeStamp = ms.timeStamp;
        imageFiles = ms.imageFiles;
	  }

    @Override
    public synchronized void run() {
        check();
	  }

    private boolean check() {
        if (msg.contains("@") && Autoreply.instence.CC.getAt(msg) == -1000) {
            sendMessage(fromGroup, fromQQ, "野蛮假at");
            return true;
		  }
        if (msg.equalsIgnoreCase("loaddic")) {
            Autoreply.instence.addGroupDic();
            sendMessage(fromGroup, fromQQ, "loaded");
            return true;
		  }
        if (msg.equals("椰叶查询")) {
            sendMessage(fromGroup, fromQQ, "查询结果：" + Autoreply.instence.CC.at(fromQQ));
            return true;
		  }
        if (Methods.checkAt(fromGroup, fromQQ, msg)) {//@
            return true;
		  }
        GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
        if (groupConfig.isRepeat() && Autoreply.instence.repeatManager.check(fromGroup, fromQQ, msg, imageFiles)) {// 复读
            return true;
		  }
        if (msg.equals(".live")) {
            String msgSend;
            final StringBuilder stringBuilder = new StringBuilder();
            Autoreply.instence.liveListener.livePersonMap.forEach(new BiConsumer<Integer, LivePerson>() {
				  @Override
				  public void accept(Integer key, LivePerson livePerson) {
					  if (livePerson.lastStatus) {
						  stringBuilder.append(Autoreply.instence.configManager.getPersonInfoFromBid(key).name).append("正在直播").append(livePerson.liveUrl).append("\n");
						}
					}
				});
            msgSend = stringBuilder.toString();
            Autoreply.sendMessage(fromGroup, fromQQ, msgSend.equals("") ? "居然没有飞机佬直播" : msgSend);
            return true;
		  }
        if (msg.startsWith("[CQ:location,lat=")) {
            sendMessage(fromGroup, 0, Autoreply.instence.CC.location(35.594993, 118.869838, 15, "守矢神社", "此生无悔入东方 来世愿生幻想乡"));
            return true;
		  }
        if (msg.contains("大膜法")) {
            if (!groupConfig.isMoshenfusong()) {
                return true;
			  }
            switch (msg) {
                case "大膜法 膜神复诵":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, new Random().nextInt(4)));
				  break;
                case "大膜法 膜神复诵 Easy":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 0));
				  break;
                case "大膜法 膜神复诵 Normal":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 1));
				  break;
                case "大膜法 膜神复诵 Hard":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 2));
				  break;
                case "大膜法 膜神复诵 Lunatic":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 3));
				  break;
                case "大膜法 膜神复诵 Overdrive":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 4));
				  break;
                case "大膜法 c568连":
				  Autoreply.instence.threadPool.execute(new MoShenFuSong(fromGroup, fromQQ, 5));
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
        if (groupConfig.isNvZhuang() && Methods.isNvZhuang(fromGroup, fromQQ, msg)) {
            return true;
		  }
        if (groupConfig.isBarcode() && Autoreply.instence.barcodeManager.check(fromGroup, fromQQ, msg, imageFiles)) {// 二维码
            return true;
		  }
        if (groupConfig.isSearchPic() && Autoreply.instence.picSearchManager.check(fromGroup, fromQQ, msg, imageFiles)) {// 搜索图片
            return true;
		  }
        //   if (groupConfig.isKuiping() && Methods.checkLook(fromGroup, msg)) {// 窥屏检测
        //       return true;
        //    }
        if (groupConfig.isBilibiliCheck() && Autoreply.instence.biliLinkInfo.check(fromGroup, fromQQ, msg)) {// 比利比利链接详情
            return true;
		  }
        if (groupConfig.isCqCode() && Autoreply.instence.CQcodeManager.check(fromGroup, msg)) {// 特殊信息(签到分享等)
            return true;
		  }
        if (Autoreply.instence.banner.checkBan(fromQQ, fromGroup, msg)) {// 禁言
            return true;
		  }
        if (groupConfig.isCuigeng() && Autoreply.instence.updateManager.check(fromGroup, msg)) {
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
        if (msg.equals("查看群统计")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.groupCount.getMyCount(fromGroup));
            return true;
		  }
        if (msg.equals("查看群排行")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.groupCount.getTheFirst());
            return true;
		  }
        if (msg.equals("查看活跃数据")) {
            sendMessage(fromGroup, fromQQ, "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + fromGroup);
            return true;
		  }
        if (Autoreply.instence.picEditManager.check(fromGroup, fromQQ, msg)) {
            return true;
		  }
		if (msg.equals(".jrrp")) {
			Member m=Autoreply.CQ.getGroupMemberInfo(fromGroup, fromQQ);
			String res;
			long oneDay=24 * 60 * 60 * 1000;				
			int ipro=Integer.parseInt(MD5.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / oneDay)).substring(26), 16) % 10001;
			float fpro=((float)ipro)/100;
			if (m != null) {
				res = String.format("%s今天会在%.2f%%处疮痍",m.getNick(),fpro);
			  } else {
				res = String.format("你今天会在%.2f%%处疮痍",fpro);
			  }
			sendMessage(fromGroup, 0, res);
		  }
		Autoreply.instence.seqManager.check(fromGroup, fromQQ, msg);
        return groupConfig.isDic() && Autoreply.instence.dicReplyManager.check(fromGroup, fromQQ, msg);
	  }
  }
