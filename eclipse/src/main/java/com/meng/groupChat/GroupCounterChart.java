package com.meng.groupChat;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.picProcess.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;   

public class GroupCounterChart {
	public static GroupCounterChart ins;
	public HashMap<Long,GroupCounterChartBean> groupsMap = new HashMap<>(32);
	private File file;
	public DayChart dchart;
	public MonthChart mchart;
	public ChartDrawer chartDrawer=new ChartDrawer();
	public GroupCounterChart() {
		file = new File(Autoreply.appDirectory + "properties\\GroupCount2.json");
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
        Type type = new TypeToken<HashMap<Long, GroupCounterChartBean>>() {
        }.getType();
        groupsMap = Autoreply.gson.fromJson(Tools.FileTool.readString(file), type);
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					saveData();
				}
			});
		dchart = new DayChart();
		mchart = new MonthChart();
	}
	
	public void addSpeak(long group, int times) {
		GroupCounterChartBean gs=groupsMap.get(group);
		if (gs == null) {
			gs = new GroupCounterChartBean();
			groupsMap.put(group, gs);
		}
		gs.all += times;
		HashMap<Integer,Integer> everyHourHashMap = gs.hour.get(Tools.CQ.getDate());
		if (everyHourHashMap == null) {
			everyHourHashMap = new HashMap<>();
			gs.hour.put(Tools.CQ.getDate(), everyHourHashMap);
		}
		Date da=new Date();
		int nowHour=da.getHours();
		if (everyHourHashMap.get(nowHour) == null) {
			everyHourHashMap.put(nowHour, times);
		} else {
			int stored=everyHourHashMap.get(nowHour);
			stored += times;
			everyHourHashMap.put(nowHour, stored);
		}
	}

	public HashMap<Integer,Integer> getSpeak(long group, String date) {
		GroupCounterChartBean gs = groupsMap.get(group);
		if (gs == null) {
			return null;
		}
		HashMap<Integer,Integer> hr = gs.hour.get(date);
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

	public class DayChart {
		public DayChart() {  

		}
		public File check(GroupCounterChartBean gs) {
			return chartDrawer.draw24hChart(gs);
		}    
	}

	public class MonthChart {
		public MonthChart() {  

		}
		public File check(GroupCounterChartBean gs) {
			return chartDrawer.draw30dChart(gs);
		}
	}
}
