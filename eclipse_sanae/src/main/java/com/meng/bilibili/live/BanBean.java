package com.meng.bilibili.live;
import java.util.*;

public class BanBean {
	public int code;
	public String msg;
	public String  message;
	public ArrayList<Data> data;

	public class Data {
		public long id;
		public long roomid;
		public long uid;
		public int type;
		public long adminid;
		public String block_end_time;
		public String ctime;
		public String msg;
		public String msg_time;
		public String uname;
		public String admin_name;
	  }
  }
