package com.sjwoh.airview.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.charts.client.ChartType;
import com.sjwoh.airview.client.entity.API;

// Entry point classes define <code>onModuleLoad()</code>.
public class AirView implements EntryPoint {
	// The message displayed to the user when the server cannot be reached or
	// returns an error.
	private static final String SERVER_ERROR = "An error occurred while attempting to contact the server. Please check your network connection and try again.";

	// Create a remote service proxy to talk to the server-side Greeting
	// service.
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private boolean chartGenerated = false;
	private boolean legendHidden = false;

	APICharts apiCharts = new APICharts();

	// This is the entry point method.
	@Override
	public void onModuleLoad() {
		/*
		 * Window.enableScrolling(false); Window.setMargin("0px");
		 * RootLayoutPanel.get().add(getMainPanel());
		 */

		Panel mainPanel = getMainPanel();
		RootPanel.get("graph-chart").add(mainPanel);
		RootPanel.get("map").setVisible(true);
		RootPanel.get("graph-chart").setVisible(false);
		RootPanel.get("legend-img-horizontal").setVisible(false);

		// Link the HTML map-link to onclickhandler
		Anchor.wrap(DOM.getElementById("map-link")).addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("map").setVisible(true);
				RootPanel.get("graph-chart").setVisible(false);
				RootPanel.get("legend-img-horizontal").setVisible(false);
			}
		});

		// Link the preexisting side options to onclickhandler
		Anchor.wrap(DOM.getElementById("graph-link")).addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("map").setVisible(false);
				RootPanel.get("graph-chart").setVisible(true);
				if (!legendHidden) {
					RootPanel.get("legend-img-horizontal").setVisible(true);
				} else {
					RootPanel.get("legend-img-horizontal").setVisible(false);
				}
			}
		});
	}

	private List<String> getNegeris() {
		List<String> negeris = new ArrayList<String>();
		negeris.add("Johor");
		negeris.add("Kedah");
		negeris.add("Kelantan");
		negeris.add("Melaka");
		negeris.add("Negeri Sembilan");
		negeris.add("Pahang");
		negeris.add("Perak");
		negeris.add("Perlis");
		negeris.add("Pulau Pinang");
		negeris.add("Sabah");
		negeris.add("Sarawak");
		negeris.add("Selangor");
		negeris.add("Terengganu");
		negeris.add("Wilayah Persekutuan");

		return negeris;
	}

	private List<String> getYears() {
		List<String> years = new ArrayList<String>();
		years.add("2005");
		years.add("2006");
		years.add("2007");
		years.add("2008");
		years.add("2009");
		years.add("2013");
		years.add("2014");
		years.add("2015");

		return years;
	}

	public Panel getMainPanel() {
		final List<String> negeris = getNegeris();
		final List<String> years = getYears();

		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		mainPanel.setSize("100%", "100%");

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setSize("100%", "100%");
		leftPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		leftPanel.getElement().setId("left-panel");

		// Graph plotter menu title bar
		final Label lblMenu = new Label("Graph Plotter Menu");
		lblMenu.getElement().setId("graph-menu-txt");

		// final Button lineChartButton = new Button("Line Chart Button");
		final Button generateLineChartButton = new Button("Generate");
		generateLineChartButton.getElement().setId("btnLine");

		final ToggleButton toggleButton = new ToggleButton("Display API Legend", "Hide API Legend");
		toggleButton.getElement().setId("btnLine");
		toggleButton.setDown(true);
		toggleButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (toggleButton.isDown()) {
					RootPanel.get("legend-img-horizontal").setVisible(true);
				} else {
					RootPanel.get("legend-img-horizontal").setVisible(false);
				}

				legendHidden = !legendHidden;
			}
		});

		final ListBox lineChartOptionList = new ListBox();
		for (int i = 0; i < negeris.size(); i++) {
			lineChartOptionList.addItem(negeris.get(i));
			lineChartOptionList.setVisibleItemCount(1);
		}

		final ListBox lineChartYearList = new ListBox();
		for (int i = 0; i < years.size(); i++) {
			lineChartYearList.addItem(years.get(i));
			lineChartYearList.setVisibleItemCount(1);
		}

		final ListBox listBoxChartType = new ListBox();
		listBoxChartType.addItem("Line Chart");
		listBoxChartType.addItem("Bar Chart");
		listBoxChartType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (!chartGenerated) {
					return;
				}

				if (listBoxChartType.getSelectedIndex() == 0) {
					apiCharts.updateChart(ChartType.LINE);
				} else {
					apiCharts.updateChart(ChartType.BAR);
				}
			}
		});

		// LINE 178 TO 259 DOES NOT DO ANYTHING. JUST SHOWS THAT KAWASAN CAN BE
		// CHANGED BASED ON NEGERI SELECTION
		final String kawasanJohor[] = { "Pasir Gudang", "Larkin Lama", "Muar", "Kota Tinggi" };
		final String kawasanKedah[] = { "Langkawi", "Alor Setar", "Bakar Arang, Sg. Petani" };
		final String kawasanKelantan[] = { "Tanah Merah ", "SMK Tanjung Chat, Kota Bharu" };
		final String kawasanMelaka[] = { "Bukit Rambai", "Bandaraya Melaka" };
		final String kawasanNegeriSembilan[] = { "Nilai", "Seremban", "Port Dickson" };
		final String kawasanPahang[] = { "Jerantut", "Indera Mahkota, Kuantan", "Balok Baru, Kuantan" };
		final String kawasanPerak[] = { "Jalan Tasek, Ipoh", "S K Jalan Pegoh, Ipoh", "Kg. Air Putih, Taiping",
				"Seri Manjung", "Tanjung Malim" };
		final String kawasanPerlis[] = { "Kangar" };
		final String kawasanPulauPinang[] = { "USM", "Perai", "Seberang Jaya 2, Perai" };
		final String kawasanSabah[] = { "Kota Kinabalu", "Tawau", "Keningau", "Sandakan" };
		final String kawasanSarawak[] = { "Limbang", "Samarahan", "Sri Aman", "Kapit", "Kuching", "Sibu", "Bintulu",
				"Miri", "ILP Miri", "Sarikei" };
		final String kawasanSelangor[] = { "Pelabuhan Kelang", "Petaling Jaya", "Banting", "Shah Alam",
				"Kuala Selangor" };
		final String kawasanTerengganu[] = { "Kemaman", "Paka", "Kuala Terengganu" };
		final String kawasanWilayahPersekutuan[] = { "Batu Muda,Kuala Lumpur", "Cheras,Kuala Lumpur", "Putrajaya",
				"Labuan" };
		final ListBox lineChartAdditionalOptionList = new ListBox();
		for (int i = 0; i < kawasanJohor.length; i++) {
			lineChartAdditionalOptionList.addItem(kawasanJohor[i]);
		}
		lineChartAdditionalOptionList.setVisibleItemCount(kawasanJohor.length);

		lineChartOptionList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				int selectedNegeri = lineChartOptionList.getSelectedIndex();
				lineChartAdditionalOptionList.clear();
				String[] kawasanList = null;

				switch (selectedNegeri) {
				case 0:
					kawasanList = kawasanJohor;
					break;
				case 1:
					kawasanList = kawasanKedah;
					break;
				case 2:
					kawasanList = kawasanKelantan;
					break;
				case 3:
					kawasanList = kawasanMelaka;
					break;
				case 4:
					kawasanList = kawasanNegeriSembilan;
					break;
				case 5:
					kawasanList = kawasanPahang;
					break;
				case 6:
					kawasanList = kawasanPerak;
					break;
				case 7:
					kawasanList = kawasanPerlis;
					break;
				case 8:
					kawasanList = kawasanPulauPinang;
					break;
				case 9:
					kawasanList = kawasanSabah;
					break;
				case 10:
					kawasanList = kawasanSarawak;
					break;
				case 11:
					kawasanList = kawasanSelangor;
					break;
				case 12:
					kawasanList = kawasanTerengganu;
					break;
				case 13:
					kawasanList = kawasanWilayahPersekutuan;
					break;
				}

				for (int i = 0; i < kawasanList.length; i++) {
					lineChartAdditionalOptionList.addItem(kawasanList[i]);
				}
				lineChartAdditionalOptionList.setVisibleItemCount(kawasanList.length);
			}
		});

		leftPanel.add(lblMenu);
		leftPanel.add(listBoxChartType);
		leftPanel.add(lineChartOptionList);
		leftPanel.add(lineChartYearList);
		leftPanel.add(generateLineChartButton);
		leftPanel.add(toggleButton);
		// leftPanel.add(lineChartButton);
		// leftPanel.add(lineChartAdditionalOptionList);

		// mainPanel.addNorth(new HTML("<h1>AirView</h1>"), 100);
		mainPanel.addWest(leftPanel, 150);
		mainPanel.add(apiCharts.getDockLayoutPanel());

		class LineChartHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				final String negeri = lineChartOptionList.getItemText(lineChartOptionList.getSelectedIndex());
				final String year = lineChartYearList.getItemText(lineChartYearList.getSelectedIndex());

				greetingService.greetServer(negeri, year, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						showCustomDialog(caught.getMessage());
					}

					public void onSuccess(String result) {
						Set<API> setAPI = parseResultNew(result);
						if (setAPI == null) {
							showCustomDialog(SERVER_ERROR);
							return;
						}

						if (listBoxChartType.getSelectedIndex() == 0) {
							apiCharts.updateChart(ChartType.LINE, negeri, Integer.parseInt(year), setAPI);
						} else {
							apiCharts.updateChart(ChartType.BAR, negeri, Integer.parseInt(year), setAPI);
						}

						chartGenerated = true;
					}
				});
			}
		}
		LineChartHandler lineChartHandler = new LineChartHandler();
		generateLineChartButton.addClickHandler(lineChartHandler);

		return mainPanel;
	}

	/**
	 * Draws Custom Dialog box.
	 * 
	 * @return DialogBox
	 */
	private DialogBox showCustomDialog(String message) {
		final DialogBox dialog = new DialogBox(false, true);
		// Set caption
		dialog.setText("Data Fetch Status");
		dialog.setWidth("300px");

		// Set content
		Label content = new Label(message);

		if (dialog.isAutoHideEnabled()) {
			dialog.setWidget(content);
		} else {
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setSpacing(2);
			vPanel.add(content);
			vPanel.add(new Label("\n"));
			vPanel.add(new Button("Close", new ClickHandler() {
				public void onClick(ClickEvent event) {
					dialog.hide();
				}
			}));
			dialog.setWidget(vPanel);
		}

		dialog.setPopupPosition(600, 150);
		dialog.show();

		return dialog;
	}

	private Set<API> parseResultNew(String result) {
		if (result.equals("")) {
			return null;
		}

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

		for (int i = 0; i < jsonArray.size(); i++) {
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
			if (extractedDigit.equals("")) {
				apiValue = 0;
			} else {
				try {
					apiValue = Integer.parseInt(extractedDigit);
				} catch (Exception ex) {
					apiValue = 0;
				}
			}

			if (setAPI.isEmpty()) {
				api.addTarikhAndValue(tarikhText, apiValue);
				setAPI.add(api);
			} else {
				if (!setAPI.contains(api)) {
					api.addTarikhAndValue(tarikhText, apiValue);
					setAPI.add(api);
				} else {
					for (Iterator<API> iterator = setAPI.iterator(); iterator.hasNext();) {
						API tempAPI = iterator.next();

						if (tempAPI.equals(api)) {
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
