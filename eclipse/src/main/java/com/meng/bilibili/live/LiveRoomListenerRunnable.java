package com.meng.bilibili.live;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;
import org.jsoup.*;

public class LiveRoomListenerRunnable implements Runnable {
	public Map<String,String> liveHead=new HashMap<>();
	public HashSet<MsgBean> peopleSet=new HashSet<MsgBean>();
	public HashSet<String> motherSet=new HashSet<>();


	public LiveRoomListenerRunnable() {
		liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://live.bilibili.com");

		File notherMapFile = new File(Autoreply.appDirectory + "mother.json");
        if (!notherMapFile.exists()) {
            saveMotherMap();
		  }
        try {
            Type token = new TypeToken<HashSet<String>>() {
			  }.getType();
            motherSet = new Gson().fromJson(Methods.readFileToString(notherMapFile.getAbsolutePath()), token);
		  } catch (Exception e) {
            e.printStackTrace();
		  }

	  }

	public boolean addMotherWord(String s) {
		try {
			motherSet.add(s);
			saveMotherMap();
		  } catch (Exception e) {
			return false;
		  }
		return true;
	  }

	@Override
	public void run() {
		while (true) {
			try {
				for (LivePerson lp:Autoreply.instence.liveListener.livePersonMap.values()) {
					String json=readDanmakuData(Autoreply.instence.cookieManager.cookie.Sunny, lp.roomID);
					HistoryDanmaku hd=new Gson().fromJson(json, HistoryDanmaku.class);
					ArrayList<MsgBean> msgs=hd.data.room;
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (MsgBean jo:msgs) {
						try {
							long timeStamp=simpleDateFormat.parse(jo.timeline).getTime();				
							if (System.currentTimeMillis() - timeStamp > 5 * 60 * 1000) {
								continue;
							  }
							String name=jo.nickname;
							String msg=jo.text;
							long uid=jo.uid; 
							if (!peopleSet.contains(jo)) {			
								if (containsMother(jo.text) && jo.text.contains("点歌")) {
									//System.out.println("你点你妈呢");
									Autoreply.instence.naiManager.sendDanmaku(lp.roomID, Autoreply.instence.cookieManager.cookie.Sunny, "您点您妈呢");
									Autoreply.instence.naiManager.sendDanmaku(lp.roomID, Autoreply.instence.cookieManager.cookie.Luna, "您点您妈呢");
									Autoreply.instence.naiManager.sendDanmaku(lp.roomID, Autoreply.instence.cookieManager.cookie.Star, "您点您妈呢");				
								  }
								PersonInfo pi1=Autoreply.instence.configManager.getPersonInfoFromBid(uid);
								PersonInfo pi2=Autoreply.instence.configManager.getPersonInfoFromLiveId(Integer.parseInt(lp.roomID));
								String n1=pi1 == null ?name: pi1.name;
								Autoreply.instence.sendMessage(1023432971, 0, n1 + "出现在" + pi2.name + "的直播间" + lp.roomID);
							  }
							peopleSet.add(jo);
						  } catch (ParseException e) {
							e.printStackTrace();
						  }
					  }
					Iterator it=peopleSet.iterator();
					while (it.hasNext()) {
						MsgBean entry= (MsgBean) it.next();
						if (System.currentTimeMillis() - simpleDateFormat.parse(entry.timeline).getTime() > 10 * 60 * 1000) {
							it.remove(); 
						  }
					  }
				  }
			  } catch (Exception e) {
				e.printStackTrace();
			  }
			try {
				Thread.sleep(2000);
			  } catch (InterruptedException e) {

			  } 
		  }
	  }

	public String readDanmakuData(String cookie, final String roomId) {
        Connection.Response response = null;
        try {
            Connection connection = Jsoup.connect("https://api.live.bilibili.com/ajax/msg");
            String csrf =Methods. cookieToMap(cookie).get("bili_jct");
            connection.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0")
			  .headers(liveHead)
			  .ignoreContentType(true)
			  .referrer("https://live.bilibili.com/" + roomId)
			  .cookies(Methods.cookieToMap(cookie))
			  .method(Connection.Method.POST)
			  .data("roomid", roomId)
			  .data("visit_id", "")
			  .data("csrf_token", csrf)
			  .data("csrf", csrf);
            response = connection.execute();
            if (response.statusCode() != 200) {
                System.out.println(response.statusCode());
			  }
            return response.body();
		  } catch (Exception e) {
			return null;
		  }
	  } 

	private boolean containsMother(String msg) {
		for (String s:motherSet) {
			if (msg.contains(s)) {
				return true;
			  }
		  }
		return false;
	  }

	private void saveMotherMap() {
        try {
            File file = new File(Autoreply.appDirectory + "mother.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(motherSet));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
