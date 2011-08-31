package com.idega.block.caldav.bean;

import java.io.Serializable;

public class Calendar implements Serializable {

	private static final long serialVersionUID = 5103954102796564166L;

	private String name, path, encodedPath;
	
	public Calendar(String name, String path, String encodedPath) {
		super();
		
		this.name = name;
		this.path = path;
		this.encodedPath = encodedPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getEncodedPath() {
		return encodedPath;
	}

	public void setEncodedPath(String encodedPath) {
		this.encodedPath = encodedPath;
	}

	@Override
	public String toString() {
		return "Calendar " + getName() + " at " + getPath();
	}
}