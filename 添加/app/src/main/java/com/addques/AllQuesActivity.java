package com.addques;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.addques.sanae.*;
import java.util.*;

public class AllQuesActivity extends Activity {
	public static AllQuesActivity ins;
	public ArrayList<QA> quesList=new ArrayList<>();
	private ListView listview;
	public QuesAdapter quesAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ins = this;
		setContentView(R.layout.all_ques_activity);
		listview = (ListView) findViewById(R.id.all_quesListView);
		quesAdapter = new QuesAdapter(this, quesList);
		listview.setAdapter(quesAdapter);
		TabActivity.ins.configManager.send(SanaeDataPack.encode(SanaeDataPack._41getAllQuestion).getData());
	}
}
