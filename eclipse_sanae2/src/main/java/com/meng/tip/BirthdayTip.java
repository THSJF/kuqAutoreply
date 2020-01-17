package com.meng.tip;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import org.meowy.cqp.jcq.entity.*;

import org.meowy.cqp.jcq.entity.Member;


public class BirthdayTip {
	private HashSet<Long> tiped=new HashSet<Long>();
	private File ageFile;
	private HashMap<Long,Integer> memberMap=new HashMap<>();
	public BirthdayTip() {

		ageFile = new File(Autoreply.ins.appDirectory + "/properties/birthday.json");
        if (!ageFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<HashMap<Long,Integer>>() {
        }.getType();
        memberMap = Autoreply.gson.fromJson(Tools.FileTool.readString(ageFile), type);

		Autoreply.ins.threadPool.execute(new Runnable(){

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
			List<Group> groups=Autoreply.ins.getCoolQ().getGroupList();
			for (Group group:groups) {
				List<Member> members=Autoreply.ins.getCoolQ().getGroupMemberList(group.getId());
				for (Member member:members) {
					if (memberMap.get(member.getQQId()) != null && member.getAge() > memberMap.get(member.getQQId())) {
						if (!tiped.contains(member.getQQId())) {
							Autoreply.sendMessage(0, member.getQQId(), "生日快乐!");
							tiped.add(member.getQQId());
						}
					}
					memberMap.put(member.getQQId(), member.getAge());
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
