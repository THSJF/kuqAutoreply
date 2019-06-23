package com.meng;

public class MessageSender {
	private int subType = 0;
	private int msgId = 0;
	private int font = 0;
	private long fromGroup = 0;
	private long fromQQ = 0;
	private String msg = "";
	private long timeStamp = 0;

	public MessageSender(long fromGroup, long fromQQ, String msg, long timeStamp, int msgId) {
		this.fromGroup = fromGroup;
		this.fromQQ = fromQQ;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.msgId = msgId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getFont() {
		return font;
	}

	public void setFont(int font) {
		this.font = font;
	}

	public long getFromGroup() {
		return fromGroup;
	}

	public void setFromGroup(long fromGroup) {
		this.fromGroup = fromGroup;
	}

	public long getFromQQ() {
		return fromQQ;
	}

	public void setFromQQ(long fromQQ) {
		this.fromQQ = fromQQ;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
