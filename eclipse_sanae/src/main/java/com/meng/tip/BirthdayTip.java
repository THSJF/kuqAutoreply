package com.meng.tip;
import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import com.sobte.cqp.jcq.entity.Member;

public class BirthdayTip {
	private HashSet<Long> tiped=new HashSet<Long>();
	private File ageFile;
	private HashMap<Long,Integer> memberMap=new HashMap<>();
	public BirthdayTip() {

		ageFile = new File(Autoreply.appDirectory + "/properties/birthday.json");
        if (!ageFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<HashMap<Long,Integer>>() {
        }.getType();
        memberMap = Autoreply.gson.fromJson(Tools.FileTool.readString(ageFile), type);

		Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						check();
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			});
	}

	public void check() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.HOUR_OF_DAY) == 6 && c.get(Calendar.MINUTE) == 1) {
			List<Group> groups=Autoreply.CQ.getGroupList();
			for (Group group:groups) {
				List<Member> members=Autoreply.CQ.getGroupMemberList(group.getId());
				for (Member member:members) {
					if (memberMap.get(member.getQqId()) != null && member.getAge() > memberMap.get(member.getQqId())) {
						if (!tiped.contains(member.getQqId())) {
							Autoreply.sendMessage(0, member.getQqId(), "生日快乐!");
							tiped.add(member.getQqId());
						}
					}
					memberMap.put(member.getQqId(), member.getAge());
				}
			}
			saveConfig();
			tiped.clear();
		}
	}

	private void saveConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(ageFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(Autoreply.gson.toJson(memberMap));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
