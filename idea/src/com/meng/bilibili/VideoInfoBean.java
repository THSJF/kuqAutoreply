package com.meng.bilibili;

public class VideoInfoBean {
	// 读取到的数据为json格式，根据这个json写出的javabean
	public String code;
	public String message;
	public String ttl;
	public Data data;

	// 重写toString()方法
	@Override
	public String toString() {
		return data.toString();
	}

	public class Data { // 内部类要定义成public的
		public String aid;
		public String view;
		public String danmaku;
		public String reply;
		public String favorite;
		public String coin;
		public String share;
		public String now_rank;
		public String his_rank;
		public String like;
		public String dislike;
		public String no_reprint;
		public String copyright;

		@Override
		public String toString() {
			return view + "次播放," + danmaku + "条弹幕," + reply + "条评论," + coin + "个硬币," + share + "次分享," + favorite
					+ "人收藏," + like + "人赞。";// ," + dislike + "人踩。";
		}

	}
}