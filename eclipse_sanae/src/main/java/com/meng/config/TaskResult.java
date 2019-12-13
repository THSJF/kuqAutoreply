package com.meng.config;
import com.meng.tools.*;

public class TaskResult {
	public byte[] data=null;

	public TaskResult(byte b) {
		data = new byte[]{ b };
	}

	public TaskResult(short s) {
		data = Tools.BitConverter.getBytes(s);
	}

	public TaskResult(int i) {
		data = Tools.BitConverter.getBytes(i);
	}

	public TaskResult(long l) {
		data = Tools.BitConverter.getBytes(l);
	}

	public TaskResult(float f) {
		data = Tools.BitConverter.getBytes(f);
	}

	public TaskResult(double d) {
		data = Tools.BitConverter.getBytes(d);
	}

	public TaskResult(String s) {
		data = Tools.BitConverter.getBytes(s);
	}

}
