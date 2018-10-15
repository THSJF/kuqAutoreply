package com.meng;

public class FileTipUploader {

	private long groupNumber = 0;
	private long QQNumber = 0;
	private long fileLastUpload = 0;
	private long fileLastTipTime = 0;

	public FileTipUploader(long groupNumber, long QQNumber) {
		this.groupNumber = groupNumber;
		this.QQNumber = QQNumber;
	}

	public long getFileLastUpload() {
		return fileLastUpload;
	}

	public void setFileLastUpload(long fileLastUpload) {
		this.fileLastUpload = fileLastUpload;
	}

	public long getfileLastTipTime() {
		return fileLastTipTime;
	}

	public void setfileLastTipTime(long fileLastTipTime) {
		this.fileLastTipTime = fileLastTipTime;
	}

	public long getGroupNumber() {
		return groupNumber;
	}

	public long getQQNumber() {
		return QQNumber;
	}
}
