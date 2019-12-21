package com.meng.messageProcess;
import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class GroupCounter {
	public HashMap<Long,GroupSpeak> groupsMap = new HashMap<>(32);
	public File file;
	public GroupCounter() {
		file = new File(Autoreply.appDirectory + "properties\\GroupCount.json");
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(groupsMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Type type = new TypeToken<HashMap<Long, GroupSpeak>>() {
        }.getType();
        groupsMap = Autoreply.gson.fromJson(Tools.FileTool.readString(file), type);
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					saveData();
				}
			});
	}
	private class GroupSpeak {
		public int all=0;
		public HashMap<String,ArrayList<Integer>> hour=new HashMap<>(16);		
	}

	public void addSpeak(long group, int times) {
		GroupSpeak gs=groupsMap.get(group);
		if (gs == null) {
			gs = new GroupSpeak();
			groupsMap.put(group, gs);
		}
		gs.all += times;
		ArrayList<Integer> hr = gs.hour.get(Tools.CQ.getDate());
		if (hr == null) {
			hr = new ArrayList<>(32);
			for (int i=0;i < 24;++i) {
				hr.add(0);
			}
			gs.hour.put(Tools.CQ.getDate(), hr);
		}
		Date da=new Date();
		int nowHour=da.getHours();
		int stored=hr.get(nowHour);
		stored += times;
		hr.set(nowHour, stored);
	}

	public ArrayList<Integer> getSpeak(long group, String date) {
		GroupSpeak gs=groupsMap.get(group);
		if (gs == null) {
			return null;
		}
		ArrayList<Integer> hr = gs.hour.get(date);
		if (hr == null) {
			return null;
		}
		return hr;
	}

	private void saveData() {
        while (true) {
            try {
                Thread.sleep(60000);
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(groupsMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
