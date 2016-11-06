package com.sjwoh.airview.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
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
        final String completeAPILink = "http://www.data.gov.my/data/api/action/datastore_search_sql?sql=";
        final String q = "SELECT \"Tarikh\", \"Negeri\", \"Kawasan\", \"API\" from \"a864e6cd-759e-4a7e-a672-fa4d3b709e2e\" WHERE \"Negeri\" = 'Sarawak'"
                + " LIMIT 10000";
        
        final int bufferSize = 8192;
        final char[] buffer = new char[bufferSize];
        final StringBuilder content = new StringBuilder();
        
        URL url = null;

        try
        {
            url = new URL(completeAPILink + URLEncoder.encode(q, "UTF-8"));
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
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
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
