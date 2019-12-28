package com.meng.gameData.TouHou;

import com.meng.*;
import com.meng.config.*;
import com.meng.messageProcess.*;
import com.meng.tools.*;
import java.util.*;

public class TouHouKnowledge {
	public static TouHouKnowledge ins;
	public HashMap<Long,Boolean> userMap=new HashMap<>();

	public TouHouKnowledge() {
		/*	question.put("紫奥义「弹幕结界」自机无敌时符卡会暂停", true);
		 question.put("「纯粹的疯狂」是角随固", true);
		 question.put("「无双风神」是东方风神录HL难度下的符卡", false);
		 */
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
				Autoreply.sendMessage(fromGroup, fromQQ,ans  ? "回答正确": "回答错误");
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
		return false;
	}
}
