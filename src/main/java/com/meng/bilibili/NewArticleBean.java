package com.meng.bilibili;

import java.util.List;

public class NewArticleBean {
	public String status;
	public Data data;

	public class Data {

		public List<Articles> articles;
		public int pn;
		public int ps;
		public int count;

		public class Articles {
			public int id;
			public Category category;
			public List<Categories> categories;
			public String title;
			public String summary;
			public String banner_url;
			public int template_id;
			public int state;
			public Author author;
			public int reprint;
			public List<String> image_urls;
			public int publish_time;
			public int ctime;
			public Stats stats;
			public List<Tags> tags;
			public int words;
			public String dynamic;
			public List<String> origin_image_urls;
			public boolean list;
			public String is_like;
			public Media media;

			public class Category {
				public int id;
				public int parent_id;
				public String name;
			}

			public class Categories {
				public int id;
				public int parent_id;
				public String name;
			}

			public class Author {
				public int mid;
				public String name;
				public String face;
				public Pendant pendant;
				public Official_verify official_verify;
				public Nameplate nameplate;

				public class Pendant {
					public int pid;
					public String name;
					public String image;
					public int expire;
				}

				public class Official_verify {
					public int type;
					public int desc;

				}

				public class Nameplate {
					public int nid;
					public String name;
					public String image;
					public String image_small;
					public String level;
					public String condition;
				}
			}

			public class Stats {
				public int view;
				public int favorite;
				public int like;
				public int dislike;
				public int reply;
				public int share;
				public int coin;
				public int dynamic;
			}

			public class Tags {
				public int tid;
				public String name;
			}

			public class Media {
				public int score;
				public int media_id;
				public String title;
				public String cover;
				public String area;
				public int type_id;
				public String type_name;
				public int spoiler;
			}
		}
	}
}
