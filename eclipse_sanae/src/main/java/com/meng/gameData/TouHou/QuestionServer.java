package com.meng.gameData.TouHou;

import com.meng.config.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.*;
import org.java_websocket.handshake.*;
import org.java_websocket.server.*;
import java.io.*;
import com.meng.*;

public class QuestionServer extends WebSocketServer {

	public QuestionServer(int port) {
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
		switch (dataRec.getVersion()) {
			case 1:
			case 2:
			case 3:
				sdp = SanaeDataPack.encode(SanaeDataPack.opNotification, dataRec);
				sdp.write("旧版本已弃用");
				break;
			case 4:
				switch (dataRec.getOpCode()) {
					case SanaeDataPack.opAddQuestion:
						TouHouKnowledge.QA qa40= new TouHouKnowledge.QA();
						qa40.setFlag(dataRec.readInt());
						qa40.q = dataRec.readString();
						int ans40=dataRec.readInt();
						qa40.t = dataRec.readInt();
						for (int i=0;i < ans40;++i) {
							qa40.a.add(dataRec.readString());
						}
						qa40.r = dataRec.readString();
						if (qa40.r.equals("")) {
							qa40.r = null;
						}
						if (dataRec.hasNext()) {
							qa40.l = (int)dataRec.readFile(TouHouKnowledge.ins.imagePath, TouHouKnowledge.ins.qaList.size() + ".jpg").length();
						}
						TouHouKnowledge.ins.addQA(qa40);
						sdp = SanaeDataPack.encode(SanaeDataPack.opNotification, dataRec);
						sdp.write("添加成功");
						break;
					case SanaeDataPack.opAllQuestion:
						sdp = writeQA(TouHouKnowledge.ins.qaList);
						break;
					case SanaeDataPack.opSetQuestion:
						TouHouKnowledge.QA qa43= new TouHouKnowledge.QA();
						qa43.setFlag(dataRec.readInt());
						qa43.q = dataRec.readString();
						int ans43=dataRec.readInt();
						qa43.t = dataRec.readInt();
						for (int i=0;i < ans43;++i) {
							qa43.a.add(dataRec.readString());
						}
						qa43.r = dataRec.readString();
						if (qa43.r.equals("")) {
							qa43.r = null;
						}
						if (dataRec.hasNext()) {
							qa43.l = (int)dataRec.readFile(TouHouKnowledge.ins.imagePath, qa43.getId() + ".jpg").length();
						}
						TouHouKnowledge.ins.setQA(qa43);
						sdp = SanaeDataPack.encode(SanaeDataPack.opNotification, dataRec);
						sdp.write("修改成功");
						break;
					case SanaeDataPack.opQuestionPic:
						sdp = SanaeDataPack.encode(SanaeDataPack.opQuestionPic, dataRec);
						int id = dataRec.readInt();
						File img = new File(TouHouKnowledge.ins.imagePath + id + ".jpg");
						sdp.write(id);
						sdp.write(img);
						break;
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
		System.out.println("quesServer started!");
		setConnectionLostTimeout(100);
	}
	private SanaeDataPack writeQA(ArrayList<TouHouKnowledge.QA> qas) {
		SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack.opAllQuestion);
		for (TouHouKnowledge.QA qa:qas) {
			sdp.write(qa.getFlag());//flag
			sdp.write(qa.l);
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
	
