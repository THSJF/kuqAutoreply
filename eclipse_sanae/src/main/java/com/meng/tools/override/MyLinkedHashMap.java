package com.meng.tools.override;
import java.util.*;

public class MyLinkedHashMap<K,V> extends LinkedHashMap {

	@Override
	public String toString() {
        Set<Map.Entry<K, V>> keyset = this.entrySet();
        Iterator<Map.Entry<K, V>> i = keyset.iterator();
        if (!i.hasNext()) {
			return "";
		  }
        StringBuffer buffer = new StringBuffer();
        while (true) {
            Map.Entry<K, V> me = i.next();
            K key = me.getKey();
            V value = me.getValue();
            buffer.append(key.toString() + " ");
            buffer.append(value.toString() + "\n");
            if (!i.hasNext()) {
				return buffer.toString();
			  }
		  }
	  }
  }
