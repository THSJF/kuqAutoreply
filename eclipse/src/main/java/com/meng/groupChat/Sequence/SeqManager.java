package com.meng.groupChat.Sequence;
import com.google.gson.*;
import com.google.gson.internal.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.lang.reflect.*;
import com.google.gson.reflect.*;

public class SeqManager {
    private ArrayList<SeqBean> seqs=new ArrayList<>();
	private HashMap<String, ArrayList<String>> jsonData = new HashMap<>();

	public SeqManager() {
		File jsonFile = new File(Autoreply.appDirectory + "seq.json");
        if (!jsonFile.exists()) {
            saveData();
		  }
		Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {
		  }.getType();
        jsonData = new Gson().fromJson(Methods.readFileToString(jsonFile.getAbsolutePath()), type);
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
			if (msg.equals(sb.content[sb.pos])) {
				++sb.pos;			
				if (sb.pos < sb.content.length) {
					++sb.pos;
					if (sb.pos >= sb.content.length - 1) {
						sb.pos = 0;
					  }
					return sb.content[sb.pos - 1];
				  }
				if (sb.flag == 1) {
					Autoreply.instence.useCount.decLife(fromQQ);
					Autoreply.instence.groupCount.decLife(fromGroup);
				  } else if (sb.flag == 2) {
					Autoreply.instence.useCount.incMengEr(fromQQ);
					Autoreply.instence.groupCount.incMengEr(fromGroup);
				  }
				break;
			  }
		  }
		return null;
	  }

	private void saveData() {
        try {
            File file = new File(Autoreply.appDirectory + "seq.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(jsonData));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
