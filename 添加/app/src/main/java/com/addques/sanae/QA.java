package com.addques.sanae;

import java.util.*;

public class QA {
	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	public int id=0;
	public int type=0;
	public int d=0;
	public String q;
	public ArrayList<String> a = new ArrayList<>();
	public int t;//trueAns
	public String r;//reason
	/*
	 @Override

	 public String toString() {
	 StringBuilder sb=new StringBuilder("题目ID:").append(id).append("\n");
	 sb.append("难度:");
	 switch (d) {
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
	 case 5:
	 sb.append("k");
	 }
	 sb.append("\n");
	 sb.append("\n分类:");
	 switch (type) {
	 case 0:
	 sb.append("未定义");
	 break;
	 case touhouBase:
	 sb.append("东方project基础");
	 break;
	 case _2unDanmakuIntNew:
	 sb.append("整数作");
	 break;
	 case _2unDanmakuAll:
	 sb.append("官方弹幕作");
	 break;
	 case _2unNotDanmaku:
	 sb.append("官方格斗作");
	 break;
	 case _2unAll:
	 sb.append("官方作");
	 break;
	 case otherDanmaku:
	 sb.append("同人弹幕");
	 }
	 sb.append("\n").append(q).append("\n");
	 int i=1;
	 for (String s:a) {
	 if (s.equals("")) {
	 continue;
	 }
	 sb.append(i++).append(": ").append(s).append("\n");
	 }
	 return sb.toString();
	 }*/
}

