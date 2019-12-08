package com.meng.bilibili.main;

import com.google.gson.*;
import com.meng.*;
import com.meng.config.*;
import com.meng.config.javabeans.*;
import com.meng.config.sanae.*;
import com.meng.tools.*;
import java.util.*;

public class UpdateListener implements Runnable {

    private ArrayList<UpdatePerson> updatePerson = new ArrayList<>();

    public UpdateListener(ConfigManager configManager) {
        for (PersonInfo cb : configManager.configJavaBean.personInfo) {
            addPerson(cb);
        }
    }

    private void addPerson(PersonInfo personInfo) {
        if (personInfo.bid == 0) {
            return;
        }
        updatePerson.add(new UpdatePerson(personInfo.name, personInfo.bid));
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0, updatePersonSize = updatePerson.size(); i < updatePersonSize; i++) {
                    UpdatePerson updater = updatePerson.get(i);
                    Gson gson = new Gson();
                    NewVideoBean.Data.Vlist vlist = null;
                    NewArticleBean.Data.Articles articles = null;
                    try {
                        vlist = gson.fromJson(Methods.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + updater.bid + "&page=1&pagesize=1").replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"), NewVideoBean.class).data.vlist.get(0);
                    } catch (Exception e) {
                    }
                    try {
                        articles = gson.fromJson(Methods.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + updater.bid + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"), NewArticleBean.class).data.articles.get(0);
                    } catch (Exception e) {
                    }
                    if (articles != null) {
                        if (articles.publish_time > updater.lastArtical) {
                            if (updater.needTipArtical) {
								try {
									RitsukageDataPack dp=RitsukageDataPack.encode(RitsukageDataPack._8newArtical, System.currentTimeMillis());
									dp.write(1, updater.name);
									dp.write(2, articles.title);
									dp.write(1, articles.id);
									Autoreply.instence.connectServer.broadcast(dp.getData());						
								} catch (Exception e) {
									Autoreply.sendMessage(Autoreply.mainGroup, 0, e.toString());
								}
								tip(String.valueOf(updater.bid), updater.name + "发布新文章" + articles.id + ":" + articles.title);
								Autoreply.instence.sanaeServer.send(SanaeDataPack.encode(SanaeDataPack._37newArtical).write(updater.name).write(articles.title).write(articles.id));
                            } else {
                                updater.needTipArtical = true;
                            }
                            updater.lastArtical = articles.publish_time;
                        }
                    }
                    if (vlist != null) {
                        if (vlist.created > updater.lastVideo) {
                            if (updater.needTipVideo) {
								try {
									RitsukageDataPack dp=RitsukageDataPack.encode(RitsukageDataPack._7newVideo, System.currentTimeMillis());
									dp.write(1, updater.name);
									dp.write(2, vlist.title);
									dp.write(1, vlist.aid);
									Autoreply.instence.connectServer.broadcast(dp.getData());
								} catch (Exception e) {
									Autoreply.sendMessage(Autoreply.mainGroup, 0, e.toString());
								}
                                tip(String.valueOf(updater.bid), updater.name + "发布新视频" + vlist.aid + ":" + vlist.title);
                            } else {
                                updater.needTipVideo = true;
                            }
                            updater.lastVideo = vlist.created;
                        }
                    }
                    Thread.sleep(2000);
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                System.out.println("更新监视出了问题：");
                e.printStackTrace();
            }
        }
    }

    private void tip(String updater, String msg) {
		if (Autoreply.instence.configManager.getPersonInfoFromBid(Integer.parseInt(updater)).isTipVidoe()) {
            Autoreply.sendMessage(Autoreply.mainGroup, 0, msg, true);
            ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromBid(Long.parseLong(updater)).tipIn;
            if (groupList != null) {
                for (long group : groupList) {
                    Autoreply.sendMessage(group, 0, msg, true);
                }
            }
        }
    }

	class UpdatePerson {
		public String name = "";
		public int bid = 0;
		public long lastVideo = 0;
		public long lastArtical = 0;
		public boolean needTipArtical = false;
		public boolean needTipVideo = false;

		public UpdatePerson(String name, int bid) {
			this.name = name;
			this.bid = bid;
		}
	}
}
