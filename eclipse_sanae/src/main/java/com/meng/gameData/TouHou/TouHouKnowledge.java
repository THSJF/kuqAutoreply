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
	public HashMap<Long,Boolean> userMap=new HashMap<>();

	public HashMap<Long,QA> qaMap=new HashMap<>();

	public ArrayList<QA> qaList=new ArrayList<>();
	private File qafile;

	public static final int easy=0;
	public static final int normal=1;
	public static final int hard=2;
	public static final int lunatic=3;

	public static final int touhouBase=1;
	public static final int th15=2;

	public TouHouKnowledge() {
		qafile = new File(Autoreply.appDirectory + "/qa.json");
        if (!qafile.exists()) {
            saveData();
        }
        Type type = new TypeToken<ArrayList<QA>>() {
        }.getType();
        qaList = Autoreply.gson.fromJson(Tools.FileTool.readString(qafile), type);
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("-问答添加 ")) {
			String[] arr=msg.split(" ");
			if (arr.length != 3) {
				Autoreply.sendMessage(fromGroup, 0, "语句格式错误");
			}
			AddQuestionBean ab=new AddQuestionBean(fromQQ, arr[1], arr[2].equalsIgnoreCase("t"));
			ConfigManager.ins.SanaeConfig.quesWait.add(ab);
			Autoreply.sendMessage(fromGroup, 0, String.format("问题:%s\n答案:%s\n添加成功,待审核", ab.q, ab.a ? "t": "f"));
			ConfigManager.ins.saveSanaeConfig();
			return true;
		}
		if (msg.equals("-问答添加审核")) {
			AddQuestionBean ab = ConfigManager.ins.SanaeConfig.quesWait.get(0);
			Autoreply.sendMessage(fromGroup, 0, String.format("用户:%d\n问题:%s\n答案:%s", ab.u, ab.q, ab.a ? "t": "f"));
			return true;
		}
		if (msg.equalsIgnoreCase("-问答添加审核 t")) {
			AddQuestionBean ab = ConfigManager.ins.SanaeConfig.quesWait.remove(0);
			ConfigManager.ins.SanaeConfig.ques.put(ab.q, ab.a);
			MessageWaitManager.ins.addTip(ab.u, String.format("用户:%d\n问题:%s\n答案:%s\n审核通过,获得1信仰", ab.u, ab.q, ab.a ? "t": "f"));
			FaithManager.ins.addFaith(ab.u, 1);
			ConfigManager.ins.saveSanaeConfig();
			Autoreply.sendMessage(fromGroup, 0, String.format("用户:%d\n问题:%s\n答案:%s\n处理成功", ab.u, ab.q, ab.a ? "t": "f"));
			return true;
		}
		if (msg.equalsIgnoreCase("-问答添加审核 f")) {
			AddQuestionBean ab = ConfigManager.ins.SanaeConfig.quesWait.remove(0);
			ConfigManager.ins.saveSanaeConfig();
			Autoreply.sendMessage(fromGroup, 0, String.format("用户:%d\n问题:%s\n答案:%s\n处理成功", ab.u, ab.q, ab.a ? "t": "f"));
			return true;
		}
		if (msg.equals("-车万问答")) {
			String[] keys = ConfigManager.ins.SanaeConfig.ques.keySet().toArray(new String[ConfigManager.ins.SanaeConfig.ques.size()]);
			String randomKey = keys[Autoreply.ins.random.nextInt(keys.length)];
			userMap.put(fromQQ, ConfigManager.ins.SanaeConfig.ques.get(randomKey));
			Autoreply.sendMessage(fromGroup, 0, randomKey);
			return true;
		} else if (userMap.get(fromQQ) != null) {
			if (msg.equalsIgnoreCase("-问答回答 t")) {
				boolean ans = userMap.get(fromQQ);
				Autoreply.sendMessage(fromGroup, fromQQ, ans  ? "回答正确": "回答错误");
				userMap.remove(fromQQ);
				//if(ans){
				//	Autoreply.ins.faithManager.addFaith(fromQQ,1);
				//}
			} else if (msg.equalsIgnoreCase("-问答回答 f")) {
				boolean ans = userMap.get(fromQQ);
				Autoreply.sendMessage(fromGroup, fromQQ, ans ? "回答错误": "回答正确");
				userMap.remove(fromQQ);
				//if(!ans){
				//	Autoreply.ins.faithManager.addFaith(fromQQ,1);
				//}
			}
			return true;
		}
		if (ConfigManager.ins.isAdmin(fromQQ)) {
			if (msg.equalsIgnoreCase("-qa")) {
				QA qa=qaList.get(Autoreply.ins.random.nextInt(qaList.size()));
				StringBuilder sb=new StringBuilder(qa.q);
				qaMap.put(fromGroup, qa);
				int i=1;
				for (String s:qa.a) {
					sb.append(i++).append(".").append(s).append("\n");
				}
				sb.append("回答序号即可");
			}
			QA qa = qaMap.get(fromQQ);
			if (qa != null) {
				if (String.valueOf(qa.t + 1).equals(msg)) {
					Autoreply.sendMessage(fromGroup, 0, "回答正确");
				} else {
					Autoreply.sendMessage(fromGroup, 0, String.format("回答错误\n%s", qa.r));
				}
				qaMap.remove(fromQQ);
				return true;
			}
		}
		return false;
	}

	public void addQA(QA qa) {
		qaList.add(qa);
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

	public static class QABuilder {
		private int flag=0;
		private String q;
		private ArrayList<String> a = new ArrayList<>();
		private int t;//true answer
		private String r;//reason

		public void setFlag(int flag) {
			this.flag = flag;
		}
		public QABuilder setReason(String r) {
			this.r = r;
			return this;
		}
		public QABuilder setAnswer(String a) {
			this.a.add(a);
			return this;
		}
		public QABuilder setTrueAnswer(int index) {
			t = index;
			return this;
		}
		public QABuilder setQuestion(String q) {
			this.q = q;
			return this;
		}
		public QABuilder setDifficult(int diff) {
			flag &= (diff << 16);
			return this;
		}
		public QABuilder setKind(int kind) {
			flag &= kind;
			return this;
		}
		public QA build() {
			if (q == null || a == null) {
				return null;
			}
			QA qa=new QA();
			qa.flag = flag;
			qa.q = q;
			qa.a = a;
			qa.t = t;
			if (r == null) {
				qa.r = "无说明";
			} else {
				qa.r = r;
			}
			return qa;
		}
	}

	public static class QA {
		public int flag=0;
		public String q;
		private ArrayList<String> a = new ArrayList<>();
		public int t;//trueAns
		public String r;//reason

		public int getDiffcult() {
			return flag >>> 16;
		}
		public int getKind() {
			return flag & 0xff;
		}
	}
}
