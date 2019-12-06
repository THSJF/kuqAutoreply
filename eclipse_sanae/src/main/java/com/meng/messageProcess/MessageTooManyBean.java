package com.meng.messageProcess;

public class MessageTooManyBean {
	public long qq;//qq
	public long lastSpeakTimeStamp;//最后一次发言时间
	public long timeSubLowTimes;//最后两次发言时间差过短次数
	public int repeatTime;//同一句话重复次数
	public int lastSeconedMsgs;//一秒内消息数量
	public String lastMsg= "";//最后一句话
}
