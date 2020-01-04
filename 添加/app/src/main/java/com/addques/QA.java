package com.addques;

import java.util.*;

public class QA {
	public int flag=0;
	public String q;
	public ArrayList<String> a = new ArrayList<>();
	public int t;//trueAns
	public String r;//reason

	public int getDiffcult() {
		return flag >>> 24;
	}
	public int getKind() {
		return (flag & 0xff) >>> 16;
	}
	public int getId() {
		return flag & 0xffff;
	}
	public void setId(int id) {
		flag &= id;
	}

	@Override
	public String toString() {
		int ran=new Random().nextInt(Activity2.qas.size());
		QA qa=Activity2.qas.get(ran);
		StringBuilder sb=new StringBuilder("题目ID:").append(ran).append("\n");
		sb.append("难度:");
		switch (qa.getDiffcult()) {
			case 0:
				sb.append("easy");
				break;
			case 1:
				sb.append("normal");
				break;
			case 2:
				sb.append("hard");
				break;
			case 3:
				sb.append("lunatic");
				break;
			case 4:
				sb.append("overdrive");
				break;
		}
		sb.append("\n\n").append(qa.q).append("\n");
		int i=1;
		for (String s:qa.a) {
			if (s.equals("")) {
				continue;
			}
			sb.append(i++).append(": ").append(s).append("\n");
		}
		return sb.toString();
	}

}

