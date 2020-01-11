package com.meng.gameData.TouHou;

import com.meng.config.*;
import java.net.*;
import java.nio.*;
import java.util.*;
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
		if (dataRec.getVersion() < 2) {
			sdp = SanaeDataPack.encode(SanaeDataPack._0notification, dataRec);
			sdp.write("旧版本已弃用");	
		}
		switch (dataRec.getOpCode()) {
			case SanaeDataPack._40addQuestion:
				TouHouKnowledge.QA qa40= new TouHouKnowledge.QA();
				qa40.id = dataRec.readInt();
				qa40.type = dataRec.readInt();
				qa40.d = dataRec.readInt();
				qa40.q = dataRec.readString();
				int ans40=dataRec.readInt();
				qa40.t = dataRec.readInt();
				for (int i=0;i < ans40;++i) {
					String s=dataRec.readString();
					if (!s.equals("")) {
						qa40.a.add(s);
					}
				}
				qa40.r = dataRec.readString();
				if (qa40.r.equals("")) {
					qa40.r = null;
				}
				TouHouKnowledge.ins.addQA(qa40);
				sdp = SanaeDataPack.encode(SanaeDataPack._0notification, dataRec);
				sdp.write("添加成功");
				break;
			case SanaeDataPack._41getAllQuestion:
				sdp = writeQA(TouHouKnowledge.ins.qaList);
				break;
			case SanaeDataPack._43setQuestion:
				TouHouKnowledge.QA qa43= new TouHouKnowledge.QA();
				qa43.id = dataRec.readInt();
				qa43.type = dataRec.readInt();
				qa43.d = dataRec.readInt();
				qa43.q = dataRec.readString();
				int ans43=dataRec.readInt();
				qa43.t = dataRec.readInt();
				for (int i=0;i < ans43;++i) {
					String s=dataRec.readString();
					if (!s.equals("")) {
						qa43.a.add(s);
					}
				}
				qa43.r = dataRec.readString();
				if (qa43.r.equals("")) {
					qa43.r = null;
				}
				TouHouKnowledge.ins.setQA(qa43);
				sdp = SanaeDataPack.encode(SanaeDataPack._0notification, dataRec);
				sdp.write("添加成功");
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
	private SanaeDataPack writeQA(ArrayList<TouHouKnowledge.QA> qas) {
		SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack._42retAllQuestion);
		for (TouHouKnowledge.QA qa:qas) {
			sdp.write(qa.id);//flag
			sdp.write(qa.type);
			sdp.write(qa.d);//diff
			sdp.write(qa.q);//ques
			sdp.write(qa.a.size());//ansCount
			sdp.write(qa.t);
			for (String s:qa.a) {
				sdp.write(s);
			}
			sdp.write(qa.r == null ?"": qa.r);
		}
		return sdp;
	}
}
	
