package com.meng.bilibili.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UpdateListener implements Runnable {

    private ArrayList<UpdatePerson> updatePerson = new ArrayList<>();

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
                for (UpdatePerson updater : updatePerson) {
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
                                tip(String.valueOf(updater.bid), updater.name + "发布新文章" + articles.id + ":" + articles.title);
                            } else {
                                updater.needTipArtical = true;
                            }
                        }
                        updater.lastArtical = articles.publish_time;
                    }
                    if (vlist != null) {
                        if (vlist.created > updater.lastVideo) {
                            if (updater.needTipVideo) {
                                tip(String.valueOf(updater.bid), updater.name + "发布新视频" + vlist.aid + ":" + vlist.title);
                            } else {
                                updater.needTipVideo = true;
                            }
                        }
                        updater.lastVideo = vlist.created;
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
        Autoreply.sendMessage(1023432971, 0, msg, true);
        Autoreply.sendToMaster(msg);
        ArrayList<Long> groupList = Autoreply.instence.configManager.getPersonInfoFromBid(Long.parseLong(updater)).tipIn;
        if (groupList != null) {
            for (long group : groupList) {
                Autoreply.sendMessage(group, 0, msg, true);
            }
        }
    }
}
