package com.meng.config.javabeans;

import java.util.HashSet;
import java.util.*;

public class ConfigJavaBean {
    public HashSet<GroupConfig> groupConfigs = new HashSet<>();
    public HashSet<Long> QQNotReply = new HashSet<>();
    public HashSet<Long> blackListQQ = new HashSet<>();
    public HashSet<Long> blackListGroup = new HashSet<>();
    public HashSet<String> wordNotReply = new HashSet<>();
    public HashSet<PersonInfo> personInfo = new HashSet<>();
    public HashSet<Long> masterList = new HashSet<>();
    public HashSet<Long> adminList = new HashSet<>();
    public HashSet<Long> groupAutoAllowList = new HashSet<>();
	public HashMap<Long,String> nickname = new HashMap<>();
	public long ogg = 0;
}
