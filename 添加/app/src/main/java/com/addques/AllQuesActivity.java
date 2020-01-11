package com.addques;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.addques.sanae.*;
import java.util.*;
import android.widget.AdapterView.*;
import android.view.*;

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
		listview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, final int p3, long p4) {
					QA qa = quesList.get(p3);
					TabActivity.ins.tab.setCurrentTab(0);
					AddQuesActivity.ins.mode = 1;
					AddQuesActivity.ins.onEdit = qa;
					AddQuesActivity.ins.spType.setSelection(qa.type);
					AddQuesActivity.ins.spDiffcult.setSelection(qa.d);
					AddQuesActivity.ins.etQues.setText(qa.q);
					RadioButton rb=(RadioButton)AddQuesActivity.ins.trueAns.getChildAt(qa.t);
					rb.setChecked(true);
					AddQuesActivity.ins.etAns1.setText(qa.a.get(0));
					AddQuesActivity.ins.etAns2.setText(qa.a.get(1));
					if (qa.a.size() > 2) {
						AddQuesActivity.ins.etAns3.setText(qa.a.get(2));
					}
					if (qa.a.size() > 3) {	
						AddQuesActivity.ins.etAns4.setText(qa.a.get(3));
					}
					AddQuesActivity.ins.etReason.setText(qa.r);
				}
			});
	}
}
