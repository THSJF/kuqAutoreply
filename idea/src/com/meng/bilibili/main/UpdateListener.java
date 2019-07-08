package com.meng.bilibili.main;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.Methods;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UpdateListener implements Runnable {

    public ArrayList<UpdatePerson> updatePerson = new ArrayList<>();

    public UpdateListener(ConfigManager configManager) {
        System.out.println("更新检测启动中");
        for (PersonInfo cb : configManager.configJavaBean.personInfo) {
            addPerson(cb);
        }
        System.out.println("更新检测启动完成");
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
                for (UpdatePerson lPerson : updatePerson) {
                    Gson gson = new Gson();
                    NewVideoBean.Data.Vlist vlist = null;
                    NewArticleBean.Data.Articles articles = null;
                    try {
                        vlist = gson.fromJson(Methods.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + lPerson.uid + "&page=1&pagesize=1").replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"), NewVideoBean.class).data.vlist.get(0);
                    } catch (IndexOutOfBoundsException ee) {

                    } catch (Exception e) {
                        System.out.println("no videos");
                    }
                    try {
                        articles = gson.fromJson(Methods.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + lPerson.uid + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"), NewArticleBean.class).data.articles.get(0);
                    } catch (IndexOutOfBoundsException ee) {

                    } catch (Exception e) {
                        System.out.println("no articles");
                    }
                    if (articles != null) {
                        if (articles.publish_time > lPerson.lastArtical) {
                            if (lPerson.needTipArtical) {
                                tipArticle(lPerson.name + "发布新文章" + articles.id + ":" + articles.title);
                            } else {
                                lPerson.needTipArtical = true;
                            }
                        }
                        lPerson.lastArtical = articles.publish_time;
                    }
                    if (vlist != null) {
                        if (vlist.created > lPerson.lastVideo) {
                            if (lPerson.needTipVideo) {
                                tipVideo(lPerson.name + "发布新视频" + vlist.aid + ":" + vlist.title);
                            } else {
                                lPerson.needTipVideo = true;
                            }
                        }
                        lPerson.lastVideo = vlist.created;
                    }
                    Thread.sleep(2000);
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                Autoreply.sendToMaster("更新监视出了问题：" + e.toString());
            }
        }
    }

    private void tipVideo(String msg) {
        Autoreply.sendToMaster(msg);
        Autoreply.sendMessage(1023432971, 0, msg);
    }

    private void tipArticle(String msg) {
        Autoreply.sendToMaster(msg);
        Autoreply.sendMessage(1023432971, 0, msg);
    }
}
