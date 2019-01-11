package com.meng;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.meng.Autoreply;

public class UseCount {
	String propFodlerName = Autoreply.appDirectory + "properties\\";

	String setuPropName = propFodlerName + "setu.properties";
	String fudujiguanjiaPropName = propFodlerName + "fudujiguanjia.properties";
	String fuduPropName = propFodlerName + "fudu.properties";
	String repeatBreakerPropName = propFodlerName + "repeatBreaker.properties";
	String searchPicturePropName = propFodlerName + "searchPicture.properties";
	String bilibililinkPropName = propFodlerName + "bilibililink.properties";

	public UseCount() {
		File propFileFoder = new File(propFodlerName);

		File propFileSetu = new File(setuPropName);
		File propFileFudujiguanjia = new File(fudujiguanjiaPropName);
		File propFileFudu = new File(fuduPropName);
		File propFileSearchPicture = new File(searchPicturePropName);
		File propFileBilibiliLink = new File(bilibililinkPropName);
		File propFileRepeatBreaker = new File(repeatBreakerPropName);
		try {
			if (!propFileFoder.exists())
				propFileFoder.mkdirs();
			if (!propFileSetu.exists())
				propFileSetu.createNewFile();
			if (!propFileFudujiguanjia.exists())
				propFileFudujiguanjia.createNewFile();
			if (!propFileFudu.exists())
				propFileFudu.createNewFile();
			if (!propFileSearchPicture.exists())
				propFileSearchPicture.createNewFile();
			if (!propFileBilibiliLink.exists())
				propFileBilibiliLink.createNewFile();
			if (!propFileRepeatBreaker.exists())
				propFileRepeatBreaker.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean incSetu(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(setuPropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(setuPropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "setu times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incFudujiguanjia(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(fudujiguanjiaPropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(fudujiguanjiaPropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "fudujiguanjia times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incFudu(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(fuduPropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(fuduPropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "fudu times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incRepeatBreaker(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(repeatBreakerPropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(repeatBreakerPropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "repeatBreaker times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incSearchPicture(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(searchPicturePropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(searchPicturePropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "searchPicture times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incBilibiliLink(long qq) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(bilibililinkPropName));
			prop.load(in); /// 加载属性列表
			in.close();
			FileOutputStream oFile = new FileOutputStream(bilibililinkPropName, false);// false覆盖原本数据，true追加数据
			if (prop.get("qq" + qq) != null) {
				prop.setProperty("qq" + qq, (Integer.parseInt((String) prop.get("qq" + qq)) + 1) + "");
			} else {
				prop.setProperty("qq" + qq, 1 + "");
			}
			prop.store(oFile, "bilibililink times");
			oFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getMyCount(long qq) {
		int setu = 0;
		int fudujiguanjia = 0;
		int fudu = 0;
		int repeatBreaker = 0;
		int bilibili = 0;
		int searchPic = 0;

		Properties prop;
		InputStream in;

		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(setuPropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				setu = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(fudujiguanjiaPropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				fudujiguanjia = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(fuduPropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				fudu = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(repeatBreakerPropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				repeatBreaker = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(bilibililinkPropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				bilibili = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop = new Properties();
			in = new BufferedInputStream(new FileInputStream(searchPicturePropName));
			prop.load(in);
			in.close();
			if (prop.get("qq" + qq) != null)
				searchPic = Integer.parseInt((String) prop.get("qq" + qq));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "你共复读" + fudu + "次,带领复读" + fudujiguanjia + "次,打断复读" + repeatBreaker + "次,要色图" + setu + "次,搜图" + searchPic
				+ "次,发送哔哩哔哩链接" + bilibili + "次";

	}

}
