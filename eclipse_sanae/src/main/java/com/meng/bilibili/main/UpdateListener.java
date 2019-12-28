package com.meng.bilibili.main;

import com.google.gson.*;
import com.meng.*;
import com.meng.bilibili.*;
import com.meng.config.*;
import com.meng.tools.*;
import java.util.*;

public class UpdateListener implements Runnable {

    public UpdateListener() {
    }

    @Override
    public void run() {
        while (true) {
            try {
				for (BiliMaster updater:ConfigManager.ins.SanaeConfig.biliMaster.values()) {
                    NewVideoBean.Data.Vlist vlist = null;
                    NewArticleBean.Data.Articles articles = null;
                    try {
                        vlist = Autoreply.gson.fromJson(Tools.Network.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + updater.uid + "&page=1&pagesize=1").replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"), NewVideoBean.class).data.vlist.get(0);
                    } catch (Exception e) {
                    }
                    try {
                        articles = Autoreply.gson.fromJson(Tools.Network.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + updater.uid + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"), NewArticleBean.class).data.articles.get(0);
                    } catch (Exception e) {
                    }
                    if (articles != null) {
                        if (articles.publish_time > updater.lastArtical) {
                            if (updater.needTipArtical) {
								tipArtical(updater, articles);
                            } else {
                                updater.needTipArtical = true;
                            }
                            updater.lastArtical = articles.publish_time;
                        }
                    }
                    if (vlist != null) {
                        if (vlist.created > updater.lastVideo) {
                            if (updater.needTipVideo) {
                                tipVideo(updater, vlist);
                            } else {
                                updater.needTipVideo = true;
                            }
                            updater.lastVideo = vlist.created;
                        }
                    }
                    Thread.sleep(2000);
				}
            } catch (Exception e) {
                System.out.println("更新监视出了问题：");
                e.printStackTrace();
            }
        }
    }

    private void tipVideo(BiliMaster p, NewVideoBean.Data.Vlist vl) {
		String userName = new JsonParser().parse(Tools.Network.getSourceCode("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + p.roomID)).getAsJsonObject().get("data").getAsJsonObject().get("info").getAsJsonObject().get("uname").getAsString();
        for (int i = 0, groupListSize = p.fans.size(); i < groupListSize; i++) {
            BiliMaster.FansInGroup fans = p.fans.get(i);
			if (FaithManager.ins.getFaith(fans.qq) > 0) {
				Autoreply.sendMessage(fans.group, 0, String.format("\n%s你关注的up主「%d」发布了新视频\nAID:%d\n视频名:%s", Autoreply.CC.at(fans.qq), userName, vl.aid, vl.title));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
    }

	private void tipArtical(BiliMaster p, NewArticleBean.Data.Articles vl) {
		String userName = new JsonParser().parse(Tools.Network.getSourceCode("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + p.roomID)).getAsJsonObject().get("data").getAsJsonObject().get("info").getAsJsonObject().get("uname").getAsString();
		for (int i = 0, groupListSize = p.fans.size(); i < groupListSize; i++) {
            BiliMaster.FansInGroup fans = p.fans.get(i);
			if (FaithManager.ins.getFaith(fans.qq) > 0) {
				Autoreply.sendMessage(fans.group, 0, String.format("\n%s你关注的up主「%d」发布了新专栏\nCID:%d\n专栏名:%s", Autoreply.CC.at(fans.qq), userName, vl.id, vl.title));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
	}

	public class NewArticleBean {
		public String status;
		public Data data;
		public class Data {
			public ArrayList<Articles> articles;
			public int pn;
			public int ps;
			public int count;
			public class Articles {
				public int id;
				public Category category;
				public ArrayList<Categories> categories;
				public String title;
				public String summary;
				public String banner_url;
				public int template_id;
				public int state;
				public Author author;
				public int reprint;
				public ArrayList<String> image_urls;
				public long publish_time;
				public long ctime;
				public Stats stats;
				public ArrayList<Tags> tags;
				public int words;
				public String dynamic;
				public ArrayList<String> origin_image_urls;
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
						public String desc;

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

	public class NewVideoBean {
		public String status;
		public Data data;
		public class Data {
			public String count;
			public String pages;
			public Tlist tlist;
			public ArrayList<Vlist> vlist;
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
				public long created;
				public String length;
				public int video_review;
				public int is_pay;
				public int favorites;
				public int aid;
				public String hide_click;
			}
		}
	}
}
