package edu.vt.teja.ece4564.hokiemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * Created by teja on 10/9/13.
 */
public class ScholarTask extends AsyncTask<String, Void, String> {
    private CentralAuthenticationService cas_;
    private Context context_;
    private Scholar scholar_;

    public ScholarTask(Context context, CentralAuthenticationService CAS) {
        context_ = context;
        cas_ = CAS;
        scholar_ = new Scholar(cas_);
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

    }



}
