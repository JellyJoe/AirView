package com.sjwoh.airview.client;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.controls.Dashboard;
import com.googlecode.gwt.charts.client.controls.filter.CategoryFilter;
import com.googlecode.gwt.charts.client.controls.filter.CategoryFilterOptions;
import com.googlecode.gwt.charts.client.controls.filter.CategoryFilterState;
import com.googlecode.gwt.charts.client.controls.filter.CategoryFilterUi;
import com.googlecode.gwt.charts.client.corechart.BarChartOptions;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.SelectedValuesLayout;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.sjwoh.airview.client.entity.API;
import com.sjwoh.airview.client.entity.EnumTranslator;

public class APICharts
{
    private SimpleLayoutPanel layoutPanel;
    private DockLayoutPanel dockLayoutPanel;
    private Dashboard dashboard;
    private ChartWrapper<LineChartOptions> lineChartWrapper;
    private ChartWrapper<BarChartOptions> barChartWrapper;
    private CategoryFilter categoryFilter;
    private DataTable dataTable;
    private String title;
    private final String hAxis = "Year-Month";
    private final String vAxis = "Air Pollution Index (API)";

    public APICharts()
    {
        // Create the API Loader
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CONTROLS);
        chartLoader.loadApi(new Runnable()
        {
            @Override
            public void run()
            {
                getDockLayoutPanel().addNorth(getDashboard(), 0);
                getDockLayoutPanel().addWest(getCategoryFilter(), 150);
                getDockLayoutPanel().add(getSimpleLayoutPanel());
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
    
    public DockLayoutPanel getDockLayoutPanel()
    {
    	
        if(dockLayoutPanel == null)
        {
            dockLayoutPanel = new DockLayoutPanel(Unit.PX);
        }
        return dockLayoutPanel;
    }
    
    public Dashboard getDashboard()
    {
        if(dashboard == null)
        {
            dashboard = new Dashboard();
        }
        return dashboard;
    }
    
    private ChartWrapper<LineChartOptions> getLineChartWrapper()
    {
        if (lineChartWrapper == null)
        {
            lineChartWrapper = new ChartWrapper<LineChartOptions>();
            lineChartWrapper.setChartType(ChartType.LINE);
        }
        return lineChartWrapper;
    }
    
    private ChartWrapper<BarChartOptions> getBarChartWrapper()
    {
        if (barChartWrapper == null)
        {
        	barChartWrapper = new ChartWrapper<BarChartOptions>();
        	barChartWrapper.setChartType(ChartType.BAR);
        }
        return barChartWrapper;
    }
    
    private CategoryFilter getCategoryFilter()
    {
        if (categoryFilter == null)
        {
            categoryFilter = new CategoryFilter();
            
        }
        return categoryFilter;
    }
    
	public void updateChart(ChartType chartType)
    {
    	switch (chartType)
    	{
	    	case BAR:
	    		drawBarChart();
	    		break;
    		default:
    			drawLineChart();
    			break;
    	}
    }

    @SuppressWarnings("deprecation")
	public void updateChart(ChartType chartType, String negeri, int year, Set<API> setAPI)
    {
    	title = negeri;
    	Set<Date> setDate = new TreeSet<Date>();
    	for(Iterator<API> iterator = setAPI.iterator(); iterator.hasNext(); )
    	{
    		API tempAPI = iterator.next();

    		Map<Date, Integer> mapTarikhAndValue = tempAPI.getTarikhAndValue();
    		
    		for(Map.Entry<Date, Integer> tarikhAndValue : mapTarikhAndValue.entrySet()) {
    			if((tarikhAndValue.getKey().getYear() + 1900) == year)
    			{
        			Date tempDate = Date.valueOf((tarikhAndValue.getKey().getYear() + 1900) + "-" + (tarikhAndValue.getKey().getMonth() + 1) + "-10");

        			setDate.add(tempDate);
    			}
    		}	
    	}
    	
    	ArrayList<String> listOfDates = new ArrayList<String>();
    	CategoryFilterOptions categoryFilterOptions = CategoryFilterOptions.create();
        categoryFilterOptions.setFilterColumnIndex(0);
        CategoryFilterUi categoryFilterUi = CategoryFilterUi.create();
        categoryFilterUi.setAllowMultiple(true);
        categoryFilterUi.setAllowTyping(false);
        categoryFilterUi.setSelectedValuesLayout(SelectedValuesLayout.BELOW_STACKED);
        categoryFilterOptions.setUi(categoryFilterUi);
        categoryFilter.setOptions(categoryFilterOptions);
    	
    	dataTable = DataTable.create();
    	dataTable.addColumn(ColumnType.STRING, "Year-Month");

    	dataTable.addRows(setDate.size());
    	int row = 0;
    	for(Iterator<Date> iterator = setDate.iterator(); iterator.hasNext(); )
    	{
    		Date date = iterator.next();
    		String yearMonthText = (date.getYear() + 1900) + "-" + EnumTranslator.getMonth(date.getMonth() + 1);
    		
    		dataTable.setValue(row, 0, yearMonthText);
    		listOfDates.add(new String(yearMonthText));
    		
    		row++;
    	}
    	
    	CategoryFilterState categoryFilterState = CategoryFilterState.create();
        categoryFilterState.setSelectedValues(listOfDates.toArray(new String[listOfDates.size()]));
        categoryFilter.setState(categoryFilterState);

		int col = 1;
    	for(Iterator<API> iterator = setAPI.iterator(); iterator.hasNext(); )
    	{
    		API tempAPI = iterator.next();

    		dataTable.addColumn(ColumnType.NUMBER, tempAPI.getKawasan());

    		row = 0;
    		
        	for(Iterator<Date> iteratorInner = setDate.iterator(); iteratorInner.hasNext(); )
        	{
        		Date date = iteratorInner.next();

        		dataTable.setValue(row, col, tempAPI.getMonthYearAverage((date.getYear() + 1900), (date.getMonth() + 1)));
        		
        		row++;
        	}
        	
    		col++;
    	}
    	
    	switch (chartType)
    	{
	    	case BAR:
	    		drawBarChart();
	    		break;
    		default:
    			drawLineChart();
    			break;
    	}
    }
    
    private void drawLineChart()
    {
    	getSimpleLayoutPanel().setWidget(getLineChartWrapper());

    	LineChartOptions options = LineChartOptions.create();
    	options.setBackgroundColor("#FFFFFF");
    	options.setTitle(title);
    	options.setHAxis(HAxis.create(hAxis));
    	options.setVAxis(VAxis.create(vAxis));
    	lineChartWrapper.setOptions(options);

    	dashboard.bind(categoryFilter, lineChartWrapper);
        dashboard.draw(dataTable);
    }
    
    private void drawBarChart()
    {
    	getSimpleLayoutPanel().setWidget(getBarChartWrapper());

    	BarChartOptions options = BarChartOptions.create();
    	options.setBackgroundColor("#FFFFFF");
    	options.setTitle(title);
    	options.setHAxis(HAxis.create(vAxis));
    	options.setVAxis(VAxis.create(hAxis));
    	barChartWrapper.setOptions(options);

    	dashboard.bind(categoryFilter, barChartWrapper);
        dashboard.draw(dataTable);
    }
    
//    public void updateLineChart(String negeri, TreeMap<String, TreeMap<String, ArrayList<String>>> fullTreeMap)
//    {        
//        boolean getDateStatus = false;
//        ArrayList<String> listOfKawasan = new ArrayList<String>();
//        ArrayList<String> listOfDates = new ArrayList<String>();
//        ArrayList<ArrayList<Integer>> apiValues = new ArrayList<ArrayList<Integer>>();
//        ArrayList<Integer> specificDateAPIValues = new ArrayList<Integer>();
//        ArrayList<String> tempListOfAPI = new ArrayList<String>();
//        TreeMap<String, ArrayList<String>> treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>();
//        int averageAPI = 0;
//        for(Map.Entry<String, TreeMap<String, ArrayList<String>>> entry : fullTreeMap.entrySet())
//        {
//            listOfKawasan.add(new String(entry.getKey()));
//
//            treeMapOfDateAndAPI.clear();
//            specificDateAPIValues.clear();
//            treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>(entry.getValue());
//            for(Map.Entry<String, ArrayList<String>> secondEntry : treeMapOfDateAndAPI.entrySet())
//            {
//                tempListOfAPI.clear();
//                if(getDateStatus == false)
//                {
//                    listOfDates.add(new String(secondEntry.getKey()));
//                }
//
//                tempListOfAPI = new ArrayList<String>(secondEntry.getValue());
//                averageAPI = 0;
//
//                for(int i = 0; i < tempListOfAPI.size(); i++)
//                {
//                    if(!tempListOfAPI.get(i).equals("#") && !tempListOfAPI.get(i).equals(""))
//                    {
//                        averageAPI = averageAPI + Integer.parseInt(tempListOfAPI.get(i).replaceAll("[^0-9]", ""));
//                    }
//                    else
//                    {
//                        averageAPI = averageAPI + 30; // 30 would be better than nothing
//                    }
//                }
//
//                averageAPI = averageAPI / tempListOfAPI.size();
//                specificDateAPIValues.add(new Integer(averageAPI));
//            }
//
//            getDateStatus = true;
//            apiValues.add(new ArrayList<Integer>(specificDateAPIValues));
//        }
//        
//        CategoryFilterOptions categoryFilterOptions = CategoryFilterOptions.create();
//        categoryFilterOptions.setFilterColumnIndex(0);
//        CategoryFilterUi categoryFilterUi = CategoryFilterUi.create();
//        categoryFilterUi.setAllowMultiple(true);
//        categoryFilterUi.setAllowTyping(false);
//        categoryFilterUi.setSelectedValuesLayout(SelectedValuesLayout.BELOW_STACKED);
//        categoryFilterOptions.setUi(categoryFilterUi);
//        categoryFilter.setOptions(categoryFilterOptions);
//        CategoryFilterState categoryFilterState = CategoryFilterState.create();
//        categoryFilterState.setSelectedValues(listOfDates.toArray(new String[listOfDates.size()]));
//        categoryFilter.setState(categoryFilterState);
//
//        // prepare the data
//        DataTable dataTable = DataTable.create();
//        dataTable.addColumn(ColumnType.STRING, "Date");
//        for(int i = 0; i < listOfKawasan.size(); i++)
//        {
//            dataTable.addColumn(ColumnType.NUMBER, listOfKawasan.get(i));
//        }
//        dataTable.addRows(listOfDates.size());
//
//        for(int i = 0; i < listOfDates.size(); i++)
//        {
//            dataTable.setValue(i, 0, listOfDates.get(i));
//        }
//        for(int col = 0; col < apiValues.size(); col++)
//        {
//            for(int row = 0; row < apiValues.get(col).size(); row++)
//            {
//                dataTable.setValue(row, col + 1, apiValues.get(col).get(row).intValue());
//            }
//        }
//
//        getSimpleLayoutPanel().setWidget(getLineChartWrapper());
//        
//        LineChartOptions options = LineChartOptions.create();
//        options.setBackgroundColor("#FFFFFF");
//        options.setTitle(negeri);
//        options.setHAxis(HAxis.create("Date"));
//        options.setVAxis(VAxis.create("Average API"));
//        lineChartWrapper.setOptions(options);
//
//        dashboard.bind(categoryFilter, lineChartWrapper);
//        dashboard.draw(dataTable);
//    }
}