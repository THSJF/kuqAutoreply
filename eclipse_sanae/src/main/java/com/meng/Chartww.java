package com.meng;


import java.awt.Font;  
import java.text.SimpleDateFormat;  

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.DateAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.time.Month;  
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.*;
import java.io.*;  

public class Chartww {
	public static Chartww ins;
	public Chartww() {  
	
	}
	public void check() {
		XYDataset xydataset = createDataset();  
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Legal & General单位信托基金价格", "日期", "价格", xydataset, true, true, true);  
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));  
		ChartPanel frame1 = new ChartPanel(jfreechart, true);  
		dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));         //水平底部标题  		
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));  //垂直标题  
		ValueAxis rangeAxis=xyplot.getRangeAxis();//获取柱状  
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));  
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));//设置标题字体  
		try {
			ChartUtils.saveChartAsJPEG( 
				new File("C:/LineChartDemo1.png"), //文件保存物理路径包括路径和文件名 
				1.0f, //图片质量 ，0.0f~1.0f 
				frame1.getChart(), //图表对象 
				800, //图像宽度 ，这个将决定图表的横坐标值是否能完全显示还是显示省略号 
				480);
		} catch (IOException e) {}
	}

	private XYDataset createDataset() {  //这个数据集有点多，但都不难理解  
		TimeSeries timeseries = new TimeSeries("legal & general欧洲指数信任");  
		timeseries.add(new Month(2, 2001), 181.80000000000001D);  
		timeseries.add(new Month(3, 2001), 167.30000000000001D);  
		timeseries.add(new Month(4, 2001), 153.80000000000001D);  
		timeseries.add(new Month(5, 2001), 167.59999999999999D);  
		timeseries.add(new Month(6, 2001), 158.80000000000001D);  
		timeseries.add(new Month(7, 2001), 148.30000000000001D);  
		timeseries.add(new Month(8, 2001), 153.90000000000001D);  
		timeseries.add(new Month(9, 2001), 142.69999999999999D);  
		timeseries.add(new Month(10, 2001), 123.2D);  
		timeseries.add(new Month(11, 2001), 131.80000000000001D);  
		timeseries.add(new Month(12, 2001), 139.59999999999999D);  
		timeseries.add(new Month(1, 2002), 142.90000000000001D);  
		timeseries.add(new Month(2, 2002), 138.69999999999999D);  
		timeseries.add(new Month(3, 2002), 137.30000000000001D);  
		timeseries.add(new Month(4, 2002), 143.90000000000001D);  
		timeseries.add(new Month(5, 2002), 139.80000000000001D);  
		timeseries.add(new Month(6, 2002), 137D);  
		timeseries.add(new Month(7, 2002), 132.80000000000001D);  
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
		timeseriescollection.addSeries(timeseries);
		return timeseriescollection;  
	}    
}  
