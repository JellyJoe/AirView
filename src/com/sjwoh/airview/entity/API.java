package com.sjwoh.airview.entity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class API implements Comparable<API> {
	private String mKawasan;
	private String mNegeri;
	private Calendar mTarikh;
	private Map<Calendar, Integer> mWaktuAndValue;
	
	public API() {
		mWaktuAndValue = new TreeMap<Calendar, Integer>();
	}
	
	public String getKawasan() {
		return mKawasan;
	}
	
	public String getNegeri() {
		return mNegeri;
	}
	
	public Calendar getTarikh() {
		return mTarikh;
	}
	
	public Map<Calendar, Integer> getWaktuAndValue() {
		return mWaktuAndValue;
	}
	
	public void setKawasan(String kawasan) {
		mKawasan = kawasan;
	}
	
	public void getNegeri(String negeri) {
		mNegeri = negeri;
	}
	
	public void setTarikh(Calendar tarikh) {
		mTarikh = tarikh;
	}
	
	public void setTarikh(String tarikhText) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		mTarikh = sdf.getCalendar();
	}
	
	public void setWaktuAndValue(Map<Calendar, Integer> waktuAndValue) {
		mWaktuAndValue = waktuAndValue;
	}
	
	public void addWaktuAndValue(Calendar waktu, int value) {
		mWaktuAndValue.put(waktu, value);
	}
	
	public void addWaktuAndValue(String waktuText, int value) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		Calendar waktu = sdf.getCalendar();
		
		mWaktuAndValue.put(waktu, value);
	}
	
	@Override public boolean equals(Object object) {
	    if (!(object instanceof API)) {
	    	return false;
	    }
	    
	    API otherObject = (API) object;
	    
		if(mTarikh.get(Calendar.YEAR) == otherObject.getTarikh().get(Calendar.YEAR)) {
			if(mTarikh.get(Calendar.MONTH) == otherObject.getTarikh().get(Calendar.MONTH)) {
				if(mTarikh.get(Calendar.DAY_OF_MONTH) == otherObject.getTarikh().get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}
		}

		return false;
    }
	
	@Override public int hashCode() {
		return this.hashCode();
	}

	@Override
	public int compareTo(API api) {
		int compareResult = 0;
		
		if(mTarikh.get(Calendar.YEAR) < api.getTarikh().get(Calendar.YEAR)) {
			compareResult = -1;
		}
		else if (mTarikh.get(Calendar.YEAR) > api.getTarikh().get(Calendar.YEAR)) {
			compareResult = 1;
		}
		else {
			if(mTarikh.get(Calendar.MONTH) < api.getTarikh().get(Calendar.MONTH)) {
				compareResult = -1;
			}
			else if (mTarikh.get(Calendar.MONTH) > api.getTarikh().get(Calendar.MONTH)) {
				compareResult = 1;
			}
			else {
				if(mTarikh.get(Calendar.DAY_OF_MONTH) < api.getTarikh().get(Calendar.DAY_OF_MONTH)) {
					compareResult = -1;
				}
				else {
					compareResult = 0;
				}
			}
		}
		
		return compareResult;
	}
}
