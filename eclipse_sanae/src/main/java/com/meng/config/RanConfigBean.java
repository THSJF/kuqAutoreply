package com.meng.config;

import java.util.*;

public class RanConfigBean {
	public HashSet<Long> QQNotReply = new HashSet<>();
	public HashSet<Long> blackListQQ = new HashSet<>();
    public HashSet<Long> blackListGroup = new HashSet<>();
    public HashSet<String> wordNotReply = new HashSet<>();
    public HashSet<PersonInfo> personInfo = new HashSet<>();
    public HashSet<Long> adminList = new HashSet<>();
	public HashMap<Long,String> nicknameMap = new HashMap<>();
}
