package com.meng.config;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;

public class CookieManager {

    public Cookie cookie=new Cookie();

	public CookieManager() {
		File jsonBaseConfigFile = new File(Autoreply.appDirectory + "cookie.json");
        if (!jsonBaseConfigFile.exists()) {
            saveConfig();
		  }
        Type type = new TypeToken<Cookie>() {
		  }.getType();
        cookie = Autoreply.gson.fromJson(Tools.FileTool.readString(Autoreply.appDirectory + "cookie.json"), type);
	  }

	public void saveConfig() {
        try {
            File file = new File(Autoreply.appDirectory + "cookie.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(Autoreply.gson.toJson(cookie));
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }  
	  }

	public String getCookie(String name) {
		switch (name) {
			case "Sunny":
			  return cookie.Sunny;
			case "Luna":
			  return cookie.Luna;
			case "Star":
			  return cookie.Star;
			case "XingHuo":
			  return cookie.xinghuo;
			case "Hina":
			  return cookie.Hina;
			case "grzx":
			  return cookie.grzx;
			default:
			  return null;
		  }
	  }

	public boolean setCookie(String name, String cookieStr) {
		switch (name) {
			case "Sunny":
			  cookie.Sunny = cookieStr;
			  saveConfig();
			  return true;
			case "Luna":
			  cookie.Luna = cookieStr;
			  saveConfig();
			  return true;
			case "Star":
			  cookie.Star = cookieStr;
			  saveConfig();
			  return true;
			case "XingHuo":
			  cookie.xinghuo = cookieStr;
			  saveConfig();
			  return true;
			case "Hina":
			  cookie.Hina = cookieStr;
			  saveConfig();
			  return true;
			case "grzx":
			  cookie.grzx = cookieStr;
			  saveConfig();
			  return true;
			default:
			  return false;
		  }
	  }
  }
