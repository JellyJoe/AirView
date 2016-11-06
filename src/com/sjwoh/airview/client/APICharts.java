package com.sjwoh.airview.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.sjwoh.airview.client.entity.API;
import com.sjwoh.airview.client.entity.EnumTranslator;

public class APICharts
{
    private SimpleLayoutPanel layoutPanel;
    private PieChart pieChart;
    private LineChart lineChart;

    public APICharts()
    {
        // Create the API Loader
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable()
        {

            @Override
            public void run()
            {
                getSimpleLayoutPanel().setWidget(getPieChart());
                drawPieChart();
            }
        });
    }

    public SimpleLayoutPanel getSimpleLayoutPanel()
    {
        if(layoutPanel == null)
        {
            layoutPanel = new SimpleLayoutPanel();
        }
        return layoutPanel;
    }

    public Widget getPieChart()
    {
        if(pieChart == null)
        {
            pieChart = new PieChart();
        }
        return pieChart;
    }

    public Widget getLineChart()
    {
        if(lineChart == null)
        {
            lineChart = new LineChart();
        }
        return lineChart;
    }

    public void drawPieChart()
    {
        // Prepare the data
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Subject");
        dataTable.addColumn(ColumnType.NUMBER, "Number of students");
        dataTable.addRows(4);
        dataTable.setValue(0, 0, "History");
        dataTable.setValue(1, 0, "Computers");
        dataTable.setValue(2, 0, "Management");
        dataTable.setValue(3, 0, "Politics");
        dataTable.setValue(0, 1, 20);
        dataTable.setValue(1, 1, 25);
        dataTable.setValue(2, 1, 30);
        dataTable.setValue(3, 1, 35);

        // Draw the chart
        pieChart.draw(dataTable);
    }

    public void updatePieChart()
    {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "abc");
        dataTable.addColumn(ColumnType.NUMBER, "12313212132312312132");
        dataTable.addRows(4);
        dataTable.setValue(0, 0, "BORING");
        dataTable.setValue(1, 0, "DANK MEMES");
        dataTable.setValue(2, 0, "LOL");
        dataTable.setValue(3, 0, "TOP KEK");
        dataTable.setValue(0, 1, 10);
        dataTable.setValue(1, 1, 100);
        dataTable.setValue(2, 1, 15);
        dataTable.setValue(3, 1, 20);

        // Draw the chart
        getSimpleLayoutPanel().setWidget(getPieChart());
        pieChart.draw(dataTable);
    }

    public void updateLineChart()
    {
        String[] countries = new String[] { "Austria", "Bulgaria", "Denmark", "Greece" };
        int[] years = new int[] { 2003, 2004, 2005, 2006, 2007, 2008 };
        int[][] values = new int[][] { { 1336060, 1538156, 1576579, 1600652, 1968113, 1901067 },
                { 400361, 366849, 440514, 434552, 393032, 517206 },
                { 1001582, 1119450, 993360, 1004163, 979198, 916965 },
                { 997974, 941795, 930593, 897127, 1080887, 1056036 } };

        // Prepare the data
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Year");
        for(int i = 0; i < countries.length; i++)
        {
            dataTable.addColumn(ColumnType.NUMBER, countries[i]);
        }
        dataTable.addRows(years.length);
        for(int i = 0; i < years.length; i++)
        {
            dataTable.setValue(i, 0, String.valueOf(years[i]));
        }
        for(int col = 0; col < values.length; col++)
        {
            for(int row = 0; row < values[col].length; row++)
            {
                dataTable.setValue(row, col + 1, values[col][row]);
            }
        }

        // Set options
        LineChartOptions options = LineChartOptions.create();
        options.setBackgroundColor("#f0f0f0");
        options.setFontName("Tahoma");
        options.setTitle("Monthly Air Pollution Index (API) From 2014");
        options.setHAxis(HAxis.create("Month"));
        options.setVAxis(VAxis.create("Air Pollution Index (API)"));

        // Draw the chart
        getSimpleLayoutPanel().setWidget(getLineChart());
        lineChart.draw(dataTable, options);
    }

    public void updateLineChart(String country, TreeMap<String, TreeMap<String, ArrayList<String>>> fullTreeMap)
    {
        boolean getDateStatus = false;
        ArrayList<String> listOfKawasan = new ArrayList<String>();
        ArrayList<String> listOfDates = new ArrayList<String>();
        ArrayList<ArrayList<Integer>> apiValues = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> specificDateAPIValues = new ArrayList<Integer>();
        ArrayList<String> tempListOfAPI = new ArrayList<String>();
        TreeMap<String, ArrayList<String>> treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>();
        int averageAPI = 0;
        for(Map.Entry<String, TreeMap<String, ArrayList<String>>> entry : fullTreeMap.entrySet())
        {
            listOfKawasan.add(new String(entry.getKey()));

            treeMapOfDateAndAPI.clear();
            treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>(entry.getValue());
            for(Map.Entry<String, ArrayList<String>> secondEntry : treeMapOfDateAndAPI.entrySet())
            {
                specificDateAPIValues.clear();
                tempListOfAPI.clear();
                if(getDateStatus == false)
                {
                    listOfDates.add(new String(secondEntry.getKey()));
                }

                tempListOfAPI = new ArrayList<String>(secondEntry.getValue());
                averageAPI = 0;

                for(int i = 0; i < tempListOfAPI.size(); i++)
                {
                    if(!tempListOfAPI.get(i).equals("#"))
                    {
                        averageAPI = averageAPI + Integer.parseInt(tempListOfAPI.get(i).replaceAll("[^0-9]", ""));
                    }
                }

                averageAPI = averageAPI / tempListOfAPI.size();
                specificDateAPIValues.add(new Integer(averageAPI));
            }

            getDateStatus = true;
            apiValues.add(new ArrayList<Integer>(specificDateAPIValues));
        }

        // prepare the data
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Date");
        for(int i = 0; i < listOfKawasan.size(); i++)
        {
            dataTable.addColumn(ColumnType.NUMBER, listOfKawasan.get(i));
        }
        dataTable.addRows(listOfDates.size());
        for(int i = 0; i < listOfDates.size(); i++)
        {
            dataTable.setValue(i, 0, listOfDates.get(i));
        }
        for(int col = 0; col < apiValues.size(); col++)
        {
            for(int row = 0; row < apiValues.get(col).size(); row++)
            {
                dataTable.setValue(row, col + 1, apiValues.get(col).get(row).intValue());
            }
        }

        LineChartOptions options = LineChartOptions.create();
        options.setBackgroundColor("#f0f0f0");
        options.setFontName("Tahoma");
        options.setTitle("SARAWAK");
        options.setHAxis(HAxis.create("Date"));
        options.setVAxis(VAxis.create("Average API"));

        // Draw the chart
        getSimpleLayoutPanel().setWidget(getLineChart());
        lineChart.draw(dataTable, options);
    }
    
    public void updateLineChart(Set<API> setAPI)
    {
    	DataTable dataTable = DataTable.create();
    	dataTable.addColumn(ColumnType.STRING, "Month");
    	
    	for(Iterator<API> iterator = setAPI.iterator(); iterator.hasNext(); )
    	{
    		API tempAPI = iterator.next();
    		
    		dataTable.addColumn(ColumnType.NUMBER, tempAPI.getKawasan());
    	}
    	
    	dataTable.addRows(12);
		for(int month = 1; month < 13; month++) {
			dataTable.setValue((month - 1), 0, EnumTranslator.getMonth(month));
		}
    	
		int col = 1;
    	for(Iterator<API> iterator = setAPI.iterator(); iterator.hasNext(); )
    	{
    		API tempAPI = iterator.next();
    		
    		for(int month = 1; month < 13; month++) {
    			dataTable.setValue((month - 1), col, tempAPI.getMonthAverage(month));
    		}
    		col++;
    	}

    	LineChartOptions options = LineChartOptions.create();
    	options.setBackgroundColor("#f0f0f0");
    	options.setFontName("Tahoma");
    	options.setTitle("Monthly Air Pollution Index (API) 2015");
    	options.setHAxis(HAxis.create("Month"));
    	options.setVAxis(VAxis.create("Air Pollution Index (API)"));

    	// Draw the chart
    	getSimpleLayoutPanel().setWidget(getLineChart());
    	lineChart.draw(dataTable, options);
    }
}