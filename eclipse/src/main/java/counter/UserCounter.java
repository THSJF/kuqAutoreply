package counter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.Methods;

public class UserCounter {
	private HashMap<String, UserInfo> countMap = new HashMap<>();
	private File file;

	public UserCounter() {
		file = new File(Autoreply.appDirectory + "properties\\UserCount.json");
		if (!file.exists()) {
			saveData();
		}
		try {
			Type type = new TypeToken<HashMap<String, UserInfo>>() {
			}.getType();
			countMap = new Gson().fromJson(Methods.readFileToString(file.getAbsolutePath()), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					saveData();
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(86400000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					backupData();
				}
			}
		}).start();
	}

	public boolean incSetu(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.setu;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.setu;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incPohaitu(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.pohai;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.pohai;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incFudujiguanjia(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.repeatStart;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.repeatStart;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incFudu(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.repeat;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.repeat;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incRepeatBreaker(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.repeatBreak;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.repeatBreak;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incSearchPicture(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.sp;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.sp;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incBilibiliLink(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.biliLink;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.biliLink;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incSpeak(long qq) {
		try {
			UserInfo userInfo = countMap.get(String.valueOf(qq));
			if (userInfo == null) {
				UserInfo info = new UserInfo();
				++info.speak;
				countMap.put(String.valueOf(qq), info);
				return true;
			}
			++userInfo.speak;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getMyCount(long qq) {
		UserInfo userInfo = countMap.get(String.valueOf(qq));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("你共");
		if (userInfo.speak != 0)
			stringBuilder.append("水群" + userInfo.speak + "句").append("\n");
		if (userInfo.repeat != 0)
			stringBuilder.append("复读" + userInfo.repeat + "次").append("\n");
		if (userInfo.pohai != 0)
			stringBuilder.append("迫害" + userInfo.pohai + "次").append("\n");
		if (userInfo.repeatStart != 0)
			stringBuilder.append("带领复读" + userInfo.repeatStart + "次").append("\n");
		if (userInfo.repeatBreak != 0)
			stringBuilder.append("打断复读" + userInfo.repeatBreak + "次").append("\n");
		if (userInfo.setu != 0)
			stringBuilder.append("要色图" + userInfo.setu + "次").append("\n");
		if (userInfo.sp != 0)
			stringBuilder.append("搜图" + userInfo.sp + "次").append("\n");
		if (userInfo.biliLink != 0)
			stringBuilder.append("发送哔哩哔哩链接" + userInfo.biliLink + "次");
		return stringBuilder.toString();

	}

	public String getTheFirst() {
		int setu = 0;
		int pohai = 0;
		int repeatStart = 0;
		int repeat = 0;
		int repeatBreaker = 0;
		int biliLink = 0;
		int sp = 0;
		int speak = 0;
		String setuq = null;
		String pohaiq = null;
		String repeatStartq = null;
		String repeatq = null;
		String repeatBreakerq = null;
		String biliLinkq = null;
		String spq = null;
		String speakq = null;

		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, UserInfo>> iterator = countMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, UserInfo> entry = iterator.next();
			UserInfo userInfo = entry.getValue();
			if (userInfo.speak > speak) {
				speak = userInfo.speak;
				speakq = entry.getKey();
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
		}
		if (speakq != null)
			sb.append(speakq + "共水群" + speak + "句\n");
		if (setuq != null)
			sb.append(setuq + "共要色图" + setu + "次\n");
		if (pohaiq != null)
			sb.append(pohaiq + "共迫害" + pohai + "次\n");
		if (repeatStartq != null)
			sb.append(repeatStartq + "共带领复读" + repeatStart + "次\n");
		if (repeatq != null)
			sb.append(repeatq + "共复读" + repeat + "次\n");
		if (repeatBreakerq != null)
			sb.append(repeatBreakerq + "共打断复读" + repeatBreaker + "次\n");
		if (biliLinkq != null)
			sb.append(biliLinkq + "共发送哔哩哔哩链接" + biliLink + "次\n");
		if (spq != null)
			sb.append(spq + "共搜图" + sp + "次");
		return sb.toString();
	}

	private void saveData() {
		try {
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			fos = new FileOutputStream(file);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(new Gson().toJson(countMap));
			writer.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void backupData() {
		try {
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			File backup = new File(file.getAbsolutePath() + ".bak" + System.currentTimeMillis());
			fos = new FileOutputStream(backup);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(new Gson().toJson(countMap));
			writer.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
