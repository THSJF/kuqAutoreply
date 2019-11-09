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
import com.meng.picEdit.*;
import com.meng.ocr.sign.*;

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
				for (LivePerson lp:Autoreply.instence.liveListener.livePersonMap.values()) {
					if (lp.lastStatus) {
						hashSet.add(Autoreply.instence.configManager.getPersonInfoFromLiveId(Long.parseLong(lp.roomID)));
					}		
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
				PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromName(recievedDataPack.readString(1));
				if (pi != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi);
				} else {
					dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
					dataToSend.write(1, "");
				}
				break;
			case DataPack._10getPersonInfoByQQ:
				PersonInfo pi10=Autoreply.instence.configManager.getPersonInfoFromQQ(recievedDataPack.readNum(1));
				if (pi10 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi10);
				} else {
					dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
					dataToSend.write(1, "");
				}
				break;
			case DataPack._11getPersonInfoByBid:
				PersonInfo pi11=Autoreply.instence.configManager.getPersonInfoFromBid(recievedDataPack.readNum(1));
				if (pi11 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi11);
				} else {
					dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
					dataToSend.write(1, "");
				}
				break;
			case DataPack._12getPersonInfoByBiliLive:
				PersonInfo pi12=Autoreply.instence.configManager.getPersonInfoFromLiveId(recievedDataPack.readNum(1));
				if (pi12 != null) {
					dataToSend = DataPack.encode(DataPack._13returnPersonInfo, recievedDataPack.getTimeStamp());
					dataToSend.write(pi12);
				} else {
					dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
					dataToSend.write(1, "");
				}
				break;
			case DataPack._13returnPersonInfo:
				break;
			case DataPack._14coinsAdd:
				//dp=DataPack.encode(DataPack._14coinsAdd,dp.getTimeStamp());
				//dp.write(
				break;
			case DataPack._15groupBan:
				Methods.ban(recievedDataPack.readNum(1), recievedDataPack.readNum(2), (int)recievedDataPack.readNum(3));
				dataToSend = DataPack.encode((short)0, recievedDataPack.getTimeStamp());
				dataToSend.write(1, "禁言成功");
				break;
			case DataPack._16groupKick:
				Autoreply.CQ.setGroupKick(recievedDataPack.readNum(1), recievedDataPack.readNum(2), recievedDataPack.readNum(3) == 1);
				dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
				dataToSend.write(1, "踢出群成功");
				break;
			case DataPack._17heartBeat:
				dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
				dataToSend.write(1, "心跳收到");
				break;
			case DataPack._18FindInAll:		
				long findqq=recievedDataPack.readNum(1);
				dataToSend = DataPack.encode(DataPack._19returnFind, recievedDataPack.getTimeStamp());
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
			case DataPack._20pic:
				new JingShenZhiZhuQQManager(-1, -1, Autoreply.instence.CC.at(recievedDataPack.readNum(1)));
				dataToSend = DataPack.encode(DataPack._21returnPic, recievedDataPack.getTimeStamp());
				try { 
					File jpg=new File(Autoreply.appDirectory + "jingshenzhizhu\\" + recievedDataPack.readNum(1) + ".jpg");
					long filelength = jpg.length();
					byte[] filecontent = new byte[(int) filelength];
					FileInputStream in = new FileInputStream(jpg);
					in.read(filecontent);
					in.close();
					dataToSend.writeData(filecontent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case DataPack._21returnPic:
				break;
			case DataPack._22pic2:
				new ShenChuQQManager(-1, -1, Autoreply.instence.CC.at(recievedDataPack.readNum(1)));
				dataToSend = DataPack.encode(DataPack._21returnPic, recievedDataPack.getTimeStamp());
				try { 
					File jpg=new File(Autoreply.appDirectory + "shenchu\\" + recievedDataPack.readNum(1) + ".jpg");
					long filelength = jpg.length();
					byte[] filecontent = new byte[(int) filelength];
					FileInputStream in = new FileInputStream(jpg);
					in.read(filecontent);
					in.close();
					dataToSend.writeData(filecontent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case DataPack._23returnPic2:
				break;
			case DataPack._24MD5Random:
				dataToSend = DataPack.encode(DataPack._25returnMD5Random, recievedDataPack.getTimeStamp());
				String md5=MD5.stringToMD5(String.valueOf(recievedDataPack.readNum(1) + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
				char c=md5.charAt(0);
				if (c == '0') {
					dataToSend.write(1, 9961);
				} else if (c == '1') {
					dataToSend.write(1, 9760);
				} else {
					dataToSend.write(1, Integer.parseInt(md5.substring(26), 16) % 10001);
				}
				break;
			case DataPack._25returnMD5Random:
				break;
			case DataPack._26MD5neta:
				dataToSend = DataPack.encode(DataPack._27returnMD5neta, recievedDataPack.getTimeStamp());
				dataToSend.write(1, Autoreply.instence.diceImitate.md5RanStr(recievedDataPack.readNum(1), DiceImitate.neta));
				break;
			case DataPack._27returnMD5neta:
				break;
			case DataPack._28MD5music:
				dataToSend = DataPack.encode(DataPack._29returnMD5music, recievedDataPack.getTimeStamp());
				dataToSend.write(1, Autoreply.instence.diceImitate.md5RanStr(recievedDataPack.readNum(1), DiceImitate.music));
				break;
			case DataPack._29returnMD5music:
				break;
			case DataPack._30MD5grandma:
				dataToSend = DataPack.encode(DataPack._31returnMD5grandma, recievedDataPack.getTimeStamp());
				if (MD5.stringToMD5(String.valueOf(recievedDataPack.readNum(1) + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
					dataToSend.write(1, "八云紫");
				} else {
					dataToSend.write(1, Autoreply.instence.diceImitate.md5RanStr(recievedDataPack.readNum(1), DiceImitate.name));
				}
				break;
			case DataPack._31returnMD5grandma:
				break;
			case DataPack._32MD5overSpell:
				dataToSend = DataPack.encode(DataPack._33returnMD5overSpell, recievedDataPack.getTimeStamp());
				dataToSend.write(1, Autoreply.instence.diceImitate.md5RanStr(recievedDataPack.readNum(1), DiceImitate.spells));
				break;
			case DataPack._33returnMD5overSpell:
				break;
			default:
				dataToSend = DataPack.encode(DataPack._0notification, recievedDataPack.getTimeStamp());
				dataToSend.write(1, "操作类型错误");
		} 
		ogg.send(dataToSend.getData());
		//	DataPack ndp=DataPack.encode(DataPack._0notification, dataPack.getTimeStamp());
		//	ndp.write("成功");
		//	oggConnect.send(ndp.getData());
	}

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

