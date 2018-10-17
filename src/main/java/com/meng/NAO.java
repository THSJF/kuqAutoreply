package com.meng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class NAO {

	public NAO() {
		// TODO Auto-generated constructor stub
	}

	public static String check() throws IOException {
		File file = new File(Autoreply.appDirectory + "pic\\fanmo.jpg");
		FileInputStream fInputStream = new FileInputStream(file);
		Connection.Response response = Jsoup.connect(
				"https://saucenao.com/search.php?db=" + 999).data("file", "image.jpg", fInputStream)
				.method(Connection.Method.POST).execute();
		 if (response.statusCode() != 200) {

             switch (response.statusCode()) {
                 case 429:
                     return "";
                 default:
                     return "";
             }
         }
		 System.out.println(response.body());
		return "";
	}
}
