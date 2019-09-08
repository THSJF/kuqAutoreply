package com.meng.bilibili.live;

import com.google.gson.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class DanmakuListener extends WebSocketClient {

	public int room;
	public ConcurrentHashMap<Long,Long> peopleMap=new ConcurrentHashMap<>();


	public DanmakuListener(URI uri, int room) {
		super(uri);
		this.room = room;
	  }

	@Override
	public void onMessage(String p1) {
		// TODO: Implement this method
	  }

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		send(encode(7, "{\"platform\": \"web\",\"protover\": 1,\"roomid\": " + room + ",\"uid\": 0,\"type\": 2}").data);
		new Thread(new Runnable(){

			  @Override
			  public void run() {
				  while (true) {
					  send(encode(2, "").data);
					  try {
						  Thread.sleep(30000);
						} catch (InterruptedException e) {}
					}
				}
			}).start();
		System.out.println("open");
	  }

	@Override
	public void onMessage(ByteBuffer bs) {	
		Iterator it=peopleMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long,Long> entry= (Map.Entry<Long, Long>) it.next();
			if (System.currentTimeMillis() - entry.getValue() > 10 * 60 * 1000) {
				it.remove(); 
			  }
		  }
		DataPackage dp=decode(bs.array());
		System.out.println("on message:" + dp.body);
		if (dp.op == 5) {
			JsonObject jobj=new JsonParser().parse(dp.body).getAsJsonObject();
			if (jobj.get("cmd").getAsString().equals("DANMU_MSG")) {
				JsonArray jaar=jobj.get("info").getAsJsonArray();
				JsonArray jaar2=jaar.get(2).getAsJsonArray();
				String text=jaar.get(1).getAsString();
				String name=jaar2.get(1).getAsString();
				long uid=jaar2.get(0).getAsLong();
				System.out.println("uid:" + uid + " name:" + name + " text:" + text);

				if (peopleMap.get(uid) == null) {
				  	PersonInfo pi1=Autoreply.instence.configManager.getPersonInfoFromBid(uid);
					PersonInfo pi2=Autoreply.instence.configManager.getPersonInfoFromLiveId(room);
					String n1=pi1 == null ?name: pi1.name;
					//Autoreply.instence.sendMessage(1023432971, 0, n1 + "出现在" + pi2.name + "的直播间" + room);
					Autoreply.instence.sendMessage(1023432971, 0, n1 + "在" + pi2.name + "的直播间" + room + "说:" + text);
				  }
				peopleMap.put(uid, System.currentTimeMillis());
				if (Autoreply.instence.danmakuListenerManager.containsMother(text) && text.contains("点歌")) {
					//System.out.println("你点你妈呢");
					try {
			//			Autoreply.instence.naiManager.sendDanmaku(room + "", Autoreply.instence.cookieManager.cookie.Sunny, "您点您妈呢");
				//		Autoreply.instence.naiManager.sendDanmaku(room + "", Autoreply.instence.cookieManager.cookie.Luna, "您点您妈呢");
				//		Autoreply.instence.naiManager.sendDanmaku(room + "", Autoreply.instence.cookieManager.cookie.Star, "您点您妈呢");				
					  } catch (Exception e) {

					  }
				  }
			  }
		  }
	  }

	@Override
	public void onClose(int i, String s, boolean b) {

	  }

	@Override
	public void onError(Exception e) {
		e.printStackTrace();
	  }

	public DataPackage encode(int op, String body) {
		return new DataPackage(op, body);
	  }

	public DataPackage decode(byte[] pack) {
		return new DataPackage(pack);
	  }

	@Override
	public void close() {
		Autoreply.instence.danmakuListenerManager.listener.remove(this);
		super.close();
	  }

  }
