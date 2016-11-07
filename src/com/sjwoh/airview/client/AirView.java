package com.sjwoh.airview.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
        Window.enableScrolling(false);
        Window.setMargin("0px");

        RootLayoutPanel.get().add(getMainPanel());
    }

    public Panel getMainPanel()
    {
        DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.setSize("100%", "100%");

        VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.setSize("100%", "100%");

        final Label errorLabel = new Label("Any errors will show up here!");
        errorLabel.setSize("100%", "100%");
        final Button lineChartButton = new Button("Line Chart Button");
        final Button yetAnotherLineChartButton = new Button("Yet Another Line Chart Button");
        
        final ListBox lineChartOptionList = new ListBox();
        lineChartOptionList.addItem("Sarawak");
        lineChartOptionList.addItem("Selangor");
        lineChartOptionList.addItem("Wilayah Persekutuan");
        lineChartOptionList.setVisibleItemCount(1);
        
        // LINE 78 TO 115 DOES NOT DO ANYTHING. JUST SHOWS THAT IT CAN BE CHANGED BASED ON NEGERI SELECTION
        final String sarawakKawasan[] = {"Kapit", "Miri", "Bintulu", "Kuching", "Sibu"};
        final String selangorKawasan[] = {"Kuala Selangor", "Shah Alam", "Banting"};
        final String wilayahPersekutuanKawasan[] = {"Putrajaya", "Labuan"};
        final ListBox lineChartAdditionalOptionList = new ListBox();
        for(int i = 0; i < sarawakKawasan.length; i++)
        {
            lineChartAdditionalOptionList.addItem(sarawakKawasan[i]);
        }
        lineChartAdditionalOptionList.setVisibleItemCount(sarawakKawasan.length);
        
        lineChartOptionList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                
                int selectedNegeri = lineChartOptionList.getSelectedIndex();
                lineChartAdditionalOptionList.clear();
                String[] kawasanList = null;
                
                switch(selectedNegeri)
                {
                    case 0:
                        kawasanList = sarawakKawasan;
                        break;
                    case 1:
                        kawasanList = selangorKawasan;
                        break;
                    case 2:
                        kawasanList = wilayahPersekutuanKawasan;
                        break;
                }
                
                for(int i = 0; i < kawasanList.length; i++)
                {
                    lineChartAdditionalOptionList.addItem(kawasanList[i]);
                }
                lineChartAdditionalOptionList.setVisibleItemCount(kawasanList.length);
            }
        });

        leftPanel.add(errorLabel);
        leftPanel.add(lineChartOptionList);
        leftPanel.add(lineChartButton);
        leftPanel.add(yetAnotherLineChartButton);
        leftPanel.add(lineChartAdditionalOptionList);

        mainPanel.addNorth(new HTML("<h1>AirView</h1>"), 100);
        mainPanel.addWest(leftPanel, 150);
        mainPanel.add(apiCharts.getDockLayoutPanel());

        // creates and sets the line chart button handler
        class LineChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                greetingService.greetServer(lineChartOptionList.getItemText(lineChartOptionList.getSelectedIndex()), new AsyncCallback<String>()
                {
                    public void onFailure(Throwable caught)
                    {
                        errorLabel.setText(caught.getMessage());
                    }

                    public void onSuccess(String result)
                    {
                        apiCharts.updateLineChart(lineChartOptionList.getItemText(lineChartOptionList.getSelectedIndex()), parseResult(result));
                    }
                });
            }
        }
        LineChartHandler lineChartHandler = new LineChartHandler();
        lineChartButton.addClickHandler(lineChartHandler);
        
        class YetAnotherLineChartHandler implements ClickHandler
        {
            public void onClick(ClickEvent event)
            {
                greetingService.greetServer("Sarawak", new AsyncCallback<String>()
                {
                    public void onFailure(Throwable caught)
                    {
                        errorLabel.setText(caught.getMessage());
                    }

                    public void onSuccess(String result)
                    {
//                        errorLabel.setText(result);
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
