package edu.vt.teja.ece4564.hokiemanager;

import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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

    private ArrayList<String> assignments_ = new ArrayList<String>();
    private ArrayList<String> classes_ = new ArrayList<String>();
    private ArrayList<String> officehours_ = new ArrayList<String>();

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

        return true;
    }

    public void getEvents() throws IOException{
        String redirectURL, pageHtml;

        //portal
        HttpsURLConnection connection = (HttpsURLConnection)(new URL(SCHOLAR_PORTAL_URL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        pageHtml = getHTML(connection);

        //Log.d("LOCATION-HTML", pageHtml);

        pageHtml = pageHtml.substring(pageHtml.indexOf("class=\"toolMenuIcon icon-sakai-motd \""),
                pageHtml.indexOf("\" title=\"For posting and viewing deadlines, events, etc.\">"));
        //Log.d("LOCATION-HTML", pageHtml);
        redirectURL = pageHtml.substring(pageHtml.indexOf("href=\"")+6);
        connection.disconnect();

        //calendar
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        pageHtml = getHTML(connection);
        pageHtml = pageHtml.substring(pageHtml.indexOf("<iframe"), pageHtml.indexOf("</iframe>"));
        redirectURL = pageHtml.substring(pageHtml.indexOf("src=\"")+5, pageHtml.indexOf("\">"));
        connection.disconnect();

        //calendar iframe
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        pageHtml = getHTML(connection);
        pageHtml = pageHtml.substring(pageHtml.indexOf("name=\"sakai_csrf_token\" value=\"") + 31);
        String sakai_csrf_token = pageHtml.substring(0, pageHtml.indexOf("\" />"));
        connection.disconnect();

        //calendar POST for list of events
        connection = (HttpsURLConnection)(new URL(redirectURL).openConnection());

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Cookie", cookiesScholar_.get("scholar.vt.edu.JSESSIONID") + "; " +
                cookiesScholar_.get("lb-scholar.vt.edu") + "; " +
                cookiesScholar_.get("expire-scholar"));
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes("eventSubmit_doView=" + URLEncoder.encode("view", HTTP.UTF_8) +
                "&view=" + URLEncoder.encode("List of Events", HTTP.UTF_8) + "&sakai_csrf_token=" +
                URLEncoder.encode(sakai_csrf_token, HTTP.UTF_8));
        outputStream.flush();
        outputStream.close();

        pageHtml = getHTML(connection);
        connection.disconnect();

        Document doc = Jsoup.parse(pageHtml);

        Element table = doc.select("table").first();
        //Iterator<Element> rows = table.select("tr").iterator();
        //ArrayList<String> columnBuffer = new ArrayList<String>();

        int count;
        String[] entry = new String[5];
        String type, day, month, year;
        for (Element row : table.select("tr")) {
            type = "";
            count = 0;
            Arrays.fill(entry, "");
            for (Element column : row.select("td")) {
                pageHtml = column.toString();
                if(pageHtml.contains("From Site: \"") || pageHtml.contains("<td>&nbsp;</td>"))
                    continue;
                if (count == 0){
                    if(!pageHtml.contains("day"))   continue;
                    pageHtml = pageHtml.substring(pageHtml.indexOf("?day=")+5);
                    day = pageHtml.substring(0, pageHtml.indexOf("&"));
                    pageHtml = pageHtml.substring(pageHtml.indexOf("month=")+6);
                    month = pageHtml.substring(0, pageHtml.indexOf("&"));
                    pageHtml = pageHtml.substring(pageHtml.indexOf("year=")+5);
                    year = pageHtml.substring(0, pageHtml.indexOf("&"));
                    entry[0] = month + "/" + day + "/" + year;
                }
                else if (count == 4){
                    entry[4] = pageHtml.substring(pageHtml.indexOf("title=\"") + 7);
                    entry[4] = entry[4].substring(0, entry[4].indexOf("\">"));
                    type = pageHtml.substring(pageHtml.indexOf("alt=\"") + 5);
                    type = type.substring(0, type.indexOf("\""));
                }
                else{
                    entry[count] = pageHtml.substring(pageHtml.indexOf(">")+1, pageHtml.indexOf("</td>")).trim();
                }
                ++count;
            }

            if(type.contains("Class") || type.contains("Lecture"))
                classes_.add(entry[3] + "\n\t" + entry[0] + " " + entry[1] + "\n\t" + entry[4]);
            else if (type.contains("Meeting"))
                officehours_.add(entry[3] + "\n\t" + entry[0] + " " + entry[1] + "\n\t" + entry[4]);
            else if (type.contains("Deadline"))
                assignments_.add(entry[3] + "\n\t" + entry[0] + " " + entry[1] + "\n\t" + entry[4]);
        }
    }

    public ArrayList<String> getAssignments(){
        return assignments_;
    }

    public ArrayList<String> getClasses(){
        return classes_;
    }

    public ArrayList<String> getOfficeHours(){
        return officehours_;
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
