package com.meng.config;

import java.io.*;
import java.net.*;
import java.nio.*;
import org.java_websocket.*;
import org.java_websocket.handshake.*;
import org.java_websocket.server.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import com.google.gson.*;
import java.util.*;
import com.meng.bilibili.live.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;

public class ConnectServer extends WebSocketServer {

	private ConfigJavaBean configJavaBean;

	public ConnectServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		configJavaBean = Autoreply.instence.configManager.configJavaBean;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		//	conn.send("Welcome to the server!"); //This method sends a message to the new client
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		//	broadcast(conn + " has left the room!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {

	}
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		DataPack dp=DataPack.decode(message.array());
		System.out.println("isOgg:" + (dp.getTarget() == Autoreply.instence.configManager.configJavaBean.ogg));
		if (dp.getTarget() == Autoreply.instence.configManager.configJavaBean.ogg) {
			oggProcess(conn, dp);
		}
	}

	private void oggProcess(WebSocket ogg, DataPack recievedDataPack) {
		DataPack dataToSend=null;
		switch (recievedDataPack.getOpCode()) {
			case DataPack._0notification:
				break;
			case DataPack._1verify:
				break;
			case DataPack._2getLiveList:
				HashSet<PersonInfo> hashSet=new HashSet<>();
				for (long bid:Autoreply.instence.liveListener.livePersonMap.keySet()) {
					hashSet.add(Autoreply.instence.configManager.getPersonInfoFromBid(bid));
				}
				dataToSend = DataPack.encode(DataPack._3returnLiveList, recievedDataPack.getTimeStamp());
				dataToSend.writePersonSet(hashSet);
				break;
			case DataPack._3returnLiveList:
				break;
			case DataPack._4liveStart:
				break;
			case DataPack._5liveStop:
				break;
			case DataPack._6speakInLiveRoom:
				break;
			case DataPack._7newVideo:
				break;
			case DataPack._8newArtical:
				break;
			case DataPack._9getPersonInfoByName:
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(recievedDataPack.readString1());
				if (pi != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi);
				}
				break;
			case DataPack._10getPersonInfoByQQ:
				PersonInfo pi10=Autoreply.instence.configManager.getPersonInfoFromQQ(recievedDataPack.readNum1());
				if (pi10 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi10);
				}
				break;
			case DataPack._11getPersonInfoByBid:
				PersonInfo pi11=Autoreply.instence.configManager.getPersonInfoFromBid(recievedDataPack.readNum1());
				if (pi11 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi11);
				}
				break;
			case DataPack._12getPersonInfoByBiliLive:
				PersonInfo pi12=Autoreply.instence.configManager.getPersonInfoFromQQ(recievedDataPack.readNum1());
				if (pi12 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi12);
				}
				break;
			case DataPack._13returnPersonInfo:
				break;
			case DataPack._14coinsAdd:
				//dp=DataPack.encode(DataPack._14coinsAdd,dp.getTimeStamp());
				//dp.write(
				break;
			case DataPack._15groupBan:
				Methods.ban(recievedDataPack.readNum1(), recievedDataPack.readNum2(), (int)recievedDataPack.readNum3());
				dataToSend = DataPack.encode((short)0, recievedDataPack.getTimeStamp());
				dataToSend.write1("禁言成功");
				break;
			case DataPack._16groupKick:
				Autoreply.CQ.setGroupKick(recievedDataPack.readNum1(), recievedDataPack.readNum2(), recievedDataPack.readNum3() == 1);
				dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
				dataToSend.write1("踢出群成功");
				break;
			case DataPack._17heartBeat:
				break;
			case DataPack._18FindInAll:		
				long findqq=recievedDataPack.readNum1();
				List<Group> joinedGroups = Autoreply.CQ.getGroupList();
				HashSet<Long> qqInThis = new HashSet<>();
				for (Group group : joinedGroups) {
					if (group.getId() == 959615179L || group.getId() == 666247478L) {
						continue;
					}
					ArrayList<Member> members = (ArrayList<Member>) Autoreply.CQ.getGroupMemberList(group.getId());
					for (Member member : members) {
						if (member.getQqId() == findqq) {
							qqInThis.add(group.getId());
							break;
						}
					}
				}
				dataToSend.writeLongSet(qqInThis);
				break;
			case DataPack._19returnFind:
				break;
			default:
				dataToSend = DataPack.encode((short)0, recievedDataPack.getTimeStamp());
				dataToSend.write1("操作类型错误");
		} 
		ogg.send(dataToSend.getData());
		//	DataPack ndp=DataPack.encode(DataPack._0notification, dataPack.getTimeStamp());
		//	ndp.write("成功");
		//	oggConnect.send(ndp.getData());
	}

//	public static void main(String[] args) throws InterruptedException , IOException {
//		int port = 8887; // 843 flash policy port
//		try {
//			port = Integer.parseInt(args[0]);
//		  } catch ( Exception ex ) {
//		  }
//		ConnectServer s = new ConnectServer(port);
//		s.start();
//		System.out.println("ChatServer started on port: " + s.getPort());
//
//		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
//		while (true) {
//			String in = sysin.readLine();
//			s.broadcast(in);
//			if (in.equals("exit")) {
//				s.stop(1000);
//				break;
//			  }
//		  }
//	  }
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
		setConnectionLostTimeout(100);
	}

}

