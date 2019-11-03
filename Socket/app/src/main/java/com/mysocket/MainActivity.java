package com.mysocket;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity {
	Button connect,send;
	EditText input,result;
	DanmakuListener danmakuListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		connect = (Button) findViewById(R.id.mainButtonConnect);
		send = (Button) findViewById(R.id.mainButtonSend);
		input = (EditText) findViewById(R.id.mainEditText);
		result = (EditText) findViewById(R.id.mainEditTextResult);
		connect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
				//	showToast("开始连接");
					try {
						danmakuListener = new DanmakuListener(new URI("ws://123.207.65.93:9961"));
						danmakuListener.connect();
					} catch (URISyntaxException e) {
						showToast(e.toString());
					}
				}
			});
		send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					{
						DataPack dp=DataPack.encode(DataPack._1verify,System.currentTimeMillis());
						dp.write1(2057480282L);
						danmakuListener.send(dp.getData());
					}
					DataPack dp=DataPack.encode(DataPack._10getPersonInfoByQQ,System.currentTimeMillis());
					dp.write1(2856986197L);
					danmakuListener.send(dp.getData());
				}
			});
    }
	
	public class DanmakuListener extends WebSocketClient {

		public DanmakuListener(URI uri) {
			super(uri);
		}

		@Override
		public void onMessage(String p1) {
			// TODO: Implement this method
		}

		@Override
		public void onOpen(ServerHandshake serverHandshake) {
			showToast("connected");
		}

		@Override
		public void onMessage(ByteBuffer bs) {	
		showToast(new String(bs.array(),18,bs.array().length-18));
		}

		@Override
		public void onClose(int i, String s, boolean b) {

		}

		@Override
		public void onError(Exception e) {
			showToast(e.toString());
		}
	}
	private void showToast(final String s){
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
				}
			});
	}
}
