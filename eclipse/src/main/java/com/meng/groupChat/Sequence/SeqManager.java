package com.meng.groupChat.Sequence;
import com.google.gson.*;
import com.google.gson.internal.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class SeqManager {
	//public String[] time=new String[]{"苟","利","国","家","生","死","以","岂","因","祸","福","避","趋","之"};
	//public String[] menger=new String[]{"此","生","无","悔","入","东","方","来","世","愿","生","幻","想","乡"};
	private ArrayList<SeqBean> seqs=new ArrayList<>();
	public JsonObject jsonObject;
	public SeqManager() {
		File liveTimeFile = new File(Autoreply.appDirectory + "seq.json");
        if (!liveTimeFile.exists()) {
            saveLiveTime();
		  }
        jsonObject = new JsonParser().parse(Methods.readFileToString(liveTimeFile.getAbsolutePath())).getAsJsonObject();
		LinkedTreeMap linkedTreeMap = (LinkedTreeMap) ((Object)jsonObject);
		for (Object o : linkedTreeMap.keySet()) {
			String key = (String) o;
			JsonArray jsonArray=(JsonArray) linkedTreeMap.get(key);
			String[] content=new String[jsonArray.size()];
			for (int i=0;i < content.length;++i) {
				content[i] = jsonArray.get(i).getAsString();
			  }
			int flag=0;
			switch (key) {
				case "time":
				case "time1":
				  flag = 1;
				  break;
				case "menger":
				case "menger2":
				  flag = 2;
				  break;
			  }
			seqs.add(new SeqBean(content, flag));
		  }
	  }

	public boolean check(long fromGroup, long fromQQ, String msg) {
		for (SeqBean sb:seqs) {
			if (msg.equals(sb.content[0])) {
				sb.pos = 0;
			  }
			if (msg.equals(sb.content[sb.pos])) {
				++sb.pos;			
				if (sb.pos >= sb.content.length) {
					sb.pos = 0;
				  } else {
					Autoreply.sendMessage(fromGroup, 0, sb.content[sb.pos]);
				  }
				++sb.pos;
				if (sb.flag == 1) {
					Autoreply.instence.useCount.decLife(fromQQ);
					Autoreply.instence.groupCount.decLife(fromGroup);
				  } else if (sb.flag == 2) {
					Autoreply.instence.useCount.incMengEr(fromQQ);
					Autoreply.instence.groupCount.incMengEr(fromGroup);
				  }
				return true;
			  }
		  }
		return false;
	  }

	private void saveLiveTime() {
        try {
            File file = new File(Autoreply.appDirectory + "seq.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(new Object()));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
