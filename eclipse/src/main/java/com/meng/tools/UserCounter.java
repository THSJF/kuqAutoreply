package com.meng.tools;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;

public class UserCounter {
    private HashMap<Long, UserInfo> countMap = new HashMap<>();
    private File file;

    public class UserInfo {
        public int speak = 0;
        public int pic = 0;
        public int biliLink = 0;
        public int repeat = 0;
        public int repeatStart = 0;
        public int repeatBreak = 0;
        public int pohai = 0;
        public int sp = 0;
        public int setu = 0;
        public int mengEr = 0;
        public int ban = 0;
        public int gban = 0;
        public int time = 0;
        public int grass = 0;
        //     public ArrayList<Integer> count=new ArrayList<>(16);
        //    UserInfo(){
        //       for(int i=0;i<14;++i){
        //           count.add(0);
        //      }
        // }
    }

    public UserCounter() {
        file = new File(Autoreply.appDirectory + "properties\\UserCount.json");
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Type type = new TypeToken<HashMap<Long, UserInfo>>() {
        }.getType();
        countMap = Autoreply.gson.fromJson(Tools.FileTool.readString(file), type);
        Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					saveData();
				}
			});
        Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
    }


    public void incSpeak(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.speak;
    }

    public void incPic(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.pic;
    }

    public void incSetu(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.setu;
    }

    public void incPohaitu(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.pohai;
    }

    public void incFudujiguanjia(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.repeatStart;
    }

    public void incFudu(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.repeat;
    }

    public void incRepeatBreaker(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.repeatBreak;
    }

    public void incSearchPicture(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.sp;
    }

    public void incBilibiliLink(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.biliLink;
    }

    public void incMengEr(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.mengEr;
    }

    public void incBanCount(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.ban;
    }

    public void incGbanCount(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.gban;
    }

    public void decLife(long qq) {
        UserInfo userInfo = getBean(qq);
        --userInfo.time;
    }

    public void incGrass(long qq) {
        UserInfo userInfo = getBean(qq);
        ++userInfo.grass;
    }

    private UserInfo getBean(long qq) {
        UserInfo userInfo = countMap.get(qq);
        if (userInfo == null) {
            userInfo = new UserInfo();
            countMap.put(qq, userInfo);
        }
        return userInfo;
    }

    public String getMyCount(long qq) {
        if (qq == 2856986197L) {
            return "你共水群9961句\n发图9961张\n复读9961次\n迫害9961次\n带领复读9961次\n打断复读9961次\n色图9961次\n搜图9961次\n发送哔哩哔哩链接9961次\n无悔发言9961次\n口球9961次\n给大佬递口球9961次\n时间9961秒\n为绿化贡献9961棵草";
        }
		if (qq == 1620628713 || qq == 2089693971) {

			UserInfo userInfo = countMap.get(2089693971L);
			if (userInfo == null) {
				userInfo = new UserInfo();
			}
			UserInfo userInfo2 = countMap.get(1620628713L);
			if (userInfo2 == null) {
				userInfo2 = new UserInfo();
			}
			StringBuilder stringBuilder = new StringBuilder();
			if (qq != Autoreply.CQ.getLoginQQ()) {
				stringBuilder.append("你共");
			}
			if (userInfo.speak != 0 || userInfo2.speak != 0) {
				stringBuilder.append("水群").append(userInfo.speak + userInfo2.speak).append("句");
			}
			if (userInfo.pic != 0 || userInfo2.pic != 0) {
				stringBuilder.append("\n").append("发图").append(userInfo.pic + userInfo2.pic).append("张");
			}
			if (userInfo.repeat != 0 || userInfo2.repeat != 0) {
				stringBuilder.append("\n").append("复读").append(userInfo.repeat + userInfo2.speak).append("次");
			}
			if (userInfo.pohai != 0 || userInfo2.pohai != 0) {
				stringBuilder.append("\n").append("迫害").append(userInfo.pohai + userInfo2.pohai).append("次");
			}
			if (userInfo.repeatStart != 0 || userInfo2.repeatStart != 0) {
				stringBuilder.append("\n").append("带领复读").append(userInfo.repeatStart + userInfo2.repeatStart).append("次");
			}
			if (userInfo.repeatBreak != 0 || userInfo2.repeatBreak != 0) {
				stringBuilder.append("\n").append("打断复读").append(userInfo.repeatBreak + userInfo2.repeatBreak).append("次");
			}
			if (userInfo.setu != 0 || userInfo2.setu != 0) {
				stringBuilder.append("\n").append("色图").append(userInfo.setu + userInfo2.setu).append("次");
			}
			if (userInfo.sp != 0 || userInfo2.sp != 0) {
				stringBuilder.append("\n").append("搜图").append(userInfo.sp + userInfo2.sp).append("次");
			}
			if (userInfo.biliLink != 0 || userInfo2.biliLink != 0) {
				stringBuilder.append("\n").append("发送哔哩哔哩链接").append(userInfo.biliLink + userInfo2.biliLink).append("次");
			}
			if (userInfo.mengEr != 0 || userInfo2.mengEr != 0) {
				stringBuilder.append("\n").append("无悔发言").append(userInfo.mengEr + userInfo2.mengEr).append("次");
			}
			if (userInfo.ban != 0 || userInfo2.ban != 0) {
				stringBuilder.append("\n").append("口球").append(userInfo.ban + userInfo2.ban).append("次");
			}
			if (userInfo.gban != 0 || userInfo2.gban != 0) {
				stringBuilder.append("\n").append("给大佬递口球").append(userInfo.gban + userInfo2.gban).append("次");
			}
			if (userInfo.time != 0 || userInfo2.time != 0) {
				stringBuilder.append("\n").append("时间").append(userInfo.time + userInfo2.time).append("秒");
			}
			if (userInfo.grass != 0 || userInfo2.grass != 0) {
				stringBuilder.append("\n").append("为绿化贡献").append(userInfo.grass + userInfo2.grass).append("棵草");
			}
			return stringBuilder.toString();
		}
        UserInfo userInfo = countMap.get(qq);
        StringBuilder stringBuilder = new StringBuilder();
        if (qq != Autoreply.CQ.getLoginQQ()) {
            stringBuilder.append("你共");
        }
        if (userInfo.speak != 0) {
            stringBuilder.append("水群").append(userInfo.speak).append("句");
        }
        if (userInfo.pic != 0) {
            stringBuilder.append("\n").append("发图").append(userInfo.pic).append("张");
        }
        if (userInfo.repeat != 0) {
            stringBuilder.append("\n").append("复读").append(userInfo.repeat).append("次");
        }
        if (userInfo.pohai != 0) {
            stringBuilder.append("\n").append("迫害").append(userInfo.pohai).append("次");
        }
        if (userInfo.repeatStart != 0) {
            stringBuilder.append("\n").append("带领复读").append(userInfo.repeatStart).append("次");
        }
        if (userInfo.repeatBreak != 0) {
            stringBuilder.append("\n").append("打断复读").append(userInfo.repeatBreak).append("次");
        }
        if (userInfo.setu != 0) {
            stringBuilder.append("\n").append("色图").append(userInfo.setu).append("次");
        }
        if (userInfo.sp != 0) {
            stringBuilder.append("\n").append("搜图").append(userInfo.sp).append("次");
        }
        if (userInfo.biliLink != 0) {
            stringBuilder.append("\n").append("发送哔哩哔哩链接").append(userInfo.biliLink).append("次");
        }
        if (userInfo.mengEr != 0) {
            stringBuilder.append("\n").append("无悔发言").append(userInfo.mengEr).append("次");
        }
        if (userInfo.ban != 0) {
            stringBuilder.append("\n").append("口球").append(userInfo.ban).append("次");
        }
        if (userInfo.gban != 0) {
            stringBuilder.append("\n").append("给大佬递口球").append(userInfo.gban).append("次");
        }
        if (userInfo.time != 0) {
            stringBuilder.append("\n").append("时间").append(userInfo.time).append("秒");
        }
        if (userInfo.grass != 0) {
            stringBuilder.append("\n").append("为绿化贡献").append(userInfo.grass).append("棵草");
        }
        return stringBuilder.toString();
    }

    public String getTheFirst() {
        int setu = 0;
        int pic = 0;
        int pohai = 0;
        int repeatStart = 0;
        int repeat = 0;
        int repeatBreaker = 0;
        int biliLink = 0;
        int sp = 0;
        int speak = 0;
        int mengEr = 0;
        int ban = 0;
        int time = 0;
        int grass = 0;
        long setuq = 0;
        long picq = 0;
        long pohaiq = 0;
        long repeatStartq = 0;
        long repeatq = 0;
        long repeatBreakerq = 0;
        long biliLinkq = 0;
        long spq = 0;
        long speakq = 0;
        long mengErq = 0;
        long banq = 0;
        long timeq = 0;
        long grassq = 0;

        for (Entry<Long, UserInfo> entry : countMap.entrySet()) {
            if (entry.getKey().equals(Autoreply.CQ.getLoginQQ())) {
                continue;
            }
            UserInfo userInfo = entry.getValue();
            if (userInfo.speak > speak) {
                speak = userInfo.speak;
                speakq = entry.getKey();
            }
            if (userInfo.pic > pic) {
                pic = userInfo.pic;
                picq = entry.getKey();
            }
            if (userInfo.setu > setu) {
                setu = userInfo.setu;
                setuq = entry.getKey();
            }
            if (userInfo.pohai > pohai) {
                pohai = userInfo.pohai;
                pohaiq = entry.getKey();
            }
            if (userInfo.repeatStart > repeatStart) {
                repeatStart = userInfo.repeatStart;
                repeatStartq = entry.getKey();
            }
            if (userInfo.repeat > repeat) {
                repeat = userInfo.repeat;
                repeatq = entry.getKey();
            }
            if (userInfo.repeatBreak > repeatBreaker) {
                repeatBreaker = userInfo.repeatBreak;
                repeatBreakerq = entry.getKey();
            }
            if (userInfo.biliLink > biliLink) {
                biliLink = userInfo.biliLink;
                biliLinkq = entry.getKey();
            }
            if (userInfo.sp > sp) {
                sp = userInfo.sp;
                spq = entry.getKey();
            }
            if (userInfo.mengEr > mengEr) {
                mengEr = userInfo.mengEr;
                mengErq = entry.getKey();
            }
            if (userInfo.ban > ban) {
                ban = userInfo.ban;
                banq = entry.getKey();
            }
            if (userInfo.time < time) {
                time = userInfo.time;
                timeq = entry.getKey();
            }
            if (userInfo.grass > grass) {
                grass = userInfo.grass;
                grassq = entry.getKey();
            }
        }
        StringBuilder sb = new StringBuilder();
        if (speakq != 0) {
            sb.append(speakq).append("水群").append(speak).append("句");
        }
        if (picq != 0) {
            sb.append("\n").append(picq).append("发图").append(pic).append("张");
        }
        if (setuq != 0) {
            sb.append("\n").append(setuq).append("色图").append(setu).append("次");
        }
        if (pohaiq != 0) {
            sb.append("\n").append(pohaiq).append("迫害").append(pohai).append("次");
        }
        if (repeatStartq != 0) {
            sb.append("\n").append(repeatStartq).append("带领复读").append(repeatStart).append("次");
        }
        if (repeatq != 0) {
            sb.append("\n").append(repeatq).append("复读").append(repeat).append("次");
        }
        if (repeatBreakerq != 0) {
            sb.append("\n").append(repeatBreakerq).append("打断复读").append(repeatBreaker).append("次");
        }
        if (biliLinkq != 0) {
            sb.append("\n").append(biliLinkq).append("发送哔哩哔哩链接").append(biliLink).append("次");
        }
        if (spq != 0) {
            sb.append("\n").append(spq).append("搜图").append(sp).append("次");
        }
        if (mengErq != 0) {
            sb.append("\n").append(mengErq).append("无悔发言").append(mengEr).append("次");
        }
        if (banq != 0) {
            sb.append("\n").append(banq).append("口球").append(ban).append("次");
        }
        if (timeq != 0) {
            sb.append("\n").append(timeq).append("时间").append(time).append("秒");
        }
        if (grassq != 0) {
            sb.append("\n").append(grassq).append("为绿化贡献").append(grass).append("棵草");
        }
        return sb.toString();
    }

    private void saveData() {
        while (true) {
            try {
                Thread.sleep(60000);
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void backupData() {
        while (true) {
            try {
                Thread.sleep(86400000);
                File backup = new File(file.getAbsolutePath() + ".bak" + System.currentTimeMillis());
                FileOutputStream fos = new FileOutputStream(backup);
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(new Gson().toJson(countMap));
                writer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
