package com.addques;
import android.app.*;
import android.os.*;
import java.util.*;
import android.widget.*;

public class Activity2 extends Activity {
	public static ArrayList<QA> qas=new ArrayList<>();
	public static TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv=new TextView(this);
		setContentView(tv);
	
		}
	
	
}
