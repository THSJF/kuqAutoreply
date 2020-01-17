package com.addques;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.RadioGroup.*;
import com.addques.sanae.*;
import java.io.*;
import java.net.*;
import java.util.*;

import android.view.View.OnClickListener;

public class TabActivity extends android.app.TabActivity {

	public static TabActivity ins;

	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	public SanaeConnect sanaeConnect;
	public TabHost tab;

	public Button btnSend,btnClean,btnImage;
	public EditText etQues,etAns1,etAns2,etAns3,etAns4,etReason;
	public Spinner spDiffcult,spType,spFiDiff,spFiType;
	public RadioGroup rgTrueAnswer;
	public ImageView imageView;

	public int mode=0;
	public QA onEdit;
	public int trueAnswer=-1;

	public ArrayList<QA> alAllQa=new ArrayList<>();
	public ArrayList<QA> nowQaList=new ArrayList<>();
	private ListView lvAllQa;
	public QuesAdapter quesAdapter;

	public File chooseFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity);
		ins = this;
		tab = (TabHost) findViewById(android.R.id.tabhost);
		tab.setup();
        LayoutInflater i=LayoutInflater.from(this); 
		i.inflate(R.layout.add_ques_activity, tab.getTabContentView()); 
		i.inflate(R.layout.all_ques_activity, tab.getTabContentView());
		btnClean = (Button) findViewById(R.id.clean);
		btnSend = (Button) findViewById(R.id.mainButtonSend);
		btnImage = (Button) findViewById(R.id.add_ques_activityButton_addPic);
		etQues = (EditText)findViewById(R.id.ques);
		spDiffcult = (Spinner) findViewById(R.id.diff);
		spType = (Spinner) findViewById(R.id.type);
		spFiDiff = (Spinner) findViewById(R.id.all_ques_activitySpinner_diff);
		spFiType = (Spinner) findViewById(R.id.all_ques_activitySpinner_type);
		rgTrueAnswer = (RadioGroup)findViewById(R.id.trueans);
		etAns1 = (EditText)findViewById(R.id.ans1);
		etAns2 = (EditText)findViewById(R.id.ans2);
		etAns3 = (EditText)findViewById(R.id.ans3);
		etAns4 = (EditText)findViewById(R.id.ans4);
		etReason = (EditText)findViewById(R.id.reason);
		imageView = (ImageView) findViewById(R.id.add_ques_activityImageView);
		btnSend.setOnClickListener(onClick);
		btnClean.setOnClickListener(onClick);
		btnImage.setOnClickListener(onClick);
		spDiffcult.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"easy","normal","hard","lunatic","overdrive","kidding"}));
		spType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"未分类","车万基础","新作整数作","官方弹幕作","官方非弹幕","官方所有","同人弹幕"}));
		spFiDiff.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"all","easy","normal","hard","lunatic","overdrive","kidding"}));
		spFiType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"全部分类","未分类","车万基础","新作整数作","官方弹幕作","官方非弹幕","官方所有","同人弹幕"}));
		spFiType.setOnItemSelectedListener(onItemSelect);
		spFiDiff.setOnItemSelectedListener(onItemSelect);
		rgTrueAnswer.setOnCheckedChangeListener(new OnCheckedChangeListener(){

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
        tab.addTab(tab.newTabSpec("tab1").setIndicator("添加问题" , null).setContent(R.id.add_ques_activityLinearLayout));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("浏览问题" , null).setContent(R.id.all_ques_activityLinearLayout));
		try {
			sanaeConnect = new SanaeConnect(new URI("ws://123.207.65.93:9001"));
			sanaeConnect.connect();
		} catch (URISyntaxException e) {
			showToast(e.toString());
		}
		new Thread(new Runnable(){

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10000);
							sanaeConnect.send("h");
						} catch (Exception e) {
							showToast("连接断开");
							sanaeConnect.reconnect();
						}
					}
				}
			}).start();
		lvAllQa = (ListView) findViewById(R.id.all_quesListView);
		nowQaList.addAll(alAllQa);
		quesAdapter = new QuesAdapter(this, nowQaList);
		lvAllQa.setAdapter(quesAdapter);
		lvAllQa.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, final int p3, long p4) {
					QA qa = nowQaList.get(p3);
					tab.setCurrentTab(0);
					mode = 1;
					onEdit = qa;
					spType.setSelection(qa.getType());
					spDiffcult.setSelection(qa.getDifficulty());
					etQues.setText(qa.q);
					((RadioButton)rgTrueAnswer.getChildAt(qa.t)).setChecked(true);
					etAns1.setText(qa.a.get(0));
					etAns2.setText(qa.a.get(1));
					etAns3.setText("");
					etAns4.setText("");
					etReason.setText("");
					if (qa.a.size() > 2) {
						etAns3.setText(qa.a.get(2));
					}
					if (qa.a.size() > 3) {	
						etAns4.setText(qa.a.get(3));
					}
					etReason.setText(qa.r);
				}
			});
	}

	public void showToast(final String s) {
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Toast.makeText(TabActivity.this, s, Toast.LENGTH_SHORT).show();
				}
			});
	}

	OnItemSelectedListener onItemSelect=new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
			refresh();
		}

		@Override
		public void onNothingSelected(AdapterView<?> p1) {
			// TODO: Implement this method
		}
	};

	private void refresh() {
		nowQaList.clear();
		int dif = spFiDiff.getSelectedItemPosition() - 1;
		int ty = spFiType.getSelectedItemPosition() - 1;
		if (ty == -1) {
			if (dif == -1) {
				nowQaList.addAll(alAllQa);
			} else {	
				for (QA qa:alAllQa) {
					if (qa.getDifficulty() == dif) {
						nowQaList.add(qa);	
					}
				}
			}
		} else {
			if (dif == -1) {
				for (QA qa:alAllQa) {
					if (qa.getType() == ty) {
						nowQaList.add(qa);	
					}
				}
			} else {
				for (QA qa:alAllQa) {
					if (qa.getType() == ty && qa.getDifficulty() == dif) {
						nowQaList.add(qa);	
					}
				}
			}	
		}
		quesAdapter.notifyDataSetChanged();
	}

	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonSend:
					if (mode == 0) {
						QA qa = new QA();
						qa.setId(alAllQa.size());
						qa.setType(spType.getSelectedItemPosition());
						qa.setDifficulty(spDiffcult.getSelectedItemPosition());
						qa.q = etQues.getText().toString();
						qa.t = trueAnswer;
						String s1 = etAns1.getText().toString();
						qa.a.add(s1.equals("") ?"是": s1);
						String s2 = etAns2.getText().toString();
						qa.a.add(s2.equals("") ?"否": s2);
						if (!etAns3.getText().toString().equals("")) {
							qa.a.add(etAns3.getText().toString());
						}
						if (!etAns4.getText().toString().equals("")) {
							qa.a.add(etAns4.getText().toString());
						}
						qa.r = etReason.getText().toString();
						alAllQa.add(qa);
						refresh();
						SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack.opAddQuestion);
						sdp.write(qa.getFlag());
						sdp.write(qa.q);
						sdp.write(qa.a.size());
						sdp.write(qa.t);
						for (String s:qa.a) {
							sdp.write(s);
						}
						sdp.write(qa.r);
						if (chooseFilePath != null) {
							sdp.write(chooseFilePath);
						}
						try {
							sanaeConnect.send(sdp.getData());
						} catch (Exception e) {
							showToast(e.toString());
						}
						showToast("正在发送");	
					} else if (mode == 1) {
						onEdit.setType(spType.getSelectedItemPosition());
						onEdit.setDifficulty(spDiffcult.getSelectedItemPosition());
						onEdit.q = etQues.getText().toString();
						onEdit.t = trueAnswer;
						onEdit.a.clear();
						String s1 = etAns1.getText().toString();
						onEdit.a.add(s1.equals("") ?"是": s1);
						String s2 = etAns2.getText().toString();
						onEdit.a.add(s2.equals("") ?"否": s2);
						if (!etAns3.getText().toString().equals("")) {
							onEdit.a.add(etAns3.getText().toString());
						}
						if (!etAns4.getText().toString().equals("")) {
							onEdit.a.add(etAns4.getText().toString());
						}
						onEdit.r = etReason.getText().toString();
						refresh();
						SanaeDataPack sdp=SanaeDataPack.encode(SanaeDataPack.opSetQuestion);
						sdp.write(onEdit.getFlag());
						sdp.write(onEdit.q);
						sdp.write(onEdit.a.size());
						sdp.write(onEdit.t);
						for (String s:onEdit.a) {
							sdp.write(s);
						}
						sdp.write(onEdit.r);
						if (chooseFilePath != null) {
							sdp.write(chooseFilePath);
						}
						try {
							sanaeConnect.send(sdp.getData());
						} catch (Exception e) {
							showToast(e.toString());
						}
						showToast("正在发送");	
						mode = 0;
						onEdit = null;
						clean();
						refresh();
						tab.setCurrentTab(1);
					}
					imageView.setImageBitmap(null);
					imageView.setVisibility(View.GONE);
					break;
				case R.id.clean:
					clean();
					break;
				case R.id.add_ques_activityButton_addPic:
					if (chooseFilePath == null) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						startActivityForResult(intent, 234);
					} else {
						showToast("仅可以添加一张图片");
					}
					break;
			}
		}
	};

	private void clean() {
		etQues.setText("");
		etAns1.setText("");
		etAns2.setText("");
		etAns3.setText("");
		etAns4.setText("");
		etReason.setText("");
	}
	
	public void cleanNotify(int notifyId){
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(notifyId);
	}

	public void sendNotify(int notifyId) {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent();
		/* intent.setAction(intent.ACTION_CALL);
		 intent.setData(Uri.parse("tel:10086"));
		 */
		// 创建意图
		// 第一个参数：上下文
		// 第二个参数：请求码
		// 第三个参数：显示的次数
		PendingIntent contentIntent = PendingIntent.getActivity(TabActivity.this, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(this)
			.setContentTitle("问答图片接收中")
			.setContentText(notifyId + ".jpg")
			.setWhen(System.currentTimeMillis())
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(contentIntent).build();
			manager.notify(notifyId, notification);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 234:
					chooseFilePath = new File(FileChooseUtil.getInstance(this).getChooseFileResultPath(data.getData()));
					etQues.setText(etQues.getText().toString() + "(image)");
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageBitmap(BitmapFactory.decodeFile(chooseFilePath.getAbsolutePath()));
					break;
			}
		}
	}
}
