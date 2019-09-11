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

public class ConnectServer extends WebSocketServer {

  private ConfigJavaBean configJavaBean;
	public ConnectServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		configJavaBean=Autoreply.instence.configManager.configJavaBean;
	  }

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
	//	conn.send("Welcome to the server!"); //This method sends a message to the new client
		conn.send(DataPack.encode(27,new Gson().toJson(configJavaBean)).getData());
		//	broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
	//	System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
	  }

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	//	broadcast(conn + " has left the room!");
	//	System.out.println(conn + " has left the room!");
	  }

	@Override
	public void onMessage(WebSocket conn, String message) {
	//	broadcast(message);
	//	System.out.println(conn + ": " + message);
	  }
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
	//	broadcast(message.array());
	//	System.out.println(conn + ": " + message);
	//conn.send(message.array());
	DataPack dp=DataPack.decode(message.array());
	Gson gson=new Gson();
		switch (dp.getOpCode()) {
            case 0:
			  configJavaBean.groupConfigs.add(gson.fromJson(dp.getString(), GroupConfig.class));
			  break;
            case 1:
			  configJavaBean.QQNotReply.add(Long.parseLong(dp.getString()));
			  break;
            case 2:
			  configJavaBean.wordNotReply.add(dp.getString());
			  break;
            case 3:
			  configJavaBean.personInfo.add(gson.fromJson(dp.getString(), PersonInfo.class));
			  break;
            case 4:
			  configJavaBean.masterList.add(Long.parseLong(dp.getString()));
			  break;
            case 5:
			  configJavaBean.adminList.add(Long.parseLong(dp.getString()));
			  break;
            case 6:
			  configJavaBean.groupAutoAllowList.add(Long.parseLong(dp.getString()));
			  break;
            case 7:
			  configJavaBean.blackListQQ.add(Long.parseLong(dp.getString()));
			  break;
            case 8:
			  configJavaBean.blackListGroup.add(Long.parseLong(dp.getString()));
			  break;
            case 9:
			  configJavaBean.groupConfigs.remove(gson.fromJson(dp.getString(), GroupConfig.class));
			  break;
            case 10:
			  configJavaBean.QQNotReply.remove(Long.parseLong(dp.getString()));
			  break;
            case 11:
			  configJavaBean.wordNotReply.remove(dp.getString());
			  break;
            case 12:
			  configJavaBean.personInfo.remove(gson.fromJson(dp.getString(), PersonInfo.class));
			  break;
            case 13:
			  configJavaBean.masterList.remove(Long.parseLong(dp.getString()));
			  break;
            case 14:
			  configJavaBean.adminList.remove(Long.parseLong(dp.getString()));
			  break;
            case 15:
			  configJavaBean.groupAutoAllowList.remove(Long.parseLong(dp.getString()));
			  break;
            case 16:
			  configJavaBean.blackListQQ.remove(Long.parseLong(dp.getString()));
			  break;
            case 17:
			  configJavaBean.blackListGroup.remove(Long.parseLong(dp.getString()));
			  break;
            case 18:
			  GroupConfig groupConfig = gson.fromJson(dp.getString(), GroupConfig.class);
			  for (GroupConfig gc : configJavaBean.groupConfigs) {
				  if (gc.groupNumber == groupConfig.groupNumber) {
					  configJavaBean.groupConfigs.remove(gc);
					  configJavaBean.groupConfigs.add(groupConfig);
					  break;
                    }
                }
			  break;
            case 19:
			  String[] split = dp.getString().split(" ");
			  long qq = Long.parseLong(split[0]);
			  for (long l : configJavaBean.QQNotReply) {
				  if (l == qq) {
					  configJavaBean.QQNotReply.remove(l);
					  configJavaBean.QQNotReply.add(Long.parseLong(split[1]));
					  break;
                    }
                }
			  break;
            case 20:
			  String[] split2 = dp.getString().split(" ");
			  for (String s : configJavaBean.wordNotReply) {
				  if (s.equals(split2[0])) {
					  configJavaBean.wordNotReply.remove(s);
					  configJavaBean.wordNotReply.add(split2[1]);
					  break;
                    }
                }
			  break;
            case 21:
			  String[] obj = dp.getString().split(" ");
			  PersonInfo oldPersonInfo = gson.fromJson(obj[0], PersonInfo.class);
			  PersonInfo newPersonInfo = gson.fromJson(obj[1], PersonInfo.class);
			  for (PersonInfo pi : configJavaBean.personInfo) {
				  if (pi.name.equals(oldPersonInfo.name) && pi.qq == oldPersonInfo.qq && pi.bid == oldPersonInfo.bid && pi.bliveRoom == oldPersonInfo.bliveRoom) {
					  configJavaBean.personInfo.remove(oldPersonInfo);
					  break;
                    }
                }
			  configJavaBean.personInfo.add(newPersonInfo);
			  break;
            case 22:
			  String[] splitmaster = dp.getString().split(" ");
			  long qqm = Long.parseLong(splitmaster[0]);
			  for (long l : configJavaBean.QQNotReply) {
				  if (l == qqm) {
					  configJavaBean.masterList.remove(l);
					  configJavaBean.masterList.add(Long.parseLong(splitmaster[1]));
					  break;
                    }
                }
			  break;
            case 23:
			  String[] splitadmin = dp.getString().split(" ");
			  long qqa = Long.parseLong(splitadmin[0]);
			  for (long l : configJavaBean.QQNotReply) {
				  if (l == qqa) {
					  configJavaBean.adminList.remove(l);
					  configJavaBean.adminList.add(Long.parseLong(splitadmin[1]));
					  break;
                    }
                }
			  break;
            case 24:
			  String[] splitgroup = dp.getString().split(" ");
			  long qqg = Long.parseLong(splitgroup[0]);
			  for (long l : configJavaBean.QQNotReply) {
				  if (l == qqg) {
					  configJavaBean.groupAutoAllowList.remove(l);
					  configJavaBean.groupAutoAllowList.add(Long.parseLong(splitgroup[1]));
					  break;
                    }
                }
			  break;
            case 25:
			  String[] splitblackqq = dp.getString().split(" ");
			  long qqblack = Long.parseLong(splitblackqq[0]);
			  for (long l : configJavaBean.blackListQQ) {
				  if (l == qqblack) {
					  configJavaBean.blackListQQ.remove(l);
					  configJavaBean.blackListQQ.add(Long.parseLong(splitblackqq[1]));
					  break;
                    }
                }
			  break;
            case 26:
			  String[] splitblackgroup = dp.getString().split(" ");
			  long blackGroup = Long.parseLong(splitblackgroup[0]);
			  for (long l : configJavaBean.blackListGroup) {
				  if (l == blackGroup) {
					  configJavaBean.blackListGroup.remove(l);
					  configJavaBean.blackListGroup.add(Long.parseLong(splitblackgroup[1]));
					  break;
                    }
                }
			  break;   
		  }
		  Autoreply.instence.configManager.saveConfig();
		  conn.send(DataPack.encode(-1,"成功").getBodyData());
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
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	  }

  }

