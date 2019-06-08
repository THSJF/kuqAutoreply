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
				public int tid;
				public int count;
				public String name;
			}
		}

		public class Vlist {
			public int comment;
			public int typeid;
			public int play;
			public String pic;
			public String subtitle;
			public String description;
			public String copyright;
			public String title;
			public int review;
			public String author;
			public int mid;
			public int is_union_video;
			public int created;
			public String length;
			public int video_review;
			public int is_pay;
			public int favorites;
			public int aid;
			public String hide_click;
		}
	}
}
