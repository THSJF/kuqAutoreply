package com.meng.bilibili;

public class VideoInfoBean {
	// 读取到的数据为json格式，根据这个json写出的javabean
	private String code;
	private String message;
	private String ttl;
	private Data data;

	// 重写toString()方法
	@Override
	public String toString() {
		return data.toString();
	}

	public class Data { // 内部类要定义成public的
		private String aid;
		private String view;
		private String danmaku;
		private String reply;
		private String favorite;
		private String coin;
		private String share;
		private String now_rank;
		private String his_rank;
		private String like;
		private String dislike;
		private String no_reprint;
		private String copyright;

		@Override
		public String toString() {
			return view + "次播放," + danmaku + "条弹幕," + reply + "条评论," + coin + "个硬币," + share + "次分享," + favorite
					+ "人收藏," + like + "人赞," + dislike + "人踩";
		}

	}
}