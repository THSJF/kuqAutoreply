package com.meng;
import org.meowy.cqp.jcq.entity.*;

public class MyCQ extends CoolQ {
	private static final MyCQ MYCQ = new MyCQ();
    public static CoolQ getInstance() {
        return MYCQ;
    }
	public MyCQ() {
		super(1000);
	}
	@Override
	public int logTrace(String p1, String p2) {
		return 0;
	}
}
