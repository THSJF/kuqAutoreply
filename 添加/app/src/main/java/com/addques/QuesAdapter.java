package com.addques;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.addques.sanae.*;
import java.io.*;
import java.util.*;

public class QuesAdapter extends BaseAdapter {
	private AllQuesActivity context;
	private ArrayList<QA> infosList;

	public QuesAdapter(AllQuesActivity context, ArrayList<QA> infosSet) {
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
		holder.tvId.setText(qa.id);
		holder.tvDiff.setText(qa.d);
		holder.tvType.setText(qa.type);
		holder.tvQues.setText(qa.q);
		holder.tvAns.setText(qa.a.get(qa.t));
		holder.tvReason.setText(qa.r == null ?"无说明": qa.r);

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
