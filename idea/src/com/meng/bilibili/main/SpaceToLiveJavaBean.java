package com.meng.bilibili.main;

public class SpaceToLiveJavaBean {
    public int code = 0;
    public String msg = "";
    public String message = "";
    public SpaceToLiveData data = new SpaceToLiveData();

    public class SpaceToLiveData {
        public int roomStatus = 0;
        public int roundStatus = 0;
        public int liveStatus = 0;
        public String url = "";
        public String title = "";
        public String cover = "";
        public int online = 0;
        public int roomid = 0;
        public int broadcast_type = 0;
    }

}
