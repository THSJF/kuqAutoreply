package com.addques;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.addques.sanae.*;
import java.util.*;

public class QuesAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<QA> infosList;
	private StringBuilder stringBuilder=new StringBuilder();
	public QuesAdapter(Activity context, ArrayList<QA> infosSet) {
		this.context = context;
		this.infosList = infosSet;
	}

	public int getCount() {
		return infosList.size();
	}

	public Object getItem(int position) {
		return infosList.get(position);
	}

	public long getItemId(int position) {
		return infosList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(R.layout.ques_list_item, null);
			holder = new ViewHolder();
			holder.tvId = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_id);
			holder.tvDiff = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_diff);
			holder.tvType = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_type);
			holder.tvQues = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_ques);
			holder.tvAns = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_ans);
			holder.tvReason = (TextView) convertView.findViewById(R.id.ques_list_itemTextView_reason);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		QA qa = infosList.get(position);
		holder.tvId.setText("id:" + qa.id);
		stringBuilder.setLength(0);
		switch (qa.d) {
			case 0:
				stringBuilder.append("easy");
				holder.tvDiff.setTextColor(Color.GREEN);
				break;
			case 1:
				stringBuilder.append("normal");
				holder.tvDiff.setTextColor(Color.rgb(0x00, 0xcc, 0xff));
				break;
			case 2:
				stringBuilder.append("hard");
				holder.tvDiff.setTextColor(Color.BLUE);
				break;
			case 3:
				stringBuilder.append("lunatic");
				holder.tvDiff.setTextColor(Color.rgb(0xff, 0x99, 0x00));
				break;
			case 4:
				stringBuilder.append("overdrive");
				holder.tvDiff.setTextColor(Color.RED);
				break;
			case 5:
				stringBuilder.append("kidding");
				holder.tvDiff.setTextColor(Color.CYAN);
				break;
			default:
				stringBuilder.append("未知");
		}
		holder.tvDiff.setText(stringBuilder.toString());
		stringBuilder.setLength(0);
		switch (qa.type) {
			case 0:
				stringBuilder.append("未分类");
				break;
			case 1:
				stringBuilder.append("车万基础");
				break;
			case 2:
				stringBuilder.append("新作整数作");
				break;
			case 3:
				stringBuilder.append("官方弹幕作");
				break;
			case 4:
				stringBuilder.append("官方非弹幕");
				break;
			case 5:
				stringBuilder.append("官方所有");
				break;
			case 6:
				stringBuilder.append("同人弹幕");
				break;
			default:
				stringBuilder.append("未知");
		}	
		holder.tvType.setText(stringBuilder.toString());
		holder.tvQues.setText("问题:" + qa.q);
		holder.tvAns.setText("答案:" + qa.a.get(qa.t));
		if (qa.r.equals("")) {
			holder.tvReason.setVisibility(View.GONE);	
		} else {
			holder.tvReason.setVisibility(View.VISIBLE);
			holder.tvReason.setText("原因:" + qa.r);
		}
		return convertView;
	}

	private class ViewHolder {
		public TextView tvId;
		public TextView tvDiff;
		public TextView tvType;
		public TextView tvQues;
		public TextView tvAns;
		public TextView tvReason;
	}
}
