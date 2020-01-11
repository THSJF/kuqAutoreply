package com.meng.gameData.TouHou;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.*;
import com.meng.messageProcess.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class TouHouKnowledge {
	public static TouHouKnowledge ins;
	public HashMap<Long,QA> qaMap=new HashMap<>();
	public String imagePath;
	public ArrayList<QA> qaList=new ArrayList<>();
	private File qafile;

	public static final int easy=0;
	public static final int normal=1;
	public static final int hard=2;
	public static final int lunatic=3;

	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	public TouHouKnowledge() {
		qafile = new File(Autoreply.appDirectory + "/qa.json");
        if (!qafile.exists()) {
            saveData();
        }
        Type type = new TypeToken<ArrayList<QA>>() {
        }.getType();
        qaList = Autoreply.gson.fromJson(Tools.FileTool.readString(qafile), type);
		imagePath = Autoreply.appDirectory + "/qaImages/";
		File imageFolder=new File(imagePath);
		if (!imageFolder.exists()) {
			imageFolder.mkdirs();
		}
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-qa 设置抢答")) {
			PersonConfig pcfg=ConfigManager.ins.getPersonCfg(fromQQ);
			pcfg.setQaAllowOther(pcfg.isQaAllowOther() ?false: true);
			Autoreply.sendMessage(fromGroup, 0, "允许抢答:" + pcfg.isQaAllowOther());
			ConfigManager.ins.saveSanaeConfig();
			return true;
		}
		if (msg.startsWith("抢答[")) {
			String ans=msg.substring(msg.indexOf("]") + 1);
			long target=Autoreply.CC.getAt(msg);
			if (ConfigManager.ins.getPersonCfg(target).isQaAllowOther()) {
				QA qa = qaMap.get(fromQQ);
				if (qa != null) {
					if (String.valueOf(qa.t + 1).equals(ans)) {
						Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(fromQQ) + "回答正确");
					} else {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s回答错误", Autoreply.CC.at(fromQQ)));
					}
					qaMap.remove(fromQQ);
					return true;
				}
			} else {
				Autoreply.sendMessage(fromGroup, 0, "该用户不允许别人抢答");
			}
			return true;
		}
		QA qa = qaMap.get(fromQQ);
		if (qa != null && msg.equalsIgnoreCase("-qa")) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(fromQQ) + "你还没有回答");
			return true;
		}
		if (qa != null) {
			if (String.valueOf(qa.t + 1).equals(msg)) {
				Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(fromQQ) + "回答正确");
			} else {
				Autoreply.sendMessage(fromGroup, 0, String.format("%s回答错误", Autoreply.CC.at(fromQQ)));
			}
			qaMap.remove(fromQQ);
			return true;
		}
		if (msg.equalsIgnoreCase("-qa")) {
			int ran=Autoreply.ins.random.nextInt(qaList.size());
			QA qa2=qaList.get(ran);
			StringBuilder sb=new StringBuilder(Autoreply.CC.at(fromQQ)).append("\n题目ID:").append(ran).append("\n");
			sb.append("难度:");
			switch (qa2.getDifficulty()) {
				case 0:
					sb.append("e");
					break;
				case 1:
					sb.append("n");
					break;
				case 2:
					sb.append("h");
					break;
				case 3:
					sb.append("l");
					break;
				case 4:
					sb.append("o");
					break;
				case 5:
					sb.append("k");
			}
			sb.append("\n分类:");
			switch (qa2.getType()) {
				case 0:
					sb.append("未定义");
					break;
				case touhouBase:
					sb.append("东方project基础");
					break;
				case _2unDanmakuIntNew:
					sb.append("新作整数作");
					break;
				case _2unDanmakuAll:
					sb.append("官方弹幕作");
					break;
				case _2unNotDanmaku:
					sb.append("官方非弹幕");
					break;
				case _2unAll:
					sb.append("官方所有");
					break;
				case otherDanmaku:
					sb.append("同人弹幕");
			}	
			sb.append("\n").append(qa2.q).append("\n");
			int change=Autoreply.ins.random.nextInt(qa2.a.size());
			exchange(qa2.a, qa2.t, change);
			qa2.t = change;
			saveData();
			qaMap.put(fromQQ, qa2);
			int i=1;
			for (String s:qa2.a) {
				if (s.equals("")) {
					continue;
				}
				sb.append(i++).append(": ").append(s).append("\n");
			}
			sb.append("回答序号即可");
			Autoreply.sendMessage(fromGroup, 0, sb.toString());
			return true;
		}
		return false;
	}

	public void addQA(QA qa) {
		qa.setId(qaList.size());
		qaList.add(qa);
		saveData();
	}

	public void setQA(QA qa) {
		qaList.set(qa.getId(), qa);
		saveData();
	}

	private void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(qafile);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(new Gson().toJson(qaList));
			writer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public <T> void exchange(List<T> list, int pos1, int pos2) {
		if (null == list || list.size() < 2) {
			throw new IllegalStateException("The list illegal");
		}
		T ele1 = list.get(pos1);
		list.set(pos1, list.get(pos2));
		list.set(pos2, ele1);
	}

	public static class QA {
		public int flag=0;
		//flag: id(16bit)					type(8bit)		diffculty(8bit)
		//	0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0|0 0 0 0 0 0 0 0|0 0 0 0 0 0 0 0
		public int l=0;//file length
		public String q;
		public ArrayList<String> a = new ArrayList<>();
		public int t;//trueAns
		public String r;

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getFlag() {
			return flag;
		}

		public void setDifficulty(int d) {
			flag &= 0xffffff00;
			flag |= d;
		}

		public int getDifficulty() {
			return flag & 0xff;
		}

		public void setId(int id) {
			flag &= 0x0000ffff;
			flag |= (id << 16);
		}

		public int getId() {
			return (flag >> 16) & 0xff;
		}

		public void setType(int type) {
			flag &= 0xffff00ff;
			flag |= (type << 8);
		}

		public int getType() {
			return (flag >> 8) & 0xff;
		}
	}
}
