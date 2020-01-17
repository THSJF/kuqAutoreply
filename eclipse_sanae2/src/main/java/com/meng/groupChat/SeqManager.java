package com.meng.groupChat;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.*;
import java.lang.reflect.*;
import java.util.*;

public class SeqManager {
	public static SeqManager ins;
    private ArrayList<SeqBean> seqs=new ArrayList<>();
	private HashMap<String, ArrayList<String>> jsonData = new HashMap<>();

	public SeqManager() {
		Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {
		}.getType();
        jsonData = new Gson().fromJson(ConfigManager.ins.getSeq(), type);
   		for (String key : jsonData.keySet()) {
			ArrayList<String> al=jsonData.get(key);
			String[] content=al.toArray(new String[al.size()]);
			int flag=0;
			if (key.startsWith("time")) {
				flag = 1;
			} else if (key.startsWith("menger")) {
				flag = 2;
			}
			seqs.add(new SeqBean(content, flag));
		}
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		String s=null;
		s = dealMsg(fromGroup, fromQQ, msg);
		if (s != null) {
			Autoreply.sendMessage(fromGroup, 0, s);
		}
		return false;
	}

	public String dealMsg(long fromGroup, long fromQQ, String msg) {
		for (SeqBean sb:seqs) {
			if (msg.equals(sb.content[0])) {
				sb.pos = 0;
			}
			if (sb.flag == 1) {
				ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack.opDecTime).write(fromGroup).write(fromQQ));
			} else if (sb.flag == 2) {
				ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack.opIncMengEr).write(fromGroup).write(fromQQ));
			}
			if (msg.equals(sb.content[sb.pos])) {
				++sb.pos;			
				if (sb.pos < sb.content.length) {
					++sb.pos;
					if (sb.pos >= sb.content.length - 1) {
						sb.pos = 0;
					}
					if (sb.pos == 0) {
						return sb.content[sb.content.length - 1];
					} else {
						return sb.content[sb.pos - 1];
					}
				}
				break;
			}
		}
		return null;
	}
	private class SeqBean {
		public String[] content;
		public int pos=0;
		public int flag=0;
		public SeqBean(String[] array, int flag) {
			content = array;
			this.flag = flag;
		}
	}
}
