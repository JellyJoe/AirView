package com.sjwoh.airview.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.sjwoh.airview.client.entity.API;

public class AirViewTest extends GWTTestCase {
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "com.sjwoh.airview.AirView";
	}
	
	public void testGetNegeri() {
		API api = new API();
		api.setNegeri("Sarawak");
		api.setKawasan("Sibu");
		
		assertNotNull(api);
		assertEquals("Sarawak", api.getNegeri());
	}
	
	public void testGetKawasan() {
		API api = new API();
		api.setNegeri("Sarawak");
		api.setKawasan("Sibu");
		
		assertNotNull(api);
		assertEquals("Sibu", api.getKawasan());
	}

	public void testGetMonthAverage() {
		API api = new API();
		api.setNegeri("Sarawak");
		api.setKawasan("Sibu");
		api.addTarikhAndValue("2016-03-11", 30);
		api.addTarikhAndValue("2016-03-14", 40);
		api.addTarikhAndValue("2016-03-20", 0);
		
		assertNotNull(api);
		assertEquals(((30 + 40) / 2), api.getMonthAverage(3));
		assertEquals(0, api.getMonthAverage(2));
	}
	
	public void testGetYearAverage() {
		API api = new API();
		api.setNegeri("Sarawak");
		api.setKawasan("Sibu");
		api.addTarikhAndValue("2016-03-11", 30);
		api.addTarikhAndValue("2016-04-14", 40);
		api.addTarikhAndValue("2016-04-16", 32);
		api.addTarikhAndValue("2016-06-20", 0);
		
		assertNotNull(api);
		assertEquals(((30 + 40 + 32) / 3), api.getYearAverage(2016));
		assertEquals(0, api.getYearAverage(2015));
	}
	
	public void testGetMonthYearAverage() {
		API api = new API();
		api.setNegeri("Sarawak");
		api.setKawasan("Sibu");
		api.addTarikhAndValue("2016-04-11", 30);
		api.addTarikhAndValue("2016-04-14", 40);
		api.addTarikhAndValue("2016-04-16", 32);
		api.addTarikhAndValue("2016-04-20", 0);
		
		assertNotNull(api);
		assertEquals(((30 + 40 + 32) / 3), api.getMonthYearAverage(2016, 4));
		assertEquals(0, api.getMonthYearAverage(2016, 5));
		assertEquals(0, api.getMonthYearAverage(2015, 4));
	}
}
