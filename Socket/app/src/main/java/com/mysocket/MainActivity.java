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
import com.google.gson.*;

public class MainActivity extends Activity {
	Button connect,send;
	EditText op,ets1,ets2,ets3,etn1,etn2,etn3,result;
	DanmakuListener danmakuListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		connect = (Button) findViewById(R.id.mainButtonConnect);
		send = (Button) findViewById(R.id.mainButtonSend);

		op = (EditText)findViewById(R.id.opCode);

		ets1 = (EditText)findViewById(R.id.ets1);
		ets2 = (EditText)findViewById(R.id.ets2);
		ets3 = (EditText)findViewById(R.id.ets3);

		etn1 = (EditText)findViewById(R.id.etn1);
		etn2 = (EditText)findViewById(R.id.etn2);
		etn3 = (EditText)findViewById(R.id.etn3);

		result = (EditText)findViewById(R.id.mainEditTextResult);

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
					DataPack dp=DataPack.encode((short)Integer.parseInt(op.getText().toString()) , System.currentTimeMillis());
					if (!ets1.getText().toString().equals("")) {
						dp.write(1, ets1.getText().toString());
					}
					if (!ets2.getText().toString().equals("")) {
						dp.write(2, ets2.getText().toString());
					}
					if (!ets3.getText().toString().equals("")) {
						dp.write(3, ets3.getText().toString());
					}
					if (!etn1.getText().toString().equals("")) {
						dp.write(1, Long.parseLong(etn1.getText().toString()));
					}
					if (!etn2.getText().toString().equals("")) {
						dp.write(2, Long.parseLong(etn2.getText().toString()));
					}
					if (!etn3.getText().toString().equals("")) {
						dp.write(3, Long.parseLong(etn3.getText().toString()));
					}	 
					danmakuListener.send(dp.getData());
					send.setText(new Gson().toJson(dp.ritsukageBean));
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
							DataPack dp=DataPack.encode(DataPack._17heartBeat, System.currentTimeMillis());
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
			DataPack dp=DataPack.decode(bs.array());
			if (dp.getOpCode() == 21) {
				saveFile(System.currentTimeMillis() + "", bs.array());
			} else {
				result.setText(new String(bs.array(), DataPack.headLength, bs.array().length - DataPack.headLength));
			}
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
