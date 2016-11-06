package com.sjwoh.airview.entity;

import java.sql.Date;

public class API {
	private String mKawasan;
	private String mNegeri;
	private Date mTarikh;
	private int mValue;
	
	public String getKawasan() {
		return mKawasan;
	}
	
	public String getNegeri() {
		return mNegeri;
	}
	
	public Date getTarikh() {
		return mTarikh;
	}
	
	public int getValue() {
		return mValue;
	}
	
	public void setKawasan(String kawasan) {
		mKawasan = kawasan;
	}
	
	public void getNegeri(String negeri) {
		mNegeri = negeri;
	}
	
	public void setTarikh(Date tarikh) {
		mTarikh = tarikh;
	}
	
	public void setTarikh(String tarikhText) {
		mTarikh = Date.valueOf(tarikhText);
	}
	
	public void setValue(int value) {
		mValue = value;
	}
	
	public void setValue(String value) {
		mValue = Integer.parseInt(value);
	}
}
