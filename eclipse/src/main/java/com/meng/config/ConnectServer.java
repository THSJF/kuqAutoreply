package com.meng.config;

import java.io.*;
import java.net.*;
import java.nio.*;
import org.java_websocket.*;
import org.java_websocket.handshake.*;
import org.java_websocket.server.*;

public class ConnectServer extends WebSocketServer {

	public ConnectServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	  }

	public ConnectServer(InetSocketAddress address) {
		super(address);
	  }

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
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
	conn.send(message.array());
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

