package com.meng.bilibili.live;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class DanmakuListenerManager {
  
	public HashSet<String> motherSet=new HashSet<>();
	public HashSet<DanmakuListener> listener=new HashSet<>();
	
	public DanmakuListenerManager() {
		File notherMapFile = new File(Autoreply.appDirectory + "mother.json");
        if (!notherMapFile.exists()) {
            saveMotherMap();
		  }
        try {
            Type token = new TypeToken<HashSet<String>>() {
			  }.getType();
            motherSet = new Gson().fromJson(Methods.readFileToString(notherMapFile.getAbsolutePath()), token);
		  } catch (Exception e) {
            e.printStackTrace();
		  }
	  }
	  
	  public DanmakuListener getListener(int room){
		for(DanmakuListener dl:listener){
		  if(dl.room==room){
			return dl;
		  }
		}
		return null;
	  }

	public boolean addMotherWord(String s) {
		try {
			motherSet.add(s);
			saveMotherMap();
		  } catch (Exception e) {
			return false;
		  }
		return true;
	  }
	  
	public boolean containsMother(String msg) {
		for (String s:motherSet) {
			if (msg.contains(s)) {
				return true;
			  }
		  }
		return false;
	  }
	  
	private void saveMotherMap() {
        try {
            File file = new File(Autoreply.appDirectory + "mother.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(motherSet));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
