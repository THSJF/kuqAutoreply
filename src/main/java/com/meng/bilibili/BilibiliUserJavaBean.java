package com.meng.bilibili;

import java.util.ArrayList;

public class BilibiliUserJavaBean {
	public ArrayList<BilibiliUser> mapBiliUser = new ArrayList<>();

	public class BilibiliUser {
		public String name = "";
		public long qq = 0;
		public int bid = 0;
		public int bliveRoom = 0;
		public boolean autoTip = false;
	}
}
