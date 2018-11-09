package com.meng.bilibili;

import java.util.List;

public class NewVideoBean {
	public String status;
	public Data data;

	public class Data {

		public String count;
		public String pages;
		public Tlist tlist;
		public List<Vlist> vlist;

		public class Tlist {
			public N n3;
			public N n4;

			public class N {
				public String tid;
				public String count;
				public String name;
			}
		}

		public class Vlist {
			public String comment;
			public String typeid;
			public String play;
			public String pic;
			public String subtitle;
			public String description;
			public String copyright;
			public String title;
			public String review;
			public String author;
			public String mid;
			public String created;
			public String length;
			public String video_review;
			public String favorites;
			public String aid;
			public String hide_click;
		}
	}
}
