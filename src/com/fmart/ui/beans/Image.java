package com.fmart.ui.beans;

import java.io.Serializable;

public class Image implements Serializable{
	
	private byte[] imagesrc;
	private String filename;
	private long fileSize;
	
	public byte[] getImagesrc() {
		return imagesrc;
	}
	public void setImagesrc(byte[] imagesrc) {
		this.imagesrc = imagesrc;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
