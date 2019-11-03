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
	private WebSocket oggConnect=null;

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
		if (dp.getOpCode() == DataPack._1verify) {
			if (dp.readNum() == configJavaBean.ogg) {
				oggConnect = conn;
			}
		}
		if (oggConnect != null) {
			oggProcess(dp);
		}
	}

	private void oggProcess(DataPack dp) {
		switch (dp.getOpCode()) {
			case DataPack._0notification:
				break;
			case DataPack._1verify:
				break;
			case DataPack._2getLiveList:
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
				break;
			case DataPack._10getPersonInfoByQQ:
				break;
			case DataPack._11getPersonInfoByBid:
				break;
			case DataPack._12getPersonInfoByBiliLive:
				break;
			case DataPack._13returnPersonInfo:
				break;
			case DataPack._14coinsExchange:
				break;
			case DataPack._15groupBan:
				break;
			case DataPack._16groupKick:
				break;
			case DataPack._17heartBeat:
				break;
		} 
		DataPack ndp=DataPack.encode(DataPack._0notification, dp.getTimeStamp());
		ndp.write("成功");
		oggConnect.send(ndp.getData());
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

