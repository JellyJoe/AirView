package com.sjwoh.airview.client.entity;

public class EnumTranslator {
	public enum ResourceId {
		_2005_2013,
		_2013_2014,
		_2014_2015
	}
	
	public static String getResource(ResourceId resourceId) {
		switch(resourceId) {
			case _2005_2013:
				return "e1578b5d-8bca-41ee-827d-2f8392d0c762";
			case _2013_2014:
				return "531380f6-4d7a-4d0a-966d-9c633cdc4d1a";
			case _2014_2015:
				return "a864e6cd-759e-4a7e-a672-fa4d3b709e2e";
		}
		
		return "";
	}
	
	public static String getMonth(int month) {
		switch(month) {
			case 1:
				return "January";
			case 2:
				return "February";
			case 3:
				return "March";
			case 4:
				return "April";
			case 5:
				return "May";
			case 6:
				return "Jun";
			case 7:
				return "July";
			case 8:
				return "August";
			case 9:
				return "September";
			case 10:
				return "October";
			case 11:
				return "November";
			case 12:
				return "December";
			default:
				return "n/A";
		}
	}
}
