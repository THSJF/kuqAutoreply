package com.meng;

public class MessageSender {
	private int subType = 0;
	private int msgId = 0;
	private int font = 0;
	private long fromGroup = 0;
	private long fromQQ = 0;
	private String msg = "";

	public MessageSender(long fromGroup, long fromQQ, String msg, int subType, int msgId, int font) {
		this.fromGroup = fromGroup;
		this.fromQQ = fromQQ;
		this.msg = msg;
		this.subType = subType;
		this.msgId = msgId;
		this.font = font;
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
