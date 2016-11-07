package com.sjwoh.airview.client.entity;

import java.sql.Date;
import java.util.Map;
import java.util.TreeMap;

public class API implements Comparable<API> {
	private String mNegeri;
	private String mKawasan;
	private Map<Date, Integer> mTarikhAndValue;

	public API() {
		mTarikhAndValue = new TreeMap<Date, Integer>();
	}

	public String getNegeri() {
		return mNegeri;
	}

	public String getKawasan() {
		return mKawasan;
	}

	@SuppressWarnings("deprecation")
	public int getMonthAverage(int month) {
		int counter = 0;
		int sum = 0;

		for (Map.Entry<Date, Integer> tarikhAndValue : mTarikhAndValue.entrySet()) {
			if (tarikhAndValue.getKey().getMonth() == (month - 1) && tarikhAndValue.getValue() != 0) {
				sum += tarikhAndValue.getValue();

				counter++;
			}
		}

		return sum / counter;
	}

	@SuppressWarnings("deprecation")
	public int getYearAverage(int year) {
		int counter = 0;
		int sum = 0;

		for (Map.Entry<Date, Integer> tarikhAndValue : mTarikhAndValue.entrySet()) {
			if (tarikhAndValue.getKey().getYear() == (year - 1900) && tarikhAndValue.getValue() != 0) {
				sum += tarikhAndValue.getValue();

				counter++;
			}
		}

		return sum / counter;
	}

	@SuppressWarnings("deprecation")
	public int getMonthYearAverage(int year, int month) {
		int counter = 0;
		int sum = 0;

		for (Map.Entry<Date, Integer> tarikhAndValue : mTarikhAndValue.entrySet()) {
			if (tarikhAndValue.getKey().getYear() == (year - 1900) && tarikhAndValue.getKey().getMonth() == (month - 1)
					&& tarikhAndValue.getValue() != 0) {
				sum += tarikhAndValue.getValue();

				counter++;
			}
		}

		return sum / counter;
	}

	public Map<Date, Integer> getTarikhAndValue() {
		return mTarikhAndValue;
	}

	public void setNegeri(String negeri) {
		mNegeri = negeri;
	}

	public void setKawasan(String kawasan) {
		mKawasan = kawasan;
	}

	public void setTarikhAndValue(Map<Date, Integer> tarikhAndValue) {
		mTarikhAndValue = tarikhAndValue;
	}

	public void addTarikhAndValue(Date tarikh, int value) {
		if (mTarikhAndValue.containsKey(tarikh)) {
			return;
		}

		mTarikhAndValue.put(tarikh, value);
	}

	public void addTarikhAndValue(String tarikhText, int value) {
		Date tarikh = Date.valueOf(tarikhText);

		if (mTarikhAndValue.containsKey(tarikh)) {
			return;
		}

		mTarikhAndValue.put(tarikh, value);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof API)) {
			return false;
		}

		API otherObject = (API) object;

		if (!mNegeri.equals(otherObject.getNegeri())) {
			return false;
		}

		if (!mKawasan.equals(otherObject.getKawasan())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	@Override
	public int compareTo(API api) {
		int compareResult = 0;

		if ((compareResult = mNegeri.compareTo(api.getNegeri())) != 0) {
			return compareResult;
		} else {
			if ((compareResult = mKawasan.compareTo(api.getKawasan())) != 0) {
				return compareResult;
			}
		}

		return compareResult;
	}
}
