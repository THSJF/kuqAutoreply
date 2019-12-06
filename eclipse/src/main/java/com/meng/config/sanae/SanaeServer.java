package com.meng.config.sanae;

import com.meng.*;
import com.meng.bilibili.live.*;
import com.meng.config.javabeans.*;
import com.meng.dice.*;
import com.meng.picEdit.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.*;
import org.java_websocket.handshake.*;
import org.java_websocket.server.*;

public class SanaeServer extends WebSocketServer {

	public SanaeServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
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
		SanaeDataPack rsdp=SanaeDataPack.decode(message.array());
		SanaeDataPack sdp=null;
		switch (rsdp.getOpCode()) {
			case SanaeDataPack._1getConfig:
				sdp = SanaeDataPack.encode(SanaeDataPack._2retConfig, rsdp);
				sdp.write(Autoreply.gson.toJson(Autoreply.instence.configManager.configJavaBean));
				break;
			case SanaeDataPack._3getOverSpell:
				sdp =  SanaeDataPack.encode(SanaeDataPack._4retOverSpell, rsdp);
				sdp.write(Autoreply.instence.diceImitate.md5RanStr(rsdp.readLong(), DiceImitate.spells));
				break;
			case SanaeDataPack._5getOverPersent:
				sdp = SanaeDataPack.encode(SanaeDataPack._6retOverPersent, rsdp);
				String md5=Methods.stringToMD5(String.valueOf(rsdp.readLong() + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
				char c=md5.charAt(0);
				if (c == '0') {
					sdp.write(9961);
				} else if (c == '1') {
					sdp.write(9760);
				} else {
					sdp.write(Integer.parseInt(md5.substring(26), 16) % 10001);
				}
				break;

		}
		if (sdp != null) {
			conn.send(sdp.getData());
		}
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
