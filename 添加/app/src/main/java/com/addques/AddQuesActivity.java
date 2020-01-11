package com.addques;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.RadioGroup.*;
import com.addques.sanae.*;
import java.util.*;

import android.view.View.OnClickListener;

public class AddQuesActivity extends Activity {

	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	public static AddQuesActivity ins;
	public Button btnSend,btnClean;
	public EditText etQues,etAns1,etAns2,etAns3,etAns4,etReason;
	public Spinner spDiffcult,spType;
	public RadioGroup trueAns;

	public int mode=0;
	public QA onEdit;

	public ArrayList<String> recieved = new ArrayList<>();
	int trueAnswer=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ques_activity);
		ins = this;
		btnClean = (Button) findViewById(R.id.clean);
		btnSend = (Button) findViewById(R.id.mainButtonSend);
		etQues = (EditText)findViewById(R.id.ques);
		spDiffcult = (Spinner) findViewById(R.id.diff);
		spType = (Spinner) findViewById(R.id.type);
		trueAns = (RadioGroup)findViewById(R.id.trueans);
		etAns1 = (EditText)findViewById(R.id.ans1);
		etAns2 = (EditText)findViewById(R.id.ans2);
		etAns3 = (EditText)findViewById(R.id.ans3);
		etAns4 = (EditText)findViewById(R.id.ans4);
		etReason = (EditText)findViewById(R.id.reason);
		btnSend.setOnClickListener(onClick);
		btnClean.setOnClickListener(onClick);
		spDiffcult.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"easy","normal","hard","lunatic","overdrive","kidding"}));
		spType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"未分类","车万基础","新作弹幕作","官方所有弹幕作","官方非弹幕","官方所有","同人弹幕"}));
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
					if (mode == 0) {
						SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack._40addQuestion);
						writeQa(sdp);
					} else if (mode == 1) {
						SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack._43setQuestion);
						writeQa(sdp);
						mode = 0;
						onEdit = null;
						clean();
					}
					break;
				case R.id.clean:
					clean();
					break;
			}
		}

		private void writeQa(SanaeDataPack sdp) {
			sdp.write(onEdit == null ?0: onEdit.id);
			sdp.write(spType.getSelectedItemPosition());
			sdp.write(spDiffcult.getSelectedItemPosition());
			sdp.write(etQues.getText().toString());//ques
			sdp.write(4);//ansCount
			sdp.write(trueAnswer);
			String s1 = etAns1.getText().toString();
			sdp.write(s1.equals("") ?"是": s1);
			String s2 = etAns2.getText().toString();
			sdp.write(s2.equals("") ?"否": s2);
			sdp.write(etAns3.getText().toString());
			sdp.write(etAns4.getText().toString());
			sdp.write(etReason.getText().toString());
			TabActivity.ins.configManager.send(sdp.getData());
			TabActivity.ins.showToast("正在发送");
		}
	};

	public void clean() {
		etQues.setText("");
		etAns1.setText("");
		etAns2.setText("");
		etAns3.setText("");
		etAns4.setText("");
		etReason.setText("");
	}
}

