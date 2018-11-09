package com.meng.bilibili;

import java.util.List;

public class NewArticleBean {
	public String status;
	public Data data;

	public class Data {

		public List<Articles> articles;
		public String pn;
		public String ps;
		public String count;

		public class Articles {
			public String id;
			public Category category;
			public List<Categories> categories;
			public String title;
			public String summary;
			public String banner_url;
			public String template_id;
			public String state;
			public Author author;
			public String reprint;
			public List<String> image_urls;
			public String publish_time;
			public String ctime;
			public Stats stats;
			public List<Tags> tags;
			public String words;
			public String dynamic;
			public List<String> origin_image_urls;
			public String list;
			public String is_like;
			public Media media;

			public class Category {
				public String id;
				public String parent_id;
				public String name;
			}

			public class Categories {
				public String id;
				public String parent_id;
				public String name;
			}

			public class Author {
				public String mid;
				public String name;
				public String face;
				public Pendant pendant;
				public Official_verify official_verify;
				public Nameplate nameplate;

				public class Pendant {
					public String pid;
					public String name;
					public String image;
					public String expire;
				}

				public class Official_verify {
					public String type;
					public String desc;

				}

				public class Nameplate {
					public String nid;
					public String name;
					public String image;
					public String image_small;
					public String level;
					public String condition;
				}
			}

			public class Stats {
				public String view;
				public String favorite;
				public String like;
				public String dislike;
				public String reply;
				public String share;
				public String coin;
				public String dynamic;
			}

			public class Tags {
				public String tid;
				public String name;
			}

			public class Media {
				public String score;
				public String media_id;
				public String title;
				public String cover;
				public String area;
				public String type_id;
				public String type_name;
				public String spoiler;
			}
		}
	}
}
