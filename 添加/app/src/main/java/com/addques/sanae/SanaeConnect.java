package com.addques.sanae;

import com.addques.*;
import java.net.*;
import java.nio.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;
import android.os.*;
import java.io.*;

public class SanaeConnect extends WebSocketClient {

	String folder=Environment.getExternalStorageDirectory() + "/pictures/sanae/questions/";
	public SanaeConnect(URI uri) {
		super(uri);
	}

	@Override
	public void onMessage(String p1) {

	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		TabActivity.ins.showToast("连接到苗");
		TabActivity.ins.sanaeConnect.send(SanaeDataPack.encode(SanaeDataPack.opAllQuestion).getData());
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		SanaeDataPack dataPackRecieved=SanaeDataPack.decode(bs.array());
		SanaeDataPack dataToSend=null;
		switch (dataPackRecieved.getOpCode()) {
			case SanaeDataPack.opNotification:
				TabActivity.ins.showToast(dataPackRecieved.readString());
				break;
			case SanaeDataPack.opAllQuestion:
				TabActivity.ins.alAllQa.clear();
				readQAs(dataPackRecieved);
				TabActivity.ins.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TabActivity.ins.quesAdapter.notifyDataSetChanged();
						}
					});
				break;
			case SanaeDataPack.opQuestionPic:
				File ffo=new File(folder);
				if (!ffo.exists()) {
					ffo.mkdirs();
				}
				int id=dataPackRecieved.readInt();
				dataPackRecieved.readFile(folder, id + ".jpg");
				break;
			default:
				dataToSend = SanaeDataPack.encode(SanaeDataPack.opNotification, dataPackRecieved);
				dataToSend.write("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend.getData());
			} catch (WebsocketNotConnectedException e) {
				TabActivity.ins.showToast("和苗的连接已断开");
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
		while (sdp.hasNext()) {
			QA qa=new QA();
			qa.setFlag(sdp.readInt());
			qa.l = sdp.readInt();
			qa.q = sdp.readString();
			File img=new File(folder + qa.getId() + ".jpg");
			if (qa.q.contains("(image)")) {
				if (!img.exists() || (int)img.length() != qa.l) {
					SanaeDataPack sa=SanaeDataPack.encode(SanaeDataPack.opQuestionPic);
					sa.write(qa.getId());
					send(sa.getData());
				}
			}
			int anss=sdp.readInt();
			qa.t = sdp.readInt();
			for (int i=0;i < anss;++i) {
				qa.a.add(sdp.readString());
			}
			qa.r = sdp.readString();
			TabActivity.ins.alAllQa.add(qa);
		}
	}
}
