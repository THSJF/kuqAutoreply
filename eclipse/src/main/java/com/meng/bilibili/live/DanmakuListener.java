package com.meng.bilibili.live;

import com.google.gson.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.groupChat.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class DanmakuListener extends WebSocketClient {

	public PersonInfo roomMaster;
	public ConcurrentHashMap<Long,Long> peopleMap=new ConcurrentHashMap<>();
	public Repeater repeater;

	public DanmakuListener(URI uri, PersonInfo roomMaster) {
		super(uri);
		this.roomMaster = roomMaster;
		repeater = new Repeater();
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
			if (System.currentTimeMillis() - entry.getValue() > 60 * 60 * 1000) {
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
		try {
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
					Autoreply.instence.naiManager.grzxMsg(roomMaster.bliveRoom + "", s);
				  }
			  } 
		  } catch (JsonSyntaxException je) {
			System.out.println(dp.body);
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
		String r=repeater.dealMsg(msg);
		if (r != null) {
			return r;
		  }
		String r1=Autoreply.instence.seqManager.dealMsg(0, 0, msg);
		if (r1 != null) {
			return r1;
		  }
		return null;
	  }

  }
