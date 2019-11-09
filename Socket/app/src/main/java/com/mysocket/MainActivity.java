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
import java.util.*;
import android.widget.AdapterView.*;
import android.content.*;

public class MainActivity extends Activity {
	Button send;
	EditText op,ets1,ets2,ets3,etn1,etn2,etn3;
	TextView result;
	ListView lv;
	DanmakuListener danmakuListener;
	ArrayList<String> recieved=new ArrayList<>();
	ArrayAdapter<String> adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		send = (Button) findViewById(R.id.mainButtonSend);
		op = (EditText)findViewById(R.id.opCode);
		ets1 = (EditText)findViewById(R.id.ets1);
		ets2 = (EditText)findViewById(R.id.ets2);
		ets3 = (EditText)findViewById(R.id.ets3);
		etn1 = (EditText)findViewById(R.id.etn1);
		etn2 = (EditText)findViewById(R.id.etn2);
		etn3 = (EditText)findViewById(R.id.etn3);
		result = (TextView)findViewById(R.id.mainEditTextResult);
		lv = (ListView)findViewById(R.id.mainListView);
		send.setOnClickListener(onClick);
		adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recieved);
		lv.setAdapter(adp);
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					String s=(String) p1.getItemAtPosition(p3);
					Intent inte=new Intent(MainActivity.this, Activity2.class);
					inte.putExtra("content", s);
					startActivity(inte);
				}
			});
		try {
			danmakuListener = new DanmakuListener(new URI("ws://123.207.65.93:9961"));
			danmakuListener.connect();
		} catch (URISyntaxException e) {
			showToast(e.toString());
		}
    }



	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
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
					result.setText("发送内容:\n" + new Gson().toJson(dp.ritsukageBean).replaceAll(",\"s[1-9]\":\"\"", "").replaceAll(",\"n[1-9]\":0", ""));
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
			} else if (dp.getOpCode() != 17) {
				//	result.setText(new String(bs.array(), DataPack.headLength, bs.array().length - DataPack.headLength));
				recieved.add(0, "opCode:" + dp.getOpCode() + " json:" + new String(bs.array(), DataPack.headLength, bs.array().length - DataPack.headLength).replaceAll(",\"s[1-9]\":\"\"", "").replaceAll(",\"n[1-9]\":0", ""));
				runOnUiThread(new Runnable(){

						@Override
						public void run() {
							adp.notifyDataSetChanged();
						}
					});
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
