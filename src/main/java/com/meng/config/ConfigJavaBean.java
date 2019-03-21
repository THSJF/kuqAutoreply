package com.meng.config;

import java.util.ArrayList;

public class ConfigJavaBean {
	public ArrayList<String> mapGroupReply = new ArrayList<>();
	public ArrayList<String> mapQQNotReply = new ArrayList<>();
	public ArrayList<String> mapWordNotReply = new ArrayList<>();
	public ArrayList<String> mapGroupRepeater = new ArrayList<>();
	public ArrayList<String> mapGroupDicReply = new ArrayList<>();
	public ArrayList<BiliUser> mapBiliUser = new ArrayList<>();

	public class BiliUser {
		public String name="";
		public String qq="";
		public String bid="";

		public BiliUser(String name,String qq,String bid) { 
			this.name=name;
			this.qq=qq;
			this.bid=bid;
		}
	}

}
