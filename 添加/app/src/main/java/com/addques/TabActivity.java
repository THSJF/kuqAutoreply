package com.addques;
import android.content.*;
import android.os.*;
import android.widget.*;
import com.addques.sanae.*;
import java.net.*;

public class TabActivity extends android.app.TabActivity {

	public static TabActivity ins;
	public SanaeConnect configManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity);
		ins=this;
        TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
		tab.setup();
        tab.addTab(tab.newTabSpec("tab1").setIndicator("添加问题" , null).setContent(new Intent(this,AddQuesActivity.class)));
        tab.addTab(tab.newTabSpec("tab1").setIndicator("浏览问题" , null).setContent(new Intent(this,AllQuesActivity.class)));
		try {
			configManager = new SanaeConnect(new URI("ws://123.207.65.93:9001"));
			configManager.connect();
		} catch (URISyntaxException e) {
			showToast(e.toString());
		}
		new Thread(new Runnable(){

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(30000);
							configManager.send("heart");
						} catch (Exception e) {
							showToast("连接断开");
							configManager.reconnect();
						}
					}
				}
			}).start();
			
			
			
		}
		
		
	public void showToast(final String s) {
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Toast.makeText(TabActivity.this, s, Toast.LENGTH_SHORT).show();
				}
			});
	}
}
