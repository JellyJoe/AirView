package com.sjwoh.airview.client;

import java.util.ArrayList;
import java.util.TreeMap;

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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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

        final Label label1 = new Label("SAMPLE TEXT");
        label1.setSize("100%", "100%");
        final Button lineChartButton = new Button("Line Chart Button");
        final Button pieChartButton = new Button("Pie Chart Button");
        final Button anotherLineChartButton = new Button("Another Line Chart Button");

        leftPanel.add(label1);
        leftPanel.add(lineChartButton);
        leftPanel.add(pieChartButton);
        leftPanel.add(anotherLineChartButton);

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
                greetingService.greetServer("Sarawak", new AsyncCallback<String>()
                {
                    public void onFailure(Throwable caught)
                    {
                        label1.setText(caught.getMessage());
                    }

                    public void onSuccess(String result)
                    {
//                        label1.setText(result);
                        apiCharts.updateLineChart("Sarawak", parseResult(result));
                    }
                });
            }
        }
        AnotherLineChartHandler anotherLineChartHandler = new AnotherLineChartHandler();
        anotherLineChartButton.addClickHandler(anotherLineChartHandler);

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

}
