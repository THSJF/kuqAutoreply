package com.meng.tools.override;
import java.util.*;

public class MyLinkedHashMap<K,V> extends LinkedHashMap {

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (Entry entry:entrySet()) {
			sb.append(entry.getKey().toString()).append(" ").append(entry.getValue().toString()).append("\n");
		  }
		return sb.toString();
	  }

  }
