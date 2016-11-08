package com.sjwoh.airview.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void greetServer(String input1, String input2, AsyncCallback<String> callback) throws IllegalArgumentException;
}
