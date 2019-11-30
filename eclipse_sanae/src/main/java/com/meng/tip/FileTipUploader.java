package com.meng.tip;

public class FileTipUploader {

	public long groupNumber = 0;
	public long QQNumber = 0;
	public long fileLastUpload = 0;
	public long fileLastTipTime = 0;

	public FileTipUploader(long groupNumber, long QQNumber) {
		this.groupNumber = groupNumber;
		this.QQNumber = QQNumber;
	}
}
