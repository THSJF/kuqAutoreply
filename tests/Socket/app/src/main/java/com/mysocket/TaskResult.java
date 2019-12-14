package com.mysocket;

public class TaskResult {
	public byte[] data=null;

	public TaskResult(byte b) {
		data = new byte[]{ b };
	}

	public TaskResult(short s) {
		data = BitConverter.getBytes(s);
	}

	public TaskResult(int i) {
		data = BitConverter.getBytes(i);
	}

	public TaskResult(long l) {
		data = BitConverter.getBytes(l);
	}

	public TaskResult(float f) {
		data = BitConverter.getBytes(f);
	}

	public TaskResult(double d) {
		data = BitConverter.getBytes(d);
	}

	public TaskResult(String s) {
		data = BitConverter.getBytes(s);
	}

}
