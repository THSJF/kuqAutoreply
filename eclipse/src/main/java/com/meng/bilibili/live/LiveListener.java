package com.meng.bilibili.live;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.bilibili.main.*;
import com.meng.config.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.function.Function;
import org.jsoup.*;

public class LiveListener implements Runnable {

    public ConcurrentHashMap<Integer, LivePerson> livePersonMap = new ConcurrentHashMap<>();
    private boolean loadFinish = false;
    private ConcurrentHashMap<String, String> liveTimeMap = new ConcurrentHashMap<>();

    public LiveListener(final ConfigManager configManager) {
        System.out.println("直播检测启动中");
        Autoreply.instence.threadPool.execute(new Runnable() {
			  @Override
			  public void run() {
				  for (PersonInfo cb : configManager.configJavaBean.personInfo) {
					  LiveListener.this.checkPerson(cb);
					}
				  loadFinish = true;
				  System.out.println("直播检测启动完成");
				}
			});
        File liveTimeFile = new File(Autoreply.appDirectory + "liveTime2.json");
        if (!liveTimeFile.exists()) {
            saveLiveTime();
		  }
        try {
            Type token = new TypeToken<ConcurrentHashMap<String, String>>() {
			  }.getType();
            liveTimeMap = new Gson().fromJson(Methods.readFileToString(liveTimeFile.getAbsolutePath()), token);
		  } catch (Exception e) {
            e.printStackTrace();
		  }
	  }

