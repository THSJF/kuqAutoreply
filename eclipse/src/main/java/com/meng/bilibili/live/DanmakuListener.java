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

	public PersonInfo roomMaster;
	public ConcurrentHashMap<Long,Long> peopleMap=new ConcurrentHashMap<>();


	public DanmakuListener(URI uri, PersonInfo roomMaster) {
		super(uri);
		this.roomMaster = roomMaster;
	  }

	@Override
	public void onMessage(String p1) {
		// TODO: Implement this method
	  }

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		send(encode(7, "{\"platform\": \"web\",\"protover\": 1,\"roomid\": " + roomMaster.bliveRoom + ",\"uid\": 0,\"type\": 2}").data);
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
		byte[] bytes=bs.array();
		int offset=0;  
		do{
			DataPackage dp=decode(bytes, offset);
			offset += dp.length;
			switch (dp.op) {
				case 2:
				  break;
				case 3:
				  break;
				case 5:
				  onDanmaku(dp);
				  break;
				case 7:
				  break;
				case 8:
				  break;
			  }
		  }while(offset < bytes.length - 1);

	  }

	private void onDanmaku(DataPackage dp) {
		JsonObject jobj=new JsonParser().parse(dp.body).getAsJsonObject();
		if (jobj.get("cmd").getAsString().equals("DANMU_MSG")) {
			JsonArray jaar=jobj.get("info").getAsJsonArray();
			JsonArray jaar2=jaar.get(2).getAsJsonArray();
			String text=jaar.get(1).getAsString();
			String name=jaar2.get(1).getAsString();
			long uid=jaar2.get(0).getAsLong();
			//	System.out.println("uid:" + uid + " name:" + name + " text:" + text);
			PersonInfo pi1=Autoreply.instence.configManager.getPersonInfoFromBid(uid);
			PersonInfo pi2=Autoreply.instence.configManager.getPersonInfoFromLiveId(roomMaster.bliveRoom);
			String n1=pi1 == null ?name: pi1.name;
			if (peopleMap.get(uid) == null) {
				Autoreply.instence.sendMessage(1023432971, 0, n1 + "出现在" + pi2.name + "的直播间" + roomMaster.bliveRoom);
			  }
			peopleMap.put(uid, System.currentTimeMillis());
			Autoreply.instence.sendMessage(666247478, 0,  pi2.name + roomMaster.bliveRoom + " " + n1 + ":" + text);
			if (Autoreply.instence.danmakuListenerManager.containsMother(text) && text.startsWith("点歌")) {
				try {
					Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Sunny, "您点您妈呢");
					Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Luna, "您点您妈呢");
					Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Star, "您点您妈呢");				
				  } catch (Exception e) {

				  }
				return;
			  }
			if (uid == 64483321 && text.startsWith("ban.")) {
				String ss[]=text.split("\\.");
				String blockid=ss[1];
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(blockid);
				if (pi != null) {
					blockid = pi.bid + "";
				  }
				Autoreply.instence.liveListener.setBan(1023432971, roomMaster.bliveRoom + "", blockid, ss[2]);
				return;
			  }
			String s=dealMsg(roomMaster.bliveRoom, uid, text);
			if (s != null) {
				Autoreply.instence.naiManager.grzxMsg(roomMaster.bliveRoom + "", text);
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

	public DataPackage decode(byte[] pack, int pos) {
		return new DataPackage(pack, pos);
	  }

	@Override
	public void close() {
		Autoreply.instence.danmakuListenerManager.listener.remove(this);
		super.close();
	  }

	private String dealMsg(long fromRoom, long fromUser, String msg) {
		if (msg.equals("此生无悔入东方")) {
			return "来世愿生幻想乡";
		  }
		if (msg.equals("红魔地灵夜神雪")) {
			return "永夜风神星莲船";
		  }
		if (msg.equals("非想天则文花贴")) {
			return "萃梦神灵绯想天";
		  }
		if (msg.equals("冥界地狱异变起")) {
			return "樱下华胥主谋现";
		  }	  
		if (msg.equals("净罪无改渡黄泉")) {
			return "华鸟风月是非辨";
		  }	  
		if (msg.equals("境界颠覆入迷途")) {
			return "幻想花开啸风弄";
		  }
		if (msg.equals("二色花蝶双生缘")) {
			return "前缘未尽今生还";
		  }
		if (msg.equals("星屑洒落雨霖铃")) {
			return "虹彩彗光银尘耀";
		  }
		if (msg.equals("无寿迷蝶彼岸归")) {
			return "幻真如画妖如月";
		  } 
		if (msg.equals("永劫夜宵哀伤起")) {
			return "幼社灵中幻似梦";
		  }
		if (msg.equals("追忆往昔巫女缘")) {
			return "须弥之间冥梦现";
		  }
		if (msg.equals("境界颠覆入迷途")) {
			return "歌雅风颂心无念";
		  }
		if (msg.equals("仁榀华诞井中天")) {
			return "幻想花开啸风弄";
		  }	                         
		return null;
	  }

  }
