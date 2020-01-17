package com.meng.groupChat;

import com.meng.*;
import com.meng.config.*;
import java.util.*;

public class RepeaterManager {
	public static RepeaterManager ins;
    private HashMap<Long, Repeater> repeaters = new HashMap<>();

    public RepeaterManager() {

    }

    public void addRepeater(long group) {
        repeaters.put(group, new Repeater(group));
    }

    public boolean check(long group, long qq, String msg) {
		Repeater rp=repeaters.get(group);
		if (rp == null) {
			rp = new Repeater(group);
			repeaters.put(group, rp);
		}
        return rp.check(group, qq, msg);
    }

	private class Repeater {
		private String lastMessageRecieved = "";
		private boolean lastStatus = false;
		long groupNumber = 0;

		public Repeater(long groupNumber) {
			this.groupNumber = groupNumber;
		}

		public boolean check(long fromGroup, long fromQQ, String msg) {
			boolean b = false; 
			b = checkRepeatStatu(fromGroup, fromQQ, msg);
			lastMessageRecieved = msg;
			return b;
		}

		// 复读状态
		private boolean checkRepeatStatu(long group, long qq, String msg) {
			boolean b = false;
			if (!lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatStart(group, qq, msg);
			}
			if (lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatRunning(group, qq, msg);
			}
			if (lastStatus && !lastMessageRecieved.equals(msg)) {
				b = repeatEnd(group, qq, msg);
			}
			lastStatus = lastMessageRecieved.equals(msg);
			return b;
		}

		private boolean repeatEnd(long group, long qq, String msg) {
			ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack.opIncRepeatBreak).write(group).write(qq));
			return false;
		}

		private boolean repeatRunning(long group, long qq, String msg) {
			ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack.opIncRepeat).write(group).write(qq));
			return false;
		}

		private boolean repeatStart(long group,  long qq,  String msg) {
			ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack.opIncRepeatStart).write(qq));
			Autoreply.sendMessage(group, 0, msg);
			return true;
		}
	}
}