    private void checkPerson(PersonInfo personInfo) {
        if (personInfo.bliveRoom == -1) {
            return;
		  }
        if (personInfo.bliveRoom == 0) {
            if (personInfo.bid != 0) {
                SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + personInfo.bid), SpaceToLiveJavaBean.class);
                if (sjb.data.roomid == 0) {
                    personInfo.bliveRoom = -1;
                    Autoreply.instence.configManager.saveConfig();
                    return;
				  }
                personInfo.bliveRoom = sjb.data.roomid;
                Autoreply.instence.configManager.saveConfig();
                System.out.println("检测到用户" + personInfo.name + "(" + personInfo.bid + ")的直播间" + personInfo.bliveRoom);
                try {
                    Thread.sleep(100);
				  } catch (InterruptedException e) {
                    e.printStackTrace();
				  }
			  }
		  }
	  }

    @Override
    public void run() {
        while (true) {
            try {
                if (!loadFinish) {
                    Thread.sleep(1000);
                    continue;
				  }
                for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
                    if (personInfo.bliveRoom == 0 || personInfo.bliveRoom == -1) {
                        continue;
					  }
                    SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + personInfo.bid), SpaceToLiveJavaBean.class);
                    boolean living = sjb.data.liveStatus == 1;
					if (living) {
						if (Autoreply.instence.danmakuListenerManager.getListener(personInfo.bliveRoom) == null) {
							DanmakuListener dl=new DanmakuListener(new URI("wss://broadcastlv.chat.bilibili.com:2245/sub"), personInfo);
							dl.connect();
							Autoreply.instence.danmakuListenerManager.listener.add(dl);
						  }
					  } else {
						DanmakuListener dl=Autoreply.instence.danmakuListenerManager.getListener(personInfo.bliveRoom);
						if (dl != null) {
							dl.close();
						  } 
					  }
                    LivePerson livePerson = livePersonMap.computeIfAbsent(personInfo.bid, new Function<Integer, LivePerson>() {
						  @Override
						  public LivePerson apply(Integer k) {
							  return new LivePerson();
							}
						});
                    livePerson.liveUrl = sjb.data.url;
					livePerson.roomID = sjb.data.roomid + "";
                    if (livePerson.needTip) {
                        if (!livePerson.lastStatus && living) {
                            onStart(personInfo, livePerson);
						  } else if (livePerson.lastStatus && !living) {
                            onStop(personInfo, livePerson);
						  }
					  }
                    livePerson.lastStatus = living;
                    livePerson.needTip = true;
                    Thread.sleep(1000);
				  }
			  } catch (Exception e) {
                System.out.println("直播监视出了问题：");
                e.printStackTrace();
			  }
		  }
	  }

    public void saveNow() {
        for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
            if (personInfo.bliveRoom == 0 || personInfo.bliveRoom == -1) {
                continue;
			  }
            LivePerson livePerson = livePersonMap.computeIfAbsent(personInfo.bid, new Function<Integer, LivePerson>() {
				  @Override
				  public LivePerson apply(Integer k) {
					  return new LivePerson();
					}
				});
            countLiveTime(personInfo, livePerson);
            livePerson.liveStartTimeStamp = System.currentTimeMillis() / 1000;
		  }
	  }

    private void onStart(PersonInfo personInfo, LivePerson livePerson) {
        livePerson.liveStartTimeStamp = System.currentTimeMillis() / 1000;
        tipStart(personInfo);
	  }

    private void onStop(PersonInfo personInfo, LivePerson livePerson) {
        countLiveTime(personInfo, livePerson);
        tipFinish(personInfo);
	  }

    private void countLiveTime(PersonInfo personInfo, LivePerson livePerson) {
        String timeStr = liveTimeMap.get(personInfo.name);
        if (timeStr == null) {
            liveTimeMap.put(personInfo.name, String.valueOf(System.currentTimeMillis() / 1000 - livePerson.liveStartTimeStamp));
		  } else {
            long time = Long.parseLong(timeStr);
            time += System.currentTimeMillis() / 1000 - livePerson.liveStartTimeStamp;
            liveTimeMap.put(personInfo.name, String.valueOf(time));
		  }
        saveLiveTime();
	  }
	  
	public void setBan(long fromGroup, String roomId, String blockId, String hour) {
		if (Integer.parseInt(hour)==0) {  
			String jsonStr=Methods.getSourceCode("https://api.live.bilibili.com/liveact/ajaxGetBlockList?roomid="+roomId+"&page=1", Autoreply.instence.cookieManager.cookie.grzx);
			BanBean bb=new Gson().fromJson(jsonStr, BanBean.class);
			long bid=Integer.parseInt(blockId);
			String eventId="";
			for (BanBean.Data data:bb.data) {
				if (data.uid == bid) {
					eventId = data.id + "";
					break;
				  }
			  }
			Connection.Response response = null;
			try {
				Map<String, String> liveHead = new HashMap<>();
				liveHead.put("Host", "api.live.bilibili.com");
				liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
				liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				liveHead.put("Connection", "keep-alive");
				liveHead.put("Origin", "https://live.bilibili.com");
				Connection connection = Jsoup.connect("https://api.live.bilibili.com/banned_service/v1/Silent/del_room_block_user");
				String csrf = Methods.cookieToMap(Autoreply.instence.cookieManager.cookie.grzx).get("bili_jct");
				connection.userAgent(Autoreply.instence.userAgent)
				  .headers(liveHead)
				  .ignoreContentType(true)
				  .referrer("https://live.bilibili.com/" + roomId)
				  .cookies(Methods.cookieToMap(Autoreply.instence.cookieManager.cookie.grzx))
				  .method(Connection.Method.POST)
				  .data("roomid", roomId)
				  .data("id", eventId)
				  .data("csrf_token", csrf)
				  .data("csrf", csrf)
				  .data("visit_id", "");
				response = connection.execute();
				if (response.statusCode() != 200) {
					return;
				  }
				JsonParser parser = new JsonParser();
				JsonObject obj = parser.parse(response.body()).getAsJsonObject();
				switch (obj.get("code").getAsInt()) {
					case 0:
					  if (!obj.get("message").getAsString().equals("")) {
						  Autoreply.sendMessage(fromGroup, 0, obj.getAsJsonObject("message").getAsString());
						} else {
						  Autoreply.sendMessage(fromGroup, 0, blockId + "在直播间" + roomId + "被禁言" + hour + "小时");
						}
					  break;			
					default:
					  Autoreply.sendMessage(fromGroup, 0, response.body());
					  break;
				  }
			  } catch (Exception e) {
				if (response != null) {
					Autoreply.sendMessage(fromGroup, 0, "服务器无回应");
				  }
			  } 
		  } else {
			Connection.Response response = null;
			try {
				Map<String, String> liveHead = new HashMap<>();
				liveHead.put("Host", "api.live.bilibili.com");
				liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
				liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				liveHead.put("Connection", "keep-alive");
				liveHead.put("Origin", "https://live.bilibili.com");
				Connection connection = Jsoup.connect("https://api.live.bilibili.com/banned_service/v2/Silent/add_block_user");
				String csrf = Methods.cookieToMap(Autoreply.instence.cookieManager.cookie.grzx).get("bili_jct");
				connection.userAgent(Autoreply.instence.userAgent)
				  .headers(liveHead)
				  .ignoreContentType(true)
				  .referrer("https://live.bilibili.com/" + roomId)
				  .cookies(Methods.cookieToMap(Autoreply.instence.cookieManager.cookie.grzx))
				  .method(Connection.Method.POST)
				  .data("hour", hour)
				  .data("roomid", roomId)
				  .data("block_uid", blockId)
				  .data("csrf_token", csrf)
				  .data("csrf", csrf)
				  .data("visit_id", "");
				response = connection.execute();
				if (response.statusCode() != 200) {
					return;
				  }
				JsonParser parser = new JsonParser();
				JsonObject obj = parser.parse(response.body()).getAsJsonObject();
				switch (obj.get("code").getAsInt()) {
					case 0:
					  if (!obj.get("message").getAsString().equals("")) {
						  Autoreply.sendMessage(fromGroup, 0, obj.getAsJsonObject("message").getAsString());
						} else {
						  Autoreply.sendMessage(fromGroup, 0, blockId + "在直播间" + roomId + "被禁言" + hour + "小时");
						}
					  break;
					default:
					  Autoreply.sendMessage(fromGroup, 0, response.body());
					  break;
				  }
			  } catch (Exception e) {
				if (response != null) {
					Autoreply.sendMessage(fromGroup, 0, "服务器无回应");
				  }
			  }
		  }
	  }
    private void tipStart(PersonInfo p) {
        if (!p.isTipLive()) {
            return;
		  }
        Autoreply.sendMessage(1023432971, 0, p.name + "开始直播" + p.bliveRoom, true);
        ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromName(p.name).tipIn;
        for (int i = 0, groupListSize = groupList.size(); i < groupListSize; i++) {
            long group = groupList.get(i);
            Autoreply.sendMessage(group, 0, p.name + "开始直播" + p.bliveRoom, true);
		  }
	  }

    private void tipFinish(PersonInfo p) {
        if (!p.isTipLive()) {
            return;
		  }
        Autoreply.sendMessage(1023432971, 0, p.name + "直播结束" + p.bliveRoom, true);
        ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromName(p.name).tipIn;
        for (int i = 0, groupListSize = groupList.size(); i < groupListSize; i++) {
            long group = groupList.get(i);
            Autoreply.sendMessage(group, 0, p.name + "直播结束" + p.bliveRoom, true);
		  }
	  }

    public String getLiveTimeCount() {
        Iterator<Entry<String, String>> iterator = liveTimeMap.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append(cal(Long.parseLong(entry.getValue()))).append("\n");
		  }
        return stringBuilder.toString();
	  }

    private String cal(long second) {
        long h = 0;
        long min = 0;
        long temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp > 60) {
                min = temp / 60;
			  }
		  } else {
            min = second / 60;
		  }
        if (h == 0) {
            return min + "分";
		  } else {
            return h + "时" + min + "分";
		  }
	  }

    private void saveLiveTime() {
        try {
            File file = new File(Autoreply.appDirectory + "liveTime2.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(liveTimeMap));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
