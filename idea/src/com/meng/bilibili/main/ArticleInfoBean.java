package com.meng.bilibili.main;

import java.util.List;

public class ArticleInfoBean {
    // 读取到的数据为json格式，根据这个json写出的javabean
    public String code;
    public String message;
    public String ttl;
    public Data data;

    @Override
    public String toString() {
        return data.toString();
    }

    public class Data {
        public String like;
        public String attention;
        public String favorite;
        public String coin;

        public Stats stats;

        public String title;
        public String banner_url;
        public String mid;
        public String author_name;
        public String is_author;

        public List<String> image_urls;
        public List<String> origin_image_urls;

        public String shareable;
        public String show_later_watch;
        public String show_small_window;
        public String in_list;
        public String pre;
        public String next;

        @Override
        public String toString() {
            return "标题：" + title + "\n作者：" + author_name + "\n" + stats.toString();
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

            @Override
            public String toString() {
                return view + "次阅读," + reply + "条评论," + coin + "个硬币," + share + "次分享," + favorite + "人收藏," + like
                        + "人赞," + dislike + "人踩";
            }
        }
    }
}