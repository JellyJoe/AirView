package com.sjwoh.airview.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.sjwoh.airview.client.GreetingService;
import com.sjwoh.airview.client.entity.EnumTranslator;
import com.sjwoh.airview.client.entity.EnumTranslator.ResourceId;
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
        String source = "http://www.data.gov.my/data/api/action/datastore_search_sql?sql=";
        String query = "SELECT * FROM \"" + EnumTranslator.getResource(ResourceId._2014_2015) + "\" WHERE EXTRACT(YEAR FROM \"Tarikh\") = '2015' LIMIT 10";
//        completeAPILink.append("&q='");
//        completeAPILink.append(country);
//        completeAPILink.append("'");

        final int bufferSize = 4096;
        final char[] buffer = new char[bufferSize];
        final StringBuilder content = new StringBuilder();
        URL url = null;

        try
        {
            url = new URL(source + URLEncoder.encode(query, "UTF-8"));
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
			e.printStackTrace();
		}

        try
        {
            URLConnection con = url.openConnection();
            con.setConnectTimeout(60000);
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
