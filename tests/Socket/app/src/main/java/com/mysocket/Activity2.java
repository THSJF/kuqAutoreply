package com.mysocket;
import android.app.*;
import android.os.*;
import android.widget.*;

public class Activity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		String s = getIntent().getStringExtra("content");
		if (s == null) {
			finish();
			return;
		}
		TextView tv = (TextView) findViewById(R.id.main2TextView);
		tv.setText(s);
	}

}
