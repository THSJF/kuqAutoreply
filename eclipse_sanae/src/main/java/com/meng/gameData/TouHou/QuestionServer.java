package com.meng.gameData.TouHou;

import com.meng.*;
import com.meng.config.*;
import com.meng.dice.*;
import com.meng.tools.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import org.java_websocket.*;
import org.java_websocket.handshake.*;
import org.java_websocket.server.*;

public class QuestionServer extends WebSocketServer {

	public QuestionServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("websocket连接");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("websocket断开");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {

	}
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		SanaeDataPack dataRec=SanaeDataPack.decode(message.array());
		SanaeDataPack sdp=null;
		switch (dataRec.getOpCode()) {
			case SanaeDataPack._40addQuestion:
				TouHouKnowledge.QABuilder qab= new TouHouKnowledge.QABuilder();
				qab.setFlag(dataRec.readInt());
				qab.setQuestion(dataRec.readString());
				int anss=dataRec.readInt();
				qab.setTrueAnswer(dataRec.readInt());
				for (int i=0;i < anss;++i) {
					qab.setAnswer(dataRec.readString());
				}
				qab.setReason(dataRec.readString());
				TouHouKnowledge.ins.addQA(qab.build());
				sdp = SanaeDataPack.encode(SanaeDataPack._0notification, dataRec);
				sdp.write("添加成功");
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
		System.out.println("quesServer started!");
		setConnectionLostTimeout(100);
	}
}
	
