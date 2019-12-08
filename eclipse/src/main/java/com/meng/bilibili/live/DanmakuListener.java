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
				String danmakuText=jaar.get(1).getAsString();
				String speakerName=jaar2.get(1).getAsString();
				long speakerUid=jaar2.get(0).getAsLong();
				PersonInfo speakerPersonInfo=Autoreply.instence.configManager.getPersonInfoFromBid(speakerUid);
				PersonInfo roomMasterPersonInfo=Autoreply.instence.configManager.getPersonInfoFromLiveId(roomMaster.bliveRoom);
				String finallySpeakerName=speakerPersonInfo == null ?speakerName: speakerPersonInfo.name;
				peopleMap.put(speakerUid, System.currentTimeMillis());
				//	Autoreply.instence.sendMessage(666247478, 0,  pi2.name + roomMaster.bliveRoom + " " + n1 + ":" + text);
				RitsukageDataPack dataToSend=RitsukageDataPack.encode(RitsukageDataPack._6speakInLiveRoom, System.currentTimeMillis());
				dataToSend.write(1, roomMaster.bliveRoom);
				dataToSend.write(1, roomMaster.name);
				dataToSend.write(2, finallySpeakerName);
				dataToSend.write(2, speakerUid);
				dataToSend.write(3, danmakuText);
				Autoreply.instence.connectServer.broadcast(dataToSend.getData());
				if (Autoreply.instence.danmakuListenerManager.containsMother(danmakuText) && danmakuText.startsWith("点歌")) {
					try {
						Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Sunny, "您点您妈呢");
						Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Luna, "您点您妈呢");
						Autoreply.instence.naiManager.sendDanmaku(roomMaster.bliveRoom + "", Autoreply.instence.cookieManager.cookie.Star, "您点您妈呢");				
					} catch (Exception e) {

					}
					return;
				}
				if (speakerUid == 64483321 && danmakuText.startsWith("ban.")) {
					String ss[]=danmakuText.split("\\.");
					String blockid=ss[1];
					PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(blockid);
					if (pi != null) {
						blockid = pi.bid + "";
					}
					Autoreply.instence.liveListener.setBan(Autoreply.mainGroup, roomMaster.bliveRoom + "", blockid, ss[2]);
					return;
				}
				String s=dealMsg(roomMaster.bliveRoom, speakerUid, danmakuText);
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

	class Repeater {
		private String lastMessageRecieved = "";
		private boolean lastStatus = false;
		private int repeatCount=0;

		public Repeater() {

		}

		public String dealMsg(String msg) {
			String b = null;
			if (!lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatStart(msg);
			} else if (lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatRunning(msg);
			} else if (lastStatus && !lastMessageRecieved.equals(msg)) {
				b = repeatEnd(msg);
			}
			lastStatus = lastMessageRecieved.equals(msg);
			lastMessageRecieved = msg;
			return b;
		}

		private String repeatEnd(String msg) {
			return null;
		}

		private String repeatRunning(String msg) {
			return null;
		}

		private String repeatStart(String msg) {   
			++repeatCount;
			if (msg.contains("蓝") || msg.contains("藍")) {
				return null;
			}
			if (msg.contains("此生无悔入东方")) {
				return msg;
			}
			if (repeatCount < 3) {
				++repeatCount;
				return msg;
			} else if (repeatCount == 3) {
				++repeatCount;
				return "你直播间天天复读";
			} else {
				String newmsg = new StringBuilder(msg).reverse().toString();
				repeatCount = 0;
				return newmsg.equals(msg) ? newmsg + " " : newmsg;
			}
		}
	}
}
