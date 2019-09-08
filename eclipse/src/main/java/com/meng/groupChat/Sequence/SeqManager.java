package com.meng.groupChat.Sequence;
import java.util.*;
import com.meng.*;

public class SeqManager {
	public String[] time=new String[]{"苟","利","国","家","生","死","以","岂","因","祸","福","避","趋","之"};
	private ArrayList<SeqBean> seqs=new ArrayList<>();

	public SeqManager() {
		seqs.add(new SeqBean(time, 1));
	  }

	public boolean check(long fromGroup, long fromQQ, String msg) {
		for (SeqBean sb:seqs) {
			if (msg.equals(sb.content[sb.pos])) {
				if (msg.equals(sb.content[0])) {
					sb.pos = 0;
				  }
				++sb.pos;			
				if (sb.pos >= sb.content.length) {
					sb.pos = 0;
				  } else {
					Autoreply.sendMessage(fromGroup, 0, sb.content[sb.pos]);
				  }
				++sb.pos;
				if (sb.flag == 1) {
					Autoreply.instence.useCount.decLife(fromQQ);
					Autoreply.instence.groupCount.decLife(fromGroup);
				  }
				return true;
			  }
		  }
		return false;
	  }
  }
