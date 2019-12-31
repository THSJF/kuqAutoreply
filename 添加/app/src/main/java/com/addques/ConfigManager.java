package com.addques;

import com.addques.*;
import com.mysocket.*;
import java.lang.reflect.*;
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
		
		MainActivity.instence.showToast("连接到鬼人正邪");
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		SanaeDataPack dataPackRecieved=SanaeDataPack.decode(bs.array());
		SanaeDataPack dataToSend=null;
		switch (dataPackRecieved.getOpCode()) {
			case SanaeDataPack._0notification:
				break;
			default:
				dataToSend = SanaeDataPack.encode(SanaeDataPack._0notification, dataPackRecieved);
				dataToSend.write("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend.getData());
			} catch (WebsocketNotConnectedException e) {
				MainActivity.instence.showToast("和鬼人正邪的连接已断开");
				reconnect();
			}
		}
		MainActivity.instence.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity.instence.adp.notifyDataSetChanged();
				}	
			});
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onError(Exception e) {

	}

}
