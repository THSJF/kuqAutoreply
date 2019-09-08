package com.meng.bilibili.live;

public class MsgBean {
	public String text="";
	public String nickname="";
	public long uid;
	public String timeline="";

	@Override
	public int hashCode() {
		return (int)uid;
	  }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MsgBean)) {
			return false;
		  }
		MsgBean mb=(MsgBean) o;
		return uid == mb.uid;
	  }

  }


