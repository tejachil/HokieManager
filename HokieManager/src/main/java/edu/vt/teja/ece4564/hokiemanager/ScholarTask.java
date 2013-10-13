package edu.vt.teja.ece4564.hokiemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by teja on 10/9/13.
 */
public class ScholarTask extends AsyncTask<String, Void, String> {
    private CentralAuthenticationService cas_;
    private Context context_;
    private Scholar scholar_;
    private ExpandableListView expListView_;
    private MenuItem menuItem_;

    public ScholarTask(Context context, CentralAuthenticationService CAS, ExpandableListView expListView, MenuItem menuItem) {
        context_ = context;
        cas_ = CAS;
        scholar_ = new Scholar(cas_);
        expListView_ = expListView;
        menuItem_ = menuItem;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params){
        Log.d("Location", "Reached Do In background of Scholar: ");
        try{
            scholar_.loginScholar();
            scholar_.getEvents();
        }
        catch (IOException e){

        }
        return "ret";
    }

    @Override
    protected void onPostExecute(String result) {
        //ExpandableListView expListView = (ExpandableListView) context_.findViewById(R.id.listView_scholarEvents);
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;

        // preparing list data
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Classes and Lectures");
        listDataHeader.add("Assignments and Deadlines");
        listDataHeader.add("Office Hours");

        // Adding child data
        List<String> classes = scholar_.getClasses();

        List<String> assignments = scholar_.getAssignments();

        List<String> officeHours = scholar_.getOfficeHours();

        listDataChild.put(listDataHeader.get(0), classes); // Header, Child data
        listDataChild.put(listDataHeader.get(1), assignments);
        listDataChild.put(listDataHeader.get(2), officeHours);


        listAdapter = new ExpandableListAdapter(context_, listDataHeader, listDataChild);

        // setting list adapter
        expListView_.setAdapter(listAdapter);

        Toast.makeText(context_, "Updated Tasks List from Scholar", Toast.LENGTH_SHORT).show();
        menuItem_.collapseActionView();
        menuItem_.setActionView(null);
    }
}
