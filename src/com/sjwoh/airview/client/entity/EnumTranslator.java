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
				return "Jan";
			case 2:
				return "Feb";
			case 3:
				return "Mar";
			case 4:
				return "Apr";
			case 5:
				return "May";
			case 6:
				return "Jun";
			case 7:
				return "Jul";
			case 8:
				return "Aug";
			case 9:
				return "Sep";
			case 10:
				return "Oct";
			case 11:
				return "Nov";
			case 12:
				return "Dec";
			default:
				return "n/A";
		}
	}
}
