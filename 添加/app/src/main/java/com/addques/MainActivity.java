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

public class MainActivity extends Activity {
	Button send;
	EditText ques,trueAns,ans1,ans2,ans3,ans4,reason;
	TextView result;
	ConfigManager configManager;
	public ArrayList<String> recieved = new ArrayList<>();
	public ArrayAdapter<String> adp;
	public static MainActivity instence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		instence = this;
		send = (Button) findViewById(R.id.mainButtonSend);
		ques = (EditText)findViewById(R.id.ques);
		trueAns = (EditText)findViewById(R.id.trueans);
		ans1 = (EditText)findViewById(R.id.ans1);
		ans2 = (EditText)findViewById(R.id.ans2);
		ans3 = (EditText)findViewById(R.id.ans3);
		ans4 = (EditText)findViewById(R.id.ans4);
		reason = (EditText)findViewById(R.id.reason);
		result = (TextView)findViewById(R.id.mainEditTextResult);
		send.setOnClickListener(onClick);
		adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recieved);

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
    }

	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonSend:
					SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack._40addQuestion);
					sdp.write(0);//flag
					sdp.write(ques.getText().toString());//ques
					sdp.write(4);//ansCount
					sdp.write(Integer.parseInt(trueAns.getText().toString()));
					sdp.write(ans1.getText().toString());
					sdp.write(ans2.getText().toString());
					sdp.write(ans3.getText().toString());
					sdp.write(ans4.getText().toString());
					sdp.write(reason.getText().toString());
					configManager.send(sdp.getData());
					result.setText("发送完成");
					//recieved.add(configManager.getOverSpell(2856986197L));
					adp.notifyDataSetChanged();
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

