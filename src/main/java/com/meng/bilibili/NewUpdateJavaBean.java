package com.meng.bilibili;

import java.util.List;

public class NewUpdateJavaBean {
	String status;
	Data data;

	public class Data {

		String count;
		String pages;
		Tlist tlist;
		List<Vlist> vlist;

		public class Tlist {
			public N n3;
			public N n4;

			public class N {
				String tid;
				String count;
				String name;
			}
		}

		public class Vlist {
			String comment;
			String typeid;
			String play;
			String pic;
			String subtitle;
			String description;
			String copyright;
			String title;
			String review;
			String author;
			String mid;
			String created;
			String length;
			String video_review;
			String favorites;
			String aid;
			String hide_click;

			public String getAid() {
				return aid;
			}

			public String getCreated() {
				return created;
			}
		}
	}
}
