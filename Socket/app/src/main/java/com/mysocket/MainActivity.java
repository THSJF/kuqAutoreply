package com.mysocket;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

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
		connect.setOnClickListener(onClick);
		send.setOnClickListener(onClick);
    }

	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonConnect:
					//	showToast("开始连接");
					try {
						danmakuListener = new DanmakuListener(new URI("ws://123.207.65.93:9961"));
						danmakuListener.connect();
					} catch (URISyntaxException e) {
						showToast(e.toString());
					}
					break;
				case R.id.mainButtonSend:
					final DataPack dp=DataPack.encode((short)20 , System.currentTimeMillis());
					dp.write1(28569867L);
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
					break;
			}
		}


	};

	public class DanmakuListener extends WebSocketClient {

		public DanmakuListener(URI uri) {
			super(uri);
		}

		@Override
		public void onMessage(String p1) {
			showToast("stringMsg:" + p1);
		}

		@Override
		public void onOpen(ServerHandshake serverHandshake) {
			showToast("connected");
			new Thread(new Runnable(){

					@Override
					public void run() {
						while (true) {
							final DataPack dp=DataPack.encode(DataPack._17heartBeat, System.currentTimeMillis());
							dp.write1(2856986197L);
							danmakuListener.send(dp.getData());	
							try {
								Thread.sleep(30000);
							} catch (InterruptedException e) {}
						}
					}
				}).start();
		}

		@Override
		public void onMessage(ByteBuffer bs) {	
		//	DataPack dp=DataPack.decode(bs.array());
		//	if (dp.getOpCode() == 21) {
				saveFile(System.currentTimeMillis() + "", bs.array());
		//	} else {
		//		result.setText(new String(bs.array(), DataPack.headLength, bs.array().length - DataPack.headLength));
		//	}
		}

		@Override
		public void onClose(int i, String s, boolean b) {

		}

		@Override
		public void onError(Exception e) {
			showToast(e.toString());
		}
	}

	private void showToast(final String s) {
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
				}
			});
	}

	public void saveFile(String name, byte[] bytes) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/1/" + name + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, DataPack.headLength, bytes.length - DataPack.headLength);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
