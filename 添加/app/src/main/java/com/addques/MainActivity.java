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

	Button send,clean;
	EditText ques,ans1,ans2,ans3,ans4,reason;
	Spinner diff;
	int idiff=0;
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
		clean = (Button) findViewById(R.id.clean);
		send = (Button) findViewById(R.id.mainButtonSend);
		ques = (EditText)findViewById(R.id.ques);
		diff = (Spinner) findViewById(R.id.diff);
		trueAns = (RadioGroup)findViewById(R.id.trueans);
		ans1 = (EditText)findViewById(R.id.ans1);
		ans2 = (EditText)findViewById(R.id.ans2);
		ans3 = (EditText)findViewById(R.id.ans3);
		ans4 = (EditText)findViewById(R.id.ans4);
		reason = (EditText)findViewById(R.id.reason);
		result = (TextView)findViewById(R.id.mainEditTextResult);
		send.setOnClickListener(onClick);
		clean.setOnClickListener(onClick);
		ArrayAdapter<String> adpt=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"easy","normal","hard","lunatic","overdrive"});
		diff.setAdapter(adpt);
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
		try {
			configManager = new ConfigManager(new URI("ws://123.207.65.93:9001"));
			configManager.connect();
			new Thread(new Runnable(){

					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(30000);
							} catch (InterruptedException e) {}
							configManager.send("heart");
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
					sdp.write(idiff << 24);//flag
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
					ans1.setText("");
					ans2.setText("");
					ans3.setText("");
					ans4.setText("");
					reason.setText("");
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

