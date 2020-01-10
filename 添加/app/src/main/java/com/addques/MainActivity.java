package com.addques;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.addques.*;
import java.net.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.widget.RadioGroup.*;

public class MainActivity extends Activity {

	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	Button send,clean,allques;
	EditText ques,ans1,ans2,ans3,ans4,reason;
	Spinner diff,typeSp;
	int idiff=0;
	int type=0;
	TextView result;
	RadioGroup trueAns;
	ConfigManager configManager;
	public ArrayList<String> recieved = new ArrayList<>();
	public static MainActivity instence;
	int trueAnswer=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		instence = this;
		allques = (Button) findViewById(R.id.allque);
		clean = (Button) findViewById(R.id.clean);
		send = (Button) findViewById(R.id.mainButtonSend);
		ques = (EditText)findViewById(R.id.ques);
		diff = (Spinner) findViewById(R.id.diff);
		typeSp = (Spinner) findViewById(R.id.type);
		trueAns = (RadioGroup)findViewById(R.id.trueans);
		ans1 = (EditText)findViewById(R.id.ans1);
		ans2 = (EditText)findViewById(R.id.ans2);
		ans3 = (EditText)findViewById(R.id.ans3);
		ans4 = (EditText)findViewById(R.id.ans4);
		reason = (EditText)findViewById(R.id.reason);
		result = (TextView)findViewById(R.id.mainEditTextResult);
		//allques.setOnClickListener(onClick);
		send.setOnClickListener(onClick);
		clean.setOnClickListener(onClick);
		diff.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"easy","normal","hard","lunatic","overdrive"}));
		diff.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					idiff = p3;
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
					// TODO: Implement this method
				}
			});
		typeSp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"未分类","车万基础","新作弹幕作","官方所有弹幕作","官方非弹幕","官方所有","同人弹幕"}));
		typeSp.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					type = p3;
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
					// TODO: Implement this method
				}
			});
		try {
			configManager = new ConfigManager(new URI("ws://123.207.65.93:9001"));
			configManager.connect();

			new Thread(new Runnable(){

					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(30000);
								configManager.send("heart");
							} catch (Exception e) {
								showToast("连接断开");
							}
						}
					}
				}).start();
		} catch (URISyntaxException e) {
			showToast(e.toString());
		}
		trueAns.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(RadioGroup p1, int p2) {
					switch (p2) {
						case R.id.r1:
							trueAnswer = 0;
							break;
						case R.id.r2:
							trueAnswer = 1;
							break;
						case R.id.r3:
							trueAnswer = 2;
							break;
						case R.id.r4:
							trueAnswer = 3;
							break;
					}		
				}
			});
    }



	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonSend:
					SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack._40addQuestion);
					sdp.write(0);
					sdp.write(type);
					sdp.write(idiff);
					sdp.write(ques.getText().toString());//ques
					sdp.write(4);//ansCount
					sdp.write(trueAnswer);
					sdp.write(ans1.getText().toString());
					sdp.write(ans2.getText().toString());
					sdp.write(ans3.getText().toString());
					sdp.write(ans4.getText().toString());
					sdp.write(reason.getText().toString());
					configManager.send(sdp.getData());
					showToast("发送成功");
					break;
				case R.id.clean:
					ques.setText("");
					ans1.setText("");
					ans2.setText("");
					ans3.setText("");
					ans4.setText("");
					reason.setText("");
					break;
				case R.id.allque:
					startActivity(new Intent(MainActivity.this, Activity2.class));
					break;
			}
		}
	};

	public void showToast(final String s) {
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
				}
			});
	}
}

