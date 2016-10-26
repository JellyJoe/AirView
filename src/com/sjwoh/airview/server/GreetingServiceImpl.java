package com.sjwoh.airview.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.sjwoh.airview.client.GreetingService;
import com.sjwoh.airview.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService
{
    public String greetServer(String input)
    {
        return getURLContent(input);
    }

    private String getURLContent(String country)
    {
        // only gets 5 so far
        final String APILink = "http://www.data.gov.my/data/api/action/datastore_search?resource_id=a864e6cd-759e-4a7e-a672-fa4d3b709e2e&limit=1500";
        StringBuilder completeAPILink = new StringBuilder();
        completeAPILink.append(APILink);
        completeAPILink.append("&q='");
        completeAPILink.append(country);
        completeAPILink.append("'");

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder content = new StringBuilder();
        URL url = null;

        try
        {
            url = new URL(completeAPILink.toString());
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }

        try
        {
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            Reader in = new InputStreamReader(is, "UTF-8");

            for(;;)
            {
                int numberOfCharacterRead = in.read(buffer, 0, buffer.length);
                if(numberOfCharacterRead < 0)
                    break;
                content.append(buffer, 0, numberOfCharacterRead);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return content.toString();
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     * 
     * @param html
     *            the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html)
    {
        if(html == null)
        {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

}
