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
	EditText op,ets1,ets2,ets3,etn1,etn2,etn3;
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
		op = (EditText)findViewById(R.id.opCode);
		ets1 = (EditText)findViewById(R.id.ets1);
		ets2 = (EditText)findViewById(R.id.ets2);
		ets3 = (EditText)findViewById(R.id.ets3);
		etn1 = (EditText)findViewById(R.id.etn1);
		etn2 = (EditText)findViewById(R.id.etn2);
		etn3 = (EditText)findViewById(R.id.etn3);
		result = (TextView)findViewById(R.id.mainEditTextResult);
		send.setOnClickListener(onClick);
		adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recieved);
		
		try {
			configManager = new ConfigManager(new URI("ws://123.207.65.93:9760"));
			configManager.connect();
		} catch (URISyntaxException e) {
			showToast(e.toString());
		}
    }



	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonSend:
					SanaeDataPack sdp=SanaeDataPack.encode((short)Integer.parseInt(op.getText().toString()) , System.currentTimeMillis());
					String s="";
					if (!ets1.getText().toString().equals("")) {
						sdp.write(ets1.getText().toString());
						s += ets1.getText().toString();
						s += " ";
					}
					if (!ets2.getText().toString().equals("")) {
						sdp.write(ets2.getText().toString());
						s += ets2.getText().toString();
						s += " ";
					}
					if (!ets3.getText().toString().equals("")) {
						sdp.write(ets3.getText().toString());
						s += ets3.getText().toString();
						s += " ";
					}
					if (!etn1.getText().toString().equals("")) {
						sdp.write(Long.parseLong(etn1.getText().toString()));
						s += etn1.getText().toString();
						s += " ";
					}
					if (!etn2.getText().toString().equals("")) {
						sdp.write(Long.parseLong(etn2.getText().toString()));
						s += etn2.getText().toString();
						s += " ";
					}
					if (!etn3.getText().toString().equals("")) {
						sdp.write(Long.parseLong(etn3.getText().toString()));
						s += etn3.getText().toString();
						s += " ";
					}	 
					configManager.send(sdp.getData());
					result.setText("发送内容:\n" + s);
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

