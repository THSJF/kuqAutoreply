package com.meng;
import java.util.*;
import java.util.concurrent.*;

public class SpellCollect {
	private ConcurrentHashMap<String,HashSet<String>> chm=new ConcurrentHashMap<>();
	public SpellCollect() {

	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("#幻币转账") && fromQQ == Autoreply.instence.configManager.configJavaBean.ogg) {
			List<Long> chan=Autoreply.instence.CC.getAts(msg);
			if(!Autoreply.instence.configManager.isMaster(chan.get(1))){
				return false;
			}
			int coins=0;
			try {
				coins = Integer.parseInt(msg.substring(msg.indexOf("转账", 6) + 2, msg.indexOf("个幻币")));		
			} catch (NumberFormatException e) {
				Autoreply.sendMessage(fromGroup, 0, e.toString());
			}
			HashSet<String> tmpSet=chm.get(String.valueOf(chan.get(0)));
			if (tmpSet == null) {
				tmpSet = new HashSet<String>();
				chm.put(String.valueOf(chan.get(0)), tmpSet);
			}
			Random r=new Random();
			StringBuilder sb=new StringBuilder();
			for (int i=0;i < coins;++i) {
				for (int j=0;j < 5;++j) {
					String s=Autoreply.instence.diceImitate.spells[r.nextInt(Autoreply.instence.diceImitate.spells.length)];
					tmpSet.add(s);
					sb.append(s);
				}
			}
			Autoreply.sendMessage(fromGroup, 0, "get:" + sb.toString());
			Autoreply.sendMessage(fromGroup, 0, chm.toString());
		}

		return false;
	}
}
