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
		qafile = new File(Autoreply.ins.appDirectory + "/qa.json");
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
		if (msg.equals("-qa 设置抢答")) {
			PersonConfig pcfg=ConfigManager.ins.getPersonCfg(fromQQ);
			pcfg.setQaAllowOther(pcfg.isQaAllowOther() ?false: true);
			Autoreply.sendMessage(fromGroup, 0, "允许抢答:" + pcfg.isQaAllowOther());
			ConfigManager.ins.saveSanaeConfig();
			return true;
		}
		if (msg.startsWith("抢答[")) {
			String ans=msg.substring(msg.indexOf("]") + 1).replace(" ", "");
			long target=Autoreply.ins.getCQCode().getAt(msg);
			if (ConfigManager.ins.getPersonCfg(target).isQaAllowOther()) {
				QA qa = qaMap.get(target);
				if (qa != null) {
					if (String.valueOf(qa.t + 1).equals(ans)) {
						Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().at(fromQQ) + "回答正确");
					} else {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s回答错误", Autoreply.ins.getCQCode().at(fromQQ)));
					}
					qaMap.remove(target);
					return true;
				}
			} else {
				Autoreply.sendMessage(fromGroup, 0, "该用户不允许别人抢答");
			}
			return true;
		}
		QA qa = qaMap.get(fromQQ);
		if (qa != null && msg.equalsIgnoreCase("-qa")) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().at(fromQQ) + "你还没有回答");
			return true;
		}
		if (qa != null) {
			if (String.valueOf(qa.t + 1).equals(msg)) {
				Autoreply.sendMessage(fromGroup, 0, Autoreply.ins.getCQCode().at(fromQQ) + "回答正确");
			} else {
				Autoreply.sendMessage(fromGroup, 0, String.format("%s回答错误\n%s", Autoreply.ins.getCQCode().at(fromQQ), qa.r == null ?"": qa.r));
			}
			qaMap.remove(fromQQ);
			return true;
		}
		if (msg.equalsIgnoreCase("-qa")) {
			int ran=Autoreply.ins.random.nextInt(qaList.size());
			QA qa2=qaList.get(ran);
			StringBuilder sb=new StringBuilder(Autoreply.ins.getCQCode().at(fromQQ)).append("\n题目ID:").append(ran).append("\n");
			sb.append("难度:");
			switch (qa2.d) {
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
			sb.append("\n\n").append(qa2.q).append("\n");

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
		qa.id = qaList.size();
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

	public <T> void exchange(List<T> list, int pos1, int pos2) {
		if (null == list || list.size() < 2) {
			throw new IllegalStateException("The list illegal");
		}
		T ele1 = list.get(pos1);
		list.set(pos1, list.get(pos2));
		list.set(pos2, ele1);
	}

	public static class QA {
		public int id=0;
		public int d=0;
		public String q;
		public ArrayList<String> a = new ArrayList<>();
		public int t;//trueAns
		public String r;//reason
	}
}
