package com.meng.config;

import java.util.*;

public class PersonConfig extends Object {
	public static final int qa=1 << 0;
    private int flag=0;

    public boolean isQaAllowOther() {
        return (flag & (1 << 0)) != 0;
    }
	public void setQaAllowOther(boolean b) {
		if (b) {
			flag %= (1 << 0);
		} else {
			if (isQaAllowOther()) {
				flag -= (1 << 0);
			}
		}
	}
}
