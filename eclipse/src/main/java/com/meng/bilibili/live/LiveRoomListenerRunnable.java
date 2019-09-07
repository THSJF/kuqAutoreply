package com.meng.bilibili.live;

import com.google.gson.*;
import java.text.*;
import java.util.*;
import org.jsoup.*;
import com.meng.tools.*;
import com.meng.*;
import com.meng.config.javabeans.*;

public class LiveRoomListenerRunnable implements Runnable {
	public Map<String,String> liveHead=new HashMap<>();
		public HashMap<Long,Long> peopleMap=new HashMap<>();

	public LiveRoomListenerRunnable() {
		liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://live.bilibili.com");
	  }

	@Override
	public void run() {
		while (true) {
			try{
				for(LivePerson lp:Autoreply.instence.liveListener.livePersonMap.values()){

					String json=readDanmakuData(Autoreply.instence.cookieManager.cookie.Sunny, lp.roomID);
					JsonObject jobj=new JsonParser().parse(json).getAsJsonObject();
					JsonArray jaar=jobj.get("data").getAsJsonObject().get("room").getAsJsonArray();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					for (JsonElement jo:jaar) {
						try {
							long timeStamp=simpleDateFormat.parse(jo.getAsJsonObject().get("timeline").getAsString()).getTime();
							//	String name=jo.get("nickname").getAsString();
							long uid=jo.getAsJsonObject().get("uid").getAsLong();
							if (System.currentTimeMillis() - timeStamp > 60 * 60 * 1000) {
								continue;
							  }
							if (!peopleMap.containsKey(uid)) {
								peopleMap.put(uid, timeStamp);
								PersonInfo pi1=Autoreply.instence.configManager.getPersonInfoFromBid(uid);
								PersonInfo pi2=Autoreply.instence.configManager.getPersonInfoFromLiveId(Integer.parseInt(lp.roomID));

								System.out.println(pi1.name + "出现在"+pi2.name+"的直播间"+lp.roomID);
							  }							
						  } catch (ParseException e) {
							e.printStackTrace();
						  }
					  }
					Iterator it=peopleMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Long,Long> entry=(Map.Entry<Long, Long>) it.next();
						if (System.currentTimeMillis() - entry.getValue() > 60 * 60 * 1000) {
							it.remove(); 
						  }
					  }
					
				  }
			}catch(Exception e){
			  e.printStackTrace();
			  }
			  try {
				Thread.sleep(1000);
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
}
