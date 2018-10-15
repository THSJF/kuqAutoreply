package com.meng;

public class BilibiliVideoInfoJavaBean {
	private String code;
	private String message;
	private String ttl;
	private Data data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "" + data;
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

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}

		public String getView() {
			return view;
		}

		public void setView(String view) {
			this.view = view;
		}

		public String getDanmaku() {
			return danmaku;
		}

		public void setDanmaku(String danmaku) {
			this.danmaku = danmaku;
		}

		public String getReply() {
			return reply;
		}

		public void setReply(String reply) {
			this.reply = reply;
		}

		public String getFavorite() {
			return favorite;
		}

		public void setFavorite(String favorite) {
			this.favorite = favorite;
		}

		public String getCoin() {
			return coin;
		}

		public void setCoin(String coin) {
			this.coin = coin;
		}

		public String getShare() {
			return share;
		}

		public void setShare(String share) {
			this.share = share;
		}

		public String getNow_rank() {
			return now_rank;
		}

		public void setNow_rank(String now_rank) {
			this.now_rank = now_rank;
		}

		public String getHis_rank() {
			return his_rank;
		}

		public void setHis_rank(String his_rank) {
			this.his_rank = his_rank;
		}

		public String getLike() {
			return like;
		}

		public void setLike(String like) {
			this.like = like;
		}

		public String getDislike() {
			return dislike;
		}

		public void setDislike(String dislike) {
			this.dislike = dislike;
		}

		public String getNo_reprint() {
			return no_reprint;
		}

		public void setNo_reprint(String no_reprint) {
			this.no_reprint = no_reprint;
		}

		public String getCopyright() {
			return copyright;
		}

		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return view + "次播放," + danmaku + "条弹幕," + reply + "条回复," + coin + "个硬币," + share + "次分享," + favorite
					+ "人收藏," + like + "人赞," + dislike + "人踩";
		}

	}
}