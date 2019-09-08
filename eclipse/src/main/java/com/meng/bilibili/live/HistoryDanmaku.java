package com.meng.bilibili.live;
import java.util.*;

public class HistoryDanmaku {
	public Data data=new Data();

	@Override
	public int hashCode() {
		return 1;
	  }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof HistoryDanmaku)) {
			return false;
		  }
		return data.equals((Data)o);
	  }  

	public class Data {
		public ArrayList<MsgBean> room=new ArrayList<>();

		@Override
		public int hashCode() {
			return 2;
		  }

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Data)) {
				return false;
			  }
			Data d=(HistoryDanmaku.Data) o;
			ArrayList<MsgBean> al1=d.room;
			if (al1.size() != room.size()) {
				return false;
			  }
			for (int i=0;i < al1.size();++i) {
				if (!(room.get(i).equals(al1.get(i)))) {
					return false;
				  }
			  }
			return true;     
		  }	
	  }
  }


