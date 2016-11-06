package com.sjwoh.airview.entity;

public class Resource {
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
}
