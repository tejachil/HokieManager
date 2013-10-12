package edu.vt.teja.ece4564.hokiemanager;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teja on 10/12/13.
 */
public class Scholar {
    private final static String SCHOLAR_LOGIN_URL = "https://scholar.vt.edu/portal/login";
    private final static String SCHOLAR_PORTAL_URL = "https://scholar.vt.edu/portal/";
    private final static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.66 Safari/537.36";


    private HashMap<String,String> cookiesCAS_;
    private HashMap<String,String> cookiesScholar_ = new HashMap<String,String>();

    public Scholar(CentralAuthenticationService auth){
        cookiesCAS_ = auth.getCookies();
    }

    public boolean loginScholar() throws IOException {
        String redirectURL, newCookie, pageHtml;

        // Redirect 3 SCHOLAR
        HttpsURLConnection connection = (HttpsURLConnection)(new URL(SCHOLAR_LOGIN_URL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        redirectURL = connection.getHeaderField("Location");

        // Add the following cookies: scholar.vt.edu.JSESSIONID, lb-scholar.vt.edu, expire-scholar
        for(String cookie : connection.getHeaderFields().get("Set-Cookie"))
            cookiesScholar_.put(cookie.substring(0,cookie.indexOf("=")),cookie.substring(0,cookie.indexOf(";")));

        connection.disconnect();
        Log.d("Location", "Finished Redirect 4- Scholar");

        //SCHOLAR container
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        redirectURL = connection.getHeaderField("Location");

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        cookiesScholar_.put(newCookie.substring(0,newCookie.indexOf("=")),newCookie.substring(0,newCookie.indexOf(";")));

        connection.disconnect();

        //?service=https
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesCAS_.get("JSESSIONID") + "; " + cookiesCAS_.get("CASTGC"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        connection.disconnect();

        //login?service=
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesCAS_.get("JSESSIONID") + "; " + cookiesCAS_.get("CASTGC"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        connection.disconnect();

        //container?ticket
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        cookiesScholar_.put(newCookie.substring(0,newCookie.indexOf("=")),newCookie.substring(0,newCookie.indexOf(";")));
        connection.disconnect();

        //container?ticker
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        cookiesScholar_.put(newCookie.substring(0,newCookie.indexOf("=")),newCookie.substring(0,newCookie.indexOf(";")));
        connection.disconnect();

        //portal
        connection = (HttpsURLConnection)(new URL(SCHOLAR_PORTAL_URL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        pageHtml = getHTML(connection);

        Log.d("FINAL-SCHOLAR", pageHtml);

        connection.disconnect();

        return true;
    }

    private String getHTML(HttpsURLConnection connection) throws IOException {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder htmlStringBuilder = new StringBuilder();
        String htmlBuffer;
        while((htmlBuffer = inReader.readLine()) != null){
            htmlStringBuilder.append(htmlBuffer);
        }

        return htmlStringBuilder.toString();
    }

}
