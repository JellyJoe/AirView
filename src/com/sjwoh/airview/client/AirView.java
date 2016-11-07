package com.sjwoh.airview.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sjwoh.airview.client.entity.API;

// Entry point classes define <code>onModuleLoad()</code>.
public class AirView implements EntryPoint
{

    // The message displayed to the user when the server cannot be reached or
    // returns an error.
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server. Please check your network connection and try again.";

    // Create a remote service proxy to talk to the server-side Greeting
    // service.
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    APICharts apiCharts = new APICharts();

    // This is the entry point method.
    @Override
    public void onModuleLoad()
    {        
    	/*Window.enableScrolling(false);
        Window.setMargin("0px");

        RootLayoutPanel.get().add(getMainPanel());*/
    }

    public Panel getMainPanel()
    {
        DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.setSize("100%", "100%");

        VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.setSize("100%", "100%");

        final Label label1 = new Label("SAMPLE TEXT");
        label1.setSize("100%", "100%");
        final Button lineChartButton = new Button("Line Chart Button");
        final Button pieChartButton = new Button("Pie Chart Button");
        final Button anotherLineChartButton = new Button("Another Line Chart Button");
        final Button yetAnotherLineChartButton = new Button("Yet Another Line Chart Button");
        
        final ListBox lineChartOptionList = new ListBox();
        lineChartOptionList.addItem("Sarawak");
        lineChartOptionList.addItem("Selangor");
        lineChartOptionList.setVisibleItemCount(1);

        leftPanel.add(label1);
        leftPanel.add(lineChartButton);
        leftPanel.add(pieChartButton);
        leftPanel.add(lineChartOptionList);
        leftPanel.add(anotherLineChartButton);
        leftPanel.add(yetAnotherLineChartButton);

        mainPanel.addNorth(new HTML("<h1>AirView</h1>"), 100);
        mainPanel.addWest(leftPanel, 100);
        mainPanel.add(apiCharts.getSimpleLayoutPanel());

        // creates and sets the line chart button handler
        class LineChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                apiCharts.updateLineChart();
            }
        }
        LineChartHandler lineChartHandler = new LineChartHandler();
        lineChartButton.addClickHandler(lineChartHandler);

        // creates and sets the pie chart button handler
        class PieChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                apiCharts.updatePieChart();
            }
        }
        PieChartHandler pieChartHandler = new PieChartHandler();
        pieChartButton.addClickHandler(pieChartHandler);

        // creates and sets the another line chart button handler
        class AnotherLineChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                greetingService.greetServer(lineChartOptionList.getItemText(lineChartOptionList.getSelectedIndex()), new AsyncCallback<String>()
                {
                    public void onFailure(Throwable caught)
                    {
                        label1.setText(caught.getMessage());
                    }

                    public void onSuccess(String result)
                    {
                        apiCharts.updateLineChart(lineChartOptionList.getItemText(lineChartOptionList.getSelectedIndex()), parseResult(result));
                    }
                });
            }
        }
        AnotherLineChartHandler anotherLineChartHandler = new AnotherLineChartHandler();
        anotherLineChartButton.addClickHandler(anotherLineChartHandler);
        
        class YetAnotherLineChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                greetingService.greetServer("Sarawak", new AsyncCallback<String>()
                {
                    public void onFailure(Throwable caught)
                    {
                        label1.setText(caught.getMessage());
                    }

                    public void onSuccess(String result)
                    {
//                        label1.setText(result);
                        apiCharts.updateLineChart(parseResultNew(result));
                    }
                });
            }
        }
        YetAnotherLineChartHandler yetAnotherLineChartHandler = new YetAnotherLineChartHandler();
        yetAnotherLineChartButton.addClickHandler(yetAnotherLineChartHandler);

        return mainPanel;
    }

    private TreeMap<String, TreeMap<String, ArrayList<String>>> parseResult(String result)
    {
        JSONValue jsonValue;
        JSONObject jsonObject;
        JSONArray jsonArray;
        JSONString jsonString;
        String kawasan, negeri, tarikh, api;

        ArrayList<String> arrayOfAPI = new ArrayList<String>();
        TreeMap<String, ArrayList<String>> treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>();
        TreeMap<String, TreeMap<String, ArrayList<String>>> fullTreeMap = new TreeMap<String, TreeMap<String, ArrayList<String>>>();

        jsonValue = JSONParser.parseStrict(result);
        jsonObject = jsonValue.isObject();
        jsonValue = jsonObject.get("result");
        jsonObject = jsonValue.isObject();
        jsonValue = jsonObject.get("records");
        jsonArray = jsonValue.isArray();

        for(int i = 0; i < jsonArray.size(); i++)
        {
            jsonValue = jsonArray.get(i);
            jsonObject = jsonValue.isObject();

            jsonValue = jsonObject.get("Kawasan");
            jsonString = jsonValue.isString();
            kawasan = jsonString.stringValue();

            jsonValue = jsonObject.get("Negeri");
            jsonString = jsonValue.isString();
            negeri = jsonString.stringValue();

            jsonValue = jsonObject.get("Tarikh");
            jsonString = jsonValue.isString();
            tarikh = jsonString.stringValue().substring(0, 7);

            jsonValue = jsonObject.get("API");
            jsonString = jsonValue.isString();
            api = jsonString.stringValue();

            arrayOfAPI.clear();
            treeMapOfDateAndAPI.clear();

            if(fullTreeMap.isEmpty() || !fullTreeMap.containsKey(kawasan))
            {
                arrayOfAPI.add(api);
                treeMapOfDateAndAPI.put(tarikh, new ArrayList<String>(arrayOfAPI));
                fullTreeMap.put(kawasan, new TreeMap<String, ArrayList<String>>(treeMapOfDateAndAPI));
            }
            else
            {
                treeMapOfDateAndAPI = new TreeMap<String, ArrayList<String>>(fullTreeMap.get(kawasan));

                if(treeMapOfDateAndAPI.containsKey(tarikh))
                {
                    arrayOfAPI = new ArrayList<String>(treeMapOfDateAndAPI.get(tarikh));
                }

                arrayOfAPI.add(api);
                treeMapOfDateAndAPI.put(tarikh, new ArrayList<String>(arrayOfAPI));
                fullTreeMap.put(kawasan, new TreeMap<String, ArrayList<String>>(treeMapOfDateAndAPI));
            }
        }

        return fullTreeMap;
    }

    private Set<API> parseResultNew(String result)
    {
        JSONValue jsonValue;
        JSONObject jsonObject;
        JSONArray jsonArray;
        JSONString jsonString;

        jsonValue = JSONParser.parseStrict(result);
        jsonObject = jsonValue.isObject();
        jsonValue = jsonObject.get("result");
        jsonObject = jsonValue.isObject();
        jsonValue = jsonObject.get("records");
        jsonArray = jsonValue.isArray();

        Set<API> setAPI = new TreeSet<API>();

        for(int i = 0; i < jsonArray.size(); i++)
        {
        	String tarikhText;
        	int apiValue;
        	API api = new API();

            jsonValue = jsonArray.get(i);
            jsonObject = jsonValue.isObject();

            jsonValue = jsonObject.get("Kawasan");
            jsonString = jsonValue.isString();
            api.setKawasan(jsonString.stringValue());

            jsonValue = jsonObject.get("Negeri");
            jsonString = jsonValue.isString();
            api.setNegeri(jsonString.stringValue());

            jsonValue = jsonObject.get("Tarikh");
            jsonString = jsonValue.isString();
            tarikhText = jsonString.stringValue().substring(0, 9);

            jsonValue = jsonObject.get("API");
            jsonString = jsonValue.isString();
            String extractedDigit = jsonString.stringValue().replaceAll("\\D+", "");
            if(extractedDigit.equals("")) {
            	apiValue = 0;
            }
            else {
            	try {
            		apiValue = Integer.parseInt(extractedDigit);
	            }
	            catch(Exception ex) {
	            	apiValue = 0;
	            }
            }

            if(setAPI.isEmpty())
            {
            	api.addTarikhAndValue(tarikhText, apiValue);
            	setAPI.add(api);
            }
            else
            {
            	if(!setAPI.contains(api))
            	{
                	api.addTarikhAndValue(tarikhText, apiValue);
                	setAPI.add(api);
            	}
            	else
            	{
            		for(Iterator<API> iterator = setAPI.iterator(); iterator.hasNext();)
            		{
            			API tempAPI = iterator.next();

            			if(tempAPI.equals(api))
            			{
            				tempAPI.addTarikhAndValue(tarikhText, apiValue);
            				break;
            			}
            		}
            	}
            }
        }

        return setAPI;
    }
}
