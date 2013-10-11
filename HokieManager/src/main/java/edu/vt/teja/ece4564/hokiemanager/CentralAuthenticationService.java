package edu.vt.teja.ece4564.hokiemanager;

import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teja on 10/8/13.
 */
public class CentralAuthenticationService {
    private final static String HOST = "auth.vt.edu";
    private final static String ORIGIN = "https://auth.vt.edu";
    private final static String LOGIN_URL = "https://auth.vt.edu/login";
    private final static String LOGOUT_URL = "https://auth.vt.edu/logout";
    private final static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.66 Safari/537.36";

    private String username_;
    private String password_;

    public CentralAuthenticationService(String username, String password){
        username_ = username;
        password_ = password;

        try{
            login();
        }
        catch (IOException e){
            Log.e("ERROR", "IOException");
        }
    }

    private boolean login() throws IOException {
        String redirectURL, newCookie, pageHtml;
        ArrayList<String> casCookies = new ArrayList<String>();

        // Redirect 1
        HttpsURLConnection connection = (HttpsURLConnection)(new URL(LOGIN_URL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.connect();

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        casCookies.add(newCookie.substring(0,newCookie.indexOf(";")));

        redirectURL = connection.getHeaderField("Location");
        Log.i("URL", redirectURL);
        connection.disconnect();

        // Redirect 2
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setRequestProperty("Cookie", casCookies.get(0));
        connection.connect();

        pageHtml = getHTML(connection);
        HashMap formHashMap = parseLoginForm(pageHtml.substring(pageHtml.indexOf("class=\"buttons\">"), pageHtml.indexOf("</form>")));
        connection.disconnect();

        // Redirect 3
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Cookie", casCookies.get(0));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes("username=" + URLEncoder.encode(username_, HTTP.UTF_8) +
                                "&password=" + URLEncoder.encode(password_, HTTP.UTF_8) +
                                "&lt=" + URLEncoder.encode(formHashMap.get("lt").toString(), HTTP.UTF_8) +
                                "&execution=" + URLEncoder.encode(formHashMap.get("execution").toString(), HTTP.UTF_8) +
                                "&_eventId=" + URLEncoder.encode(formHashMap.get("_eventId").toString(), HTTP.UTF_8) +
                                "&submit=" + URLEncoder.encode(formHashMap.get("submit").toString(), HTTP.UTF_8));
        outputStream.flush();
        outputStream.close();

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(1).toString();
        casCookies.add(newCookie.substring(0, newCookie.indexOf(";")));

        connection.disconnect();

        // Redirect 3 SCHOLAR
        connection = (HttpsURLConnection)(new URL("https://scholar.vt.edu/portal/login").openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        redirectURL = connection.getHeaderField("Location");

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        casCookies.add(newCookie.substring(0,newCookie.indexOf(";")));  //scholar.vt.edu.JSESSIONID

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(1).toString();
        casCookies.add(newCookie.substring(0,newCookie.indexOf(";")));  //lb-scholar.vt.edu

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(2).toString();
        casCookies.add(newCookie.substring(0,newCookie.indexOf(";")));  //expire-scholar

        connection.disconnect();


        //SCHOLAR container
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(2) + "; " + casCookies.get(3) + "; " + casCookies.get(4));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        redirectURL = connection.getHeaderField("Location");

        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        casCookies.set(4, newCookie.substring(0,newCookie.indexOf(";")));  //new expire-scholar

        connection.disconnect();

        //?service=https
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(0) + "; " + casCookies.get(1));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        connection.disconnect();

        //login?service=
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(0) + "; " + casCookies.get(1));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        connection.disconnect();

        //container?ticket
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(2) + "; " + casCookies.get(3) + "; " + casCookies.get(4));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        redirectURL = connection.getHeaderField("Location");
        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        casCookies.set(4, newCookie.substring(0,newCookie.indexOf(";")));  //new expire-scholar
        connection.disconnect();

        //container?ticker
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(2) + "; " + casCookies.get(3) + "; " + casCookies.get(4));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        newCookie = connection.getHeaderFields().get("Set-Cookie").get(0).toString();
        casCookies.set(4, newCookie.substring(0,newCookie.indexOf(";")));  //new expire-scholar
        connection.disconnect();

        //portal
        connection = (HttpsURLConnection)(new URL("https://scholar.vt.edu/portal").openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", casCookies.get(2) + "; " + casCookies.get(3) + "; " + casCookies.get(4));
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

    private HashMap<String, String> parseLoginForm(String htmlToParse){
        HashMap<String, String> parsedForm = new HashMap<String, String>();
        String subStringBuffer;

        subStringBuffer = htmlToParse.substring(htmlToParse.indexOf("name=\"lt\" value=\"")+17);
        parsedForm.put("lt", subStringBuffer.substring(0, subStringBuffer.indexOf("\" />")));

        subStringBuffer = htmlToParse.substring(htmlToParse.indexOf("name=\"execution\" value=\"")+24);
        parsedForm.put("execution", subStringBuffer.substring(0, subStringBuffer.indexOf("\" />")));

        subStringBuffer = htmlToParse.substring(htmlToParse.indexOf("name=\"_eventId\" value=\"")+23);
        parsedForm.put("_eventId", subStringBuffer.substring(0, subStringBuffer.indexOf("\" />")));

        subStringBuffer = htmlToParse.substring(htmlToParse.indexOf("accesskey=\"l\" value=\"")+21);
        parsedForm.put("submit", subStringBuffer.substring(0,subStringBuffer.indexOf("\" tabindex")));

        return parsedForm;
    }
}
