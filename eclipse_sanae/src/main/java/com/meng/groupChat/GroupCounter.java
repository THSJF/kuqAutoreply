package com.meng.groupChat;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.awt.*;  
import java.text.*;  

import org.jfree.chart.*; 
import org.jfree.chart.axis.*;  
import org.jfree.chart.plot.*;  
import org.jfree.data.time.*;   

public class GroupCounter {
	public static GroupCounter ins;
	public HashMap<Long,GroupSpeak> groupsMap = new HashMap<>(32);
	private File file;
	public DayChart dchart;
	public MonthChart mchart;
	public GroupCounter() {
		file = new File(Autoreply.ins.appDirectory + "properties\\GroupCount.json");
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
		Autoreply.ins.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					saveData();
				}
			});
		dchart = new DayChart();
		mchart = new MonthChart();
	}
	public class GroupSpeak {
		public int all=0;
		public HashMap<String,HashMap<Integer,Integer>> hour=new HashMap<>(16);		
	}

	public void addSpeak(long group, int times) {
		GroupSpeak gs=groupsMap.get(group);
		if (gs == null) {
			gs = new GroupSpeak();
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
		GroupSpeak gs = groupsMap.get(group);
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
		public File check(GroupCounter.GroupSpeak gs) {
			TimeSeries timeseries = new TimeSeries("你群发言");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, -24);
			HashMap<Integer,Integer> everyHour=gs.hour.get(Tools.CQ.getDate(c.getTimeInMillis()));
			for (int i=c.get(Calendar.HOUR_OF_DAY);i < 24;++i) {
				timeseries.add(new Hour(i, c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)), everyHour.get(i) == null ?0: everyHour.get(i));
			}
			c = Calendar.getInstance();
			everyHour = gs.hour.get(Tools.CQ.getDate(c.getTimeInMillis()));
			for (int i=0;i <= c.get(Calendar.HOUR_OF_DAY);++i) {
				timeseries.add(new Hour(i, c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)), everyHour.get(i) == null ?0: everyHour.get(i));
			}
			TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
			timeseriescollection.addSeries(timeseries);
			JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("你群24小时发言", "时间", "", timeseriescollection, true, true, true);  
			XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
			dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));  
			ChartPanel frame1 = new ChartPanel(jfreechart, true);  
			dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));  		
			dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));  
			jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
			jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));  
			File pic=null;
			try {
				pic = new File(Autoreply.ins.appDirectory + "downloadImages/" + System.currentTimeMillis() + ".jpg");
				ChartUtils.saveChartAsJPEG( 
					pic,
					1.0f,
					frame1.getChart(), 
					800, 
					480);
			} catch (IOException e) {}
			return pic;
		}    
	}

	public class MonthChart {
		public MonthChart() {  

		}
		public File check(GroupCounter.GroupSpeak gs) {
			TimeSeries timeseries = new TimeSeries("你群发言");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			for (int i=0;i <= 30;++i) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				HashMap<Integer,Integer> everyHour=gs.hour.get(Tools.CQ.getDate(cal.getTimeInMillis()));
				int oneDay=0;
				if (everyHour == null) {
					oneDay = 0;
				} else {
					for (int oneHour:everyHour.values()) {
						oneDay += oneHour;
					}
				}
				timeseries.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), oneDay);
			}
			TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
			timeseriescollection.addSeries(timeseries);
			JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("你群30天发言", "时间", "", timeseriescollection, true, true, true);  
			XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
			dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));  
			ChartPanel frame1 = new ChartPanel(jfreechart, true);  
			dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); //水平底部标题  		
			dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));//垂直标题  
			jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
			jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));//设置标题字体  
			File pic=null;
			try {
				pic = new File(Autoreply.ins.appDirectory + "downloadImages/" + System.currentTimeMillis() + ".jpg");
				ChartUtils.saveChartAsJPEG( 
					pic, //文件保存物理路径包括路径和文件名 
					1.0f, //图片质量 ，0.0f~1.0f 
					frame1.getChart(), //图表对象 
					800, //图像宽度 ，这个将决定图表的横坐标值是否能完全显示还是显示省略号 
					480);
			} catch (IOException e) {}
			return pic;
		}



	}
}
