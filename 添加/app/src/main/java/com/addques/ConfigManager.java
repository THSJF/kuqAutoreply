package com.addques;

import java.net.*;
import java.nio.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;

public class ConfigManager extends WebSocketClient {

	public ConfigManager(URI uri) {
		super(uri);
	}

	@Override
	public void onMessage(String p1) {

	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		send(SanaeDataPack.encode(SanaeDataPack._41getAllQuestion).getData());
		//MainActivity.instence.showToast("连接到苗");
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		SanaeDataPack dataPackRecieved=SanaeDataPack.decode(bs.array());
		SanaeDataPack dataToSend=null;
		switch (dataPackRecieved.getOpCode()) {
			case SanaeDataPack._0notification:
				break;
			case SanaeDataPack._42retAllQuestion:
				readQAs(dataPackRecieved);

				break;
			default:
				dataToSend = SanaeDataPack.encode(SanaeDataPack._0notification, dataPackRecieved);
				dataToSend.write("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend.getData());
			} catch (WebsocketNotConnectedException e) {
				MainActivity.instence.showToast("和苗的连接已断开");
				reconnect();
			}
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onError(Exception e) {

	}
	private void readQAs(SanaeDataPack sdp) {
		StringBuilder sb=new StringBuilder();
		while (sdp.hasNext()) {
			QA qa=new QA();
			qa.id = sdp.readInt();
			qa.d = sdp.readInt();
			qa.q = sdp.readString();
			int anss=sdp.readInt();
			qa.t = sdp.readInt();
			for (int i=0;i < anss;++i) {
				qa.a.add(sdp.readString());
			}
			qa.r = sdp.readString();
			Activity2.qas.add(qa);
			sb.append(qa.toString());
		}
		MainActivity.instence.showToast(sb.toString());
	}
}
