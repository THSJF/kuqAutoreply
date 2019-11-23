package com.meng;

public class MusicInfo {
	public byte[] name;
	public int start;
	public int unknown1;
	public int repeatStart;
	public int length;
	public short format;
	public short channels;
	public int rate;
	public int avgBytesPerSec;
	public short blockAlign;
	public short bitsPerSample;
	public short cbSize;
	public short pad;

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(new String(name)).append(" ");
		sb.append(start).append(" ");
		sb.append(unknown1).append(" ");
		sb.append(repeatStart).append(" ");
		sb.append(length).append(" ");
		sb.append(format).append(" ");
		sb.append(channels).append(" ");
		sb.append(rate).append(" ");
		sb.append(avgBytesPerSec).append(" ");
		sb.append(blockAlign).append(" ");
		sb.append(bitsPerSample).append(" ");
		sb.append(cbSize).append(" ");
		sb.append(pad).append(" ");
		return sb.toString();
	}
	//    public int beanSize = 52;
}
