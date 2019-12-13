package com.meng.bilibili.main;

import com.google.gson.*;
import com.meng.*;
import com.meng.bilibili.*;
import com.meng.config.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;

public class NewUpdateManager {

    private String[] words = new String[]{"更了吗", "出来更新", "什么时候更新啊", "在？看看更新", "怎么还不更新", "更新啊草绳"};
    private ConfigManager configManager;

    public NewUpdateManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean check(long fromGroup, String msg) {
        if (msg.contains("今天更了吗") && isUpper(msg.substring(0, msg.indexOf("今天更了吗")))) {
            long videoUpdateTime = 0;
            long articalUpdateTime = 0;
            Gson gson = new Gson();
            NewVideoBean.Data.Vlist vlist = null;
            NewArticleBean.Data.Articles articles = null;
            int upId = getUpId(msg.substring(0, msg.indexOf("今天更了吗")));
            if (upId == 0) {
                return false;
            }
            try {
                vlist = gson.fromJson(Tools.Network.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + upId + "&page=1&pagesize=1").replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"), NewVideoBean.class).data.vlist.get(0);
            } catch (Exception e) {
            }
            try {
                articles = gson.fromJson(Tools.Network.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + upId + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"), NewArticleBean.class).data.articles.get(0);
            } catch (Exception e) {
            }
            if (vlist != null && articles == null) {
                videoUpdateTime = vlist.created * 1000;
                tipVideo(fromGroup, msg, videoUpdateTime, vlist);
            } else if (vlist == null && articles != null) {
                articalUpdateTime = articles.publish_time * 1000;
                tipArticle(fromGroup, msg, articalUpdateTime, articles);
            } else if (vlist != null && articles != null) {
                videoUpdateTime = vlist.created * 1000;
                articalUpdateTime = articles.publish_time * 1000;
                if (articalUpdateTime > videoUpdateTime) {
                    tipArticle(fromGroup, msg, articalUpdateTime, articles);
                } else {
                    tipVideo(fromGroup, msg, videoUpdateTime, vlist);
                }
            }
            return true;
        }
        return false;
    }

    private void tipVideo(long fromGroup, String msg, long videoUpdateTime, NewVideoBean.Data.Vlist vlist) {
        if (System.currentTimeMillis() - videoUpdateTime < 86400000) {
            Autoreply.sendMessage(fromGroup, 0, "更新莉,,,https://www.bilibili.com/video/av" + vlist.aid);
            Autoreply.sendMessage(fromGroup, 0, BiliLinkInfo.encodeBilibiliURL(vlist.aid, true));
        } else {
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.at(getUpQQ(msg.substring(0, msg.indexOf("今天更了吗")))) + Tools.ArrayTool.rfa(words));
            int days = (int) ((System.currentTimeMillis() - videoUpdateTime) / 86400000);
            if (days <= 30) {
                Autoreply.sendMessage(fromGroup, 0, "你都" + days + "天没更新了");
            } else {
                Autoreply.sendMessage(fromGroup, 0, +days + "天不更新,你咕你[CQ:emoji,id=128052]呢");
            }
        }
    }

    private void tipArticle(long fromGroup, String msg, long articalUpdateTime, NewArticleBean.Data.Articles articles) {
        if (System.currentTimeMillis() - articalUpdateTime < 86400000) {
            Autoreply.sendMessage(fromGroup, 0, "更新莉,,,https://www.bilibili.com/read/cv" + articles.id);
            Autoreply.sendMessage(fromGroup, 0, BiliLinkInfo.encodeBilibiliURL(articles.id, false));
        } else {
            Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.at(getUpQQ(msg.substring(0, msg.indexOf("今天更了吗")))) + Tools.ArrayTool.rfa(words));
            int days = (int) ((System.currentTimeMillis() - articalUpdateTime) / 86400000);
            if (days <= 30) {
                Autoreply.sendMessage(fromGroup, 0, "你都" + days + "天没更新了");
            } else {
                Autoreply.sendMessage(fromGroup, 0, +days + "天不更新,你咕你[CQ:emoji,id=128052]呢");
            }
        }
    }

    public String getAVJson(String bid) {
        return Tools.Network.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + bid + "&page=1&pagesize=1");
    }

    public long getAVLastUpdateTime(String bid) {
        NewVideoBean.Data.Vlist vlist;
        try {
            vlist = new Gson().fromJson(Tools.Network.getSourceCode("https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" + bid + "&page=1&pagesize=1").replace("\"3\":", "\"n3\":").replace("\"4\":", "\"n4\":"), NewVideoBean.class).data.vlist.get(0);
        } catch (Exception e) {
            System.out.println("no videos");
            return 0;
        }
        return vlist.created;
    }

    public String getCVJson(String bid) {
        return Tools.Network.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + bid + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp");
    }

    public long getCVLastUpdateTime(String bid) {
        NewArticleBean.Data.Articles articles;
        try {
            articles = new Gson().fromJson(Tools.Network.getSourceCode("http://api.bilibili.com/x/space/article?mid=" + bid + "&pn=1&ps=1&sort=publish_time&jsonp=jsonp"), NewArticleBean.class).data.articles.get(0);
        } catch (Exception e) {
            System.out.println("no articles");
            return 0;
        }
        return articles.publish_time;
    }

    private boolean isUpper(String msg) {
        for (PersonInfo cb : configManager.configJavaBean.personInfo) {
            if (msg.equals(cb.name) && cb.bid != 0) {
                return true;
            }
        }
        return false;
    }

    private int getUpId(String msg) {
        for (PersonInfo cb : configManager.configJavaBean.personInfo) {
            if (cb.bid == 0) {
                continue;
            }
            if (msg.equals(cb.name)) {
                return cb.bid;
            }
        }
        return 0;
    }

    private long getUpQQ(String msg) {
        for (PersonInfo cb : configManager.configJavaBean.personInfo) {
            if (msg.equals(cb.name)) {
                return cb.qq;
            }
        }
        return 0;
    }
}
