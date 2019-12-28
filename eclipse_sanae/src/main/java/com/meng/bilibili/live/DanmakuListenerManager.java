package com.meng.bilibili.live;

import com.google.gson.*;
import com.meng.bilibili.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class DanmakuListenerManager {
	public static DanmakuListenerManager ins;
	public HashSet<DanmakuListener> listener=new HashSet<>();

	public DanmakuListenerManager() {

	}
	public DanmakuListener getListener(int room) {
		for (DanmakuListener dl:listener) {
			if (dl.liveMaster.roomID == room) {
				return dl;
			}
		}
		return null;
	}

	public class DanmakuListener extends WebSocketClient {
		public BiliMaster liveMaster;
		public DanmakuListener(URI uri, BiliMaster liveMaster) {
			super(uri);
			this.liveMaster = liveMaster;
		}

		@Override
		public void onMessage(String p1) {
			// TODO: Implement this method
		}

		@Override
		public void onOpen(ServerHandshake serverHandshake) {
			send(encode(7, "{\"platform\": \"web\",\"protover\": 1,\"roomid\": " + liveMaster.roomID + ",\"uid\": 0,\"type\": 2}").data);
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
					//	Autoreply.instence.sendMessage(666247478, 0,  pi2.name + roomMaster.bliveRoom + " " + n1 + ":" + text);
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
			DanmakuListenerManager.ins.listener.remove(this);
			super.close();
		}

		public class DataPackage {
			public byte[] data;
			private int pos=0;

			public int length;
			public short headLen;
			public short version;
			public int op;
			public int seq;
			public String body="";

			public DataPackage(int opCode, String jsonStr) {
				byte[] jsonByte=jsonStr.getBytes();
				data = new byte[16 + jsonByte.length];
				write(getBytes(length = data.length));
				write(getBytes(headLen = (short)16));
				write(getBytes(version = (short)1));
				write(getBytes(op = opCode));
				write(getBytes(seq = 1));
				write(jsonByte);
			}   

			public DataPackage(byte[] pack, int offset) {
				data = pack;
				pos = offset;
				length = readInt();
				headLen = readShort();
				version = readShort();
				op = readInt();
				seq = readInt();
				try {
					body = new String(data, 16, length - 16, "utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("unsupported encoding");
				}
				data = null;
			}

			private void write(byte[] bs) {
				for (int i=0;i < bs.length;++i) {
					data[pos++] = bs[i];
				}
			}

			private byte[] getBytes(int i) {
				byte[] bs=new byte[4];
				bs[0] = (byte) ((i >> 24) & 0xff);
				bs[1] = (byte) ((i >> 16) & 0xff);
				bs[2] = (byte) ((i >> 8) & 0xff);
				bs[3] = (byte) (i & 0xff);
				return bs;	
			}

			private byte[] getBytes(short s) {
				byte[] bs=new byte[2];
				bs[0] = (byte) ((s >> 8) & 0xff);
				bs[1] = (byte) (s & 0xff) ;
				return bs;	
			}
			/*大端模式*/
			public short readShort() {
				return (short) ((data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0);
			}

			public int readInt() {
				return (data[pos++] & 0xff) << 24 | (data[pos++] & 0xff) << 16 | (data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0;
			}

		}
	}

}
