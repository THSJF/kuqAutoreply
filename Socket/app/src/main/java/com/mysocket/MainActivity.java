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
import com.google.gson.*;

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
					final DataPack dp=DataPack.encode(DataPack._15groupBan, System.currentTimeMillis());
					dp.write1(1023432971L);
					dp.write2(2856986197L);
					dp.write3(360);
					danmakuListener.send(dp.getData());
				/*	runOnUiThread(new Runnable(){

							@Override
							public void run() {
								byte[] bss=dp.getData();
								for(int i=0;i<bss.length;++i){
									result.setText(result.getText().toString()+" "+bss[i]);
								}
								DataPack dp2=DataPack.decode(dp.getData());
								showToast(new Gson().toJson(dp.ritsukageBean));
								//showToast(dp2.sss);
								result.setText(dp2.sss+" len:"+dp2.sss.length());
							}
						});*/
				}
			});
    }
	
	public class DanmakuListener extends WebSocketClient {

		public DanmakuListener(URI uri) {
			super(uri);
		}

		@Override
		public void onMessage(String p1) {
			showToast("stringMsg:"+p1);
		}

		@Override
		public void onOpen(ServerHandshake serverHandshake) {
			showToast("connected");
		}

		@Override
		public void onMessage(ByteBuffer bs) {	
			System.out.println(new String(bs.array(),DataPack.headLength,bs.array().length-DataPack.headLength));
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
