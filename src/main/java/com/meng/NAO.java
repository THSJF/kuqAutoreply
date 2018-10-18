package com.meng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class NAO extends Thread {

	private long fromQQ = 0;
	private long fromGroup = -1;
	private File pic = null;

	public NAO(long fromGroup, long fromQQ, File pic) {
		this.fromGroup = fromGroup;
		this.fromQQ = fromQQ;
		this.pic = pic;
	}

	@Override
	public void run() {
		try {
			check(fromQQ, pic);
		} catch (IOException e) {
			if (fromGroup == -1) {
				Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "少女折寿中……");
			}
		}
	}

	private void check(long fromQQ, File picF) throws IOException {
		FileInputStream fInputStream = new FileInputStream(picF);
		Connection.Response response = Jsoup.connect("https://saucenao.com/search.php?db=" + 999)
				.data("file", "image.jpg", fInputStream).method(Connection.Method.POST).execute();
		if (response.statusCode() != 200) {
			switch (response.statusCode()) {
			case 429:
				return;
			}
		}
		ArrayList<String> items = getTable(response.body());
		if (items == null) {
			if (fromGroup == -1) {
				Autoreply.sendPrivateMessage(fromQQ, "没有相似度较高的图片");
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "没有相似度较高的图片");
			}
			return;
		}
		ArrayList<NaoJavabean> its = getEles(items);
		StringBuilder sBuilder = new StringBuilder();
		int size = its.size() > 3 ? 3 : its.size();
		for (int i = 0; i < size; i++) {
			NaoJavabean tmp = its.get(i);
			File dFile = null;
			try {
				File files = new File(Autoreply.appDirectory + "picSearch\\tmp\\");
				if (!files.exists()) {
					files.mkdirs();
				}
				URL url = new URL(tmp.getLuesuotu());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				InputStream is = connection.getInputStream();
				dFile = new File(Autoreply.appDirectory + "picSearch\\tmp\\", Autoreply.random.nextInt() + "pic.jpg");
				FileOutputStream out = new FileOutputStream(dFile);
				int ii = 0;
				while ((ii = is.read()) != -1) {
					out.write(ii);
				}
				out.close();
				is.close();
			} catch (Exception e) {
				Autoreply.sendPrivateMessage(fromQQ, e.toString());
			}
			sBuilder.append(Autoreply.CC.image(dFile) + "\n图片链接：" + tmp.getPid() + "\n画师：" + tmp.getUid() + "\n相似度："
					+ tmp.getSimilar() + "\n\n");
		}
		if (fromGroup == -1) {
			Autoreply.sendPrivateMessage(fromQQ, sBuilder.toString());
		} else {
			Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + sBuilder.toString());
		}
	}

	private ArrayList<String> getTable(String html) {
		ArrayList<String> items = new ArrayList<String>();
		int tableStart = 0;
		int tableEnd = 0;
		if (html.indexOf("<div class=\"result\">") == -1) {
			return null;
		}
		while (html.indexOf("<div class=\"result\">", tableEnd) != -1) {
			tableStart = html.indexOf("<div class=\"result\">", tableEnd) + "<div class=\"result\">".length();
			tableEnd = html.indexOf("</table>", tableStart);
			items.add(html.substring(tableStart, tableEnd));
		}
		return items;
	}

	private ArrayList<NaoJavabean> getEles(ArrayList<String> ls) {
		ArrayList<NaoJavabean> items = new ArrayList<NaoJavabean>();
		for (int i = 0; i < ls.size(); i++) {
			String src = ls.get(i);
			items.add(new NaoJavabean(src));
		}
		return items;
	}
}
